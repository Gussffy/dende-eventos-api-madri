package br.com.softhouse.dende.services;

import br.com.softhouse.dende.dto.*;
import br.com.softhouse.dende.model.Evento;
import br.com.softhouse.dende.model.Ingresso;
import br.com.softhouse.dende.model.Usuario;
import br.com.softhouse.dende.model.enums.StatusIngresso;
import br.com.softhouse.dende.repositories.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SERVICE DE INGRESSOS

 * Esta classe é responsável por gerenciar as operações relacionadas aos ingressos,
 * como compra, cancelamento e listagem. Ela interage com os repositórios de ingressos,
 * eventos e usuários para realizar as operações necessárias e aplicar as regras de negócio.

 * Princípios aplicados:
 * - Single Responsibility: Cada metodo tem uma responsabilidade única
 * - Validações: Todas as regras de negócio são validadas aqui
 * - Tratamento de exceções: Lança exceções com mensagens claras para o controller
 */
public class IngressoService {

    private final IngressoRepository ingressoRepository;
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;

    public IngressoService() {
        this.ingressoRepository = IngressoRepository.getInstance();
        this.eventoRepository = EventoRepository.getInstance();
        this.usuarioRepository = UsuarioRepository.getInstance();
    }

    public CompraResponseDTO comprar(Long organizadorId, Long eventoId, IngressoRequestDTO request) throws IllegalArgumentException {

        // Validar usuário
        Usuario usuario = usuarioRepository.buscarPorEmail(request.getUsuarioEmail());
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        if (!usuario.getAtivo()) {
            throw new IllegalArgumentException("Usuário inativo não pode comprar ingressos");
        }

        // Validar evento
        Evento evento = eventoRepository.buscarPorId(eventoId);
        if (evento == null) {
            throw new IllegalArgumentException("Evento não encontrado");
        }
        if (!evento.getOrganizadorId().equals(organizadorId)) {
            throw new IllegalArgumentException("Evento não pertence a este organizador");
        }
        if (!evento.getAtivo()) {
            throw new IllegalArgumentException("Evento inativo não está vendendo ingressos");
        }
        if (evento.eventoJaAconteceu()) {
            throw new IllegalArgumentException("Evento já aconteceu");
        }
        if (!evento.temIngressosDisponiveis()) {
            throw new IllegalArgumentException("Ingressos esgotados");
        }
        if (ingressoRepository.existeIngressoAtivo(usuario.getId(), eventoId)) {
            throw new IllegalArgumentException("Você já possui um ingresso ativo para este evento");
        }

        // Calcular valor esperado
        double valorEsperado = evento.getPrecoIngresso();
        Evento principal = null;

        // Validar evento principal ANTES de prosseguir
        if (evento.getEventoPrincipalId() != null) {
            principal = eventoRepository.buscarPorId(evento.getEventoPrincipalId());
            if (principal != null && principal.getAtivo()) {
                if (!principal.temIngressosDisponiveis()) {
                    throw new IllegalArgumentException("Evento principal não tem ingressos disponíveis");
                }
                valorEsperado += principal.getPrecoIngresso();
            }
        }

        // Validar valor pago
        if (request.getTotalPago() != null && Math.abs(request.getTotalPago() - valorEsperado) > 0.01) {
            throw new IllegalArgumentException(
                    String.format("Valor pago (R$ %.2f) não corresponde ao preço do ingresso (R$ %.2f)",
                            request.getTotalPago(), valorEsperado)
            );
        }

        double valorTotal = evento.getPrecoIngresso();
        List<String> codigos = new ArrayList<>();

        // Criar ingresso principal
        Ingresso ingressoPrincipal = new Ingresso(usuario.getId(), eventoId, evento.getPrecoIngresso());
        ingressoRepository.salvar(ingressoPrincipal);
        evento.venderIngresso();
        eventoRepository.atualizar(evento);
        codigos.add(ingressoPrincipal.getCodigo());

        // Se for evento vinculado, criar ingresso para evento principal
        if (principal != null && principal.getAtivo() && principal.temIngressosDisponiveis()) {
            Ingresso ingressoVinculado = new Ingresso(usuario.getId(), principal.getId(), principal.getPrecoIngresso());
            ingressoVinculado.setEventoVinculadoId(eventoId);
            ingressoVinculado.setIngressoPrincipal(false);
            ingressoRepository.salvar(ingressoVinculado);
            principal.venderIngresso();
            eventoRepository.atualizar(principal);
            valorTotal += principal.getPrecoIngresso();
            codigos.add(ingressoVinculado.getCodigo());
        }

        // Preparar mensagem de resposta
        String mensagem = codigos.size() > 1
                ? "Compra realizada com sucesso! Ingressos gerados: " + String.join(", ", codigos)
                : "Compra realizada com sucesso! Ingresso gerado: " + codigos.get(0);

        return new CompraResponseDTO(mensagem, codigos, valorTotal, "AGUARDANDO_PAGAMENTO");
    }

    public CancelamentoResponseDTO cancelar(Long usuarioId, Long ingressoId) throws IllegalArgumentException {

        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        Ingresso ingresso = ingressoRepository.buscarPorId(ingressoId);
        if (ingresso == null) {
            throw new IllegalArgumentException("Ingresso não encontrado");
        }

        if (!ingresso.getUsuarioId().equals(usuarioId)) {
            throw new IllegalArgumentException("Ingresso não pertence a este usuário");
        }

        if (!ingresso.podeSerCancelado()) {
            throw new IllegalArgumentException("Este ingresso não pode ser cancelado");
        }

        Evento evento = eventoRepository.buscarPorId(ingresso.getEventoId());
        if (evento == null) {
            throw new IllegalArgumentException("Evento associado ao ingresso não encontrado");
        }

        if (evento.eventoJaAconteceu()) {
            throw new IllegalArgumentException("Não é possível cancelar ingresso de evento já realizado");
        }

        double valorReembolso = evento.calcularReembolso(ingresso.getValorPago());

        ingresso.cancelar();
        ingressoRepository.atualizar(ingresso);

        evento.cancelarIngresso();
        eventoRepository.atualizar(evento);

        String mensagem;
        if (evento.getEstornaCancelamento()) {
            mensagem = String.format(
                    "Ingresso cancelado. Código: %s, Valor pago: R$ %.2f, Reembolso: R$ %.2f (taxa de %.1f%%)",
                    ingresso.getCodigo(),
                    ingresso.getValorPago(),
                    valorReembolso,
                    evento.getTaxaEstorno()
            );
        } else {
            mensagem = String.format(
                    "Ingresso cancelado. Código: %s, Valor pago: R$ %.2f, Este evento não oferece reembolso.",
                    ingresso.getCodigo(),
                    ingresso.getValorPago()
            );
        }

        return new CancelamentoResponseDTO(
                mensagem,
                ingresso.getValorPago(),
                valorReembolso,
                ingresso.getCodigo()
        );
    }

    public double calcularReembolso(Long ingressoId) throws IllegalArgumentException {
        Ingresso ingresso = ingressoRepository.buscarPorId(ingressoId);
        if (ingresso == null) {
            throw new IllegalArgumentException("Ingresso não encontrado");
        }

        Evento evento = eventoRepository.buscarPorId(ingresso.getEventoId());
        return evento.calcularReembolso(ingresso.getValorPago());
    }

    public List<IngressoResponseDTO> listarPorUsuario(Long usuarioId) throws IllegalArgumentException {

        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        List<Ingresso> ingressos = ingressoRepository.buscarPorUsuarioId(usuarioId);

        // Ordena os ingressos: ativos primeiro, depois por data do evento
        ingressos.sort((i1, i2) -> {
            Evento e1 = eventoRepository.buscarPorId(i1.getEventoId());
            Evento e2 = eventoRepository.buscarPorId(i2.getEventoId());

            boolean i1Ativo = i1.getStatus() == StatusIngresso.ATIVO && !e1.eventoJaAconteceu();
            boolean i2Ativo = i2.getStatus() == StatusIngresso.ATIVO && !e2.eventoJaAconteceu();

            if (i1Ativo && !i2Ativo) return -1;
            if (!i1Ativo && i2Ativo) return 1;

            return e1.getDataInicio().compareTo(e2.getDataInicio());
        });

        // Converte para DTO de resposta
        return ingressos.stream()
                .map(i -> {
                    Evento e = eventoRepository.buscarPorId(i.getEventoId());
                    return new IngressoResponseDTO(i, e.getNome(), e.getDataInicio(), e.getLocal());
                })
                .collect(Collectors.toList());
    }
}