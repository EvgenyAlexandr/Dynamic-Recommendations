# Dynamic Recommendations

Сервис для динамического формирования рекомендаций банковских продуктов на основе транзакционной истории пользователя.

## Документация

Вся документация находится в [Wiki](https://github.com/EvgenyAlexandr/Dynamic-Recommendations/wiki) проекта.

### Страницы Wiki
- [Требования и диаграммы](https://github.com/EvgenyAlexandr/Dynamic-Recommendations/wiki/Requirements)
- [Таблица трассировки](https://github.com/EvgenyAlexandr/Dynamic-Recommendations/wiki/Traceability-Matrix)
- [Архитектура приложения](https://github.com/EvgenyAlexandr/Dynamic-Recommendations/wiki/Architecture)
- [API Документация](https://github.com/EvgenyAlexandr/Dynamic-Recommendations/wiki/API-Documentation)
- [Инструкция по развертыванию](https://github.com/EvgenyAlexandr/Dynamic-Recommendations/wiki/Deployment)

## Быстрый старт

### Требования
- JDK 17
- PostgreSQL 14+

### Сборка и запуск
```bash
./mvnw clean package
java -jar target/dynamicRecommendations-0.0.1-SNAPSHOT.jar
