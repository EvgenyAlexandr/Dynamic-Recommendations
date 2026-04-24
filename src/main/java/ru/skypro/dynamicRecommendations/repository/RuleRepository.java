package ru.skypro.dynamicRecommendations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.dynamicRecommendations.entity.RuleEntity;
import java.util.UUID;

public interface RuleRepository extends JpaRepository<RuleEntity, UUID> {
}



