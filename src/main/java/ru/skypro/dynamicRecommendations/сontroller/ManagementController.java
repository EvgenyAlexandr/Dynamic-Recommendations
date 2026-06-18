package ru.skypro.dynamicRecommendations.controller;

import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.dynamicRecommendations.repository.UserDataRepository;

import java.util.Map;

/**
 * REST-контроллер для управления приложением.
 * <p>
 * Предоставляет endpoints для:
 * <ul>
 *     <li>очистки кэшей</li>
 *     <li>получения информации о версии приложения</li>
 * </ul>
 * </p>
 *
 * @author DynamicRecommendations Team
 */
@RestController
@RequestMapping("/management")
public class ManagementController {

    private final UserDataRepository userDataRepository;
    private final BuildProperties buildProperties;

    public ManagementController(UserDataRepository userDataRepository,
                                BuildProperties buildProperties) {
        this.userDataRepository = userDataRepository;
        this.buildProperties = buildProperties;
    }

    /**
     * Очищает все кэши репозитория пользовательских данных.
     *
     * @return HTTP 200 OK
     */
    @PostMapping("/clear-caches")
    public ResponseEntity<Void> clearCaches() {
        userDataRepository.clearCaches();
        return ResponseEntity.ok().build();
    }

    /**
     * Возвращает информацию о приложении (название и версию).
     *
     * @return Map с ключами "name" и "version"
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getInfo() {
        return ResponseEntity.ok(Map.of(
                "name", buildProperties.getName(),
                "version", buildProperties.getVersion()
        ));
    }
}