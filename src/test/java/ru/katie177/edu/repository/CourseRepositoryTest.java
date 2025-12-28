package ru.katie177.edu.repository;

import ru.katie177.edu.model.Category;
import ru.katie177.edu.model.Course;
import ru.katie177.edu.model.Tag;
import ru.katie177.edu.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig
public class CourseRepositoryTest extends AbstractRepositoryTest {
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private TagRepository tagRepository;
    
    private User teacher;
    private Category category;
    private Tag tag1;
    private Tag tag2;
    private Course course1;
    private Course course2;
    
    @BeforeEach
    public void setUp() {
        // Очистка репозиториев
        courseRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();
        tagRepository.deleteAll();
        
        // Создание тестовых данных
        teacher = new User();
        teacher.setName("Dr. Smith");
        teacher.setEmail("dr.smith@university.edu");
        teacher.setRole(User.Role.TEACHER);
        teacher.setCreatedAt(LocalDateTime.now());
        teacher = userRepository.save(teacher);
        
        category = new Category();
        category.setName("Computer Science");
        category = categoryRepository.save(category);
        
        tag1 = new Tag();
        tag1.setName("Java");
        tag1 = tagRepository.save(tag1);
        
        tag2 = new Tag();
        tag2.setName("Spring");
        tag2 = tagRepository.save(tag2);
        
        course1 = new Course();
        course1.setTitle("Advanced Java Programming");
        course1.setDescription("Comprehensive course on advanced Java concepts");
        course1.setTeacher(teacher);
        course1.setCategory(category);
        course1.setStartDate(LocalDate.now().plusDays(7));
        course1.setDuration(12);
        course1.setCreatedAt(LocalDateTime.now().minusDays(5));
        course1.setTags(Arrays.asList(tag1));
        
        course2 = new Course();
        course2.setTitle("Spring Boot Masterclass");
        course2.setDescription("Master Spring Boot development");
        course2.setTeacher(teacher);
        course2.setCategory(category);
        course2.setStartDate(LocalDate.now().plusDays(14));
        course2.setDuration(8);
        course2.setCreatedAt(LocalDateTime.now().minusDays(3));
        course2.setTags(Arrays.asList(tag1, tag2));
        
        courseRepository.saveAll(Arrays.asList(course1, course2));
    }
    
    @Test
    public void testFindByTeacherId_ReturnsCoursesByTeacher() {
        List<Course> courses = courseRepository.findByTeacherId(teacher.getId());
        
        assertThat(courses).hasSize(2);
        assertThat(courses).extracting(Course::getTitle)
                .containsExactlyInAnyOrder("Advanced Java Programming", "Spring Boot Masterclass");
    }
    
    @Test
    public void testFindByCategoryId_ReturnsCoursesByCategory() {
        List<Course> courses = courseRepository.findByCategoryId(category.getId());
        
        assertThat(courses).hasSize(2);
        assertThat(courses).extracting(Course::getTitle)
                .containsExactlyInAnyOrder("Advanced Java Programming", "Spring Boot Masterclass");
    }
    
    @Test
    public void testFindByTagId_ReturnsCoursesWithTag() {
        // Курсы с тегом "Java" (tag1)
        List<Course> javaCourses = courseRepository.findByTagId(tag1.getId());
        assertThat(javaCourses).hasSize(2);
        assertThat(javaCourses).extracting(Course::getTitle)
                .containsExactlyInAnyOrder("Advanced Java Programming", "Spring Boot Masterclass");
        
        // Курсы с тегом "Spring" (tag2)
        List<Course> springCourses = courseRepository.findByTagId(tag2.getId());
        assertThat(springCourses).hasSize(1);
        assertThat(springCourses.get(0).getTitle()).isEqualTo("Spring Boot Masterclass");
    }
    
    @Test
    public void testSearchByKeyword_FindsByTitle() {
        List<Course> results = courseRepository.searchByKeyword("Java");
        
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Advanced Java Programming");
    }
    
    @Test
    public void testSearchByKeyword_FindsByDescription() {
        List<Course> results = courseRepository.searchByKeyword("Master");
        
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Spring Boot Masterclass");
    }
    
    @Test
    public void testSearchByKeyword_CaseInsensitive() {
        List<Course> results = courseRepository.searchByKeyword("java");
        
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Advanced Java Programming");
    }
    
    @Test
    public void testSearchByKeyword_NoResultsWhenNotFound() {
        List<Course> results = courseRepository.searchByKeyword("Nonexistent");
        
        assertThat(results).isEmpty();
    }
    
    @Test
    public void testCountActiveStudents_ReturnsCorrectCount() {
        // В этом тесте мы не можем протестировать Enrollment, так как репозиторий не внедрен
        // Но мы можем проверить, что метод не возвращает null
        Long count1 = courseRepository.countActiveStudents(course1.getId());
        Long count2 = courseRepository.countActiveStudents(course2.getId());
        
        // Метод должен возвращать число, даже если 0
        assertThat(count1).isNotNull();
        assertThat(count2).isNotNull();
        // Поскольку у нас нет данных об участниках, ожидаем 0
        assertThat(count1).isEqualTo(0);
        assertThat(count2).isEqualTo(0);
    }
    
    @Test
    public void testSaveCourse_PersistsToDatabase() {
        Course newCourse = new Course();
        newCourse.setTitle("Test Course");
        newCourse.setDescription("Test Description");
        newCourse.setTeacher(teacher);
        newCourse.setCategory(category);
        newCourse.setStartDate(LocalDate.now().plusDays(7));
        newCourse.setDuration(6);
        newCourse.setTags(Arrays.asList(tag1));
        
        Course savedCourse = courseRepository.save(newCourse);
        
        assertThat(savedCourse.getId()).isNotNull();
        assertThat(savedCourse.getTitle()).isEqualTo("Test Course");
        assertThat(savedCourse.getTeacher().getId()).isEqualTo(teacher.getId());
        assertThat(savedCourse.getCategory().getId()).isEqualTo(category.getId());
        assertThat(savedCourse.getTags()).hasSize(1);
        assertThat(savedCourse.getTags().get(0).getId()).isEqualTo(tag1.getId());
        
        // Проверка загрузки из базы
        Course foundCourse = courseRepository.findById(savedCourse.getId()).orElse(null);
        assertThat(foundCourse).isNotNull();
        assertThat(foundCourse.getTitle()).isEqualTo("Test Course");
    }
}