package ru.skypro.dynamicRecommendations.recommendation;

import org.springframework.stereotype.Component;
import ru.skypro.dynamicRecommendations.DTO.RecommendationDto;
import ru.skypro.dynamicRecommendations.repository.UserDataRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Правило рекомендации продукта "Invest 500" (инвестиционный счёт).
 * <p>
 * Условия:
 * <ol>
 *     <li>Пользователь использует хотя бы один дебетовый продукт (DEBIT)</li>
 *     <li>Пользователь НЕ использует инвестиционные продукты (INVEST)</li>
 *     <li>Сумма пополнений по сберегательным продуктам (SAVING) превышает 1000 ₽</li>
 * </ol>
 * </p>
 *
 * @author DynamicRecommendations Team
 */
@Component
public class Invest500RuleSet implements RecommendationRuleSet {

    private static final UUID PRODUCT_ID = UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a");
    private static final String PRODUCT_NAME = "Invest 500";
    private static final String PRODUCT_DESCRIPTION = "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!";

    private final UserDataRepository userDataRepository;

    public Invest500RuleSet(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        boolean usesDebit = userDataRepository.usesProductType(userId, "DEBIT");
        boolean notUsesInvest = !userDataRepository.usesProductType(userId, "INVEST");
        Long savingDeposits = userDataRepository.getTotalDepositsByProductType(userId, "SAVING");
        boolean savingDepositsOver1000 = savingDeposits > 1000;

        if (usesDebit && notUsesInvest && savingDepositsOver1000) {
            return Optional.of(new RecommendationDto(PRODUCT_NAME, PRODUCT_ID, PRODUCT_DESCRIPTION));
        }

        return Optional.empty();
    }
}