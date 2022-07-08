package com.alterra.capstoneproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alterra.capstoneproject.domain.dao.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(value = "SELECT * from courses c WHERE c.specialization_id = ? AND c.deleted = false", nativeQuery = true)
    List<Course> searchByUserSpec(Long id);

    @Query("SELECT c FROM Course c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name,'%'))")
    List<Course> findByName(@Param("name") String name);

    @Query(value = "SELECT c from Course c WHERE c.id = ?1")
    Course searchCourse(Long id);
}
