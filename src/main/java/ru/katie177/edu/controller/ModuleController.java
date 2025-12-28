package ru.katie177.edu.controller;

import ru.katie177.edu.dto.ModuleDTO;
import ru.katie177.edu.service.ModuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
@Tag(name = "Модули", description = "Управление модулями курсов")
public class ModuleController {

    private final ModuleService moduleService;

    @Autowired
    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @GetMapping
    @Operation(summary = "Получить все модули")
    public ResponseEntity<List<ModuleDTO>> getAllModules() {
        return ResponseEntity.ok(moduleService.getAllModules());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить модуль по ID")
    public ResponseEntity<ModuleDTO> getModuleById(@PathVariable Long id) {
        return ResponseEntity.ok(moduleService.getModuleById(id));
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Получить модули курса")
    public ResponseEntity<List<ModuleDTO>> getModulesByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(moduleService.getModulesByCourse(courseId));
    }

    @PostMapping
    @Operation(summary = "Создать новый модуль")
    public ResponseEntity<ModuleDTO> createModule(@Valid @RequestBody ModuleDTO moduleDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(moduleService.createModule(moduleDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить модуль")
    public ResponseEntity<ModuleDTO> updateModule(@PathVariable Long id, @Valid @RequestBody ModuleDTO moduleDTO) {
        return ResponseEntity.ok(moduleService.updateModule(id, moduleDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить модуль")
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
        moduleService.deleteModule(id);
        return ResponseEntity.noContent().build();
    }
}