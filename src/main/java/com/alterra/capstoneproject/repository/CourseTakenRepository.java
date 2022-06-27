package com.alterra.capstoneproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alterra.capstoneproject.domain.dao.CourseTaken;

@Repository
public interface CourseTakenRepository extends JpaRepository<CourseTaken, Long> {
    @Modifying
    @Query(value = "UPDATE course_takens SET deleted = true WHERE course_id = ?", nativeQuery = true)
    void deleteCourseTakenByCourse(Long id);

    @Query(value = "SELECT ct.* FROM course_takens ct INNER JOIN users u ON ct.user_id=u.id WHERE ct.deleted = false AND ct.user_id = ?", nativeQuery = true)
    List<CourseTaken> findCourseTakenByUser(Long id);

    @Query(value = "SELECT ct.* FROM course_takens ct INNER JOIN courses c ON ct.course_id=c.id WHERE ct.deleted = false AND ct.course_id = ?", nativeQuery = true)
    List<CourseTaken> findCourseTakenByCourse(Long id);
}
