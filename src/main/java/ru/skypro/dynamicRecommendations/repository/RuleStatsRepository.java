package ru.skypro.dynamicRecommendations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.dynamicRecommendations.entity.RuleStatsEntity;
import java.util.UUID;

/**
 * JPA репозиторий для работы с сущностью {@link RuleStatsEntity}.
 *
 * @author DynamicRecommendations Team
 */
public interface RuleStatsRepository extends JpaRepository<RuleStatsEntity, UUID> {

    /**
     * Увеличивает счётчик срабатываний правила на 1.
     *
     * @param ruleId идентификатор правила
     */
    @Modifying
    @Transactional
    @Query("UPDATE RuleStatsEntity rs SET rs.count = rs.count + 1 WHERE rs.ruleId = :ruleId")
    void incrementCount(UUID ruleId);
}