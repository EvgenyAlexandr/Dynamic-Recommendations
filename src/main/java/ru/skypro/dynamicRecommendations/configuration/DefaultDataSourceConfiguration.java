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

@Configuration
@EnableJpaRepositories(
        basePackages = "ru.skypro.dynamicRecommendations.repository",
        entityManagerFactoryRef = "defaultEntityManagerFactory",
        transactionManagerRef = "defaultTransactionManager"
)
public class DefaultDataSourceConfiguration {

    @Primary
    @Bean(name = "defaultDataSourceProperties")
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties defaultDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "defaultDataSource")
    public DataSource defaultDataSource(@Qualifier("defaultDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

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

    @Primary
    @Bean(name = "defaultTransactionManager")
    public PlatformTransactionManager defaultTransactionManager(
            @Qualifier("defaultEntityManagerFactory") LocalContainerEntityManagerFactoryBean emf) {
        return new JpaTransactionManager(emf.getObject());
    }
}