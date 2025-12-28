package ru.katie177.edu.service;

import ru.katie177.edu.dto.AssignmentDTO;
import ru.katie177.edu.exception.ResourceNotFoundException;
import ru.katie177.edu.model.Assignment;
import ru.katie177.edu.model.Lesson;
import ru.katie177.edu.model.Submission;
import ru.katie177.edu.repository.AssignmentRepository;
import ru.katie177.edu.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final LessonRepository lessonRepository;

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository, LessonRepository lessonRepository) {
        this.assignmentRepository = assignmentRepository;
        this.lessonRepository = lessonRepository;
    }

    public List<AssignmentDTO> getAllAssignments() {
        return assignmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AssignmentDTO getAssignmentById(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задание не найдено с ID: " + id));
        return convertToDTO(assignment);
    }

    public List<AssignmentDTO> getAssignmentsByLesson(Long lessonId) {
        return assignmentRepository.findByLessonId(lessonId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AssignmentDTO createAssignment(AssignmentDTO assignmentDTO) {
        Lesson lesson = lessonRepository.findById(assignmentDTO.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException("Урок не найден с ID: " + assignmentDTO.getLessonId()));

        Assignment assignment = new Assignment();
        assignment.setTitle(assignmentDTO.getTitle());
        assignment.setDescription(assignmentDTO.getDescription());
        assignment.setDueDate(assignmentDTO.getDueDate());
        assignment.setMaxScore(assignmentDTO.getMaxScore());
        assignment.setLesson(lesson);

        Assignment savedAssignment = assignmentRepository.save(assignment);
        return convertToDTO(savedAssignment);
    }

    public AssignmentDTO updateAssignment(Long id, AssignmentDTO assignmentDTO) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задание не найдено с ID: " + id));

        assignment.setTitle(assignmentDTO.getTitle());
        assignment.setDescription(assignmentDTO.getDescription());
        assignment.setDueDate(assignmentDTO.getDueDate());
        assignment.setMaxScore(assignmentDTO.getMaxScore());

        if (assignmentDTO.getLessonId() != null) {
            Lesson lesson = lessonRepository.findById(assignmentDTO.getLessonId())
                    .orElseThrow(() -> new ResourceNotFoundException("Урок не найден с ID: " + assignmentDTO.getLessonId()));
            assignment.setLesson(lesson);
        }

        Assignment updatedAssignment = assignmentRepository.save(assignment);
        return convertToDTO(updatedAssignment);
    }

    public void deleteAssignment(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задание не найдено с ID: " + id));
        assignmentRepository.delete(assignment);
    }

    private AssignmentDTO convertToDTO(Assignment assignment) {
        AssignmentDTO dto = new AssignmentDTO();
        dto.setId(assignment.getId());
        dto.setTitle(assignment.getTitle());
        dto.setDescription(assignment.getDescription());
        dto.setLessonId(assignment.getLesson().getId());
        dto.setLessonTitle(assignment.getLesson().getTitle());
        dto.setModuleTitle(assignment.getLesson().getModule().getTitle());
        dto.setCourseTitle(assignment.getLesson().getModule().getCourse().getTitle());
        dto.setDueDate(assignment.getDueDate());
        dto.setMaxScore(assignment.getMaxScore());
        dto.setSubmissionCount(assignment.getSubmissions().size());

        // Рассчет среднего балла
        if (!assignment.getSubmissions().isEmpty()) {
            double average = assignment.getSubmissions().stream()
                    .filter(s -> s.getScore() != null && s.getScore() > 0)
                    .mapToInt(Submission::getScore)
                    .average()
                    .orElse(0.0);
            dto.setAverageScore(average);
        } else {
            dto.setAverageScore(0.0);
        }

        return dto;
    }
}