package com.example.demo.serviceTest;

import com.example.demo.models.*;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.InstructorRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private InstructorRepository instructorRepository;
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Admin testAdmin;
    private Instructor testInstructor;
    private Student testStudent;
    private String testToken;

    @BeforeEach
    void setUp() {
        testToken = "test.jwt.token";

        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setRole(Role.STUDENT);

        testAdmin = new Admin();
        testAdmin.setId(1L);
        testAdmin.setName("Test Admin");
        testAdmin.setEmail("admin@example.com");
        testAdmin.setPassword("password");
        testAdmin.setRole(Role.ADMIN);

        testInstructor = new Instructor();
        testInstructor.setId(1L);
        testInstructor.setName("Test Instructor");
        testInstructor.setEmail("instructor@example.com");
        testInstructor.setPassword("password");
        testInstructor.setRole(Role.INSTRUCTOR);

        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setName("Test Student");
        testStudent.setEmail("student@example.com");
        testStudent.setPassword("password");
        testStudent.setRole(Role.STUDENT);
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(testUser.getEmail(), result.getEmail());
        verify(userRepository).findById(1L);
    }

    @Test
    void getAllUsers_Success() {
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void createUser_Success() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.createUser(testUser);

        assertNotNull(result);
        assertEquals(testUser.getEmail(), result.getEmail());
        verify(userRepository).save(testUser);
    }

    @Test
    void updateUser_Success() {
        User updatedUser = new User();
        updatedUser.setName("Updated Name");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPassword("newpassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(1L, updatedUser);

        assertNotNull(result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_AsStudent_Success() {
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);
        when(jwtService.generateToken(any(User.class))).thenReturn(testToken);

        testUser.setRole(Role.STUDENT);
        Map<String, Object> result = userService.registerUser(testUser);

        assertNotNull(result);
        assertEquals(testToken, result.get("token"));
        assertNotNull(result.get("user"));
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void registerUser_AsInstructor_Success() {
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(instructorRepository.save(any(Instructor.class))).thenReturn(testInstructor);
        when(jwtService.generateToken(any(User.class))).thenReturn(testToken);

        testUser.setRole(Role.INSTRUCTOR);
        Map<String, Object> result = userService.registerUser(testUser);

        assertNotNull(result);
        assertEquals(testToken, result.get("token"));
        assertNotNull(result.get("user"));
        verify(instructorRepository).save(any(Instructor.class));
    }

    @Test
    void registerUser_AsAdmin_Success() {
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(adminRepository.save(any(Admin.class))).thenReturn(testAdmin);
        when(jwtService.generateToken(any(User.class))).thenReturn(testToken);

        testUser.setRole(Role.ADMIN);
        Map<String, Object> result = userService.registerUser(testUser);

        assertNotNull(result);
        assertEquals(testToken, result.get("token"));
        assertNotNull(result.get("user"));
        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void loginUser_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.generateToken(any(User.class))).thenReturn(testToken);

        Map<String, Object> result = userService.loginUser(testUser);

        assertNotNull(result);
        assertEquals(testToken, result.get("token"));
        assertNotNull(result.get("user"));
        verify(userRepository).findByEmail(testUser.getEmail());
    }

    @Test
    void loginUser_InvalidCredentials() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(false);

        Map<String, Object> result = userService.loginUser(testUser);

        assertNull(result);
        verify(userRepository).findByEmail(testUser.getEmail());
    }

    @Test
    void loginUser_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Map<String, Object> result = userService.loginUser(testUser);

        assertNull(result);
        verify(userRepository).findByEmail(testUser.getEmail());
    }

    @Test
    void existsByEmail_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        boolean result = userService.existsByEmail("test@example.com");

        assertTrue(result);
        verify(userRepository).existsByEmail("test@example.com");
    }

    @Test
    void deleteUser_Success() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }
}
