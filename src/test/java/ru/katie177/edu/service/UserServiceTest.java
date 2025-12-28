package ru.katie177.edu.service;

import ru.katie177.edu.dto.UserDTO;
import ru.katie177.edu.exception.DuplicateResourceException;
import ru.katie177.edu.exception.ResourceNotFoundException;
import ru.katie177.edu.model.User;
import ru.katie177.edu.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    private User user;
    private UserDTO userDTO;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setRole(User.Role.STUDENT);
        user.setCreatedAt(LocalDateTime.now().minusDays(5));
        
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("John Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setRole("STUDENT");
        userDTO.setCreatedAt(user.getCreatedAt());
    }
    
    @Test
    public void testGetAllUsers_ReturnsAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        
        List<UserDTO> result = userService.getAllUsers();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("john.doe@example.com", result.get(0).getEmail());
        assertEquals("STUDENT", result.get(0).getRole());
        verify(userRepository, times(1)).findAll();
    }
    
    @Test
    public void testGetUserById_ReturnsUserWhenFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        
        UserDTO result = userService.getUserById(1L);
        
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("STUDENT", result.getRole());
        verify(userRepository, times(1)).findById(1L);
    }
    
    @Test
    public void testGetUserById_ThrowsExceptionWhenNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(999L);
        });
        
        verify(userRepository, times(1)).findById(999L);
    }
    
    @Test
    public void testGetUserByEmail_ReturnsUserWhenFound() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));
        
        UserDTO result = userService.getUserByEmail("john.doe@example.com");
        
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("STUDENT", result.getRole());
        verify(userRepository, times(1)).findByEmail("john.doe@example.com");
    }
    
    @Test
    public void testGetUserByEmail_ThrowsExceptionWhenNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserByEmail("nonexistent@example.com");
        });
        
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }
    
    @Test
    public void testCreateUser_Success() {
        when(userRepository.existsByEmail("jane.doe@example.com")).thenReturn(false);
        // Создаем нового пользователя для возврата из мока
        User newUser = new User();
        newUser.setId(2L);
        newUser.setName("Jane Doe");
        newUser.setEmail("jane.doe@example.com");
        newUser.setRole(User.Role.STUDENT);
        newUser.setCreatedAt(LocalDateTime.now());
        
        when(userRepository.existsByEmail("jane.doe@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        
        UserDTO newUserDTO = new UserDTO();
        newUserDTO.setName("Jane Doe");
        newUserDTO.setEmail("jane.doe@example.com");
        newUserDTO.setRole("STUDENT");
        
        UserDTO result = userService.createUser(newUserDTO);
        
        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        assertEquals("jane.doe@example.com", result.getEmail());
        assertEquals("STUDENT", result.getRole());
        verify(userRepository, times(1)).existsByEmail("jane.doe@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    public void testCreateUser_ThrowsExceptionWhenEmailExists() {
        when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(true);
        
        UserDTO existingUserDTO = new UserDTO();
        existingUserDTO.setName("John Doe");
        existingUserDTO.setEmail("john.doe@example.com");
        existingUserDTO.setRole("STUDENT");
        
        assertThrows(DuplicateResourceException.class, () -> {
            userService.createUser(existingUserDTO);
        });
        
        verify(userRepository, times(1)).existsByEmail("john.doe@example.com");
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    public void testUpdateUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        // Создаем обновленного пользователя для возврата из мока
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("John Smith");
        updatedUser.setEmail("john.doe@example.com");
        updatedUser.setRole(User.Role.TEACHER);
        updatedUser.setCreatedAt(user.getCreatedAt());
        
        // Проверяем, что если email не изменился, то existsByEmail не должен вызываться
        when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(true); // same email
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        
        UserDTO updateUserDTO = new UserDTO();
        updateUserDTO.setName("John Smith");
        updateUserDTO.setEmail("john.doe@example.com"); // same email
        updateUserDTO.setRole("TEACHER");
        
        UserDTO result = userService.updateUser(1L, updateUserDTO);
        
        assertNotNull(result);
        assertEquals("John Smith", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("TEACHER", result.getRole());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    public void testUpdateUser_ThrowsExceptionWhenUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        
        UserDTO updateUserDTO = new UserDTO();
        updateUserDTO.setName("John Smith");
        updateUserDTO.setEmail("john.doe@example.com");
        updateUserDTO.setRole("TEACHER");
        
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUser(999L, updateUserDTO);
        });
        
        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    public void testUpdateUser_ThrowsExceptionWhenEmailExistsForAnotherUser() {
        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setEmail("jane.doe@example.com");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("jane.doe@example.com")).thenReturn(true);
        
        UserDTO updateUserDTO = new UserDTO();
        updateUserDTO.setName("John Smith");
        updateUserDTO.setEmail("jane.doe@example.com"); // different user has this email
        updateUserDTO.setRole("TEACHER");
        
        assertThrows(DuplicateResourceException.class, () -> {
            userService.updateUser(1L, updateUserDTO);
        });
        
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).existsByEmail("jane.doe@example.com");
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    public void testDeleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);
        
        userService.deleteUser(1L);
        
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
    
    @Test
    public void testDeleteUser_ThrowsExceptionWhenUserNotFound() {
        when(userRepository.existsById(999L)).thenReturn(false);
        
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(999L);
        });
        
        verify(userRepository, times(1)).existsById(999L);
        verify(userRepository, never()).deleteById(anyLong());
    }
    
    @Test
    public void testGetAllTeachers_ReturnsOnlyTeachers() {
        User teacher = new User();
        teacher.setId(2L);
        teacher.setName("Dr. Smith");
        teacher.setEmail("dr.smith@university.edu");
        teacher.setRole(User.Role.TEACHER);
        
        when(userRepository.findAllTeachers()).thenReturn(Arrays.asList(teacher));
        
        List<UserDTO> result = userService.getAllTeachers();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Dr. Smith", result.get(0).getName());
        assertEquals("TEACHER", result.get(0).getRole());
        verify(userRepository, times(1)).findAllTeachers();
    }
    
    @Test
    public void testGetAllStudents_ReturnsOnlyStudents() {
        User student = new User();
        student.setId(3L);
        student.setName("Jane Doe");
        student.setEmail("jane.student@university.edu");
        student.setRole(User.Role.STUDENT);
        
        when(userRepository.findAllStudents()).thenReturn(Arrays.asList(student));
        
        List<UserDTO> result = userService.getAllStudents();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Jane Doe", result.get(0).getName());
        assertEquals("STUDENT", result.get(0).getRole());
        verify(userRepository, times(1)).findAllStudents();
    }
}