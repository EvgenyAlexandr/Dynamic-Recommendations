package ru.skypro.dynamicRecommendations.servise;

import org.springframework.stereotype.Service;
import ru.skypro.dynamicRecommendations.DTO.RecommendationDto;
import ru.skypro.dynamicRecommendations.DTO.RecommendationResponse;
import ru.skypro.dynamicRecommendations.entity.RuleEntity;
import ru.skypro.dynamicRecommendations.repository.RuleRepository;
import ru.skypro.dynamicRecommendations.recommendation.RecommendationRuleSet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> staticRuleSets;
    private final RuleRepository ruleRepository;
    private final DynamicRuleService dynamicRuleService;

    public RecommendationService(List<RecommendationRuleSet> staticRuleSets,
                                 RuleRepository ruleRepository,
                                 DynamicRuleService dynamicRuleService) {
        this.staticRuleSets = staticRuleSets;
        this.ruleRepository = ruleRepository;
        this.dynamicRuleService = dynamicRuleService;
    }

    public RecommendationResponse getRecommendations(UUID userId) {
        // Статические правила
        List<RecommendationDto> staticRecommendations = staticRuleSets.stream()
                .map(rule -> rule.check(userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        // Динамические правила из БД
        List<RuleEntity> dynamicRules = ruleRepository.findAll();
        List<RecommendationDto> dynamicRecommendations = dynamicRules.stream()
                .map(rule -> dynamicRuleService.evaluateRule(rule, userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        // Объединяем
        List<RecommendationDto> allRecommendations = Stream.concat(
                staticRecommendations.stream(),
                dynamicRecommendations.stream()
        ).collect(Collectors.toList());

        return new RecommendationResponse(userId, allRecommendations);
    }

}