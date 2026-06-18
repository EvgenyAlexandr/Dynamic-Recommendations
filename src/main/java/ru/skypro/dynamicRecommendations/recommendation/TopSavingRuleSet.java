package ru.skypro.dynamicRecommendations.recommendation;

import org.springframework.stereotype.Component;
import ru.skypro.dynamicRecommendations.DTO.RecommendationDto;
import ru.skypro.dynamicRecommendations.repository.UserDataRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Правило рекомендации продукта "Top Saving" (копилка).
 * <p>
 * Условия:
 * <ol>
 *     <li>Пользователь использует хотя бы один дебетовый продукт (DEBIT)</li>
 *     <li>Сумма пополнений по DEBIT ≥ 50 000 ₽ ИЛИ по SAVING ≥ 50 000 ₽</li>
 *     <li>Сумма пополнений по DEBIT превышает сумму трат по DEBIT</li>
 * </ol>
 * </p>
 *
 * @author DynamicRecommendations Team
 */
@Component
public class TopSavingRuleSet implements RecommendationRuleSet {

    private static final UUID PRODUCT_ID = UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925");
    private static final String PRODUCT_NAME = "Top Saving";
    private static final String PRODUCT_DESCRIPTION = "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем!\n\nПреимущества «Копилки»:\n\nНакопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.\n\nПрозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.\n\nБезопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.\n\nНачните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!";

    private final UserDataRepository userDataRepository;

    public TopSavingRuleSet(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        boolean usesDebit = userDataRepository.usesProductType(userId, "DEBIT");
        Long debitDeposits = userDataRepository.getTotalDepositsByProductType(userId, "DEBIT");
        Long savingDeposits = userDataRepository.getTotalDepositsByProductType(userId, "SAVING");
        boolean depositsOver50k = (debitDeposits >= 50000) || (savingDeposits >= 50000);
        Long debitSpends = userDataRepository.getTotalSpendsByProductType(userId, "DEBIT");
        boolean depositsGreaterThanSpends = debitDeposits > debitSpends;

        if (usesDebit && depositsOver50k && depositsGreaterThanSpends) {
            return Optional.of(new RecommendationDto(PRODUCT_NAME, PRODUCT_ID, PRODUCT_DESCRIPTION));
        }

        return Optional.empty();
    }
}