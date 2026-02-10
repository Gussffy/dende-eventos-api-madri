package br.com.softhouse.dende.repositories;

import br.com.softhouse.dende.model.Organizador;

import java.util.HashMap;
import java.util.Map;

public class OrganizadorRepository {
    private static OrganizadorRepository instance = new OrganizadorRepository();
    private final Map<String, Organizador> organizadores;

    private OrganizadorRepository() {
        this.organizadores = new HashMap<>();
    }

    public static OrganizadorRepository getInstance() {
        return instance;
    }

    public void cadastrarOrganizador(Organizador organizador) {
        this.organizadores.put(organizador.getEmail(), organizador);
    }

    public Organizador buscarOrganizadorPorEmail(String email) {
        return organizadores.get(email);
    }
}