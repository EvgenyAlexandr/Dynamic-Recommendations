package ru.skypro.dynamicRecommendations.recommendation;

import ru.skypro.dynamicRecommendations.DTO.RecommendationDto;

import java.util.Optional;
import java.util.UUID;

/**
 * Интерфейс для статического правила рекомендации.
 * <p>
 * Каждая реализация инкапсулирует бизнес-логику проверки условий
 * для конкретного банковского продукта.
 * </p>
 *
 * @author DynamicRecommendations Team
 */
public interface RecommendationRuleSet {

    /**
     * Проверяет, подходит ли пользователю продукт данного правила.
     *
     * @param userId идентификатор пользователя
     * @return рекомендация, если все условия выполнены, иначе пустой Optional
     */
    Optional<RecommendationDto> check(UUID userId);
}