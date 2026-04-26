package ru.skypro.dynamicRecommendations.recommendation;

import org.springframework.stereotype.Component;
import ru.skypro.dynamicRecommendations.DTO.RecommendationDto;
import ru.skypro.dynamicRecommendations.repository.UserDataRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Правило рекомендации продукта "Простой кредит".
 * <p>
 * Условия:
 * <ol>
 *     <li>Пользователь НЕ использует кредитные продукты (CREDIT)</li>
 *     <li>Сумма пополнений по дебету (DEBIT) превышает сумму трат по дебету</li>
 *     <li>Сумма трат по дебету превышает 100 000 ₽</li>
 * </ol>
 * </p>
 *
 * @author DynamicRecommendations Team
 */
@Component
public class SimpleCreditRuleSet implements RecommendationRuleSet {

    private static final UUID PRODUCT_ID = UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f");
    private static final String PRODUCT_NAME = "Простой кредит";
    private static final String PRODUCT_DESCRIPTION = "Откройте мир выгодных кредитов с нами!\n\nИщете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно! Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.\n\nПочему выбирают нас:\n\nБыстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов.\n\nУдобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.\n\nШирокий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образование, лечение и многое другое.\n\nНе упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!";

    private final UserDataRepository userDataRepository;

    public SimpleCreditRuleSet(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        boolean notUsesCredit = !userDataRepository.usesProductType(userId, "CREDIT");
        Long debitDeposits = userDataRepository.getTotalDepositsByProductType(userId, "DEBIT");
        Long debitSpends = userDataRepository.getTotalSpendsByProductType(userId, "DEBIT");
        boolean depositsGreaterThanSpends = debitDeposits > debitSpends;
        boolean spendsOver100k = debitSpends > 100000;

        if (notUsesCredit && depositsGreaterThanSpends && spendsOver100k) {
            return Optional.of(new RecommendationDto(PRODUCT_NAME, PRODUCT_ID, PRODUCT_DESCRIPTION));
        }

        return Optional.empty();
    }
}