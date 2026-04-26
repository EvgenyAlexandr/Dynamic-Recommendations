package ru.skypro.dynamicRecommendations.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.dynamicRecommendations.DTO.*;
import ru.skypro.dynamicRecommendations.entity.QueryEntity;
import ru.skypro.dynamicRecommendations.entity.RuleEntity;
import ru.skypro.dynamicRecommendations.repository.RuleRepository;
import ru.skypro.dynamicRecommendations.service.RuleStatsService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST-контроллер для управления динамическими правилами.
 * <p>
 * Предоставляет endpoints:
 * <ul>
 *     <li>POST /rule — создание нового правила</li>
 *     <li>GET /rule — получение всех правил</li>
 *     <li>DELETE /rule/{id} — удаление правила</li>
 *     <li>GET /rule/stats — получение статистики срабатываний</li>
 * </ul>
 * </p>
 *
 * @author DynamicRecommendations Team
 */
@RestController
@RequestMapping("/rule")
public class RuleController {

    private final RuleRepository ruleRepository;
    private final RuleStatsService ruleStatsService;

    public RuleController(RuleRepository ruleRepository,
                          RuleStatsService ruleStatsService) {
        this.ruleRepository = ruleRepository;
        this.ruleStatsService = ruleStatsService;
    }

    /**
     * Создаёт новое правило.
     *
     * @param request DTO с данными правила
     * @return созданное правило
     */
    @PostMapping
    public ResponseEntity<RuleResponseDto> createRule(@RequestBody RuleRequestDto request) {
        RuleEntity entity = new RuleEntity();
        entity.setProductName(request.getProductName());
        entity.setProductId(request.getProductId());
        entity.setProductText(request.getProductText());
        entity.setQueries(convertToQueries(request.getRule()));

        RuleEntity saved = ruleRepository.save(entity);
        return ResponseEntity.ok(convertToResponse(saved));
    }

    /**
     * Возвращает список всех правил.
     *
     * @return список правил
     */
    @GetMapping
    public ResponseEntity<RuleListResponseDto> getAllRules() {
        var rules = ruleRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new RuleListResponseDto(rules));
    }

    /**
     * Удаляет правило по ID.
     *
     * @param id идентификатор правила
     * @return HTTP 204 No Content при успехе, иначе 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID id) {
        if (ruleRepository.existsById(id)) {
            ruleRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Возвращает статистику срабатываний всех правил.
     *
     * @return статистика
     */
    @GetMapping("/stats")
    public ResponseEntity<RuleStatsResponseDto> getStats() {
        return ResponseEntity.ok(ruleStatsService.getAllStats());
    }

    private RuleResponseDto convertToResponse(RuleEntity entity) {
        return new RuleResponseDto(
                entity.getId(),
                entity.getProductName(),
                entity.getProductId(),
                entity.getProductText(),
                convertToQueryDtos(entity.getQueries())
        );
    }

    private List<QueryEntity> convertToQueries(List<QueryDto> queryDtos) {
        return queryDtos.stream().map(dto -> {
            QueryEntity entity = new QueryEntity();
            entity.setQuery(dto.getQuery());
            entity.setArguments(dto.getArguments());
            entity.setNegate(dto.isNegate());
            return entity;
        }).collect(Collectors.toList());
    }

    private List<QueryDto> convertToQueryDtos(List<QueryEntity> entities) {
        return entities.stream().map(entity -> {
            QueryDto dto = new QueryDto();
            dto.setQuery(entity.getQuery());
            dto.setArguments(entity.getArguments());
            dto.setNegate(entity.isNegate());
            return dto;
        }).collect(Collectors.toList());
    }
}