package com.example.demo.user.student;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentConfig {
    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository) {
        return args -> {
            Student omar = new Student(1L,
                    "Omar",
                    "omar@gmail.com"
                    ,"123");

            Student khaled = new Student(1L,
                    "khaled",
                    "khaled@gmail.com"
                    ,"1234");

            repository.s
        };
    }
}
