package ru.skypro.dynamicRecommendations.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.skypro.dynamicRecommendations.DTO.RecommendationDto;
import ru.skypro.dynamicRecommendations.servise.RecommendationService;
import org.springframework.beans.factory.annotation.Value;
import ru.skypro.dynamicRecommendations.repository.UserDataRepository;

import java.util.List;
import java.util.UUID;

@Component
public class DynamicRecommendationsBot extends TelegramLongPollingBot {

    private final String botToken;
    private final String botName;
    private final RecommendationService recommendationService;
    private final UserDataRepository userDataRepository;

    public DynamicRecommendationsBot(@Value("${telegram.bot.token}") String botToken,
                                     @Value("${telegram.bot.name}") String botName,
                                     RecommendationService recommendationService,
                                     UserDataRepository userDataRepository) {
        this.botToken = botToken;
        this.botName = botName;
        this.recommendationService = recommendationService;
        this.userDataRepository = userDataRepository;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equalsIgnoreCase("/start")) {
                sendMessage(chatId, """
                        Привет! Я бот для рекомендаций банковских продуктов.
                        Используйте команду:
                        /recommend <Имя пользователя>
                        """);
            } else if (messageText.startsWith("/recommend")) {
                String[] parts = messageText.split(" ", 2);
                if (parts.length < 2) {
                    sendMessage(chatId, "Пожалуйста, укажите имя пользователя. Пример: /recommend Иван Иванов");
                    return;
                }
                String fullName = parts[1];
                handleRecommendation(chatId, fullName);
            } else {
                sendMessage(chatId, "Неизвестная команда. Используйте /start для справки.");
            }
        }
    }

    private void handleRecommendation(long chatId, String fullName) {
        // Поиск пользователя по имени в базе transactions
        String sql = "SELECT DISTINCT user_id, user_name FROM transactions WHERE user_name = ?";
        var results = userDataRepository.getJdbcTemplate().query(sql,
                (rs, rowNum) -> new Object[]{rs.getObject("user_id", UUID.class), rs.getString("user_name")},
                fullName);

        if (results.size() != 1) {
            sendMessage(chatId, "Пользователь не найден");
            return;
        }

        UUID userId = (UUID) results.get(0)[0];
        var response = recommendationService.getRecommendations(userId);
        List<RecommendationDto> recommendations = response.getRecommendations();

        StringBuilder answer = new StringBuilder();
        answer.append("Здравствуйте ").append(fullName).append("\n");
        answer.append("Новые продукты для вас:\n");

        if (recommendations.isEmpty()) {
            answer.append("Нет рекомендаций.");
        } else {
            for (int i = 0; i < recommendations.size(); i++) {
                RecommendationDto rec = recommendations.get(i);
                answer.append(i + 1).append(". ").append(rec.getName())
                        .append(" — ").append(rec.getText()).append("\n\n");
            }
        }

        sendMessage(chatId, answer.toString());
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() { return botName; }
    @Override
    public String getBotToken() { return botToken; }
}
