package ru.katie177.edu.controller;

import ru.katie177.edu.dto.EnrollmentDTO;
import ru.katie177.edu.model.Enrollment;
import ru.katie177.edu.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@Tag(name = "Записи на курсы", description = "Управление записями студентов на курсы")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/enroll")
    @Operation(summary = "Записать студента на курс")
    public ResponseEntity<EnrollmentDTO> enrollStudent(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enrollmentService.enrollStudent(studentId, courseId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Отписать студента от курса")
    public ResponseEntity<Void> unenrollStudent(@PathVariable Long id) {
        enrollmentService.unenrollStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Обновить статус записи")
    public ResponseEntity<EnrollmentDTO> updateEnrollmentStatus(
            @PathVariable Long id,
            @RequestParam Enrollment.EnrollmentStatus status) {
        return ResponseEntity.ok(enrollmentService.updateEnrollmentStatus(id, status));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Получить записи студента")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudent(studentId));
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Получить записи на курс")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourse(courseId));
    }

    @GetMapping("/check")
    @Operation(summary = "Проверить запись студента на курс")
    public ResponseEntity<Boolean> isStudentEnrolled(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        return ResponseEntity.ok(enrollmentService.isStudentEnrolled(studentId, courseId));
    }
}