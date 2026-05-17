package br.com.softhouse.dende.repositories.util;

import br.com.softhouse.dende.exceptions.ConfigurationException;
import br.com.dende.softhouse.annotations.Value;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
@Setter
@Getter
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

    public static ConfigProperties fromClasspath() {
        Properties properties = new Properties();

        try (InputStream inputStream = ConfigProperties.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (inputStream == null) {
                throw new ConfigurationException("Arquivo application.properties não encontrado no classpath");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new ConfigurationException("Erro ao carregar application.properties", e);
        }

        ConfigProperties config = new ConfigProperties();
        config.setDatasourceUrl(properties.getProperty("datasource.url"));
        config.setDatasourceUsername(properties.getProperty("datasource.username"));
        config.setDatasourcePassword(properties.getProperty("datasource.password"));
        config.setDatasourceDriverClassName(properties.getProperty("datasource.driver-class-name"));
        config.setDatasourceHikariMaximumPoolSize(properties.getProperty("datasource.hikari.maximum-pool-size"));
        config.setDatasourceHikariMinimumIdle(properties.getProperty("datasource.hikari.minimum-idle"));
        config.setDatasourceHikariConnectionTimeout(properties.getProperty("datasource.hikari.connection-timeout"));
        return config;
    }
}

