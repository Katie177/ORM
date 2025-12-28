package ru.katie177.edu.service;

import ru.katie177.edu.dto.LessonDTO;
import ru.katie177.edu.exception.ResourceNotFoundException;
import ru.katie177.edu.model.Lesson;
import ru.katie177.edu.model.Module;
import ru.katie177.edu.repository.LessonRepository;
import ru.katie177.edu.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LessonService {

    private final LessonRepository lessonRepository;
    private final ModuleRepository moduleRepository;

    @Autowired
    public LessonService(LessonRepository lessonRepository, ModuleRepository moduleRepository) {
        this.lessonRepository = lessonRepository;
        this.moduleRepository = moduleRepository;
    }

    public List<LessonDTO> getAllLessons() {
        return lessonRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LessonDTO getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Урок не найден с ID: " + id));
        return convertToDTO(lesson);
    }

    public List<LessonDTO> getLessonsByModule(Long moduleId) {
        return lessonRepository.findByModuleId(moduleId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LessonDTO createLesson(LessonDTO lessonDTO) {
        Module module = moduleRepository.findById(lessonDTO.getModuleId())
                .orElseThrow(() -> new ResourceNotFoundException("Модуль не найден с ID: " + lessonDTO.getModuleId()));

        Lesson lesson = new Lesson();
        lesson.setTitle(lessonDTO.getTitle());
        lesson.setContent(lessonDTO.getContent());
        lesson.setVideoUrl(lessonDTO.getVideoUrl());
        lesson.setResourcesUrl(lessonDTO.getResourcesUrl());
        lesson.setModule(module);

        Lesson savedLesson = lessonRepository.save(lesson);
        return convertToDTO(savedLesson);
    }

    public LessonDTO updateLesson(Long id, LessonDTO lessonDTO) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Урок не найден с ID: " + id));

        lesson.setTitle(lessonDTO.getTitle());
        lesson.setContent(lessonDTO.getContent());
        lesson.setVideoUrl(lessonDTO.getVideoUrl());
        lesson.setResourcesUrl(lessonDTO.getResourcesUrl());

        if (lessonDTO.getModuleId() != null) {
            Module module = moduleRepository.findById(lessonDTO.getModuleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Модуль не найден с ID: " + lessonDTO.getModuleId()));
            lesson.setModule(module);
        }

        Lesson updatedLesson = lessonRepository.save(lesson);
        return convertToDTO(updatedLesson);
    }

    public void deleteLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Урок не найден с ID: " + id));
        lessonRepository.delete(lesson);
    }

    private LessonDTO convertToDTO(Lesson lesson) {
        LessonDTO dto = new LessonDTO();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setContent(lesson.getContent());
        dto.setVideoUrl(lesson.getVideoUrl());
        dto.setResourcesUrl(lesson.getResourcesUrl());
        dto.setModuleId(lesson.getModule().getId());
        dto.setModuleTitle(lesson.getModule().getTitle());
        dto.setCourseTitle(lesson.getModule().getCourse().getTitle());
        dto.setAssignmentCount(lesson.getAssignments().size());
        return dto;
    }
}