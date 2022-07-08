package com.alterra.capstoneproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alterra.capstoneproject.domain.dao.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>  {
    @Query(value = "SELECT * FROM reports r WHERE r.deleted = false AND r.course_taken_id = ? AND r.material_id = ?", nativeQuery = true)
    Report getReport(Long courseTakenId, Long materialId);

    @Query(value = "SELECT * FROM reports r WHERE r.deleted = false AND r.course_taken_id = ? AND r.completed = true", nativeQuery = true)
    List<Report> findByCompleted(Long courseTakenId);
}
