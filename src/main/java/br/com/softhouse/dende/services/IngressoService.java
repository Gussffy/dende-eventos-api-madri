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
    SERVICE DE INGRESSOS

    Esta classe é responsável por gerenciar as operações relacionadas aos ingressos, como compra, cancelamento e listagem.
    Ela interage com os repositórios de ingressos, eventos e usuários para realizar as operações necessárias e aplicar as regras de negócio.
 */
public class IngressoService {

    // Instâncias dos repositórios para acessar os dados de ingressos, eventos e usuários
    private final IngressoRepository ingressoRepositorio;
    private final EventoRepository eventoRepositorio;
    private final UsuarioRepository usuarioRepositorio;

    // Construtor que inicializa os repositórios
    public IngressoService() {
        this.ingressoRepositorio = IngressoRepository.getInstance();
        this.eventoRepositorio = EventoRepository.getInstance();
        this.usuarioRepositorio = UsuarioRepository.getInstance();
    }

    // Metodo para comprar um ingresso para um evento específico (User story 13)
    public CompraResponseDTO comprar(Long organizadorId, Long eventoId, IngressoRequestDTO request) throws IllegalArgumentException {
        Usuario usuario = usuarioRepositorio.buscarPorEmail(request.getUsuarioEmail());

        // Validações para garantir que o usuário existe
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        // Validação para garantir que o usuário está ativo
        if (!usuario.getAtivo()) {
            throw new IllegalArgumentException("Usuário inativo não pode comprar ingressos");
        }


        Evento evento = eventoRepositorio.buscarPorId(eventoId);
        // Validações para garantir que o evento existe
        if (evento == null) {
            throw new IllegalArgumentException("Evento não encontrado");
        }
        // Validação para garantir que o evento pertence ao organizador informado
        if (!evento.getOrganizadorId().equals(organizadorId)) {
            throw new IllegalArgumentException("Evento não pertence a este organizador");
        }
        // Validação para garantir que o evento está ativo
        if (!evento.getAtivo()) {
            throw new IllegalArgumentException("Evento inativo não está vendendo ingressos");
        }
        // Validação para garantir que o evento ainda não aconteceu
        if (evento.eventoJaAconteceu()) {
            throw new IllegalArgumentException("Evento já aconteceu");
        }
        // Validação para garantir que o evento tem ingressos disponíveis
        if (!evento.temIngressosDisponiveis()) {
            throw new IllegalArgumentException("Ingressos esgotados");
        }
        // Validação para garantir que o usuário não possui um ingresso ativo para o mesmo evento
        if (ingressoRepositorio.existeIngressoAtivo(usuario.getId(), eventoId)) {
            throw new IllegalArgumentException("Você já possui um ingresso ativo para este evento");
        }

        //Calcula o valor esperado para a compra
        // Se o evento for um subevento, o valor esperado inclui o preço do ingresso do evento principal
        double valorEsperado = evento.getPrecoIngresso();
        if (evento.getEventoPrincipalId() != null) {
            Evento principal = eventoRepositorio.buscarPorId(evento.getEventoPrincipalId());
            if (principal != null && principal.getAtivo()) {
                valorEsperado += principal.getPrecoIngresso();
            }
        }

        //// Se o evento for um subevento, o valor esperado inclui o preço do ingresso do evento principal
        if (request.getTotalPago() != null && Math.abs(request.getTotalPago() - valorEsperado) > 0.01) {
            throw new IllegalArgumentException("Valor pago não corresponde ao preço do ingresso");
        }

        // Realiza a compra do ingresso, criando um ingresso principal para o evento e, se for um subevento, um ingresso vinculado para o evento principal
        double valorTotal = evento.getPrecoIngresso();
        List<String> codigos = new ArrayList<>();

        // Cria o ingresso principal para o evento
        Ingresso ingressoPrincipal = new Ingresso(usuario.getId(), eventoId, evento.getPrecoIngresso());
        ingressoRepositorio.salvar(ingressoPrincipal);  // Salva o ingresso principal no repositório
        evento.venderIngresso();                        // Incrementa a contagem de ingressos vendidos no evento
        eventoRepositorio.atualizar(evento);            // Atualiza o evento no repositório para refletir a venda do ingresso
        codigos.add(ingressoPrincipal.getCodigo());     // Adiciona o código do ingresso principal à lista de códigos para a resposta

        // Se o evento for um subevento, cria um ingresso vinculado para o evento principal
        if (evento.getEventoPrincipalId() != null) {
            Evento principal = eventoRepositorio.buscarPorId(evento.getEventoPrincipalId());
            // Validações para garantir que o evento principal existe, está ativo e tem ingressos disponíveis
            if (principal != null && principal.getAtivo() && principal.temIngressosDisponiveis()) {
                Ingresso ingressoVinculado = new Ingresso(usuario.getId(), principal.getId(), principal.getPrecoIngresso());
                ingressoVinculado.setEventoVinculadoId(eventoId);
                ingressoVinculado.setIngressoPrincipal(false);
                ingressoRepositorio.salvar(ingressoVinculado);
                principal.venderIngresso();
                eventoRepositorio.atualizar(principal);
                valorTotal += principal.getPrecoIngresso();
                codigos.add(ingressoVinculado.getCodigo());
            }
        }

        // Prepara a mensagem de resposta, indicando se um ou mais ingressos foram gerados com base na quantidade de códigos gerados
        String mensagem = codigos.size() > 1 ?
                "Compra realizada com sucesso! Ingressos gerados." :
                "Compra realizada com sucesso! Ingresso gerado.";

        return new CompraResponseDTO(mensagem, codigos, valorTotal, "AGUARDANDO_PAGAMENTO");
    }

    // Metodo para cancelar um ingresso específico de um usuário (User story 14)
    public CancelamentoResponseDTO cancelar(Long usuarioId, Long ingressoId) throws IllegalArgumentException {
        // Validações para garantir que o usuário existe
        Usuario usuario = usuarioRepositorio.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        Ingresso ingresso = ingressoRepositorio.buscarPorId(ingressoId);

        // Validações para garantir que o ingresso existe, pertence ao usuário e pode ser cancelado
        if (ingresso == null) {
            throw new IllegalArgumentException("Ingresso não encontrado");
        }

        // Validação para garantir que o ingresso pertence ao usuário que está tentando cancelar
        if (!ingresso.getUsuarioId().equals(usuarioId)) {
            throw new IllegalArgumentException("Ingresso não pertence a este usuário");
        }

        // Validação para garantir que o ingresso está ativo e pode ser cancelado (não pode ser cancelado se já tiver sido cancelado ou se o evento já tiver acontecido)
        if (!ingresso.podeSerCancelado()) {
            throw new IllegalArgumentException("Este ingresso não pode ser cancelado");
        }
        Evento evento = eventoRepositorio.buscarPorId(ingresso.getEventoId());

        // Validação para garantir que o evento associado ao ingresso ainda não aconteceu, pois não é possível cancelar ingressos de eventos já realizados
        if (evento.eventoJaAconteceu()) {
            throw new IllegalArgumentException("Não é possível cancelar ingresso de evento já realizado");
        }

        // Calcula o valor do reembolso com base nas regras do evento, considerando o valor pago pelo ingresso e a política de estorno do evento
        double valorReembolso = evento.calcularReembolso(ingresso.getValorPago());

        // Realiza o cancelamento do ingresso, alterando seu status para CANCELADO e atualizando o evento para refletir a disponibilidade do ingresso cancelado
        ingresso.cancelar();
        ingressoRepositorio.atualizar(ingresso);
        evento.cancelarIngresso();
        eventoRepositorio.atualizar(evento);

        // Prepara a resposta do cancelamento, incluindo a mensagem de sucesso, o valor pago, o valor do reembolso e o código do ingresso cancelado para referência
        return new CancelamentoResponseDTO(
                "Ingresso cancelado com sucesso",
                ingresso.getValorPago(),
                valorReembolso,
                ingresso.getCodigo()
        );
    }

    // Metodo para calcular o valor do reembolso de um ingresso específico, considerando as regras do evento e o valor pago pelo ingresso
    public double calcularReembolso(Long ingressoId) throws IllegalArgumentException {
        Ingresso ingresso = ingressoRepositorio.buscarPorId(ingressoId);

        // Validações para garantir que o ingresso existe
        if (ingresso == null) {
            throw new IllegalArgumentException("Ingresso não encontrado");
        }
        // Validação para garantir que o ingresso está ativo, pois somente ingressos ativos podem ser cancelados e reembolsados
        Evento evento = eventoRepositorio.buscarPorId(ingresso.getEventoId());
        return evento.calcularReembolso(ingresso.getValorPago());
    }

    // Metodo para listar todos os ingressos de um usuário específico (User story 15)
    public List<IngressoResponseDTO> listarPorUsuario(Long usuarioId) throws IllegalArgumentException {
        Usuario usuario = usuarioRepositorio.buscarPorId(usuarioId);

        // Validações para garantir que o usuário existe
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        List<Ingresso> ingressos = ingressoRepositorio.buscarPorUsuarioId(usuarioId);

        // Ordena os ingressos por status (ativos primeiro) e data do evento (mais próximos primeiro)
        ingressos.sort((i1, i2) -> {
            Evento e1 = eventoRepositorio.buscarPorId(i1.getEventoId());
            Evento e2 = eventoRepositorio.buscarPorId(i2.getEventoId());
            boolean i1Ativo = i1.getStatus() == StatusIngresso.ATIVO && !e1.eventoJaAconteceu();
            boolean i2Ativo = i2.getStatus() == StatusIngresso.ATIVO && !e2.eventoJaAconteceu();
            if (i1Ativo && !i2Ativo) return -1;
            if (!i1Ativo && i2Ativo) return 1;
            return e1.getDataInicio().compareTo(e2.getDataInicio());
        });

        // Mapeia os ingressos para DTOs de resposta, incluindo informações do evento (nome, data e local) para cada ingresso
        return ingressos.stream()
                .map(i -> {
                    Evento e = eventoRepositorio.buscarPorId(i.getEventoId());
                    return new IngressoResponseDTO(i, e.getNome(), e.getDataInicio(), e.getLocal());
                })
                .collect(Collectors.toList());
    }
}