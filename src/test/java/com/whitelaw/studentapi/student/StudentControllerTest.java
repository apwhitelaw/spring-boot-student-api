package com.whitelaw.studentapi.student;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value=StudentController.class)
@WithMockUser
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // added to make @BeforeAll method non-static
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    private ObjectMapper mapper;
    private String endpoint = "/api/v1.0/student";

    Student mockStudent = new Student(321L, "John", "john@email.com", LocalDate.now());

    @BeforeAll
    void setup() {
        // allow object mapper to find jackson-datatype-jsr310
        // required for LocalDate type
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
    }

    @Test
    void getAllStudents() throws Exception {
        Student mockStudent1 = new Student("Austin", "austin@email.com", LocalDate.now());
        Student mockStudent2 = new Student("Jack", "jack@email.com", LocalDate.now());
        Student mockStudent3 = new Student("Tyler", "tyler@email.com", LocalDate.now());
        List<Student> mockStudentList = Arrays.asList(mockStudent1, mockStudent2, mockStudent3);

        when(studentService.getAllStudents()).thenReturn(mockStudentList);
        String expected = "[{name:Austin,email:austin@email.com}," +
                            "{name:Jack,email:jack@email.com}," +
                            "{name:Tyler,email:tyler@email.com}]";

        mockMvc.perform(get(endpoint + "/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected, false));
    }

    @Test
    void getStudent() throws Exception {
        when(studentService.getStudent(anyLong())).thenReturn(mockStudent);
        String expected = "{id:321,name:John,email:john@email.com}";

        mockMvc.perform(get(endpoint + "/12345")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected, false));
    }

    @Test
    void registerStudent() throws Exception {
        when(studentService.addStudent(any(Student.class))).thenReturn(mockStudent);
        String expected = "{id:321,name:John,email:john@email.com}";

        mockMvc.perform(post(endpoint + "/")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(mockStudent))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(expected, false));
    }

    @Test
    void updateStudent() throws Exception {
        mockStudent.setName("Jake");
        mockStudent.setEmail("jake@email.com");
        when(studentService.updateStudent(anyLong(), anyMap())).thenReturn(mockStudent);
        String expected = "{id:321,name:Jake,email:jake@email.com}";

        mockMvc.perform(put(endpoint + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .param("name", "Jake")
                .param("email", "jake@email.com"))
                .andExpect(status().isOk())
                .andExpect(content().json(expected, false));
    }

    @Test
    void deleteStudent() throws Exception {
        when(studentService.deleteStudent(anyLong())).thenReturn(mockStudent);
        String expected = "{id:321,name:John,email:john@email.com}";

        mockMvc.perform(delete(endpoint + "/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected, false));
    }

}