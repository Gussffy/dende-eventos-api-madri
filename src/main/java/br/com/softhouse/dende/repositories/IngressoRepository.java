package br.com.softhouse.dende.repositories;

import br.com.softhouse.dende.exceptions.DatabaseException;
import br.com.softhouse.dende.model.Ingresso;
import br.com.softhouse.dende.model.enums.StatusIngresso;
import br.com.softhouse.dende.repositories.util.ConnectionPool;
import br.com.softhouse.dende.repositories.util.CrudRepository;
import br.com.softhouse.dende.repositories.util.rowmapper.IngressoRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * REPOSITÓRIO DE INGRESSOS
 *
 * Persistência JDBC para a tabela ingresso.
 */
public class IngressoRepository implements CrudRepository<Ingresso, Long> {
    private static IngressoRepository instance;

    private static final String SQL_INSERT = "INSERT INTO ingresso (usuario_id, evento_id, valor_pago, status, valor_estornado, data_compra, data_cancelamento) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_ID = "SELECT id, usuario_id, evento_id, valor_pago, status, valor_estornado, data_compra, data_cancelamento FROM ingresso WHERE id = ?";
    private static final String SQL_SELECT_BY_USUARIO = "SELECT id, usuario_id, evento_id, valor_pago, status, valor_estornado, data_compra, data_cancelamento FROM ingresso WHERE usuario_id = ? ORDER BY data_compra DESC";
    private static final String SQL_SELECT_BY_EVENTO = "SELECT id, usuario_id, evento_id, valor_pago, status, valor_estornado, data_compra, data_cancelamento FROM ingresso WHERE evento_id = ? ORDER BY data_compra DESC";
    private static final String SQL_SELECT_ALL = "SELECT id, usuario_id, evento_id, valor_pago, status, valor_estornado, data_compra, data_cancelamento FROM ingresso ORDER BY id";
    private static final String SQL_UPDATE = "UPDATE ingresso SET usuario_id = ?, evento_id = ?, valor_pago = ?, status = ?, valor_estornado = ?, data_compra = ?, data_cancelamento = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM ingresso WHERE id = ?";
    private static final String SQL_EXISTS_ATIVO = "SELECT 1 FROM ingresso WHERE usuario_id = ? AND evento_id = ? AND status = 'ATIVO' LIMIT 1";
    private static final String SQL_COUNT_VALID_BY_EVENTO = "SELECT COUNT(*) FROM ingresso WHERE evento_id = ? AND status NOT IN ('CANCELADO', 'REEMBOLSADO')";
    private static final String SQL_REEMBOLSAR_EVENTO = "UPDATE ingresso SET status = 'REEMBOLSADO', valor_estornado = valor_pago, data_cancelamento = ? WHERE evento_id = ? AND status = 'ATIVO'";

    private final ConnectionPool connectionPool;
    private final IngressoRowMapper rowMapper;

    private IngressoRepository() {
        this.connectionPool = ConnectionPool.getInstance();
        this.rowMapper = new IngressoRowMapper();
    }

    public static synchronized IngressoRepository getInstance() {
        if (instance == null) {
            instance = new IngressoRepository();
        }
        return instance;
    }

    @Override
    public Ingresso salvar(Ingresso ingresso) {
        if (ingresso == null) {
            throw new br.com.softhouse.dende.exceptions.ValidationException("Ingresso é obrigatório");
        }

        if (ingresso.getId() == null) {
            inserir(ingresso);
        } else {
            atualizar(ingresso);
        }
        return ingresso;
    }

    @Override
    public Ingresso buscarPorId(Long id) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return mapIngresso(resultSet);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar ingresso por id", e);
        }
    }

    public List<Ingresso> buscarPorUsuarioId(Long usuarioId) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_USUARIO)) {
            statement.setLong(1, usuarioId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Ingresso> ingressos = new ArrayList<>();
                while (resultSet.next()) {
                    ingressos.add(mapIngresso(resultSet));
                }
                return ingressos;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar ingressos por usuário", e);
        }
    }

    public List<Ingresso> buscarPorEventoId(Long eventoId) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_EVENTO)) {
            statement.setLong(1, eventoId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Ingresso> ingressos = new ArrayList<>();
                while (resultSet.next()) {
                    ingressos.add(mapIngresso(resultSet));
                }
                return ingressos;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar ingressos por evento", e);
        }
    }

    @Override
    public void atualizar(Ingresso ingresso) {
        if (ingresso == null || ingresso.getId() == null) {
            throw new br.com.softhouse.dende.exceptions.ValidationException("Ingresso com ID é obrigatório para atualização");
        }

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            preencherStatement(statement, ingresso);
            statement.setLong(9, ingresso.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar ingresso", e);
        }
    }

    public void reembolsarIngressosDoEvento(Long eventoId) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_REEMBOLSAR_EVENTO)) {
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            statement.setLong(2, eventoId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao reembolsar ingressos do evento", e);
        }
    }

    public boolean existeIngressoAtivo(Long usuarioId, Long eventoId) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_ATIVO)) {
            statement.setLong(1, usuarioId);
            statement.setLong(2, eventoId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao verificar ingresso ativo", e);
        }
    }

    public int contarIngressosValidosPorEvento(Long eventoId) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_COUNT_VALID_BY_EVENTO)) {
            statement.setLong(1, eventoId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao contar ingressos válidos do evento", e);
        }
    }

    @Override
    public void deletar(Long id) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao deletar ingresso", e);
        }
    }

    @Override
    public List<Ingresso> listarTodos() {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            List<Ingresso> ingressos = new ArrayList<>();
            while (resultSet.next()) {
                ingressos.add(mapIngresso(resultSet));
            }
            return ingressos;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar ingressos", e);
        }
    }

    private void inserir(Ingresso ingresso) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preencherStatement(statement, ingresso);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ingresso.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar ingresso", e);
        }
    }

    private void preencherStatement(PreparedStatement statement, Ingresso ingresso) throws SQLException {
        statement.setObject(1, ingresso.getUsuarioId());
        statement.setObject(2, ingresso.getEventoId());
        statement.setObject(3, ingresso.getValorPago());
        statement.setString(4, ingresso.getStatus() != null ? ingresso.getStatus().name() : null);
        statement.setObject(5, ingresso.getValorEstornado() != null ? ingresso.getValorEstornado() : 0.0);
        statement.setTimestamp(6, ingresso.getDataCompra() != null ? Timestamp.valueOf(ingresso.getDataCompra()) : Timestamp.valueOf(LocalDateTime.now()));
        if (ingresso.getDataCancelamento() != null) {
            statement.setTimestamp(7, Timestamp.valueOf(ingresso.getDataCancelamento()));
        } else {
            statement.setNull(7, java.sql.Types.TIMESTAMP);
        }
    }

    private Ingresso mapIngresso(ResultSet resultSet) throws SQLException {
        String[] row = new String[]{
                String.valueOf(resultSet.getLong("id")),
                String.valueOf(resultSet.getLong("usuario_id")),
                String.valueOf(resultSet.getLong("evento_id")),
                String.valueOf(resultSet.getDouble("valor_pago")),
                resultSet.getString("status"),
                String.valueOf(resultSet.getDouble("valor_estornado")),
                resultSet.getTimestamp("data_compra").toLocalDateTime().toString(),
                resultSet.getTimestamp("data_cancelamento") != null ? resultSet.getTimestamp("data_cancelamento").toLocalDateTime().toString() : null
        };
        return rowMapper.mapRow(row);
    }
}


