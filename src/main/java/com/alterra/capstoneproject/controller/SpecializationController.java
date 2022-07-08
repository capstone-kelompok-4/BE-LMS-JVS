package com.alterra.capstoneproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alterra.capstoneproject.domain.dao.Specialization;
import com.alterra.capstoneproject.domain.dto.SpecializationDto;
import com.alterra.capstoneproject.service.SpecializationService;
import com.alterra.capstoneproject.util.ResponseUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/specializations")
@CrossOrigin(origins = "https://capstone-project-4.herokuapp.com")
public class SpecializationController {
    @Autowired
    private SpecializationService specializationService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getSpecializations() {
        try {
            List<Specialization> specializations = specializationService.getSpecializations();
            return ResponseUtil.build("GET SPECIALIZATIONS", specializations, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getSpecialization(@PathVariable Long id) {
        try {
            Specialization specialization = specializationService.getSpecialization(id); 
            return ResponseUtil.build("GET SPECIALIZATION ID " + id, specialization, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> postSpecialization(@RequestBody SpecializationDto request) {
        try {
            Specialization specialization = specializationService.postSpecialization(request); 
            return ResponseUtil.build("SPECIALIZATION CREATED", specialization, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSpecialization(@PathVariable Long id, @RequestBody SpecializationDto request) {
        try {
            Specialization specialization = specializationService.updateSpecialization(id, request); 
            return ResponseUtil.build("SPECIALIZATION ID " + id + " UPDATED", specialization, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteSpecialization(@PathVariable Long id) {
        try {
            specializationService.deleteSpecialization(id);
            return ResponseUtil.build("SPECIALIZATION ID " + id + " DELETED", null, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
