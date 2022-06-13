package com.alterra.capstoneproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alterra.capstoneproject.domain.dao.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(value = "SELECT * FROM course c WHERE c.deleted = false AND c.id = ?", nativeQuery = true)
    Optional<Course> searchById(Long id);

    @Modifying
    @Query(value = "UPDATE course SET deleted = true WHERE specialization_id = ?", nativeQuery = true)
    void deleteCourseBySpecialization(Long id);
}
