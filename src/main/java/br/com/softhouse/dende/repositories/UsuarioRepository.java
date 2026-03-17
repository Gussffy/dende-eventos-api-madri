package br.com.softhouse.dende.repositories;

import br.com.softhouse.dende.model.Usuario;

import java.util.HashMap;
import java.util.Map;


/**
 * REPOSITÓRIO DE USUÁRIOS
 *
 * Esta classe é responsável por simular um banco de dados em memória para usuários.
 * Ela implementa o padrão SINGLETON, garantindo que exista apenas uma instância
 * em toda a aplicação.
 *
 * Funciona como se fosse uma "tabela" no banco de dados, com índices para busca
 * rápida por ID e por Email.
 */

public class UsuarioRepository {

    // Instância única do repositório (padrão Singleton)
    private static UsuarioRepository instance;

    // Mapas para armazenar os usuários, onde a chave é o ID ou Email do usuário e o valor é o objeto Usuario
    private final Map<Long, Usuario> usuariosPorId;
    private final Map<String, Usuario> usuariosPorEmail;

    // Variável para gerar IDs únicos para os usuários
    private long proximoId;

    // Construtor privado para impedir a criação de múltiplas instâncias
    private UsuarioRepository() {

        // Inicializa o mapa de IDs como um HashMap vazio
        this.usuariosPorId = new HashMap<>();

        // Inicializa o mapa de Emails como um HashMap vazio
        this.usuariosPorEmail = new HashMap<>();

        // Define que o primeiro ID a ser usado será 1
        this.proximoId = 1;
    }

    // Metodo para obter a instância única do repositório
    public static synchronized UsuarioRepository getInstance() {

        if (instance == null) {                 // Verifica se a instância ainda não foi criada
            instance = new UsuarioRepository(); // Cria a instância (chama o construtor privado)
        }
        return instance;                        // Retorna a instância (nova ou existente)

    }

    /** CRUD DE USUÁRIOS - Create, Read, Update, Delete */

    // Metodo para salvar um usuário (criar ou atualizar)
    public Usuario salvar(Usuario usuario) {
        if (usuario.getId() == null) {                       // Verifica se o usuário não tem ID (é novo)
            usuario.setId(proximoId++);                      // Atribui um ID único ao usuário e incrementa o contador para o próximo ID
        }
        usuariosPorId.put(usuario.getId(), usuario);        // Armazena o usuário no mapa de IDs, usando o ID como chave
        usuariosPorEmail.put(usuario.getEmail(), usuario);  // Armazena o usuário no mapa de Emails, usando o Email como chave
        return usuario;
    }

    // Metodo para buscar um usuário por ID
    public Usuario buscarPorId(Long id) {
        return usuariosPorId.get(id);
    }

    // Metodo para buscar um usuário por email
    public Usuario buscarPorEmail(String email) {
        return usuariosPorEmail.get(email);
    }

    // Metodo para atualizar um usuário existente
    public void atualizar(Usuario usuario) {

        // Verifica se o usuário tem um ID (deve existir para ser atualizado)
        if (usuario.getId() != null) {
            Usuario existente = usuariosPorId.get(usuario.getId()); // Busca o usuário existente pelo ID

            // Verifica se o usuario existe e se o email foi alterado
            if (existente != null && !existente.getEmail().equals(usuario.getEmail())) {

                // Remove o usuário antigo do mapa de Emails (usando o email antigo como chave)
                usuariosPorEmail.remove(existente.getEmail());

                // Atualiza o mapa de Emails com a nova entrada de email
                usuariosPorEmail.put(usuario.getEmail(), usuario);
            }

            // Atualiza o mapa de IDs (substitui o usuário antigo pelo novo)
            usuariosPorId.put(usuario.getId(), usuario);
        }
    }

    // Metodo para verificar se um email já existe
    public boolean emailExiste(String email) {
        return usuariosPorEmail.containsKey(email);
    }
}