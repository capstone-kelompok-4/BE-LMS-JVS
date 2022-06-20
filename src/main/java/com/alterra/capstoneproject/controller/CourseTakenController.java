package com.alterra.capstoneproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alterra.capstoneproject.domain.dao.CourseTaken;
import com.alterra.capstoneproject.domain.dto.CourseTakenDto;
import com.alterra.capstoneproject.service.CourseTakenService;
import com.alterra.capstoneproject.util.ResponseUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/course-takens")
public class CourseTakenController {
    @Autowired
    private CourseTakenService courseTakenService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCourseTakens() {
        try {
            List<CourseTaken> courseTakens = courseTakenService.getCourseTakens();
            return ResponseUtil.build("GET COURSE TAKENS ", courseTakens, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCourseTaken(@PathVariable Long id) {
        try {
            CourseTaken courseTaken = courseTakenService.getCourseTaken(id); 
            return ResponseUtil.build("GET COURSE TAKEN ID " + id, courseTaken, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> postCourseTaken(@RequestBody CourseTakenDto request) {
        try {
            CourseTaken courseTaken = courseTakenService.postCourseTaken(request); 
            return ResponseUtil.build("COURSE TAKEN CREATED", courseTaken, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCourseTaken(@PathVariable Long id, @RequestBody CourseTakenDto request) {
        try {
            CourseTaken courseTaken = courseTakenService.updateCourseTaken(id, request); 
            return ResponseUtil.build("COURSE TAKEN ID " + id + " UPDATED", courseTaken, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCourseTaken(@PathVariable Long id) {
        try {
            courseTakenService.deleteCourseTaken(id);
            return ResponseUtil.build("COURSE TAKEN ID " + id + " DELETED", null, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
