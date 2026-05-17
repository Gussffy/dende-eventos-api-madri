package br.com.softhouse.dende.repositories;

import br.com.softhouse.dende.exceptions.DatabaseException;
import br.com.softhouse.dende.model.Empresa;
import br.com.softhouse.dende.repositories.util.ConnectionPool;
import br.com.softhouse.dende.repositories.util.CrudRepository;
import br.com.softhouse.dende.repositories.util.rowmapper.EmpresaRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * REPOSITÓRIO DE EMPRESA
 *
 * Persistência JDBC para a tabela empresa.
 */
public class EmpresaRepository implements CrudRepository<Empresa, Long> {

    private static EmpresaRepository instance;

    private static final String SQL_INSERT = "INSERT INTO empresa (organizador_id, cnpj, razao_social, nome_fantasia) VALUES (?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_ID = "SELECT id, organizador_id, cnpj, razao_social, nome_fantasia FROM empresa WHERE id = ?";
    private static final String SQL_SELECT_BY_ORGANIZADOR = "SELECT id, organizador_id, cnpj, razao_social, nome_fantasia FROM empresa WHERE organizador_id = ?";
    private static final String SQL_SELECT_BY_CNPJ = "SELECT id, organizador_id, cnpj, razao_social, nome_fantasia FROM empresa WHERE cnpj = ?";
    private static final String SQL_SELECT_ALL = "SELECT id, organizador_id, cnpj, razao_social, nome_fantasia FROM empresa ORDER BY id";
    private static final String SQL_UPDATE = "UPDATE empresa SET organizador_id = ?, cnpj = ?, razao_social = ?, nome_fantasia = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM empresa WHERE id = ?";
    private static final String SQL_DELETE_BY_ORGANIZADOR = "DELETE FROM empresa WHERE organizador_id = ?";
    private static final String SQL_EXISTS_CNPJ = "SELECT 1 FROM empresa WHERE cnpj = ? LIMIT 1";
    private static final String SQL_EXISTS_ORGANIZADOR = "SELECT 1 FROM empresa WHERE organizador_id = ? LIMIT 1";
    private static final String SQL_COUNT = "SELECT COUNT(*) FROM empresa";

    private final ConnectionPool connectionPool;
    private final EmpresaRowMapper rowMapper;

    private EmpresaRepository() {
        this.connectionPool = ConnectionPool.getInstance();
        this.rowMapper = new EmpresaRowMapper();
    }

    public static synchronized EmpresaRepository getInstance() {
        if (instance == null) {
            instance = new EmpresaRepository();
        }
        return instance;
    }

    @Override
    public Empresa salvar(Empresa empresa) {
        if (empresa == null) {
            throw new br.com.softhouse.dende.exceptions.ValidationException("Empresa é obrigatória");
        }
        if (empresa.getId() == null) {
            inserir(empresa);
        } else {
            atualizar(empresa);
        }
        return empresa;
    }

    @Override
    public Empresa buscarPorId(Long id) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? mapEmpresa(resultSet) : null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar empresa por id", e);
        }
    }

    public Empresa buscarPorOrganizadorId(Long organizadorId) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ORGANIZADOR)) {
            statement.setLong(1, organizadorId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? mapEmpresa(resultSet) : null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar empresa por organizador", e);
        }
    }

    public Empresa buscarPorCnpj(String cnpj) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_CNPJ)) {
            statement.setString(1, cnpj);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? mapEmpresa(resultSet) : null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar empresa por CNPJ", e);
        }
    }

    public boolean cnpjExiste(String cnpj) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_CNPJ)) {
            statement.setString(1, cnpj);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao verificar CNPJ da empresa", e);
        }
    }

    public boolean organizadorTemEmpresa(Long organizadorId) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_ORGANIZADOR)) {
            statement.setLong(1, organizadorId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao verificar empresa do organizador", e);
        }
    }

    @Override
    public void atualizar(Empresa empresa) {
        if (empresa == null || empresa.getId() == null) {
            throw new br.com.softhouse.dende.exceptions.ValidationException("Empresa com ID é obrigatória para atualização");
        }

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            preencherStatement(statement, empresa);
            statement.setLong(5, empresa.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar empresa", e);
        }
    }

    @Override
    public void deletar(Long id) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao deletar empresa", e);
        }
    }

    public void deletarPorOrganizadorId(Long organizadorId) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE_BY_ORGANIZADOR)) {
            statement.setLong(1, organizadorId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao deletar empresa por organizador", e);
        }
    }

    public int contarEmpresas() {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_COUNT);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao contar empresas", e);
        }
    }

    @Override
    public List<Empresa> listarTodos() {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            List<Empresa> empresas = new ArrayList<>();
            while (resultSet.next()) {
                empresas.add(mapEmpresa(resultSet));
            }
            return empresas;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar empresas", e);
        }
    }

    private void inserir(Empresa empresa) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preencherStatement(statement, empresa);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    empresa.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar empresa", e);
        }
    }

    private void preencherStatement(PreparedStatement statement, Empresa empresa) throws SQLException {
        statement.setObject(1, empresa.getOrganizadorId());
        statement.setString(2, empresa.getCnpj());
        statement.setString(3, empresa.getRazaoSocial());
        statement.setString(4, empresa.getNomeFantasia());
    }

    private Empresa mapEmpresa(ResultSet resultSet) throws SQLException {
        String[] row = new String[]{
                String.valueOf(resultSet.getLong("id")),
                String.valueOf(resultSet.getLong("organizador_id")),
                resultSet.getString("cnpj"),
                resultSet.getString("razao_social"),
                resultSet.getString("nome_fantasia")
        };
        return rowMapper.mapRow(row);
    }
}
