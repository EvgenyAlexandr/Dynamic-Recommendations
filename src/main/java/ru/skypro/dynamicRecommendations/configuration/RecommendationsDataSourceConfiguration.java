package ru.skypro.dynamicRecommendations.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class RecommendationsDataSourceConfiguration {
    @Bean(name = "recommendationsDataSource")
    public DataSource recommendationsDataSource(@Value("${application.recommendations-db.url}") String recommendationsUrl) {
        var dataSource = new HikariDataSource();                // HikariCP — высокопроизводительный пул соединений
        dataSource.setJdbcUrl(recommendationsUrl);              // Используется H2 (файловая БД)
        dataSource.setDriverClassName("org.h2.Driver");         // Только чтение — безопасно и оптимизировано
        dataSource.setReadOnly(true);
        return dataSource;
    }

    @Bean(name = "recommendationsJdbcTemplate")
    public JdbcTemplate recommendationsJdbcTemplate(
            @Qualifier("recommendationsDataSource") DataSource dataSourced) {
        return new JdbcTemplate(dataSourced);
    }
}
