package com.example.demo;
import com.example.demo.user.Student;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	//returns json object
	@GetMapping
	public List<Student> hello() {
		return List.of(
				new Student(1L,
						"Omar",
						"omar@gmail.com"
						,"123")
		);
	}

}
