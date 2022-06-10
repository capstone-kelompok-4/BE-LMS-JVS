package com.alterra.capstoneproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alterra.capstoneproject.domain.dao.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    @Query(value = "SELECT * FROM section s WHERE s.deleted = false AND s.id = ?", nativeQuery = true)
    Optional<Section> searchById(Long id);
}
