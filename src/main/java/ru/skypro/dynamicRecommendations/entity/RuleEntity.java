package ru.skypro.dynamicRecommendations.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Сущность для хранения правила рекомендации в БД.
 * <p>
 * Правило состоит из:
 * <ul>
 *     <li>информации о продукте (название, ID, описание)</li>
 *     <li>списка запросов {@link QueryEntity}, которые должны все выполниться</li>
 *     <li>связанной статистики {@link RuleStatsEntity}</li>
 * </ul>
 * </p>
 *
 * @author DynamicRecommendations Team
 */
@Entity
@Table(name = "rules")
public class RuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "product_text", nullable = false, columnDefinition = "TEXT")
    private String productText;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "rule_id")
    private List<QueryEntity> queries;

    @OneToOne(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true)
    private RuleStatsEntity stats;

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

    public List<QueryEntity> getQueries() {
        return queries;
    }

    public void setQueries(List<QueryEntity> queries) {
        this.queries = queries;
    }

    public RuleStatsEntity getStats() {
        return stats;
    }

    public void setStats(RuleStatsEntity stats) {
        this.stats = stats;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RuleEntity that = (RuleEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(productName, that.productName) && Objects.equals(productId, that.productId) && Objects.equals(productText, that.productText) && Objects.equals(queries, that.queries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, productId, productText, queries);
    }

    @Override
    public String toString() {
        return "RuleEntity{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", productId=" + productId +
                ", productText='" + productText + '\'' +
                ", queries=" + queries +
                '}';
    }
}