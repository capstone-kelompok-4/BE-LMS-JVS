package com.alterra.capstoneproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alterra.capstoneproject.domain.dao.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    @Query(value = "SELECT * FROM sections s WHERE s.deleted = false AND s.course_id = ?", nativeQuery = true)
    List<Section> searchAll(Long courseId);
    
    @Query(value = "SELECT * FROM sections s WHERE s.deleted = false AND s.id = ? AND s.course_id = ?", nativeQuery = true)
    Optional<Section> searchById(Long id, Long courseId);
}
