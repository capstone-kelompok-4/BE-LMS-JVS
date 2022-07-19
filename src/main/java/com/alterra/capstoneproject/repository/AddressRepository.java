package com.alterra.capstoneproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alterra.capstoneproject.domain.dao.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
}
