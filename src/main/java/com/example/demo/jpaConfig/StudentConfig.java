package com.example.demo.jpaConfig;


import com.example.demo.models.Role;
import com.example.demo.models.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class StudentConfig {
    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository) {
        return args -> {
            Student omar = new Student(
                    "Omar",
                    "omar@gmail.com"
                    ,"123", Role.STUDENT);

            Student khaled = new Student(
                    "khaled",
                    "khaled@gmail.com"
                    ,"1234",Role.STUDENT);

            repository.saveAll(
                    List.of(omar, khaled)
            );
        };
    }
}