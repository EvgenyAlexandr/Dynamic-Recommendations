package ru.skypro.dynamicRecommendations.DTO;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * DTO для ответа со статистикой срабатываний правил.
 *
 * @author DynamicRecommendations Team
 */
public class RuleStatsResponseDto {
    private List<RuleStatDto> stats;

    public RuleStatsResponseDto(List<RuleStatDto> stats) {
        this.stats = stats;
    }

    public List<RuleStatDto> getStats() {
        return stats;
    }

    public void setStats(List<RuleStatDto> stats) {
        this.stats = stats;
    }

    /**
     * DTO для статистики одного правила.
     */
    public static class RuleStatDto {
        private UUID ruleId;
        private long count;

        public RuleStatDto(UUID ruleId, long count) {
            this.ruleId = ruleId;
            this.count = count;
        }

        public UUID getRuleId() {
            return ruleId;
        }

        public long getCount() {
            return count;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            RuleStatDto that = (RuleStatDto) o;
            return count == that.count && Objects.equals(ruleId, that.ruleId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ruleId, count);
        }

        @Override
        public String toString() {
            return "RuleStatDto{" +
                    "ruleId=" + ruleId +
                    ", count=" + count +
                    '}';
        }
    }
}