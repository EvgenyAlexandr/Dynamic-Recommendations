package ru.skypro.dynamicRecommendations.entity;

import jakarta.persistence.*;
import java.util.UUID;

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

    // getters / setters
    public UUID getRuleId() { return ruleId; }
    public void setRuleId(UUID ruleId) { this.ruleId = ruleId; }
    public RuleEntity getRule() { return rule; }
    public void setRule(RuleEntity rule) { this.rule = rule; }
    public long getCount() { return count; }
    public void setCount(long count) { this.count = count; }

    public void increment() { this.count++; }
}
