package com.whitelaw.studentapi.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudent(long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException(
                        "Student #" + studentId + " does not exist!"
                ));
        return student;
    }

    public Student addStudent(Student student) {
        Optional<Student> optionalStudent = studentRepository.findStudentByEmail(student.getEmail());
        if(optionalStudent.isPresent()) {
            throw new IllegalStateException("Email already in use!");
        }
        studentRepository.save(student);
        return student;
    }

    @Transactional
    public Student updateStudent(long studentId, Map<String, String> updateParams) {
        String name = updateParams.get("name");
        String email = updateParams.get("email");
        String dobString = updateParams.get("dob");
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException(
                        "Student #" + studentId + " does not exist!"
                ));
        if(name != null &&
                name.length() > 0 &&
                    !Objects.equals(student.getName(), name)) {
            student.setName(name);
        }

        if(email != null &&
                email.length() > 0 &&
                    !Objects.equals(student.getEmail(), email)) {
            Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
            if(studentOptional.isPresent()) {
                throw new IllegalStateException("Email already in use!");
            }
            student.setEmail(email);
        }

        if(dobString != null) {
            LocalDate dob = LocalDate.parse(dobString);
            if(dob.isBefore(LocalDate.now()) &&
                    !Objects.equals(student.getDob(), dob)){
                student.setDob(dob);
            }
        }

        return student;
    }

    public Student deleteStudent(long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if(!exists) {
            throw new IllegalStateException("Student #" + studentId + " does not exist!");
        }
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        Student student = optionalStudent.get();
        studentRepository.deleteById(studentId);
        return student;
    }

}
