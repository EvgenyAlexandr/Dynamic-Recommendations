package ru.skypro.dynamicRecommendations.DTO;

import java.util.List;
import java.util.UUID;

/**
 * DTO для запроса на создание правила.
 *
 * @author DynamicRecommendations Team
 */
public class RuleRequestDto {
    private String productName;
    private UUID productId;
    private String productText;
    private List<QueryDto> rule;

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