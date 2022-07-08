package com.alterra.capstoneproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alterra.capstoneproject.domain.dao.MethodologyEnum;
import com.alterra.capstoneproject.domain.dao.MethodologyLearning;

@Repository
public interface MethodologyRepository extends JpaRepository<MethodologyLearning, Long> {
    Optional<MethodologyLearning> findByName (MethodologyEnum name);
}
