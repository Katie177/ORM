package ru.katie177.edu.controller;

import ru.katie177.edu.dto.SubmissionDTO;
import ru.katie177.edu.service.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
@Tag(name = "Решения заданий", description = "Управление решениями заданий")
public class SubmissionController {

    private final SubmissionService submissionService;

    @Autowired
    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping
    @Operation(summary = "Отправить решение задания")
    public ResponseEntity<SubmissionDTO> submitAssignment(@Valid @RequestBody SubmissionDTO submissionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(submissionService.submitAssignment(submissionDTO));
    }

    @PutMapping("/{id}/grade")
    @Operation(summary = "Оценить решение")
    public ResponseEntity<SubmissionDTO> gradeSubmission(
            @PathVariable Long id,
            @RequestParam Integer score,
            @RequestParam(required = false) String feedback) {
        return ResponseEntity.ok(submissionService.gradeSubmission(id, score, feedback));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить решение по ID")
    public ResponseEntity<SubmissionDTO> getSubmissionById(@PathVariable Long id) {
        return ResponseEntity.ok(submissionService.getSubmissionById(id));
    }

    @GetMapping("/assignment/{assignmentId}")
    @Operation(summary = "Получить решения по заданию")
    public ResponseEntity<List<SubmissionDTO>> getSubmissionsByAssignment(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByAssignment(assignmentId));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Получить решения студента")
    public ResponseEntity<List<SubmissionDTO>> getSubmissionsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByStudent(studentId));
    }

    @GetMapping("/assignment/{assignmentId}/average")
    @Operation(summary = "Получить средний балл по заданию")
    public ResponseEntity<Double> getAverageScoreForAssignment(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(submissionService.getAverageScoreForAssignment(assignmentId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить решение")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        submissionService.deleteSubmission(id);
        return ResponseEntity.noContent().build();
    }
}