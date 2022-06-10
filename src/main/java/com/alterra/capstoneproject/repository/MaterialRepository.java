package com.alterra.capstoneproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alterra.capstoneproject.domain.dao.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    
}
