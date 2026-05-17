package br.com.softhouse.dende.repositories;

import br.com.softhouse.dende.exceptions.DatabaseException;
import br.com.softhouse.dende.model.Evento;
import br.com.softhouse.dende.repositories.util.ConnectionPool;
import br.com.softhouse.dende.repositories.util.CrudRepository;
import br.com.softhouse.dende.repositories.util.rowmapper.EventoRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * REPOSITÓRIO DE EVENTOS
 *
 * Persistência JDBC para a tabela evento.
 */
public class EventoRepository implements CrudRepository<Evento, Long> {

    private static EventoRepository instance;

    private static final String SQL_INSERT = "INSERT INTO evento (organizador_id, evento_principal_id, nome, descricao, pagina_web, tipo_evento, modalidade, local_evento, data_inicio, data_fim, capacidade_maxima, preco_ingresso, estorna_ingresso, taxa_estorno, ativo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_ID = "SELECT id, organizador_id, evento_principal_id, nome, descricao, pagina_web, tipo_evento, modalidade, local_evento, data_inicio, data_fim, capacidade_maxima, preco_ingresso, estorna_ingresso, taxa_estorno, ativo, data_cadastro FROM evento WHERE id = ?";
    private static final String SQL_SELECT_BY_ORGANIZADOR = "SELECT id, organizador_id, evento_principal_id, nome, descricao, pagina_web, tipo_evento, modalidade, local_evento, data_inicio, data_fim, capacidade_maxima, preco_ingresso, estorna_ingresso, taxa_estorno, ativo, data_cadastro FROM evento WHERE organizador_id = ? ORDER BY id";
    private static final String SQL_SELECT_ALL = "SELECT id, organizador_id, evento_principal_id, nome, descricao, pagina_web, tipo_evento, modalidade, local_evento, data_inicio, data_fim, capacidade_maxima, preco_ingresso, estorna_ingresso, taxa_estorno, ativo, data_cadastro FROM evento ORDER BY id";
    private static final String SQL_SELECT_ACTIVE = "SELECT id, organizador_id, evento_principal_id, nome, descricao, pagina_web, tipo_evento, modalidade, local_evento, data_inicio, data_fim, capacidade_maxima, preco_ingresso, estorna_ingresso, taxa_estorno, ativo, data_cadastro FROM evento WHERE ativo = 1 AND data_fim > NOW() ORDER BY data_inicio, nome";
    private static final String SQL_UPDATE = "UPDATE evento SET organizador_id = ?, evento_principal_id = ?, nome = ?, descricao = ?, pagina_web = ?, tipo_evento = ?, modalidade = ?, local_evento = ?, data_inicio = ?, data_fim = ?, capacidade_maxima = ?, preco_ingresso = ?, estorna_ingresso = ?, taxa_estorno = ?, ativo = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM evento WHERE id = ?";
    private static final String SQL_COUNT_VALID_TICKETS = "SELECT COUNT(*) FROM ingresso WHERE evento_id = ? AND status NOT IN ('CANCELADO', 'REEMBOLSADO')";
    private static final String SQL_EXISTS_ACTIVE_OR_RUNNING = "SELECT 1 FROM evento WHERE organizador_id = ? AND (ativo = 1 OR (data_inicio <= NOW() AND data_fim > NOW())) LIMIT 1";

    private final ConnectionPool connectionPool;
    private final EventoRowMapper rowMapper;

    private EventoRepository() {
        this.connectionPool = ConnectionPool.getInstance();
        this.rowMapper = new EventoRowMapper();
    }

    public static synchronized EventoRepository getInstance() {
        if (instance == null) {
            instance = new EventoRepository();
        }
        return instance;
    }

    @Override
    public Evento salvar(Evento evento) {
        if (evento == null) {
            throw new br.com.softhouse.dende.exceptions.ValidationException("Evento é obrigatório");
        }
        if (evento.getId() == null) {
            inserir(evento);
        } else {
            atualizar(evento);
        }
        return evento;
    }

    @Override
    public Evento buscarPorId(Long id) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return carregarIngressosVendidos(mapEvento(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar evento por id", e);
        }
    }

    public List<Evento> buscarPorOrganizadorId(Long organizadorId) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ORGANIZADOR)) {
            statement.setLong(1, organizadorId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Evento> eventos = new ArrayList<>();
                while (resultSet.next()) {
                    eventos.add(carregarIngressosVendidos(mapEvento(resultSet)));
                }
                return eventos;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar eventos por organizador", e);
        }
    }

    @Override
    public void atualizar(Evento evento) {
        if (evento == null || evento.getId() == null) {
            throw new br.com.softhouse.dende.exceptions.ValidationException("Evento com ID é obrigatório para atualização");
        }

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            preencherStatement(statement, evento);
            statement.setLong(16, evento.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar evento", e);
        }
    }

    @Override
    public List<Evento> listarTodos() {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            List<Evento> eventos = new ArrayList<>();
            while (resultSet.next()) {
                eventos.add(carregarIngressosVendidos(mapEvento(resultSet)));
            }
            return eventos;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar eventos", e);
        }
    }

    public List<Evento> listarAtivos() {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ACTIVE);
             ResultSet resultSet = statement.executeQuery()) {
            List<Evento> eventos = new ArrayList<>();
            while (resultSet.next()) {
                Evento evento = carregarIngressosVendidos(mapEvento(resultSet));
                if (evento.temIngressosDisponiveis()) {
                    eventos.add(evento);
                }
            }
            return eventos;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar eventos ativos", e);
        }
    }

    @Override
    public void deletar(Long id) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao deletar evento", e);
        }
    }

    public boolean organizadorTemEventosAtivosOuEmExecucao(Long organizadorId) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_ACTIVE_OR_RUNNING)) {
            statement.setLong(1, organizadorId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao verificar eventos ativos do organizador", e);
        }
    }

    private void inserir(Evento evento) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preencherStatement(statement, evento);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    evento.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar evento", e);
        }
    }

    private void preencherStatement(PreparedStatement statement, Evento evento) throws SQLException {
        statement.setObject(1, evento.getOrganizadorId());
        statement.setObject(2, evento.getEventoPrincipalId());
        statement.setString(3, evento.getNome());
        statement.setString(4, evento.getDescricao());
        statement.setString(5, evento.getPagina());
        statement.setString(6, evento.getTipoEvento() != null ? evento.getTipoEvento().name() : null);
        statement.setString(7, evento.getModalidade() != null ? evento.getModalidade().name() : null);
        statement.setString(8, evento.getLocal());
        statement.setObject(9, evento.getDataInicio());
        statement.setObject(10, evento.getDataFinal());
        statement.setObject(11, evento.getCapacidadeMaxima());
        statement.setObject(12, evento.getPrecoIngresso());
        statement.setBoolean(13, Boolean.TRUE.equals(evento.getEstornaCancelamento()));
        statement.setObject(14, evento.getTaxaEstorno());
        statement.setBoolean(15, Boolean.TRUE.equals(evento.getAtivo()));
    }

    private Evento mapEvento(ResultSet resultSet) throws SQLException {
        String[] row = new String[]{
                String.valueOf(resultSet.getLong("id")),
                String.valueOf(resultSet.getLong("organizador_id")),
                resultSet.getObject("evento_principal_id") != null ? String.valueOf(resultSet.getLong("evento_principal_id")) : null,
                resultSet.getString("nome"),
                resultSet.getString("descricao"),
                resultSet.getString("pagina_web"),
                resultSet.getString("tipo_evento"),
                resultSet.getString("modalidade"),
                resultSet.getString("local_evento"),
                resultSet.getTimestamp("data_inicio").toLocalDateTime().toString(),
                resultSet.getTimestamp("data_fim").toLocalDateTime().toString(),
                String.valueOf(resultSet.getInt("capacidade_maxima")),
                String.valueOf(resultSet.getDouble("preco_ingresso")),
                String.valueOf(resultSet.getBoolean("estorna_ingresso")),
                String.valueOf(resultSet.getDouble("taxa_estorno")),
                String.valueOf(resultSet.getBoolean("ativo")),
                resultSet.getTimestamp("data_cadastro") != null ? resultSet.getTimestamp("data_cadastro").toLocalDateTime().toString() : null
        };
        return rowMapper.mapRow(row);
    }

    private Evento carregarIngressosVendidos(Evento evento) {
        if (evento != null) {
            evento.setIngressosVendidos(IngressoRepository.getInstance().contarIngressosValidosPorEvento(evento.getId()));
        }
        return evento;
    }
}