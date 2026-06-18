package ru.skypro.dynamicRecommendations.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Сущность для хранения запроса правила в БД.
 * <p>
 * Поддерживаемые типы запросов:
 * <ul>
 *     <li>USER_OF — проверка использования продукта типа</li>
 *     <li>ACTIVE_USER_OF — проверка активности (>=5 транзакций)</li>
 *     <li>TRANSACTION_SUM_COMPARE — сравнение суммы с порогом</li>
 *     <li>TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW — сравнение пополнений и трат</li>
 * </ul>
 * </p>
 *
 * @author DynamicRecommendations Team
 */
@Entity
@Table(name = "queries")
public class QueryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String query;

    @ElementCollection
    @CollectionTable(name = "query_arguments", joinColumns = @JoinColumn(name = "query_id"))
    @Column(name = "argument")
    private List<String> arguments;

    private boolean negate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        QueryEntity that = (QueryEntity) o;
        return negate == that.negate && Objects.equals(id, that.id) && Objects.equals(query, that.query) && Objects.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, query, arguments, negate);
    }

    @Override
    public String toString() {
        return "QueryEntity{" +
                "id=" + id +
                ", query='" + query + '\'' +
                ", arguments=" + arguments +
                ", negate=" + negate +
                '}';
    }
}