package ru.skypro.dynamicRecommendations.сontroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.skypro.dynamicRecommendations.DTO.RuleRequestDto;
import ru.skypro.dynamicRecommendations.DTO.RuleResponseDto;
import ru.skypro.dynamicRecommendations.DTO.RuleListResponseDto;
import ru.skypro.dynamicRecommendations.DTO.QueryDto;

import ru.skypro.dynamicRecommendations.entity.RuleEntity;
import ru.skypro.dynamicRecommendations.entity.QueryEntity;
import ru.skypro.dynamicRecommendations.repository.RuleRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rule")
public class RuleController {

    private final RuleRepository ruleRepository;

    public RuleController(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

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

    @GetMapping
    public ResponseEntity<RuleListResponseDto> getAllRules() {
        var rules = ruleRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new RuleListResponseDto(rules));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID id) {
        if (ruleRepository.existsById(id)) {
            ruleRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
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