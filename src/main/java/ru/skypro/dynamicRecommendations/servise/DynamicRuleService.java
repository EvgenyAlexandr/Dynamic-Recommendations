package ru.skypro.dynamicRecommendations.servise;

import org.springframework.stereotype.Service;
import ru.skypro.dynamicRecommendations.DTO.RecommendationDto;
import ru.skypro.dynamicRecommendations.entity.QueryEntity;
import ru.skypro.dynamicRecommendations.entity.RuleEntity;
import ru.skypro.dynamicRecommendations.repository.UserDataRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DynamicRuleService {

    private final UserDataRepository userDataRepository;
    private final RuleStatsService ruleStatsService;


    public DynamicRuleService(UserDataRepository userDataRepository, RuleStatsService ruleStatsService) {
        this.userDataRepository = userDataRepository;
        this.ruleStatsService   = ruleStatsService;
    }

    public Optional<RecommendationDto> evaluateRule(RuleEntity rule, UUID userId) {
        boolean allMatches = rule.getQueries().stream()
                .allMatch(query -> evaluateQuery(query, userId));

        if (allMatches) {
            ruleStatsService.incrementStat(rule.getId()); // инкремент
            return Optional.of(new RecommendationDto(
                    rule.getProductName(),
                    rule.getProductId(),
                    rule.getProductText()
            ));
        }
        return Optional.empty();
    }


    private boolean evaluateQuery(QueryEntity query, UUID userId) {
        boolean result = switch (query.getQuery()) {
            case "USER_OF" -> userDataRepository.usesProductType(userId, query.getArguments().get(0));
            case "ACTIVE_USER_OF" -> userDataRepository.getTransactionCountByProductType(userId, query.getArguments().get(0)) >= 5;
            case "TRANSACTION_SUM_COMPARE" -> evaluateSumCompare(query, userId);
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> evaluateDepositWithdrawCompare(query, userId);
            default -> false;
        };

        return query.isNegate() != result; // XOR с отрицанием
    }

    private boolean evaluateSumCompare(QueryEntity query, UUID userId) {
        String productType = query.getArguments().get(0);
        String transactionType = query.getArguments().get(1);
        String operator = query.getArguments().get(2);
        long threshold = Long.parseLong(query.getArguments().get(3));

        Long sum = "DEPOSIT".equals(transactionType) ?
                userDataRepository.getTotalDepositsByProductType(userId, productType) :
                userDataRepository.getTotalSpendsByProductType(userId, productType);

        return switch (operator) {
            case ">" -> sum > threshold;
            case "<" -> sum < threshold;
            case "=" -> sum == threshold;
            case ">=" -> sum >= threshold;
            case "<=" -> sum <= threshold;
            default -> false;
        };
    }

    private boolean evaluateDepositWithdrawCompare(QueryEntity query, UUID userId) {
        String productType = query.getArguments().get(0);
        String operator = query.getArguments().get(1);

        Long deposits = userDataRepository.getTotalDepositsByProductType(userId, productType);
        Long withdraws = userDataRepository.getTotalSpendsByProductType(userId, productType);

        return switch (operator) {
            case ">" -> deposits > withdraws;
            case "<" -> deposits < withdraws;
            case "=" -> deposits == withdraws;
            case ">=" -> deposits >= withdraws;
            case "<=" -> deposits <= withdraws;
            default -> false;
        };
    }
}