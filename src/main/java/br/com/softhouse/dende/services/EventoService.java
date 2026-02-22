package br.com.softhouse.dende.services;

import br.com.softhouse.dende.dto.EventoRequestDTO;
import br.com.softhouse.dende.dto.EventoResponseDTO;
import br.com.softhouse.dende.dto.EventoResumoDTO;
import br.com.softhouse.dende.model.Evento;
import br.com.softhouse.dende.model.Organizador;
import br.com.softhouse.dende.model.Ingresso;
import br.com.softhouse.dende.model.enums.StatusIngresso;
import br.com.softhouse.dende.repositories.EventoRepository;
import br.com.softhouse.dende.repositories.OrganizadorRepository;
import br.com.softhouse.dende.repositories.IngressoRepository;
import java.util.List;
import java.util.stream.Collectors;

/**
    SERVICE DE EVENTOS

    Serviço responsável por toda a lógica de negócios relacionada a eventos, como cadastro, atualização, ativação, desativação e listagem.
    Ela atua como uma camada intermediária entre o Controller (camada de apresentação) e o
    Repositório (camada de dados).

 */
public class EventoService {

    // Repositórios para acessar os dados de eventos, organizadores e ingressos (CRUD)
    private final EventoRepository eventoRepositorio;
    private final OrganizadorRepository organizadorRepositorio;
    private final IngressoRepository ingressoRepositorio;

    public EventoService() {
        // Obtém a instância única dos repositórios de eventos, organizadores e ingressos (padrão Singleton)
        this.eventoRepositorio = EventoRepository.getInstance();
        this.organizadorRepositorio = OrganizadorRepository.getInstance();
        this.ingressoRepositorio = IngressoRepository.getInstance();
    }

    // Cadastrar Evento (User Stories 7)
    public EventoResponseDTO cadastrar(Long organizadorId, EventoRequestDTO request) throws IllegalArgumentException {

        // Validações de negócio para o cadastro de um evento, garantindo que todas as regras sejam respeitadas
        Organizador org = organizadorRepositorio.buscarPorId(organizadorId);
        if (org == null) {
            throw new IllegalArgumentException("Organizador não encontrado");
        }
        if (!org.getAtivo()) {
            throw new IllegalArgumentException("Organizador inativo não pode cadastrar eventos");
        }

        if (request.getNome() == null || request.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do evento é obrigatório");
        }
        if (request.getDataInicio() == null) {
            throw new IllegalArgumentException("Data de início é obrigatória");
        }
        if (request.getDataFinal() == null) {
            throw new IllegalArgumentException("Data de fim é obrigatória");
        }
        if (request.getTipoEvento() == null) {
            throw new IllegalArgumentException("Tipo do evento é obrigatório");
        }
        if (request.getModalidade() == null) {
            throw new IllegalArgumentException("Modalidade é obrigatória");
        }
        if (request.getCapacidadeMaxima() == null || request.getCapacidadeMaxima() <= 0) {
            throw new IllegalArgumentException("Capacidade máxima deve ser maior que zero");
        }
        if (request.getLocal() == null || request.getLocal().trim().isEmpty()) {
            throw new IllegalArgumentException("Local é obrigatório");
        }
        if (request.getPrecoIngresso() == null || request.getPrecoIngresso() < 0) {
            throw new IllegalArgumentException("Preço do ingresso deve ser maior ou igual a zero");
        }

        // Validação do evento principal, se fornecido, deve existir e pertencer ao mesmo organizador
        if (request.getEventoPrincipalId() != null) {
            Evento principal = eventoRepositorio.buscarPorId(request.getEventoPrincipalId());
            if (principal == null) {
                throw new IllegalArgumentException("Evento principal não encontrado");
            }
            if (!principal.getOrganizadorId().equals(organizadorId)) {
                throw new IllegalArgumentException("Evento principal não pertence a este organizador");
            }
        }

        // Criação do evento a partir do DTO de requisição e salvamento no repositório
        // O metodo toEntity() cria um novo objeto Evento com os dados do DTO e o
        Evento evento = request.toEntity(organizadorId);

        // Validação adicional para garantir que a data de início seja anterior à data de fim
        if (!evento.validarDatas()) {
            throw new IllegalArgumentException("Datas inválidas");
        }

        // O metodo salvar() atribui um ID único ao evento e o armazena no repositório
        evento = eventoRepositorio.salvar(evento);
        return new EventoResponseDTO(evento); // Retorna um DTO de resposta com os dados do evento criado
    }

    // Metodo auxiliar para buscar a entidade Evento por ID, usado internamente para validações e atualizações
    public Evento buscarEntidadePorId(Long id) throws IllegalArgumentException {
        Evento evento = eventoRepositorio.buscarPorId(id);
        if (evento == null) {
            throw new IllegalArgumentException("Evento não encontrado");
        }
        return evento;
    }

    // Atualizar Evento (User Stories 8)
    public EventoResponseDTO atualizar(Long organizadorId, Long eventoId, EventoRequestDTO request) throws IllegalArgumentException {
        Evento evento = buscarEntidadePorId(eventoId);

        // Validações de negócio para a atualização de um evento, garantindo que todas as regras sejam respeitadas
        if (!evento.getOrganizadorId().equals(organizadorId)) {
            throw new IllegalArgumentException("Este evento não pertence ao organizador");
        }
        if (!evento.getAtivo()) {
            throw new IllegalArgumentException("Não é possível alterar um evento inativo");
        }
        // Permite atualizar apenas os campos que foram fornecidos no DTO de requisição, mantendo os outros inalterados
        if (request.getNome() != null) evento.setNome(request.getNome());
        if (request.getPagina() != null) evento.setPagina(request.getPagina());
        if (request.getDescricao() != null) evento.setDescricao(request.getDescricao());
        if (request.getDataInicio() != null) evento.setDataInicio(request.getDataInicio());
        if (request.getDataFinal() != null) evento.setDataFinal(request.getDataFinal());
        if (request.getTipoEvento() != null) evento.setTipoEvento(request.getTipoEvento());
        if (request.getEventoPrincipalId() != null) {

            // Se o ID do evento principal for fornecido, valida se ele existe e pertence ao mesmo organizador antes de atualizar
            if (request.getEventoPrincipalId() != 0) {
                Evento principal = eventoRepositorio.buscarPorId(request.getEventoPrincipalId());

                // Se o evento principal não for encontrado, lança uma exceção
                if (principal == null) {
                    throw new IllegalArgumentException("Evento principal não encontrado");
                }
                // Se o evento principal não pertencer ao mesmo organizador, lança uma exceção
                if (!principal.getOrganizadorId().equals(organizadorId)) {
                    throw new IllegalArgumentException("Evento principal não pertence a este organizador");
                }
            } // Atualiza o ID do evento principal, permitindo que seja definido como null (sem evento principal) ou um ID válido
            evento.setEventoPrincipalId(request.getEventoPrincipalId());
        }

        // Atualiza os campos do evento existente apenas se eles forem fornecidos no DTO de requisição (não nulos)
        if (request.getModalidade() != null) evento.setModalidade(request.getModalidade());
        if (request.getCapacidadeMaxima() != null) evento.setCapacidadeMaxima(request.getCapacidadeMaxima());
        if (request.getLocal() != null) evento.setLocal(request.getLocal());
        if (request.getPrecoIngresso() != null) evento.setPrecoIngresso(request.getPrecoIngresso());
        if (request.getEstornaCancelamento() != null) evento.setEstornaCancelamento(request.getEstornaCancelamento());
        if (request.getTaxaEstorno() != null) evento.setTaxaEstorno(request.getTaxaEstorno());

        // Validação adicional para garantir que a data de início seja anterior à data de fim após a atualização
        if (!evento.validarDatas()) {
            throw new IllegalArgumentException("Datas inválidas após alteração");
        }

        // Salva as alterações no repositório e retorna um DTO de resposta com os dados do evento atualizado
        eventoRepositorio.atualizar(evento);
        return new EventoResponseDTO(evento);
    }

    // Ativar Evento (User Stories 9)
    public EventoResponseDTO ativar(Long organizadorId, Long eventoId) throws IllegalArgumentException {
        Evento evento = buscarEntidadePorId(eventoId);

        // Validações de negócio para a ativação de um evento, garantindo que todas as regras sejam respeitadas
        if (!evento.getOrganizadorId().equals(organizadorId)) {
            throw new IllegalArgumentException("Este evento não pertence ao organizador");
        }

        if (evento.getAtivo()) {
            throw new IllegalArgumentException("Evento já está ativo");
        }

        if (!evento.podeSerAtivado()) {
            throw new IllegalArgumentException("Evento não pode ser ativado");
        }

        // Ativa o evento, salva as alterações no repositório e retorna um DTO de resposta com os dados do evento ativado
        evento.setAtivo(true);
        eventoRepositorio.atualizar(evento);
        return new EventoResponseDTO(evento);
    }

    // Desativar Evento (User Stories 10)
    public EventoResponseDTO desativar(Long organizadorId, Long eventoId) throws IllegalArgumentException {
        Evento evento = buscarEntidadePorId(eventoId);

        // Validações de negócio para a desativação de um evento, garantindo que todas as regras sejam respeitadas
        if (!evento.getOrganizadorId().equals(organizadorId)) {
            throw new IllegalArgumentException("Este evento não pertence ao organizador");
        }
        if (!evento.getAtivo()) {
            throw new IllegalArgumentException("Evento já está inativo");
        }

        // Se o evento tiver ingressos vendidos, reembolsa os ingressos ativos e zera a contagem de ingressos vendidos antes de desativar o evento
        if (evento.getIngressosVendidos() > 0) {
            List<Ingresso> ingressos = ingressoRepositorio.buscarPorEventoId(eventoId);
            for (Ingresso ingresso : ingressos) {
                if (ingresso.getStatus() == StatusIngresso.ATIVO) {
                    ingresso.reembolsar();
                    ingressoRepositorio.atualizar(ingresso);
                }
            }
            evento.setIngressosVendidos(0);
        }

        // Desativa o evento, salva as alterações no repositório e retorna um DTO de resposta com os dados do evento desativado
        evento.setAtivo(false);
        eventoRepositorio.atualizar(evento);
        return new EventoResponseDTO(evento);
    }

    // Listar Eventos por Organizador (User Stories 11)
    public List<EventoResumoDTO> listarPorOrganizador(Long organizadorId) {
        return eventoRepositorio.buscarPorOrganizadorId(organizadorId).stream()
                .map(EventoResumoDTO::new)
                .collect(Collectors.toList());
    }

    // Listar Eventos Ativos para Feed (User Stories 12)
    public List<EventoResponseDTO> feedAtivos() {
        List<Evento> eventos = eventoRepositorio.listarAtivos();

        // Ordena os eventos por data de início (mais próximos primeiro) e, em caso de empate, por nome do evento (ordem alfabética)
        eventos.sort((e1, e2) -> {
            int cmp = e1.getDataInicio().compareTo(e2.getDataInicio());
            if (cmp == 0) cmp = e1.getNome().compareTo(e2.getNome());
            return cmp;
        });
        // Converte a lista de eventos para uma lista de DTOs de resposta, mantendo apenas os eventos que atendem aos critérios de listagem
        return eventos.stream()
                .map(EventoResponseDTO::new)
                .collect(Collectors.toList());
    }
}