package fr.abes.theses_batch_indexation.database;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import oracle.jdbc.pool.OracleDataSource;

@Configuration
public class DbConfig {

    @Bean
    @Primary
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    // ci-dessous : obligation d'utiliser une OracleDataSource sinon impossible
    // de caster le ResultSet Hikari en ResulSet Oracle dans le TheseRowMapper
    // et donc impossible d'utiliser le getOpaque (spécifique à OracleResultSet)
    // pour récuperer le XMLType

    @Bean
    @ConfigurationProperties("spring.db.datasource")
    public DataSource dataSourceLecture() {

        return DataSourceBuilder.create().url(dataSourceProperties().getUrl())
                .username(dataSourceProperties().getUsername()).password(dataSourceProperties().getPassword())
                .type(OracleDataSource.class).build();
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setJdbcUrl("jdbc:h2:mem:testdb");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }
}
