package com.example.demo.user.student;


import org.springframework.beans.factory.annotation.Autowired;
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
                    ,"123");

            Student khaled = new Student(
                    "khaled",
                    "khaled@gmail.com"
                    ,"1234");

            repository.saveAll(
                    List.of(omar, khaled)
            );
        };
    }
}
