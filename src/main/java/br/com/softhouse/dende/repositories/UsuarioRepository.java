package br.com.softhouse.dende.repositories;

import br.com.softhouse.dende.exceptions.DatabaseException;
import br.com.softhouse.dende.model.Usuario;
import br.com.softhouse.dende.model.enums.Sexo;
import br.com.softhouse.dende.repositories.util.ConnectionPool;
import br.com.softhouse.dende.repositories.util.CrudRepository;
import br.com.softhouse.dende.repositories.util.rowmapper.UsuarioRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * REPOSITÓRIO DE USUÁRIOS
 *
 * Esta classe é responsável por persistir usuários no banco de dados.
 * Ela implementa o padrão SINGLETON, garantindo que exista apenas uma instância
 * em toda a aplicação.
 */

public class UsuarioRepository implements CrudRepository<Usuario, Long> {

    private static UsuarioRepository instance;

    private static final String SQL_INSERT = "INSERT INTO usuario (nome, data_nascimento, sexo, email, senha, tipo_usuario, ativo) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_ID = "SELECT id, nome, data_nascimento, sexo, email, senha, tipo_usuario, ativo FROM usuario WHERE id = ? AND tipo_usuario = 'COMUM'";
    private static final String SQL_SELECT_BY_EMAIL = "SELECT id, nome, data_nascimento, sexo, email, senha, tipo_usuario, ativo FROM usuario WHERE email = ? AND tipo_usuario = 'COMUM'";
    private static final String SQL_SELECT_ALL = "SELECT id, nome, data_nascimento, sexo, email, senha, tipo_usuario, ativo FROM usuario WHERE tipo_usuario = 'COMUM' ORDER BY id";
    private static final String SQL_UPDATE = "UPDATE usuario SET nome = ?, data_nascimento = ?, sexo = ?, email = ?, senha = ?, tipo_usuario = ?, ativo = ? WHERE id = ? AND tipo_usuario = 'COMUM'";
    private static final String SQL_DELETE = "DELETE FROM usuario WHERE id = ? AND tipo_usuario = 'COMUM'";
    private static final String SQL_EXISTS_EMAIL = "SELECT 1 FROM usuario WHERE email = ? LIMIT 1";

    private final ConnectionPool connectionPool;
    private final UsuarioRowMapper usuarioRowMapper;

    private UsuarioRepository() {
        this.connectionPool = ConnectionPool.getInstance();
        this.usuarioRowMapper = new UsuarioRowMapper();
    }

    // Metodo para obter a instância única do repositório
    public static synchronized UsuarioRepository getInstance() {

        if (instance == null) {                 // Verifica se a instância ainda não foi criada
            instance = new UsuarioRepository(); // Cria a instância (chama o construtor privado)
        }
        return instance;                        // Retorna a instância (nova ou existente)

    }

    @Override
    public Usuario salvar(Usuario usuario) {
        if (usuario == null) {
            throw new br.com.softhouse.dende.exceptions.ValidationException("Usuário é obrigatório");
        }

        if (usuario.getId() == null) {
            inserir(usuario);
        } else {
            atualizar(usuario);
        }
        return usuario;
    }

    @Override
    public Usuario buscarPorId(Long id) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUsuario(resultSet);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar usuário por id", e);
        }
    }

    public Usuario buscarPorEmail(String email) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_EMAIL)) {
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUsuario(resultSet);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar usuário por email", e);
        }
    }

    @Override
    public void atualizar(Usuario usuario) {
        if (usuario == null || usuario.getId() == null) {
            throw new br.com.softhouse.dende.exceptions.ValidationException("Usuário com ID é obrigatório para atualização");
        }

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            preencherStatementUsuario(statement, usuario);
            statement.setLong(8, usuario.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar usuário", e);
        }
    }

    public boolean emailExiste(String email) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_EMAIL)) {
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao verificar email do usuário", e);
        }
    }

    @Override
    public void deletar(Long id) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao deletar usuário", e);
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {

            List<Usuario> usuarios = new ArrayList<>();
            while (resultSet.next()) {
                usuarios.add(mapUsuario(resultSet));
            }
            return usuarios;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar usuários", e);
        }
    }

    private void inserir(Usuario usuario) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preencherStatementUsuario(statement, usuario);
            statement.setString(6, "COMUM");
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuario.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar usuário", e);
        }
    }

    private void preencherStatementUsuario(PreparedStatement statement, Usuario usuario) throws SQLException {
        statement.setString(1, usuario.getNome());
        statement.setObject(2, usuario.getDataNascimento());
        statement.setString(3, sexoParaBanco(usuario.getSexo()));
        statement.setString(4, usuario.getEmail());
        statement.setString(5, usuario.getSenha());
        statement.setString(6, "COMUM");
        statement.setBoolean(7, Boolean.TRUE.equals(usuario.getAtivo()));
    }

    private Usuario mapUsuario(ResultSet resultSet) throws SQLException {
        String[] row = new String[]{
                String.valueOf(resultSet.getLong("id")),
                resultSet.getString("nome"),
                resultSet.getDate("data_nascimento").toLocalDate().toString(),
                resultSet.getString("sexo"),
                resultSet.getString("email"),
                resultSet.getString("senha"),
                resultSet.getString("tipo_usuario"),
                String.valueOf(resultSet.getBoolean("ativo"))
        };
        return usuarioRowMapper.mapRow(row);
    }

    private String sexoParaBanco(Sexo sexo) {
        if (sexo == null) {
            return null;
        }

        switch (sexo) {
            case MASCULINO:
                return "M";
            case FEMININO:
                return "F";
            case OUTRO:
                return "O";
            default:
                return sexo.name();
        }
    }
}