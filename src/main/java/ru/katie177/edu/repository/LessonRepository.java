package ru.katie177.edu.repository;

import ru.katie177.edu.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByModuleId(Long moduleId);
    List<Lesson> findByModuleIdOrderByIdAsc(Long moduleId);
}