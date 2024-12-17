package com.example.demo.user.instructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class  InstructorConfig {
    @Bean
    CommandLineRunner commandLineRunner(InstructorRepository repository) {
        return args -> {
            Instructor boody = new Instructor(1L,
                    "Boody",
                    "Boody@gmail.com"
                    ,"123");

            Instructor soly = new Instructor(1L,
                    "Soly",
                    "Soly@gmail.com"
                    ,"0000");
            repository.saveAll(
                    List.of(boody, soly)
            );

        };
    }
}
