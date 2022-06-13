package com.alterra.capstoneproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alterra.capstoneproject.domain.dao.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    @Query(value = "SELECT * FROM material m WHERE m.deleted = false AND m.section_id = ?", nativeQuery = true)
    List<Material> searchAll(Long sectionId);
    
    @Query(value = "SELECT * FROM material m WHERE m.deleted = false AND m.id = ? AND m.section_id = ?", nativeQuery = true)
    Optional<Material> searchById(Long id, Long sectionId);
    
    @Modifying
    @Query(value = "UPDATE material SET deleted = true WHERE section_id = ?", nativeQuery = true)
    void deleteMaterialBySection(Long id);
}
