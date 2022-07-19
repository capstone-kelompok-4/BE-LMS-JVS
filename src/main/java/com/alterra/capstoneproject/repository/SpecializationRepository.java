package com.alterra.capstoneproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alterra.capstoneproject.domain.dao.Specialization;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
    @Query(value = "SELECT * FROM specializations s WHERE s.deleted = false AND s.id = ?", nativeQuery = true)
    Optional<Specialization> searchById(Long id);
}
