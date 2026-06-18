package ru.skypro.dynamicRecommendations.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Конфигурация источника данных для чтения транзакций из H2.
 * <p>
 * Данный источник данных используется только для чтения (readOnly = true)
 * и предназначен для выполнения SQL-запросов к базе транзакций пользователей.
 * </p>
 *
 * @author DynamicRecommendations Team
 */
@Configuration
public class RecommendationsDataSourceConfiguration {

    /**
     * Создаёт источник данных для БД с транзакциями (H2).
     * <p>
     * Использует пул соединений HikariCP в режиме только для чтения.
     * </p>
     *
     * @param recommendationsUrl URL подключения к H2 (из application.recommendations-db.url)
     * @return настроенный DataSource для транзакций
     */
    @Bean(name = "recommendationsDataSource")
    public DataSource recommendationsDataSource(@Value("${application.recommendations-db.url}") String recommendationsUrl) {
        var dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(recommendationsUrl);
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setReadOnly(true);
        return dataSource;
    }

    /**
     * Создаёт JdbcTemplate для выполнения запросов к БД транзакций.
     *
     * @param dataSource источник данных для транзакций
     * @return JdbcTemplate для работы с H2
     */
    @Bean(name = "recommendationsJdbcTemplate")
    public JdbcTemplate recommendationsJdbcTemplate(
            @Qualifier("recommendationsDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}