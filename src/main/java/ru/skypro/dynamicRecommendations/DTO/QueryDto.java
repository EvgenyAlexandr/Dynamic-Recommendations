package ru.skypro.dynamicRecommendations.DTO;

import java.util.List;
import java.util.Objects;

/**
 * DTO для передачи данных запроса правила.
 * <p>
 * Используется при создании/получении правил через REST API.
 * </p>
 *
 * @author DynamicRecommendations Team
 */
public class QueryDto {
    private String query;
    private List<String> arguments;
    private boolean negate;

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryDto queryDto = (QueryDto) o;
        return negate == queryDto.negate && Objects.equals(query, queryDto.query) && Objects.equals(arguments, queryDto.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, arguments, negate);
    }

    @Override
    public String toString() {
        return "QueryDto{" +
                "query='" + query + '\'' +
                ", arguments=" + arguments +
                ", negate=" + negate +
                '}';
    }
}