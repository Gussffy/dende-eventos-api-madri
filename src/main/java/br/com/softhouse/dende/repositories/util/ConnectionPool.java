package br.com.softhouse.dende.repositories.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * POOL DE CONEXÕES COM HIKARICP
 *
 * Esta classe é responsável por gerenciar o pool de conexões com o banco de dados
 * utilizando HikariCP, uma das mais eficientes bibliotecas de pool de conexões.
 * Implementa o padrão SINGLETON para garantir que exista apenas uma instância
 * do pool em toda a aplicação.
 *
 * O HikariCP é configurado com base nas propriedades definidas em ConfigProperties,
 * permitindo controlar o tamanho do pool, conexões inativas mínimas, timeout de conexão, etc.
 */
@Getter
public class ConnectionPool {

    // Instância única do pool de conexões (padrão Singleton)
    private static ConnectionPool instance;

    // DataSource do HikariCP que gerencia o pool de conexões
    private final HikariDataSource dataSource;

    // Configurações de propriedades da aplicação
    private final ConfigProperties configProperties;

    private ConnectionPool() {
        this.configProperties = new ConfigProperties();
        this.dataSource = inicializarDataSource();
    }

    private HikariDataSource inicializarDataSource() {
        // Cria a configuração do HikariCP
        HikariConfig config = new HikariConfig();

        // Configura os parâmetros de conexão com o banco de dados
        config.setJdbcUrl(configProperties.getDatasourceUrl());
        config.setUsername(configProperties.getDatasourceUsername());
        config.setPassword(configProperties.getDatasourcePassword());
        config.setDriverClassName(configProperties.getDatasourceDriverClassName());

        // Configura os parâmetros do pool de conexões
        try {
            config.setMaximumPoolSize(Integer.parseInt(configProperties.getDatasourceHikariMaximumPoolSize()));
        } catch (NumberFormatException e) {
            // Se não conseguir fazer parse, usa o valor padrão do HikariCP (10)
            config.setMaximumPoolSize(10);
        }

        try {
            config.setMinimumIdle(Integer.parseInt(configProperties.getDatasourceHikariMinimumIdle()));
        } catch (NumberFormatException e) {
            // Se não conseguir fazer parse, usa o valor padrão do HikariCP (10)
            config.setMinimumIdle(10);
        }

        try {
            config.setConnectionTimeout(Long.parseLong(configProperties.getDatasourceHikariConnectionTimeout()));
        } catch (NumberFormatException e) {
            // Se não conseguir fazer parse, usa o valor padrão do HikariCP (30000ms)
            config.setConnectionTimeout(30000);
        }

        // Retorna um novo HikariDataSource com as configurações estabelecidas
        return new HikariDataSource(config);
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public boolean isClosed() {
        return dataSource == null || dataSource.isClosed();
    }
}

