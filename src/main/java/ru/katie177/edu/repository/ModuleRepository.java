package ru.katie177.edu.repository;

import ru.katie177.edu.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    List<Module> findByCourseId(Long courseId);
    List<Module> findByCourseIdOrderByOrderIndexAsc(Long courseId);
}