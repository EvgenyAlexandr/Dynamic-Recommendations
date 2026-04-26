package ru.skypro.dynamicRecommendations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.dynamicRecommendations.entity.RuleEntity;
import java.util.UUID;

/**
 * JPA репозиторий для работы с сущностью {@link RuleEntity}.
 *
 * @author DynamicRecommendations Team
 */
public interface RuleRepository extends JpaRepository<RuleEntity, UUID> {
}