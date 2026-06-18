package ru.skypro.dynamicRecommendations.DTO;

import java.util.List;
import java.util.UUID;

/**
 * DTO для ответа с данными правила.
 *
 * @author DynamicRecommendations Team
 */
public class RuleResponseDto {
    private UUID id;
    private String productName;
    private UUID productId;
    private String productText;
    private List<QueryDto> rule;

    public RuleResponseDto() {
    }

    public RuleResponseDto(UUID id, String productName, UUID productId, String productText, List<QueryDto> rule) {
        this.id = id;
        this.productName = productName;
        this.productId = productId;
        this.productText = productText;
        this.rule = rule;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public List<QueryDto> getRule() {
        return rule;
    }

    public void setRule(List<QueryDto> rule) {
        this.rule = rule;
    }
}