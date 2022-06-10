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

import com.alterra.capstoneproject.domain.dao.Material;
import com.alterra.capstoneproject.domain.dto.MaterialDto;
import com.alterra.capstoneproject.service.MaterialService;
import com.alterra.capstoneproject.util.ResponseUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/courses/{cid}/sections/{cis}/materials")
public class MaterialController {
    @Autowired
    private MaterialService materialService;

    @GetMapping
    public ResponseEntity<?> getMaterials() {
        try {
            List<Material> materials = materialService.getMaterials();
            return ResponseUtil.build("GET MATERIALS", materials, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getMaterial(@PathVariable(value = "id") Long id) {
        try {
            Material material = materialService.getMaterial(id); 
            return ResponseUtil.build("GET MATERIAL ID " + id, material, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> postMaterial(@PathVariable(value = "cis") Long sectionId, @RequestBody MaterialDto request) {
        try {
            request.setSectionId(sectionId);
            Material material = materialService.postMaterial(request); 
            return ResponseUtil.build("MATERIAL CREATED", material, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateMaterial(@PathVariable(value = "cis") Long sectionId, @PathVariable(value = "id") Long id, @RequestBody MaterialDto request) {
        try {
            request.setSectionId(sectionId);
            Material material = materialService.updateMaterial(id, request); 
            return ResponseUtil.build("MATERIAL ID " + id + " UPDATED", material, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteMaterial(@PathVariable Long id) {
        try {
            materialService.deleteMaterial(id);
            return ResponseUtil.build("MATERIAL ID " + id + " DELETED", null, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
