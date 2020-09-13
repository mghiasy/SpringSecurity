package com.example.demo.student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1,"James Bound"),
            new Student(2,"Maryam Ghiasvand"),
            new Student(3,"Jimmy Palma")
    );

    @GetMapping("{studentId}")
    public Student getStudent(@PathVariable Integer studentId){
        return STUDENTS.stream().filter(x->x.getStudentId() == studentId)
                .findFirst()
                .orElseThrow(()->new IllegalStateException("Student "+ studentId +" does not exist"));
    }
}
