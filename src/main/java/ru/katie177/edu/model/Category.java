package ru.katie177.edu.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> courses;

    public Category(Long id, String name, List<Course> courses) {
        this.id = id;
        this.name = name;
        setCourses(courses);
    }

    public void setCourses(List<Course> courses) {
        if (this.courses == null) {
            this.courses = new ArrayList<>();
        } else {
            this.courses.clear();
        }
        if (courses != null) {
            this.courses.addAll(courses);
            for (Course course : courses) {
                course.setCategory(this);
            }
        }
    }

    public void addCourse(Course course) {
        courses.add(course);
        course.setCategory(this);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        course.setCategory(null);
    }
}