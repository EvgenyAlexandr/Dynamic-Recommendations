package ru.skypro.dynamicRecommendations.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class RecommendationResponse {
    @JsonProperty("user_id")        // Аннотация Json для сериализации в snake_case
    private UUID userId;

    private List<RecommendationDto> recommendations;

    public RecommendationResponse() {}

    public RecommendationResponse(UUID userId, List<RecommendationDto> recommendations) {
        this.userId = userId;
        this.recommendations = recommendations;
    }

    // Геттеры и сеттеры
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public List<RecommendationDto> getRecommendations() { return recommendations; }
    public void setRecommendations(List<RecommendationDto> recommendations) {
        this.recommendations = recommendations;
    }
}
