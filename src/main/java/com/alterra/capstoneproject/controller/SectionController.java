package com.alterra.capstoneproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alterra.capstoneproject.domain.dao.Section;
import com.alterra.capstoneproject.domain.dto.SectionDto;
import com.alterra.capstoneproject.service.SectionService;
import com.alterra.capstoneproject.util.ResponseUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/courses/{cid}/sections")
public class SectionController {
    @Autowired
    private SectionService sectionService;

    @GetMapping
    public ResponseEntity<?> getSections(@PathVariable(value = "cid") Long courseId) {
        try {
            List<Section> sections = sectionService.getSections(courseId);
            return ResponseUtil.build("GET SECTIONS", sections, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getSection(@PathVariable(value = "cid") Long courseId, @PathVariable(value = "id") Long id) {
        try {
            Section section = sectionService.getSection(courseId, id); 
            return ResponseUtil.build("GET SECTION ID " + id, section, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> postSection(@PathVariable(value = "cid") Long courseId, @RequestBody SectionDto request) {
        try {
            request.setCourseId(courseId);
            Section section = sectionService.postSection(request); 
            return ResponseUtil.build("SECTION CREATED", section, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateSection(
        @PathVariable(value = "cid") Long courseId, @PathVariable(value = "id") Long id, @RequestBody SectionDto request) 
    {
        try {
            request.setCourseId(courseId);
            Section section = sectionService.updateSection(id, request); 
            return ResponseUtil.build("SECTION ID " + id + " UPDATED", section, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteSection( @PathVariable(value = "cid") Long courseId, @PathVariable Long id) {
        try {
            sectionService.deleteSection(courseId, id);
            return ResponseUtil.build("SECTION ID " + id + " DELETED", null, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
