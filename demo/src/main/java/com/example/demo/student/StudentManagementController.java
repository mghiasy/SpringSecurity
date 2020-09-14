package com.example.demo.student;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
//@Controller is a annotation that mark a class as SpringMVC Controller
//@Rest => is a special controller that used in rest WS
@RestController
@RequestMapping("management/api/v1/students") //= @Controller + @ResponseBody
public class StudentManagementController {
    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1,"James Bound"),
            new Student(2,"Maryam Ghiasvand"),
            new Student(3,"Jimmy Palma")
    );
@GetMapping //it is a get request
    public List<Student> getStudents(){
        return STUDENTS;
    }
@PostMapping //fort add new resource to our system
//@RequsetBody : deserialization of HttpRequest onto a Java object
    public void registerStudent(@RequestBody Student student){ //to get student from requestBody
        System.out.println("reg new student: "+ student);
    }
    @DeleteMapping(path="{studentId}")
    public void deleteStudent(@PathVariable("studentId") Integer studentId){ //studentId come from the path
        System.out.println("delete student: "+ studentId);
    }
    @PutMapping(path="{studentId}")
    public void updateStudent(@PathVariable("studentId") Integer studentId,@RequestBody Student student){
        System.out.println("update student "+studentId);
        System.out.println(String.format("%s %s ", studentId,student));
    }


}
