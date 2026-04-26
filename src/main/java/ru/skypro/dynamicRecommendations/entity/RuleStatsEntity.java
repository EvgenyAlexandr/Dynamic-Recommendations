package ru.skypro.dynamicRecommendations.entity;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

/**
 * Сущность для хранения статистики срабатываний правила.
 * <p>
 * Счётчик {@code count} увеличивается при каждом успешном выполнении правила.
 * </p>
 *
 * @author DynamicRecommendations Team
 */
@Entity
@Table(name = "rule_stats")
public class RuleStatsEntity {

    @Id
    private UUID ruleId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "rule_id")
    private RuleEntity rule;

    private long count = 0;

    public UUID getRuleId() {
        return ruleId;
    }

    public void setRuleId(UUID ruleId) {
        this.ruleId = ruleId;
    }

    public RuleEntity getRule() {
        return rule;
    }

    public void setRule(RuleEntity rule) {
        this.rule = rule;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    /**
     * Увеличивает счётчик срабатываний на 1.
     */
    public void increment() {
        this.count++;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RuleStatsEntity that = (RuleStatsEntity) o;
        return count == that.count && Objects.equals(ruleId, that.ruleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleId, count);
    }

    @Override
    public String toString() {
        return "RuleStatsEntity{" +
                "ruleId=" + ruleId +
                ", count=" + count +
                '}';
    }
}
