package br.com.softhouse.dende.repositories;

import br.com.softhouse.dende.model.Ingresso;
import br.com.softhouse.dende.model.enums.StatusIngresso;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REPOSITÓRIO DE INGRESSOS
 *
 * Repositório para gerenciar os ingressos, utilizando armazenamento em memória.
 * Implementa o padrão Singleton para garantir que haja apenas uma instância do repositório.
 */
public class IngressoRepository {
    private static IngressoRepository instance;
    private final Map<Long, Ingresso> ingressos;
    private long proximoId;

    // Construtor privado para impedir a criação de múltiplas instâncias
    private IngressoRepository() {
        this.ingressos = new HashMap<>();
        this.proximoId = 1;
    }

    // Metodo para obter a instância única do repositório
    public static synchronized IngressoRepository getInstance() {
        if (instance == null) {
            instance = new IngressoRepository();
        }
        return instance;
    }

    /** CRUD DE INGRESSOS - Create, Read, Update, Delete */

    // Metodo para salvar um ingresso (criar ou atualizar)
    public Ingresso salvar(Ingresso ingresso) {
        if (ingresso.getId() == null) {
            ingresso.setId(proximoId++);
        }
        ingressos.put(ingresso.getId(), ingresso);
        return ingresso;
    }

    // Metodo para buscar um ingresso por ID
    public Ingresso buscarPorId(Long id) {
        return ingressos.get(id);
    }

    // Metodo para buscar ingressos por ID do usuário
    public List<Ingresso> buscarPorUsuarioId(Long usuarioId) {
        return ingressos.values().stream()
                .filter(i -> i.getUsuarioId().equals(usuarioId))
                .collect(Collectors.toList());
    }

    // Metodo para buscar ingressos por ID do evento
    public List<Ingresso> buscarPorEventoId(Long eventoId) {
        return ingressos.values().stream()
                .filter(i -> i.getEventoId().equals(eventoId))
                .collect(Collectors.toList());
    }

    // Metodo para atualizar um ingresso existente
    public void atualizar(Ingresso ingresso) {
        if (ingresso.getId() != null) {
            ingressos.put(ingresso.getId(), ingresso);
        }
    }

    // Metodo para reembolsar todos os ingressos ativos de um evento
    public void reembolsarIngressosDoEvento(Long eventoId) {
        buscarPorEventoId(eventoId).stream()
                .filter(i -> i.getStatus() == StatusIngresso.ATIVO)
                .forEach(i -> {
                    i.reembolsar();
                    atualizar(i);
                });
    }

    // Metodo para verificar se um usuário possui um ingresso ativo para um evento específico
    public boolean existeIngressoAtivo(Long usuarioId, Long eventoId) {
        return ingressos.values().stream()
                .anyMatch(i -> i.getUsuarioId().equals(usuarioId) &&
                        i.getEventoId().equals(eventoId) &&
                        i.getStatus() == StatusIngresso.ATIVO);
    }
}