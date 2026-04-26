package ru.skypro.dynamicRecommendations.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Конфигурация основного источника данных (PostgreSQL) для хранения правил и статистики.
 * <p>
 * Данный источник данных используется для JPA-репозиториев и хранит сущности:
 * <ul>
 *     <li>{@link ru.skypro.dynamicRecommendations.entity.RuleEntity} — правила рекомендаций</li>
 *     <li>{@link ru.skypro.dynamicRecommendations.entity.QueryEntity} — запросы правил</li>
 *     <li>{@link ru.skypro.dynamicRecommendations.entity.RuleStatsEntity} — статистика срабатываний</li>
 * </ul>
 * </p>
 *
 * @author DynamicRecommendations Team
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "ru.skypro.dynamicRecommendations.repository",
        entityManagerFactoryRef = "defaultEntityManagerFactory",
        transactionManagerRef = "defaultTransactionManager"
)
public class DefaultDataSourceConfiguration {

    /**
     * Создаёт свойства источника данных из конфигурации {@code spring.datasource.*}.
     *
     * @return свойства источника данных
     */
    @Primary
    @Bean(name = "defaultDataSourceProperties")
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties defaultDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * Создаёт основной источник данных (PostgreSQL).
     *
     * @param properties свойства источника данных
     * @return настроенный DataSource
     */
    @Primary
    @Bean(name = "defaultDataSource")
    public DataSource defaultDataSource(@Qualifier("defaultDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    /**
     * Создаёт фабрику EntityManager для работы с JPA.
     *
     * @param dataSource источник данных
     * @return фабрика EntityManager
     */
    @Primary
    @Bean(name = "defaultEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean defaultEntityManagerFactory(
            @Qualifier("defaultDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("ru.skypro.dynamicRecommendations.entity");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return emf;
    }

    /**
     * Создаёт менеджер транзакций для JPA.
     *
     * @param emf фабрика EntityManager
     * @return менеджер транзакций
     */
    @Primary
    @Bean(name = "defaultTransactionManager")
    public PlatformTransactionManager defaultTransactionManager(
            @Qualifier("defaultEntityManagerFactory") LocalContainerEntityManagerFactoryBean emf) {
        return new JpaTransactionManager(emf.getObject());
    }
}