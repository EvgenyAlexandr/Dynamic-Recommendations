package ru.skypro.dynamicRecommendations.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Репозиторий для работы с данными пользователя из БД транзакций (H2).
 * <p>
 * Все методы используют кэширование Caffeine с временем жизни 10 минут
 * для оптимизации производительности.
 * </p>
 *
 * @author DynamicRecommendations Team
 */
@Repository
public class UserDataRepository {

    private final JdbcTemplate jdbcTemplate;

    private Cache<String, Boolean> usesProductTypeCache;
    private Cache<String, Long> totalDepositsCache;
    private Cache<String, Long> totalSpendsCache;
    private Cache<String, Long> totalDepositsByProductCache;
    private Cache<String, Long> transactionCountCache;

    public UserDataRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Инициализирует кэши после создания бина.
     */
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

        transactionCountCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
    }

    /**
     * Получает случайную сумму транзакции пользователя.
     *
     * @param user идентификатор пользователя
     * @return сумма транзакции или 0, если транзакций нет
     */
    public int getRandomTransactionAmount(UUID user) {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT amount FROM transactions t WHERE t.user_id = ? LIMIT 1",
                Integer.class,
                user);
        return result != null ? result : 0;
    }

    /**
     * Проверяет, использует ли пользователь продукт определённого типа.
     *
     * @param userId      идентификатор пользователя
     * @param productType тип продукта (DEBIT, CREDIT, INVEST, SAVING)
     * @return true, если пользователь использует продукт данного типа
     */
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

    /**
     * Получает сумму пополнений по типу продукта.
     *
     * @param userId      идентификатор пользователя
     * @param productType тип продукта
     * @return сумма пополнений (DEPOSIT)
     */
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

    /**
     * Получает сумму трат по типу продукта.
     *
     * @param userId      идентификатор пользователя
     * @param productType тип продукта
     * @return сумма трат (WITHDRAW)
     */
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

    /**
     * Получает сумму пополнений по конкретному продукту.
     *
     * @param userId    идентификатор пользователя
     * @param productId идентификатор продукта
     * @return сумма пополнений
     */
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

    /**
     * Получает количество транзакций пользователя по типу продукта.
     *
     * @param userId      идентификатор пользователя
     * @param productType тип продукта
     * @return количество транзакций
     */
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

    /**
     * Возвращает JdbcTemplate для выполнения прямых SQL-запросов.
     *
     * @return JdbcTemplate
     */
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * Очищает все кэши.
     */
    public void clearCaches() {
        usesProductTypeCache.invalidateAll();
        totalDepositsCache.invalidateAll();
        totalSpendsCache.invalidateAll();
        totalDepositsByProductCache.invalidateAll();
        transactionCountCache.invalidateAll();
    }
}