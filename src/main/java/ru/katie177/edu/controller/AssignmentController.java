package ru.katie177.edu.controller;

import ru.katie177.edu.dto.AssignmentDTO;
import ru.katie177.edu.service.AssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@Tag(name = "Задания", description = "Управление заданиями уроков")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping
    @Operation(summary = "Получить все задания")
    public ResponseEntity<List<AssignmentDTO>> getAllAssignments() {
        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить задание по ID")
    public ResponseEntity<AssignmentDTO> getAssignmentById(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.getAssignmentById(id));
    }

    @GetMapping("/lesson/{lessonId}")
    @Operation(summary = "Получить задания урока")
    public ResponseEntity<List<AssignmentDTO>> getAssignmentsByLesson(@PathVariable Long lessonId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByLesson(lessonId));
    }

    @PostMapping
    @Operation(summary = "Создать новое задание")
    public ResponseEntity<AssignmentDTO> createAssignment(@Valid @RequestBody AssignmentDTO assignmentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assignmentService.createAssignment(assignmentDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить задание")
    public ResponseEntity<AssignmentDTO> updateAssignment(@PathVariable Long id, @Valid @RequestBody AssignmentDTO assignmentDTO) {
        return ResponseEntity.ok(assignmentService.updateAssignment(id, assignmentDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить задание")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}