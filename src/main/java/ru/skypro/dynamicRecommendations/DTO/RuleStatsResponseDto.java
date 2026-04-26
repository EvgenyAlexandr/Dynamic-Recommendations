package ru.skypro.dynamicRecommendations.DTO;

import java.util.List;
import java.util.UUID;

public class RuleStatsResponseDto {
    private List<RuleStatDto> stats;

    public RuleStatsResponseDto(List<RuleStatDto> stats) { this.stats = stats; }
    public List<RuleStatDto> getStats() { return stats; }
    public void setStats(List<RuleStatDto> stats) { this.stats = stats; }

    public static class RuleStatDto {
        private UUID ruleId;
        private long count;

        public RuleStatDto(UUID ruleId, long count) { this.ruleId = ruleId; this.count = count; }
        public UUID getRuleId() { return ruleId; }
        public long getCount() { return count; }
    }
}
