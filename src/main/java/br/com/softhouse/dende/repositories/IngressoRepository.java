package br.com.softhouse.dende.repositories;

import br.com.softhouse.dende.model.Ingresso;
import br.com.softhouse.dende.model.enums.StatusIngresso;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IngressoRepository {
    private static IngressoRepository instance;
    private final Map<Long, Ingresso> ingressos;
    private long proximoId;

    private IngressoRepository() {
        this.ingressos = new HashMap<>();
        this.proximoId = 1;
    }

    public static synchronized IngressoRepository getInstance() {
        if (instance == null) {
            instance = new IngressoRepository();
        }
        return instance;
    }

    public Ingresso salvar(Ingresso ingresso) {
        if (ingresso.getId() == null) {
            ingresso.setId(proximoId++);
        }
        ingressos.put(ingresso.getId(), ingresso);
        return ingresso;
    }

    public Ingresso buscarPorId(Long id) {
        return ingressos.get(id);
    }

    public List<Ingresso> buscarPorUsuarioId(Long usuarioId) {
        return ingressos.values().stream()
                .filter(i -> i.getUsuarioId().equals(usuarioId))
                .collect(Collectors.toList());
    }

    public List<Ingresso> buscarPorEventoId(Long eventoId) {
        return ingressos.values().stream()
                .filter(i -> i.getEventoId().equals(eventoId))
                .collect(Collectors.toList());
    }

    public void atualizar(Ingresso ingresso) {
        if (ingresso.getId() != null) {
            ingressos.put(ingresso.getId(), ingresso);
        }
    }

    public void reembolsarIngressosDoEvento(Long eventoId) {
        buscarPorEventoId(eventoId).stream()
                .filter(i -> i.getStatus() == StatusIngresso.ATIVO)
                .forEach(i -> {
                    i.reembolsar();
                    atualizar(i);
                });
    }

    public boolean existeIngressoAtivo(Long usuarioId, Long eventoId) {
        return ingressos.values().stream()
                .anyMatch(i -> i.getUsuarioId().equals(usuarioId) &&
                        i.getEventoId().equals(eventoId) &&
                        i.getStatus() == StatusIngresso.ATIVO);
    }
}