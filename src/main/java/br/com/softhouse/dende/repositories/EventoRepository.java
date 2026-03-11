package br.com.softhouse.dende.repositories;

import br.com.softhouse.dende.model.Evento;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 REPOSITÓRIO DE EVENTOS

 Repositório para gerenciar os eventos, utilizando armazenamento em memória.
 Implementa o padrão Singleton para garantir que haja apenas uma instância do repositório.

 */

public class EventoRepository {

    // Instância única do repositório (padrão Singleton)
    private static EventoRepository instance;

    // Mapa para armazenar os eventos, onde a chave é o ID do evento e o valor é o objeto Evento
    private final Map<Long, Evento> eventos;
    private long proximoId; // Variável para gerar IDs únicos para os eventos

    private EventoRepository() {
        this.eventos = new HashMap<>();    // Inicializa o mapa de eventos como um HashMap vazio
        this.proximoId = 1;                // Define que o primeiro ID a ser usado será 1
    }

    // Metodo para obter a instância única do repositório
    public static synchronized EventoRepository getInstance() {
        if (instance == null) {                 // Verifica se a instância ainda não foi criada
            instance = new EventoRepository();  // Se não, cria uma nova instância do repositório
        }
        return instance;                        // Retorna a instância única do repositório
    }

    /** CRUD DE EVENTOS - Create, Read, Update, Delete */

    // Metodo para salvar um evento (criar ou atualizar)
    public Evento salvar(Evento evento) {

        if (evento.getId() == null) {   // Verifica se o evento ainda não tem um ID (novo evento)
            evento.setId(proximoId++);  // Atribui um ID único ao evento e incrementa o contador para o próximo ID
        }
        eventos.put(evento.getId(), evento); // Armazena o evento no mapa, usando o ID como chave
        return evento;
    }

    // Metodo para buscar um evento por ID
    public Evento buscarPorId(Long id) {
        return eventos.get(id);
    }

    // Metodo para buscar eventos por ID do organizador
    public List<Evento> buscarPorOrganizadorId(Long organizadorId) {

        // Filtra os eventos no mapa para retornar apenas aqueles cujo ID do organizador corresponde ao ID fornecido
        return eventos.values().stream()
                .filter(e -> e.getOrganizadorId().equals(organizadorId))
                .collect(Collectors.toList());
    }

    // Metodo para atualizar um evento existente
    public void atualizar(Evento evento) {

        // Verifica se o evento tem um ID válido (não nulo) antes de atualizar
        if (evento.getId() != null) {

            // Atualiza o evento no mapa, usando o ID como chave
            eventos.put(evento.getId(), evento);
        }
    }

    // Metodo para listar todos os eventos
    public List<Evento> listarTodos() {
        return List.copyOf(eventos.values());
    }

    // Metodo para listar apenas os eventos ativos, que ainda não aconteceram e que têm ingressos disponíveis
    public List<Evento> listarAtivos() {
        return eventos.values().stream()
                .filter(Evento::getAtivo)
                .filter(e -> !e.eventoJaAconteceu())
                .filter(Evento::temIngressosDisponiveis)
                .collect(Collectors.toList());
    }

    // Metodo para verificar se um organizador tem eventos ativos ou em andamento, usado para validação de exclusão de organizador
    public boolean organizadorTemEventosAtivosOuEmExecucao(Long organizadorId) {
        return eventos.values().stream()            // Percorre os eventos no mapa
                .anyMatch(e -> e.getOrganizadorId().equals(organizadorId) && // Verifica se o evento pertence ao organizador e se está ativo ou em andamento
                        (e.getAtivo() || e.eventoEmAndamento())); // Retorna true se encontrar algum evento que atenda a essas condições, caso contrário retorna false
    }
}