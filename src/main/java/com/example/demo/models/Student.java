package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Student extends User {
    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> enrolledCourses;

    private int level;

    // default constructor
    public Student() {
    }

    public String getLessonContent(Long courseId, Long lessonId) {
        for (Course course : enrolledCourses) {
            if (course.getId().equals(courseId)) {
                for (Lesson lesson : course.getLessons()) {
                    if (lesson.getId().equals(lessonId)) {
                        return lesson.getContent();
                    }
                }
            }
        }
        throw new IllegalArgumentException("Lesson not found in enrolled courses");
    }

    public void submitAssignment(Long assignmentId, String submissionContent) {
        Assignment assignment = null;
        for (Course course : enrolledCourses) {
            for (Assignment a : course.getAssignments()) {
                if (a.getId().equals(assignmentId)) {
                    assignment = a;
                    break;
                }
            }
        }
        if (assignment == null) {
            throw new IllegalArgumentException("Assignment not found in enrolled courses");
        }

        LocalDateTime currentDate = LocalDateTime.now();
        if (currentDate.isAfter(assignment.getDueDate())) {
            throw new IllegalArgumentException("Cannot submit assignment after the due date");
        }

        assignment.submitAssignment(this.getId(), submissionContent);  // Use the student's ID to store the submission
    }

    public List<Assignment> viewAssignments(Long courseId) {
        for (Course course : enrolledCourses) {
            if (course.getId().equals(courseId)) {
                return course.getAssignments();
            }
        }
        throw new IllegalArgumentException("Student is not enrolled in the course with ID: " + courseId);
    }

    public List<Quiz> viewQuizzes(Long courseId) {
        for (Course course : enrolledCourses) {
            if (course.getId().equals(courseId)) {
                return course.getQuizzes(); // Assuming Course has a `getQuizzes` method
            }
        }
        throw new IllegalArgumentException("Student is not enrolled in the course with ID: " + courseId);
    }

//    public void takeQuiz(Long quizId, List<Answer> answers) {
//        for (Course course : enrolledCourses) {
//            for (Quiz quiz : course.getQuizzes()) {
//                if (quiz.getId().equals(quizId)) {
//                    if (quiz.isSubmittedBy(this.getId())) {
//                        throw new IllegalArgumentException("Quiz has already been submitted.");
//                    }
//                    if (quiz.getDeadline().isBefore(LocalDateTime.now())) {
//                        throw new IllegalArgumentException("Quiz deadline has passed.");
//                    }
//                    quiz.submitAnswers(this.getId(), answers); // Assuming the Quiz class has a `submitAnswers` method
//                    return;
//                }
//            }
//        }
//        throw new IllegalArgumentException("Quiz not found in enrolled courses.");
//    }


    // unenroll from a course
    public void unenrollCourse(Course course) {
        this.enrolledCourses.remove(course);
    }
}
