package ru.katie177.edu.controller;

import ru.katie177.edu.dto.ProfileDTO;
import ru.katie177.edu.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@Tag(name = "Профили", description = "Управление профилями пользователей")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить профиль пользователя")
    public ResponseEntity<ProfileDTO> getProfileByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getProfileByUserId(userId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить профиль по ID")
    public ResponseEntity<ProfileDTO> getProfileById(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.getProfileById(id));
    }

    @PostMapping
    @Operation(summary = "Создать профиль")
    public ResponseEntity<ProfileDTO> createProfile(@Valid @RequestBody ProfileDTO profileDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(profileService.createProfile(profileDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить профиль")
    public ResponseEntity<ProfileDTO> updateProfile(@PathVariable Long id, @Valid @RequestBody ProfileDTO profileDTO) {
        return ResponseEntity.ok(profileService.updateProfile(id, profileDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить профиль")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}