package com.alterra.capstoneproject.controller;

import java.security.Principal;
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

import com.alterra.capstoneproject.domain.dao.CourseTaken;
import com.alterra.capstoneproject.domain.dto.CourseTakenDto;
import com.alterra.capstoneproject.domain.dto.RateCourse;
import com.alterra.capstoneproject.domain.dto.StatusCourse;
import com.alterra.capstoneproject.service.CourseTakenService;
import com.alterra.capstoneproject.util.ResponseUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/course-takens")
@CrossOrigin(origins = "https://capstone-project-4.herokuapp.com")
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

    @GetMapping(value = "/history")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCourseTakenByUser(Principal principal) {
        try {
            List<CourseTaken> courseTakens = courseTakenService.getCourseTakenByUser(principal.getName());
            return ResponseUtil.build("GET COURSE TAKENS BY USER", courseTakens, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/courses/{id}")
    public ResponseEntity<?> getCourseTakenByCourse(@PathVariable Long id) {
        try {
            List<CourseTaken> courseTakens = courseTakenService.getCourseTakenByCourse(id);
            return ResponseUtil.build("GET COURSE TAKENS BY COURSE", courseTakens, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> postCourseTaken(Principal principal, @RequestBody CourseTakenDto request) {
        try {
            request.setEmail(principal.getName());
            CourseTaken courseTaken = courseTakenService.postCourseTaken(request); 
            return ResponseUtil.build("COURSE TAKEN CREATED", courseTaken, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }    

    @PutMapping(value = "/update-status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStatus(Principal principal, @PathVariable Long id, @RequestBody StatusCourse request) {
        try {
            CourseTaken courseTakens = courseTakenService.updateStatus(id, request.getStatus());
            return ResponseUtil.build("UPDATE COURSE TAKEN STATUS", courseTakens, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/update-scores/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateScore(Principal principal, @PathVariable Long id,  @RequestBody CourseTakenDto request) {
        try {
            request.setEmail(principal.getName());
            CourseTaken courseTakens = courseTakenService.updateProgress(id, request);
            return ResponseUtil.build("SCORE COURSE TAKEN " + id + " WITH MATERIAL ID " + request.getMaterialId() + " UPDATED", courseTakens, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping(value = "/rates/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateRating(Principal principal, @PathVariable Long id, @RequestBody RateCourse request) {
        try {
            request.setCourseTakenId(id);
            CourseTaken courseTakens = courseTakenService.updateRating(principal.getName(), request);
            return ResponseUtil.build("UPDATE COURSE TAKEN RATE", courseTakens, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/progress/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateProgress(Principal principal, @PathVariable Long id, @RequestBody CourseTakenDto request) {
        try {
            request.setEmail(principal.getName());
            CourseTaken courseTaken = courseTakenService.updateProgress(id, request);
            return ResponseUtil.build("PROGRESS COURSE TAKEN ID " + id + " UPDATED", courseTaken, HttpStatus.OK);
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
