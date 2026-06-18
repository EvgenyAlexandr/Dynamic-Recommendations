package ru.skypro.dynamicRecommendations.DTO;

import java.util.List;

/**
 * DTO для ответа со списком всех правил.
 *
 * @author DynamicRecommendations Team
 */
public class RuleListResponseDto {
    private List<RuleResponseDto> data;

    public RuleListResponseDto() {
    }

    public RuleListResponseDto(List<RuleResponseDto> data) {
        this.data = data;
    }

    public List<RuleResponseDto> getData() {
        return data;
    }

    public void setData(List<RuleResponseDto> data) {
        this.data = data;
    }
}