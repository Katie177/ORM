package ru.katie177.edu.service;

import ru.katie177.edu.dto.ModuleDTO;
import ru.katie177.edu.exception.ResourceNotFoundException;
import ru.katie177.edu.model.Course;
import ru.katie177.edu.model.Module;
import ru.katie177.edu.repository.ModuleRepository;
import ru.katie177.edu.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public ModuleService(ModuleRepository moduleRepository, CourseRepository courseRepository) {
        this.moduleRepository = moduleRepository;
        this.courseRepository = courseRepository;
    }

    public List<ModuleDTO> getAllModules() {
        return moduleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ModuleDTO getModuleById(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Модуль не найден с ID: " + id));
        return convertToDTO(module);
    }

    public List<ModuleDTO> getModulesByCourse(Long courseId) {
        return moduleRepository.findByCourseId(courseId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ModuleDTO createModule(ModuleDTO moduleDTO) {
        Course course = courseRepository.findById(moduleDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Курс не найден с ID: " + moduleDTO.getCourseId()));

        Module module = new Module();
        module.setTitle(moduleDTO.getTitle());
        module.setDescription(moduleDTO.getDescription());
        module.setCourse(course);
        module.setOrderIndex(moduleDTO.getOrderIndex());

        Module savedModule = moduleRepository.save(module);
        return convertToDTO(savedModule);
    }

    public ModuleDTO updateModule(Long id, ModuleDTO moduleDTO) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Модуль не найден с ID: " + id));

        module.setTitle(moduleDTO.getTitle());
        module.setDescription(moduleDTO.getDescription());
        module.setOrderIndex(moduleDTO.getOrderIndex());

        if (moduleDTO.getCourseId() != null) {
            Course course = courseRepository.findById(moduleDTO.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Курс не найден с ID: " + moduleDTO.getCourseId()));
            module.setCourse(course);
        }

        Module updatedModule = moduleRepository.save(module);
        return convertToDTO(updatedModule);
    }

    public void deleteModule(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Модуль не найден с ID: " + id));
        moduleRepository.delete(module);
    }

    private ModuleDTO convertToDTO(Module module) {
        ModuleDTO dto = new ModuleDTO();
        dto.setId(module.getId());
        dto.setTitle(module.getTitle());
        dto.setDescription(module.getDescription());
        dto.setCourseId(module.getCourse().getId());
        dto.setCourseTitle(module.getCourse().getTitle());
        dto.setOrderIndex(module.getOrderIndex());
        dto.setLessonCount(module.getLessons().size());
        dto.setHasQuiz(module.getQuiz() != null);
        return dto;
    }
}