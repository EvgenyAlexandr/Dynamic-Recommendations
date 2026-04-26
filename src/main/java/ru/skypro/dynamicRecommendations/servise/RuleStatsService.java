package ru.skypro.dynamicRecommendations.servise;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.dynamicRecommendations.DTO.RuleStatsResponseDto;
import ru.skypro.dynamicRecommendations.entity.RuleEntity;
import ru.skypro.dynamicRecommendations.entity.RuleStatsEntity;
import ru.skypro.dynamicRecommendations.repository.RuleRepository;
import ru.skypro.dynamicRecommendations.repository.RuleStatsRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RuleStatsService {
    private final RuleStatsRepository ruleStatsRepository;
    private final RuleRepository ruleRepository;

    public RuleStatsService(RuleStatsRepository ruleStatsRepository, RuleRepository ruleRepository) {
        this.ruleStatsRepository = ruleStatsRepository;
        this.ruleRepository = ruleRepository;
    }

    @Transactional
    public void incrementStat(UUID ruleId) {
        if (!ruleStatsRepository.existsById(ruleId)) {
            RuleStatsEntity stats = new RuleStatsEntity();
            stats.setRuleId(ruleId);
            stats.setCount(0);
            ruleStatsRepository.save(stats);
        }
        ruleStatsRepository.incrementCount(ruleId);
    }

    public RuleStatsResponseDto getAllStats() {
        List<RuleEntity> allRules = ruleRepository.findAll();
        List<RuleStatsResponseDto.RuleStatDto> stats = allRules.stream()
                .map(rule -> {
                    long count = ruleStatsRepository.findById(rule.getId())
                            .map(RuleStatsEntity::getCount)
                            .orElse(0L);
                    return new RuleStatsResponseDto.RuleStatDto(rule.getId(), count);
                })
                .collect(Collectors.toList());
        return new RuleStatsResponseDto(stats);
    }
}
