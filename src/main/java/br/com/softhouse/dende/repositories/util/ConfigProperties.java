package br.com.softhouse.dende.repositories.util;

import br.com.dende.softhouse.annotations.Value;

/**
 * CLASSE DE CONFIGURAÇÃO DE PROPRIEDADES
 *
 * Esta classe é responsável por mapear todas as propriedades definidas no arquivo
 * application.properties para o código Java, usando a annotation @Value do framework
 * dendeframework. Funciona como um centralizador de configurações da aplicação.
 *
 * As propriedades mapeadas incluem:
 * - Configurações do DataSource (URL, usuário, senha, driver)
 * - Configurações do HikariCP (pool size, idle connections, timeout)
 */
public class ConfigProperties {

    // Propriedades do DataSource
    @Value(key = "datasource.url")
    private String datasourceUrl;

    @Value(key = "datasource.username")
    private String datasourceUsername;

    @Value(key = "datasource.password")
    private String datasourcePassword;

    @Value(key = "datasource.driver-class-name")
    private String datasourceDriverClassName;

    // Propriedades do HikariCP
    @Value(key = "datasource.hikari.maximum-pool-size")
    private String datasourceHikariMaximumPoolSize;

    @Value(key = "datasource.hikari.minimum-idle")
    private String datasourceHikariMinimumIdle;

    @Value(key = "datasource.hikari.connection-timeout")
    private String datasourceHikariConnectionTimeout;

    // Getters para acessar as propriedades
    public String getDatasourceUrl() {
        return datasourceUrl;
    }

    public void setDatasourceUrl(String datasourceUrl) {
        this.datasourceUrl = datasourceUrl;
    }

    public String getDatasourceUsername() {
        return datasourceUsername;
    }

    public void setDatasourceUsername(String datasourceUsername) {
        this.datasourceUsername = datasourceUsername;
    }

    public String getDatasourcePassword() {
        return datasourcePassword;
    }

    public void setDatasourcePassword(String datasourcePassword) {
        this.datasourcePassword = datasourcePassword;
    }

    public String getDatasourceDriverClassName() {
        return datasourceDriverClassName;
    }

    public void setDatasourceDriverClassName(String datasourceDriverClassName) {
        this.datasourceDriverClassName = datasourceDriverClassName;
    }

    public String getDatasourceHikariMaximumPoolSize() {
        return datasourceHikariMaximumPoolSize;
    }

    public void setDatasourceHikariMaximumPoolSize(String datasourceHikariMaximumPoolSize) {
        this.datasourceHikariMaximumPoolSize = datasourceHikariMaximumPoolSize;
    }

    public String getDatasourceHikariMinimumIdle() {
        return datasourceHikariMinimumIdle;
    }

    public void setDatasourceHikariMinimumIdle(String datasourceHikariMinimumIdle) {
        this.datasourceHikariMinimumIdle = datasourceHikariMinimumIdle;
    }

    public String getDatasourceHikariConnectionTimeout() {
        return datasourceHikariConnectionTimeout;
    }

    public void setDatasourceHikariConnectionTimeout(String datasourceHikariConnectionTimeout) {
        this.datasourceHikariConnectionTimeout = datasourceHikariConnectionTimeout;
    }
}

