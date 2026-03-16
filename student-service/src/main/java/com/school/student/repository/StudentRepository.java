package com.school.student.repository;

import com.school.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Student entity.
 * Provides data access methods for student records.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Finds a student by their unique student ID.
     *
     * @param studentId Unique student identifier
     * @return Optional containing student if found
     */
    Optional<Student> findByStudentId(String studentId);
}
