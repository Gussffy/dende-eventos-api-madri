package br.com.softhouse.dende.repositories;

import br.com.softhouse.dende.exceptions.DatabaseException;
import br.com.softhouse.dende.model.Empresa;
import br.com.softhouse.dende.model.Organizador;
import br.com.softhouse.dende.repositories.util.ConnectionPool;
import br.com.softhouse.dende.repositories.util.CrudRepository;
import br.com.softhouse.dende.repositories.util.rowmapper.OrganizadorRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * REPOSITÓRIO DE ORGANIZADORES
 *
 * Persistência JDBC para a tabela usuario, filtrando tipo_usuario = ORGANIZADOR.
 */
public class OrganizadorRepository implements CrudRepository<Organizador, Long> {

    private static OrganizadorRepository instance;

    private static final String SQL_INSERT = "INSERT INTO usuario (nome, data_nascimento, sexo, email, senha, tipo_usuario, ativo) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_ID = "SELECT id, nome, data_nascimento, sexo, email, senha, tipo_usuario, ativo FROM usuario WHERE id = ? AND tipo_usuario = 'ORGANIZADOR'";
    private static final String SQL_SELECT_BY_EMAIL = "SELECT id, nome, data_nascimento, sexo, email, senha, tipo_usuario, ativo FROM usuario WHERE email = ? AND tipo_usuario = 'ORGANIZADOR'";
    private static final String SQL_SELECT_ALL = "SELECT id, nome, data_nascimento, sexo, email, senha, tipo_usuario, ativo FROM usuario WHERE tipo_usuario = 'ORGANIZADOR' ORDER BY id";
    private static final String SQL_UPDATE = "UPDATE usuario SET nome = ?, data_nascimento = ?, sexo = ?, email = ?, senha = ?, tipo_usuario = ?, ativo = ? WHERE id = ? AND tipo_usuario = 'ORGANIZADOR'";
    private static final String SQL_DELETE = "DELETE FROM usuario WHERE id = ? AND tipo_usuario = 'ORGANIZADOR'";
    private static final String SQL_EXISTS_EMAIL = "SELECT 1 FROM usuario WHERE email = ? LIMIT 1";

    private final ConnectionPool connectionPool;
    private final OrganizadorRowMapper rowMapper;

    private OrganizadorRepository() {
        this.connectionPool = ConnectionPool.getInstance();
        this.rowMapper = new OrganizadorRowMapper();
    }

    public static synchronized OrganizadorRepository getInstance() {
        if (instance == null) {
            instance = new OrganizadorRepository();
        }
        return instance;
    }

    @Override
    public Organizador salvar(Organizador organizador) {
        if (organizador == null) {
            throw new br.com.softhouse.dende.exceptions.ValidationException("Organizador é obrigatório");
        }
        if (organizador.getId() == null) {
            inserir(organizador);
        } else {
            atualizar(organizador);
        }
        return organizador;
    }

    @Override
    public Organizador buscarPorId(Long id) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return carregarEmpresa(mapOrganizador(resultSet));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar organizador por id", e);
        }
    }

    public Organizador buscarPorEmail(String email) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_EMAIL)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return carregarEmpresa(mapOrganizador(resultSet));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar organizador por email", e);
        }
    }

    @Override
    public void atualizar(Organizador organizador) {
        if (organizador == null || organizador.getId() == null) {
            throw new br.com.softhouse.dende.exceptions.ValidationException("Organizador com ID é obrigatório para atualização");
        }

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            preencherStatement(statement, organizador);
            statement.setLong(8, organizador.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar organizador", e);
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
            throw new DatabaseException("Erro ao verificar email do organizador", e);
        }
    }

    @Override
    public void deletar(Long id) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao deletar organizador", e);
        }
    }

    @Override
    public List<Organizador> listarTodos() {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            List<Organizador> organizadores = new ArrayList<>();
            while (resultSet.next()) {
                organizadores.add(carregarEmpresa(mapOrganizador(resultSet)));
            }
            return organizadores;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar organizadores", e);
        }
    }

    private void inserir(Organizador organizador) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preencherStatement(statement, organizador);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    organizador.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar organizador", e);
        }
    }

    private void preencherStatement(PreparedStatement statement, Organizador organizador) throws SQLException {
        statement.setString(1, organizador.getNome());
        statement.setObject(2, organizador.getDataNascimento());
        statement.setString(3, sexoParaBanco(organizador.getSexo()));
        statement.setString(4, organizador.getEmail());
        statement.setString(5, organizador.getSenha());
        statement.setString(6, "ORGANIZADOR");
        statement.setBoolean(7, Boolean.TRUE.equals(organizador.getAtivo()));
    }

    private Organizador mapOrganizador(ResultSet resultSet) throws SQLException {
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
        return rowMapper.mapRow(row);
    }

    private Organizador carregarEmpresa(Organizador organizador) {
        if (organizador == null) {
            return null;
        }
        Empresa empresa = EmpresaRepository.getInstance().buscarPorOrganizadorId(organizador.getId());
        organizador.setEmpresa(empresa);
        return organizador;
    }

    private String sexoParaBanco(br.com.softhouse.dende.model.enums.Sexo sexo) {
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