package ru.skypro.dynamicRecommendations.service;

import org.springframework.stereotype.Service;
import ru.skypro.dynamicRecommendations.DTO.RecommendationDto;
import ru.skypro.dynamicRecommendations.entity.QueryEntity;
import ru.skypro.dynamicRecommendations.entity.RuleEntity;
import ru.skypro.dynamicRecommendations.repository.UserDataRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для вычисления динамических правил, загруженных из базы данных.
 * <p>
 * Поддерживает следующие типы запросов:
 * <ul>
 *     <li><b>USER_OF</b> — проверяет, использует ли пользователь продукт указанного типа</li>
 *     <li><b>ACTIVE_USER_OF</b> — проверяет, совершено ли >=5 транзакций по продукту</li>
 *     <li><b>TRANSACTION_SUM_COMPARE</b> — сравнивает сумму пополнений/трат с порогом</li>
 *     <li><b>TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW</b> — сравнивает сумму пополнений и трат</li>
 * </ul>
 * </p>
 *
 * @author DynamicRecommendations Team
 */
@Service
public class DynamicRuleService {

    private final UserDataRepository userDataRepository;
    private final RuleStatsService ruleStatsService;

    public DynamicRuleService(UserDataRepository userDataRepository, RuleStatsService ruleStatsService) {
        this.userDataRepository = userDataRepository;
        this.ruleStatsService = ruleStatsService;
    }

    /**
     * Вычисляет правило для конкретного пользователя.
     *
     * @param rule   правило для проверки
     * @param userId идентификатор пользователя
     * @return рекомендация, если все условия выполнены, иначе пустой Optional
     */
    public Optional<RecommendationDto> evaluateRule(RuleEntity rule, UUID userId) {
        boolean allMatches = rule.getQueries().stream()
                .allMatch(query -> evaluateQuery(query, userId));

        if (allMatches) {
            ruleStatsService.incrementStat(rule.getId());
            return Optional.of(new RecommendationDto(
                    rule.getProductName(),
                    rule.getProductId(),
                    rule.getProductText()
            ));
        }
        return Optional.empty();
    }

    /**
     * Вычисляет один запрос правила.
     *
     * @param query  запрос для вычисления
     * @param userId идентификатор пользователя
     * @return результат проверки с учётом флага negate
     */
    private boolean evaluateQuery(QueryEntity query, UUID userId) {
        boolean result = switch (query.getQuery()) {
            case "USER_OF" -> userDataRepository.usesProductType(userId, query.getArguments().get(0));
            case "ACTIVE_USER_OF" -> userDataRepository.getTransactionCountByProductType(userId, query.getArguments().get(0)) >= 5;
            case "TRANSACTION_SUM_COMPARE" -> evaluateSumCompare(query, userId);
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> evaluateDepositWithdrawCompare(query, userId);
            default -> false;
        };

        return query.isNegate() != result;
    }

    /**
     * Вычисляет запрос типа TRANSACTION_SUM_COMPARE.
     *
     * @param query  запрос
     * @param userId идентификатор пользователя
     * @return результат сравнения суммы с порогом
     */
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

    /**
     * Вычисляет запрос типа TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW.
     *
     * @param query  запрос
     * @param userId идентификатор пользователя
     * @return результат сравнения суммы пополнений и трат
     */
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