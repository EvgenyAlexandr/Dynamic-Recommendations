package ru.skypro.dynamicRecommendations.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
public class UserDataRepository {
    private final JdbcTemplate jdbcTemplate;

    private Cache<String, Boolean> usesProductTypeCache;
    private Cache<String, Long> totalDepositsCache;
    private Cache<String, Long> totalSpendsCache;
    private Cache<String, Long> totalDepositsByProductCache;

    private Cache<String, Long> transactionCountCache;

    @PostConstruct
    public void initCaches() {
        usesProductTypeCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();

        totalDepositsCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();

        totalSpendsCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();

        totalDepositsByProductCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();

        // В initCaches()
        transactionCountCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
    }



    public UserDataRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getRandomTransactionAmount(UUID user){
        Integer result = jdbcTemplate.queryForObject(
                "SELECT amount FROM transactions t WHERE t.user_id = ? LIMIT 1",
                Integer.class,
                user);
        return result != null ? result : 0;
    }

    // Проверяет, использует ли пользователь продукт определенного типа
    public boolean usesProductType(UUID userId, String productType) {
        String key = userId + ":" + productType;
        return usesProductTypeCache.get(key, k -> {
            String sql = """
                SELECT COUNT(*) > 0 
                FROM transactions t 
                JOIN products p ON t.product_id = p.id 
                WHERE t.user_id = ? AND p.type = ?
                """;
            Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, userId, productType);
            return Boolean.TRUE.equals(result);
        });
    }

    // Получает сумму пополнений по типу продукта
    public Long getTotalDepositsByProductType(UUID userId, String productType) {
        String key = userId + ":" + productType;
        return totalDepositsCache.get(key, k -> {
            String sql = """
                SELECT COALESCE(SUM(t.amount), 0) 
                FROM transactions t 
                JOIN products p ON t.product_id = p.id 
                WHERE t.user_id = ? AND p.type = ? AND t.type = 'DEPOSIT'
                """;
            Long result = jdbcTemplate.queryForObject(sql, Long.class, userId, productType);
            return result != null ? result : 0L;
        });
    }

    // Получает сумму трат по типу продукта
    public Long getTotalSpendsByProductType(UUID userId, String productType) {
        String key = userId + ":" + productType;
        return totalSpendsCache.get(key, k -> {
            String sql = """
                SELECT COALESCE(SUM(t.amount), 0) 
                FROM transactions t 
                JOIN products p ON t.product_id = p.id 
                WHERE t.user_id = ? AND p.type = ? AND t.type = 'WITHDRAW'
                """;
            Long result = jdbcTemplate.queryForObject(sql, Long.class, userId, productType);
            return result != null ? result : 0L;
        });
    }

    // Получает сумму пополнений по конкретному продукту
    public Long getTotalDepositsByProduct(UUID userId, UUID productId) {
        String key = userId + ":" + productId;
        return totalDepositsByProductCache.get(key, k -> {
            String sql = """
                SELECT COALESCE(SUM(amount), 0) 
                FROM transactions 
                WHERE user_id = ? AND product_id = ? AND type = 'DEPOSIT'
                """;
            Long result = jdbcTemplate.queryForObject(sql, Long.class, userId, productId);
            return result != null ? result : 0L;
        });
    }

    // Метод с кешированием
    public long getTransactionCountByProductType(UUID userId, String productType) {
        String key = userId + ":" + productType;
        return transactionCountCache.get(key, k -> {
            String sql = """
            SELECT COUNT(*) 
            FROM transactions t 
            JOIN products p ON t.product_id = p.id 
            WHERE t.user_id = ? AND p.type = ?
            """;
            Long result = jdbcTemplate.queryForObject(sql, Long.class, userId, productType);
            return result != null ? result : 0L;
        });
    }

    public JdbcTemplate getJdbcTemplate() { return jdbcTemplate; }

    public void clearCaches() {
        usesProductTypeCache.invalidateAll();
        totalDepositsCache.invalidateAll();
        totalSpendsCache.invalidateAll();
        totalDepositsByProductCache.invalidateAll();
        transactionCountCache.invalidateAll();
    }

}
