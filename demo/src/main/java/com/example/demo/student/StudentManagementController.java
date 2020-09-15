package com.example.demo.student;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

//@Controller is a annotation that mark a class as SpringMVC Controller
//@Rest => is a special controller that used in rest WS
@RestController
@RequestMapping("management/api/v1/students") //= @Controller + @ResponseBody
public class StudentManagementController {
    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1, "James Bound"),
            new Student(2, "Maryam Ghiasvand"),
            new Student(3, "Jimmy Palma")
    );

    @GetMapping //it is a get request
    //here is used ROLE for access
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ADMINTRAINEE')") //preAuth with some condition inside: hasRole('ROLE_') hasAnyRole('ROLE_') hasAuthorize('permission') hasAnyAuthorize('permission')
    public List<Student> getStudents() {
        return STUDENTS;
    }

    @PostMapping //fort add new resource to our system
    @PreAuthorize("hasAuthority('student:write')") //working with permission for access
    //@RequsetBody : deserialization of HttpRequest onto a Java objectÒØ
    public void registerStudent(@RequestBody Student student) { //to get student from requestBody
        System.out.println("reg new student: " + student);
    }

    @DeleteMapping(path = "{studentId}")
    @PreAuthorize("hasAuthority('student:write')") // return 403 forbidden error for other users
    public void deleteStudent(@PathVariable("studentId") Integer studentId) { //studentId come from the path
        System.out.println("delete student: " + studentId);
    }

    @PutMapping(path = "{studentId}")
    @PreAuthorize("hasAuthority('student:write')")
    public void updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody Student student) {
        System.out.println("update student " + studentId);
        System.out.println(String.format("%s %s ", studentId, student));
    }


}
