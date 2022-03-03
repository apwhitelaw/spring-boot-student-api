package com.whitelaw.studentapi.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="/api/v1.0/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable long id) {
        return studentService.getStudent(id);
    }

    @PostMapping("/")
    public ResponseEntity<Student> registerStudent(@RequestBody Student newStudent) {
            try{
                Student student = studentService.addStudent(newStudent);
                return new ResponseEntity<>(student, HttpStatus.CREATED);
            } catch(IllegalStateException e) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable long id, @RequestParam Map<String, String> requestParams) {
        try {
            Student student = studentService.updateStudent(id, requestParams);
            return new ResponseEntity<>(student, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable long id) {
        try{
            Student student = studentService.deleteStudent(id);
            return new ResponseEntity<>(student, HttpStatus.OK);
        } catch(IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

}
