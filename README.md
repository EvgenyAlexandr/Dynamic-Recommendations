# Telegram Bot - Dynamic Recommendations

📌 Описание проекта

Проект представляет собой backend-систему банковского приложения с базовыми операциями и системой рекомендаций, интегрированной в Telegram-бота.
Система анализирует поведение пользователей и формирует персонализированные рекомендации на основе динамических правил.


## ⚙️ Документация

Вся документация находится в [Wiki](https://github.com/EvgenyAlexandr/Dynamic-Recommendations/wiki) проекта.

### Страницы Wiki
- [Требования и диаграммы](https://github.com/EvgenyAlexandr/Dynamic-Recommendations/wiki/Requirements)
- [Таблица трассировки](https://github.com/EvgenyAlexandr/Dynamic-Recommendations/wiki/Traceability-Matrix)
- [Архитектура приложения](https://github.com/EvgenyAlexandr/Dynamic-Recommendations/wiki/Architecture)
- [API Документация](https://github.com/EvgenyAlexandr/Dynamic-Recommendations/wiki/API-Documentation)
- [Инструкция по развертыванию](https://github.com/EvgenyAlexandr/Dynamic-Recommendations/wiki/Deployment)

## Быстрый старт

### Требования
- JDK 17+
- Maven 3.8+
- PostgreSQL 13+

### 📦 Сборка и запуск
```bash
./mvnw clean package
```
```bash
java -jar target/dynamicRecommendations-0.0.1-SNAPSHOT.jar
```

## Swagger/OpenAPI

Документация API доступна:
```bash
http://localhost:8080/swagger-ui/index.html
```

