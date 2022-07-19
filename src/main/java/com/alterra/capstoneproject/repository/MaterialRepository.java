package com.alterra.capstoneproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alterra.capstoneproject.domain.dao.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    @Query(value = "SELECT * FROM materials m WHERE m.deleted = false AND m.section_id = ?", nativeQuery = true)
    List<Material> searchAll(Long sectionId);
    
    @Query(value = "SELECT * FROM materials m WHERE m.deleted = false AND m.id = ? AND m.section_id = ?", nativeQuery = true)
    Optional<Material> searchById(Long id, Long sectionId);

    @Query(value = "SELECT * FROM materials m INNER JOIN sections s ON s.id = m.section_id WHERE m.deleted = false AND s.course_id = ? AND s.deleted = false", nativeQuery = true)
    List<Material> countMaterial(Long courseId);
}
