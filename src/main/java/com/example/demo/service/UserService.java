package com.example.demo.service;

import com.example.demo.models.Instructor;
import com.example.demo.models.Role;
import com.example.demo.models.Student;
import com.example.demo.models.User;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.InstructorRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private StudentRepository studentRepository;

    public User getUserById(String id) {
        return userRepository.findById(Long.valueOf(id)).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.updateProfile(updatedUser.getName(), updatedUser.getEmail(), updatedUser.getPassword());
            return userRepository.save(existingUser);
        }
        return null;
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(Long.valueOf(id));
    }

    public Map<String, Object> registerUser(User user) {
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        newUser.setRole(user.getRole());

        User savedUser = userRepository.save(newUser);
        if (newUser.getRole().equals(Role.INSTRUCTOR)) {
            Instructor instructor = new Instructor();
            instructor.setId(savedUser.getId());
            instructor.setName(savedUser.getName());
            instructor.setEmail(savedUser.getEmail());
            instructor.setPassword(savedUser.getPassword());
            instructor.setRole(savedUser.getRole());
            instructorRepository.save(instructor);
        }else if (newUser.getRole().equals(Role.STUDENT)) {
            Student student = new Student();
            student.setId(savedUser.getId());
            student.setName(savedUser.getName());
            student.setEmail(savedUser.getEmail());
            student.setRole(savedUser.getRole());
            studentRepository.save(student);
        }
        String token = jwtService.generateToken(savedUser);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", savedUser);

        return response;
    }

    public Map<String, Object> loginUser(User user) {
        User foundUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (foundUser == null || !bCryptPasswordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            return null;
        }
        String token = jwtService.generateToken(foundUser);
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", foundUser);
        return response;
    }
}
