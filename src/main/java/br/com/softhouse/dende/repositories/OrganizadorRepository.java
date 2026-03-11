package br.com.softhouse.dende.repositories;

import br.com.softhouse.dende.model.Organizador;

import java.util.HashMap;
import java.util.Map;

/**
 * REPOSITÓRIO DE ORGANIZADORES
 *
 * Repositório para gerenciar os organizadores, utilizando armazenamento em memória.
 * Implementa o padrão Singleton para garantir que haja apenas uma instância do repositório.
 */

public class OrganizadorRepository {

    // Instância única do repositório (padrão Singleton)
    private static OrganizadorRepository instance;

    /**
     * Mapas para buscar organizadores por ID, Email e CNPJ
     * Map<Long, Organizador>: estrutura chave-valor onde a chave é Long (ID) e o valor é Organizador
     * Map<String, Organizador>: estrutura chave-valor onde a chave é String (Email ou CNPJ) e o valor é Organizador
     */

    private final Map<Long, Organizador> organizadoresPorId;
    private final Map<String, Organizador> organizadoresPorEmail;
    private final Map<String, Organizador> organizadoresPorCnpj;

    // Variável para gerar IDs únicos para os organizadores
    private long proximoId;

    // Construtor privado para impedir a criação de múltiplas instâncias
    private OrganizadorRepository() {
        this.organizadoresPorId = new HashMap<>();      // Inicializa o mapa de IDs como um HashMap vazio
        this.organizadoresPorEmail = new HashMap<>();   // Inicializa o mapa de Emails como um HashMap vazio
        this.organizadoresPorCnpj = new HashMap<>();    // Inicializa o mapa de CNPJs como um HashMap vazio

        this.proximoId = 1;     // Define que o primeiro ID a ser usado será 1
    }

    // Metodo para obter a instância única do repositório
    public static synchronized OrganizadorRepository getInstance() {
        if (instance == null) {
            instance = new OrganizadorRepository();
        }
        return instance;
    }

    /** CRUD DE ORGANIZADORES - Create, Read, Update, Delete */

    // Metodo para salvar um organizador (criar ou atualizar)
    public Organizador salvar(Organizador organizador) {

        if (organizador.getId() == null) {      //Verifica se o organizador ainda não tem um ID (novo organizador)
            organizador.setId(proximoId++);     //Atribui um ID único ao organizador e incrementa o contador de IDs
        }

        organizadoresPorId.put(organizador.getId(), organizador);   //Armazena o organizador no mapa de IDs, usando o ID como chave
        organizadoresPorEmail.put(organizador.getEmail(), organizador); //Armazena o organizador no mapa de Emails, usando o Email como chave

        // Verifica se o CNPJ do organizador não é nulo ou vazio antes de armazenar no mapa de CNPJs
        if (organizador.getCnpj() != null && !organizador.getCnpj().isEmpty()) {
            organizadoresPorCnpj.put(organizador.getCnpj(), organizador);
        }

        // Retorna o organizador salvo (com ID atribuído)
        return organizador;
    }

    // Metodo para buscar um organizador por ID
    public Organizador buscarPorId(Long id) {
        return organizadoresPorId.get(id);
    }

    // Metodo para buscar um organizador por email
    public Organizador buscarPorEmail(String email) {
        return organizadoresPorEmail.get(email);
    }

    // Metodo para buscar um organizador por CNPJ
    public Organizador buscarPorCnpj(String cnpj) {
        return organizadoresPorCnpj.get(cnpj);
    }

    // Metodo para atualizar um organizador existente
    public void atualizar(Organizador organizador) {

        // Verifica se o organizador tem um ID (deve ter para ser atualizado)
        if (organizador.getId() != null) {
            Organizador existente = organizadoresPorId.get(organizador.getId()); // Busca o organizador existente pelo ID

            // Verifica se o organizador existe e se o email foi alterado
            if (existente != null) {
                if (!existente.getEmail().equals(organizador.getEmail())) {

                    // Remove o organizador antigo do mapa de Emails (usando o email antigo como chave)
                    organizadoresPorEmail.remove(existente.getEmail());

                    // Atualiza o mapa de Emails com a nova entrada de email
                    organizadoresPorEmail.put(organizador.getEmail(), organizador);
                }

                // Verifica se o CNPJ foi alterado e se o novo CNPJ não é nulo ou vazio
                if (existente.getCnpj() != null && organizador.getCnpj() != null &&
                        !existente.getCnpj().equals(organizador.getCnpj())) {

                    // Remove o organizador antigo do mapa de CNPJs (usando o CNPJ antigo como chave)
                    organizadoresPorCnpj.remove(existente.getCnpj());

                    // Atualiza o mapa de CNPJs com a nova entrada de CNPJ
                    organizadoresPorCnpj.put(organizador.getCnpj(), organizador);
                }

                // Atualiza o mapa de IDs com a nova entrada do organizador
                organizadoresPorId.put(organizador.getId(), organizador);
            }
        }
    }

    // Metodo para verificar se um email já existe no repositório
    public boolean emailExiste(String email) {
        return organizadoresPorEmail.containsKey(email);
    }

    // Metodo para verificar se um CNPJ já existe no repositório
    public boolean cnpjExiste(String cnpj) {
        return cnpj != null && !cnpj.isEmpty() && organizadoresPorCnpj.containsKey(cnpj);
    }
}