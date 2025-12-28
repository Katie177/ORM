package ru.katie177.edu.service;

import ru.katie177.edu.dto.SubmissionDTO;
import ru.katie177.edu.exception.ResourceNotFoundException;
import ru.katie177.edu.exception.DuplicateResourceException;
import ru.katie177.edu.model.Assignment;
import ru.katie177.edu.model.Submission;
import ru.katie177.edu.model.User;
import ru.katie177.edu.repository.SubmissionRepository;
import ru.katie177.edu.repository.UserRepository;
import ru.katie177.edu.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;

    @Autowired
    public SubmissionService(SubmissionRepository submissionRepository,
                             UserRepository userRepository,
                             AssignmentRepository assignmentRepository) {
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
        this.assignmentRepository = assignmentRepository;
    }

    public SubmissionDTO submitAssignment(SubmissionDTO submissionDTO) {
        // Проверка на дублирование
        if (submissionRepository.existsByStudentIdAndAssignmentId(
                submissionDTO.getStudentId(), submissionDTO.getAssignmentId())) {
            throw new DuplicateResourceException("Решение уже отправлено для этого задания");
        }

        User student = userRepository.findById(submissionDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Студент не найден с ID: " + submissionDTO.getStudentId()));

        Assignment assignment = assignmentRepository.findById(submissionDTO.getAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Задание не найдено с ID: " + submissionDTO.getAssignmentId()));

        Submission submission = new Submission();
        submission.setStudent(student);
        submission.setAssignment(assignment);
        submission.setContent(submissionDTO.getContent());
        submission.setFilePath(submissionDTO.getFilePath());

        Submission savedSubmission = submissionRepository.save(submission);
        return convertToDTO(savedSubmission);
    }

    public SubmissionDTO gradeSubmission(Long submissionId, Integer score, String feedback) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Решение не найдено с ID: " + submissionId));

        submission.setScore(score);

        Submission gradedSubmission = submissionRepository.save(submission);
        return convertToDTO(gradedSubmission);
    }

    public List<SubmissionDTO> getSubmissionsByAssignment(Long assignmentId) {
        return submissionRepository.findByAssignmentId(assignmentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SubmissionDTO> getSubmissionsByStudent(Long studentId) {
        return submissionRepository.findByStudentId(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public SubmissionDTO getSubmissionById(Long id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Решение не найдено с ID: " + id));
        return convertToDTO(submission);
    }

    public void deleteSubmission(Long id) {
        if (!submissionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Решение не найдено с ID: " + id);
        }
        submissionRepository.deleteById(id);
    }

    public Double getAverageScoreForAssignment(Long assignmentId) {
        return submissionRepository.findAverageScoreByAssignmentId(assignmentId)
                .orElse(0.0);
    }

    private SubmissionDTO convertToDTO(Submission submission) {
        SubmissionDTO dto = new SubmissionDTO();
        dto.setId(submission.getId());
        dto.setAssignmentId(submission.getAssignment().getId());
        dto.setAssignmentTitle(submission.getAssignment().getTitle());
        dto.setStudentId(submission.getStudent().getId());
        dto.setStudentName(submission.getStudent().getName());
        dto.setContent(submission.getContent());
        dto.setFilePath(submission.getFilePath());
        dto.setScore(submission.getScore());
        dto.setFeedback(submission.getFeedback());
        dto.setSubmittedAt(submission.getSubmittedAt());
        dto.setGradedAt(submission.getGradedAt());
        return dto;
    }
}