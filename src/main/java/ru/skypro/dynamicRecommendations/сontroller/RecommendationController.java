package ru.skypro.dynamicRecommendations.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.dynamicRecommendations.DTO.RecommendationResponse;
import ru.skypro.dynamicRecommendations.service.RecommendationService;

import java.util.UUID;

/**
 * REST-контроллер для получения рекомендаций.
 * <p>
 * Endpoint: GET /recommendation/{user_id}
 * </p>
 *
 * @author DynamicRecommendations Team
 */
@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Возвращает рекомендации для указанного пользователя.
     *
     * @param userId идентификатор пользователя (UUID)
     * @return список рекомендаций
     */
    @GetMapping("/{user_id}")
    public ResponseEntity<RecommendationResponse> getRecommendations(
            @PathVariable("user_id") UUID userId) {

        RecommendationResponse response = recommendationService.getRecommendations(userId);
        return ResponseEntity.ok(response);
    }
}