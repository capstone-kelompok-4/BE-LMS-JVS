package com.alterra.capstoneproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alterra.capstoneproject.domain.dao.CourseTaken;

@Repository
public interface CourseTakenRepository extends JpaRepository<CourseTaken, Long> {
    @Query(value = "SELECT ct.* FROM course_takens ct WHERE ct.deleted = false AND ct.user_id = ?", nativeQuery = true)
    List<CourseTaken> findCourseTakenByUser(Long id);

    @Query(value = "SELECT ct.* FROM course_takens ct WHERE ct.deleted = false AND ct.course_id = ?", nativeQuery = true)
    List<CourseTaken> findCourseTakenByCourse(Long id);

    @Query(value = "SELECT COUNT(ct.rate) FROM course_takens ct WHERE ct.course_id = ? AND ct.deleted = false", nativeQuery = true)
    Integer countRateByCourse(Long id);

    @Query(value = "SELECT SUM(ct.rate) FROM course_takens ct WHERE ct.course_id = ? AND ct.deleted = false", nativeQuery = true)
    Double sumRateByCourse(Long id);
}
