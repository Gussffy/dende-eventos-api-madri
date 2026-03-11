package br.com.softhouse.dende.services;

import br.com.softhouse.dende.dto.EventoRequestDTO;
import br.com.softhouse.dende.dto.EventoResponseDTO;
import br.com.softhouse.dende.dto.EventoResumoDTO;
import br.com.softhouse.dende.mappers.EventoMapper;
import br.com.softhouse.dende.model.Evento;
import br.com.softhouse.dende.model.Organizador;
import br.com.softhouse.dende.model.Ingresso;
import br.com.softhouse.dende.model.enums.StatusIngresso;
import br.com.softhouse.dende.repositories.EventoRepository;
import br.com.softhouse.dende.repositories.IngressoRepository;
import br.com.softhouse.dende.repositories.OrganizadorRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 SERVICE DE EVENTOS

 Serviço responsável por toda a lógica de negócios relacionada a eventos, como cadastro, atualização, ativação, desativação e listagem.
 Ela atua como uma camada intermediária entre o Controller (camada de apresentação) e o
 Repositório (camada de dados).

 Princípios aplicados:
 - Single Responsibility: Cada metodo tem uma responsabilidade única
 - Validações: Todas as regras de negócio são validadas aqui
 - Tratamento de exceções: Lança exceções com mensagens claras para o controller
 */
public class EventoService {

    private final EventoRepository eventoRepository;
    private final OrganizadorRepository organizadorRepository;
    private final IngressoRepository ingressoRepository;

    public EventoService() {
        this.eventoRepository = EventoRepository.getInstance();
        this.organizadorRepository = OrganizadorRepository.getInstance();
        this.ingressoRepository = IngressoRepository.getInstance();
    }

    public EventoResponseDTO cadastrar(Long organizadorId, EventoRequestDTO request) throws IllegalArgumentException {
        // Verificar se o organizador existe e está ativo
        Organizador org = organizadorRepository.buscarPorId(organizadorId);
        if (org == null) {
            throw new IllegalArgumentException("Organizador não encontrado");
        }
        if (!org.getAtivo()) {
            throw new IllegalArgumentException("Organizador inativo não pode cadastrar eventos");
        }

        // Validar evento principal (se existir)
        if (request.getEventoPrincipalId() != null) {
            Evento principal = eventoRepository.buscarPorId(request.getEventoPrincipalId());
            if (principal == null) {
                throw new IllegalArgumentException("Evento principal não encontrado");
            }
            if (!principal.getOrganizadorId().equals(organizadorId)) {
                throw new IllegalArgumentException("Evento principal não pertence a este organizador");
            }
        }

        // Converter DTO para entidade usando o mapper
        Evento evento = EventoMapper.toEntity(request, organizadorId);

        // US7: Validar datas
        if (!evento.validarDatas()) {
            throw new IllegalArgumentException("Datas inválidas: verifique se a data de início é futura, " +
                    "data de fim é posterior à data de início e duração mínima de 30 minutos");
        }

        // Evento começa inativo (US7)
        evento.setAtivo(false);

        // Salvar no repositório
        evento = eventoRepository.salvar(evento);

        // Retornar DTO de resposta
        return EventoMapper.toResponseDTO(evento);
    }

    public EventoResponseDTO buscarPorId(Long id) throws IllegalArgumentException {
        Evento evento = eventoRepository.buscarPorId(id);
        if (evento == null) {
            throw new IllegalArgumentException("Evento não encontrado");
        }
        return EventoMapper.toResponseDTO(evento);
    }

    public Evento buscarEntidadePorId(Long id) throws IllegalArgumentException {
        Evento evento = eventoRepository.buscarPorId(id);
        if (evento == null) {
            throw new IllegalArgumentException("Evento não encontrado");
        }
        return evento;
    }

    public EventoResponseDTO atualizar(Long organizadorId, Long eventoId, EventoRequestDTO request) throws IllegalArgumentException {
        Evento existente = buscarEntidadePorId(eventoId);

        // Verificar se o evento pertence ao organizador
        if (!existente.getOrganizadorId().equals(organizadorId)) {
            throw new IllegalArgumentException("Este evento não pertence ao organizador");
        }

        // US8: Só pode alterar eventos ativos
        if (!existente.getAtivo()) {
            throw new IllegalArgumentException("Não é possível alterar um evento inativo");
        }

        // Validar evento principal se foi alterado
        if (request.getEventoPrincipalId() != null && !request.getEventoPrincipalId().equals(existente.getEventoPrincipalId())) {
            Evento principal = eventoRepository.buscarPorId(request.getEventoPrincipalId());
            if (principal == null) {
                throw new IllegalArgumentException("Evento principal não encontrado");
            }
            if (!principal.getOrganizadorId().equals(organizadorId)) {
                throw new IllegalArgumentException("Evento principal não pertence a este organizador");
            }
        }

        // Atualizar entidade com dados do DTO
        Evento eventoAtualizado = EventoMapper.updateEntity(existente, request);

        // Validar datas novamente se foram alteradas
        if (!eventoAtualizado.validarDatas()) {
            throw new IllegalArgumentException("Datas inválidas após alteração");
        }

        // Salvar alterações
        eventoRepository.atualizar(eventoAtualizado);

        // Retornar DTO de resposta
        return EventoMapper.toResponseDTO(eventoAtualizado);
    }

    public EventoResponseDTO ativar(Long organizadorId, Long eventoId) throws IllegalArgumentException {
        Evento evento = buscarEntidadePorId(eventoId);

        if (!evento.getOrganizadorId().equals(organizadorId)) {
            throw new IllegalArgumentException("Este evento não pertence ao organizador");
        }

        if (evento.getAtivo()) {
            throw new IllegalArgumentException("Evento já está ativo");
        }

        if (!evento.podeSerAtivado()) {
            throw new IllegalArgumentException("Evento não pode ser ativado: verifique as datas");
        }

        evento.setAtivo(true);
        eventoRepository.atualizar(evento);

        return EventoMapper.toResponseDTO(evento);
    }

    public EventoResponseDTO desativar(Long organizadorId, Long eventoId) throws IllegalArgumentException {
        Evento evento = buscarEntidadePorId(eventoId);

        if (!evento.getOrganizadorId().equals(organizadorId)) {
            throw new IllegalArgumentException("Este evento não pertence ao organizador");
        }

        if (!evento.getAtivo()) {
            throw new IllegalArgumentException("Evento já está inativo");
        }

        // US10: Se tiver ingressos vendidos, reembolsar
        if (evento.getIngressosVendidos() > 0) {
            List<Ingresso> ingressos = ingressoRepository.buscarPorEventoId(eventoId);
            for (Ingresso ingresso : ingressos) {
                if (ingresso.getStatus() == StatusIngresso.ATIVO) {
                    ingresso.reembolsar();
                    ingressoRepository.atualizar(ingresso);
                }
            }
            evento.setIngressosVendidos(0);
        }

        evento.setAtivo(false);
        eventoRepository.atualizar(evento);

        return EventoMapper.toResponseDTO(evento);
    }

    public List<EventoResumoDTO> listarPorOrganizador(Long organizadorId) {
        return eventoRepository.buscarPorOrganizadorId(organizadorId).stream()
                .map(EventoMapper::toResumoDTO)
                .collect(Collectors.toList());
    }

    public List<EventoResponseDTO> feedAtivos() {
        List<Evento> eventos = eventoRepository.listarAtivos();

        // US12: Ordenar por data de início e nome
        eventos.sort((e1, e2) -> {
            int cmp = e1.getDataInicio().compareTo(e2.getDataInicio());
            if (cmp == 0) {
                cmp = e1.getNome().compareTo(e2.getNome());
            }
            return cmp;
        });

        return eventos.stream()
                .map(EventoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public boolean organizadorTemEventosAtivos(Long organizadorId) {
        return eventoRepository.organizadorTemEventosAtivosOuEmExecucao(organizadorId);
    }
}