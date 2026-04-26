package ru.skypro.dynamicRecommendations.сontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.dynamicRecommendations.repository.UserDataRepository;
import java.util.Map;

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

    @PostMapping("/clear-caches")
    public ResponseEntity<Void> clearCaches() {
        userDataRepository.clearCaches();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getInfo() {
        return ResponseEntity.ok(Map.of(
                "name", buildProperties.getName(),
                "version", buildProperties.getVersion()
        ));
    }
}