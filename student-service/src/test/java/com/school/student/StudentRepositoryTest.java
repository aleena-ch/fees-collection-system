package com.school.student;

import com.school.student.model.Student;
import com.school.student.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository repository;

    private Student student;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        student = StudentTestDataBuilder.buildStudent("STU-2026-001");
    }

    @Test
    void save_ShouldPersistStudent() {

        Student saved = repository.save(student);

        assertNotNull(saved.getId());
        assertEquals("STU-2026-001",
                saved.getStudentId());
        assertEquals(student.getStudentName(), saved.getStudentName());
        assertEquals(student.getGrade(), saved.getGrade());
        assertEquals(student.getMobileNumber(), saved.getMobileNumber());
        assertEquals(student.getSchoolName(), saved.getSchoolName());
        assertNotNull(saved.getCreatedAt());
    }

    @Test
    void findByStudentId_ShouldReturnStudent_WhenExists() {

        repository.save(student);

        Optional<Student> found =
                repository.findByStudentId(
                        "STU-2026-001");

        assertTrue(found.isPresent());
        assertEquals("STU-2026-001",
                found.get().getStudentId());
        assertEquals(student.getStudentName(),
                found.get().getStudentName());
    }

    @Test
    void findByStudentId_ShouldReturnEmpty_WhenNotExists() {

        Optional<Student> found =
                repository.findByStudentId(
                        "STU-9999-999");

        assertFalse(found.isPresent());
        assertTrue(found.isEmpty());
    }


    @Test
    void findAll_ShouldReturnAllStudents() {

        repository.save(student);
        repository.save(StudentTestDataBuilder
                .buildStudent("STU-2026-002"));
        repository.save(StudentTestDataBuilder
                .buildStudent("STU-2026-003"));

        List<Student> students = repository.findAll();

        assertEquals(3, students.size());
    }

    @Test
    void count_ShouldReturnCorrectCount() {

        repository.save(student);
        long count = repository.count();
        assertEquals(1, count);
    }

    @Test
    void delete_ShouldRemoveStudent() {

        Student saved = repository.save(student);
        repository.deleteById(saved.getId());

        Optional<Student> found =
                repository.findByStudentId(
                        "STU-2026-001");
        assertFalse(found.isPresent());
    }
}
