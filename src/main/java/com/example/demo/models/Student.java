package com.example.demo.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    // parameterized constructor
    public Student(String name, String email, String password, Role role, List<Course> enrolledCourses , int level) {
        super(name, email, password, role);
        this.enrolledCourses = enrolledCourses;
        this.level = level;
    }

    public Student(String name, String email, String password, Role role, List<Course> enrolledCourses) {
        super(name, email, password, role);
        this.enrolledCourses = enrolledCourses;
        this.level = 1;
    }

    public Student(String name, String email, String password, Role role) {
        super(name, email, password, role);

    }

    // Getters
    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public int getLevel() { return level;}

    // Setters
    public void setEnrolledCourses(List<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public void getLevel(int level ) {  this.level = level;}

    // enroll in a course
    public void enrollCourse(Course course) { this.enrolledCourses.add(course);}

    // view the available courses
    public List<Course> viewAvailableCourses(List<Course> allCourses) {
        return allCourses.stream()
                .filter(course -> !enrolledCourses.contains(course) && course.getMinLevel() <= this.level)
                .collect(Collectors.toList());
    }

    public List<Lesson> viewCourseLessons(Course course) {
        if (!this.enrolledCourses.contains(course)) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }

        return course.getLessons();
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

        Date currentDate = new Date();
        if (currentDate.after(assignment.getDueDate())) {
            throw new IllegalArgumentException("Cannot submit assignment after the due date");
        }

        assignment.submitAssignment(this.getId(), submissionContent);  // Use the student's ID to store the submission
    }

    // unenroll from a course
    public void unenrollCourse(Course course) {
        this.enrolledCourses.remove(course);
    }
}
