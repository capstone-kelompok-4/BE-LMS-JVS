package com.alterra.capstoneproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alterra.capstoneproject.domain.dao.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(value = "SELECT * FROM courses c WHERE c.deleted = false AND c.id = ?", nativeQuery = true)
    Optional<Course> searchById(Long id);

    @Modifying
    @Query(value = "UPDATE courses SET deleted = true WHERE specialization_id = ?", nativeQuery = true)
    void deleteCourseBySpecialization(Long id);

    @Query(value = "SELECT * from courses c WHERE c.specialization_id = ?", nativeQuery = true)
    List<Course> searchByUserSpec(Long id);

    @Query("select c from Course c where lower(c.name) like lower(concat('%', :name,'%'))")
    public List<Course> findByName(@Param("name") String name);
}
