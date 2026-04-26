package ru.skypro.dynamicRecommendations.recommendation;

import ru.skypro.dynamicRecommendations.DTO.RecommendationDto;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    Optional<RecommendationDto> check(UUID userId);
}
