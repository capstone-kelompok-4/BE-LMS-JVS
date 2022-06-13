package com.alterra.capstoneproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alterra.capstoneproject.domain.dao.CourseTaken;

@Repository
public interface CourseTakenRepository extends JpaRepository<CourseTaken, Long> {
    @Modifying
    @Query(value = "UPDATE course_taken SET deleted = true WHERE course_id = ?", nativeQuery = true)
    void deleteCourseTakenByCourse(Long id);
}
