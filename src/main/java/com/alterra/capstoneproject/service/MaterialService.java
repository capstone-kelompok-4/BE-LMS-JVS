package com.alterra.capstoneproject.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alterra.capstoneproject.domain.dao.Material;
import com.alterra.capstoneproject.domain.dao.Section;
import com.alterra.capstoneproject.domain.dto.MaterialDto;
import com.alterra.capstoneproject.repository.CourseRepository;
import com.alterra.capstoneproject.repository.MaterialRepository;
import com.alterra.capstoneproject.repository.SectionRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
@NoArgsConstructor
@Transactional
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<Material> getMaterials(Long courseId, Long sectionId) {
        try {
            log.info("Get course");
            courseRepository.searchById(courseId)
                .orElseThrow(() -> new Exception("COURSE ID " + courseId + " NOT FOUND"));

            log.info("Get section");
            sectionRepository.searchById(sectionId, courseId)
                .orElseThrow(() -> new Exception("SECTION ID " + sectionId + " WITH COURSE ID " + courseId +" NOT FOUND"));

            log.info("Get all materials");
            List<Material> materials = materialRepository.searchAll(sectionId);

            if(materials.isEmpty()) throw new Exception("MATERIAL IS EMPTY");

            return materials;
        } catch (Exception e) {
            log.error("Get all materials error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Material getMaterial(Long courseId, Long sectionId, Long id) {
        try {
            log.info("Get course");
            courseRepository.searchById(courseId)
                .orElseThrow(() -> new Exception("COURSE ID " + courseId + " NOT FOUND"));

            log.info("Get section");
            sectionRepository.searchById(sectionId, courseId)
                .orElseThrow(() -> new Exception("SECTION ID " + sectionId + " WITH COURSE ID " + courseId +" NOT FOUND"));

            log.info("Get material by id");
            Material material = materialRepository.searchById(id, sectionId)
                .orElseThrow(() -> 
                new Exception("MATERIAL ID " + id + " WITH COURSE ID " + courseId + " AND SECTION ID " + sectionId + " NOT FOUND")
            );

            return material;
        } catch (Exception e) {
            log.error("Get material by id error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Material postMaterial(MaterialDto request) {
        try {
            log.info("Get course");
            courseRepository.searchById(request.getCourseId())
                .orElseThrow(() -> new Exception("COURSE ID " + request.getCourseId() + " NOT FOUND"));

            log.info("Get section");
            Section section = sectionRepository.searchById(request.getSectionId(), request.getCourseId())
                .orElseThrow(() -> 
                new Exception("SECTION ID " + request.getSectionId() + " WITH COURSE ID " + request.getCourseId() +" NOT FOUND")
            );

            log.info("Post material");
            Material material = new Material();

            material.setName(request.getName());
            material.setSection(section);
            material.setType(request.getType());
            material.setUrl(request.getUrl());

            materialRepository.save(material);
            return material;
        } catch (Exception e) {
            log.error("Post material error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Material updateMaterial(Long id, MaterialDto request) {
        try {
            log.info("Get course");
            courseRepository.searchById(request.getCourseId())
                .orElseThrow(() -> new Exception("COURSE ID " + request.getCourseId() + " NOT FOUND"));

            log.info("Get section");
            Section section = sectionRepository.searchById(request.getSectionId(), request.getCourseId())
                .orElseThrow(() ->                 
                new Exception("SECTION ID " + request.getSectionId() + " WITH COURSE ID " + request.getCourseId() +" NOT FOUND")
            );

            log.info("Get material by id");
            Material material = materialRepository.searchById(id, request.getSectionId())
                .orElseThrow(() -> 
                new Exception("MATERIAL ID " + id + " WITH COURSE ID " + request.getCourseId() + 
                " AND SECTION ID " + request.getSectionId() +" NOT FOUND")
            );

            log.info("Update material");

            material.setName(request.getName());
            material.setSection(section);
            material.setType(request.getType());
            material.setUrl(request.getUrl());

            materialRepository.save(material);
            return material;
        } catch (Exception e) {
            log.error("Update material error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void deleteMaterial(Long courseId, Long sectionId, Long id) {
        try {
            log.info("Get course");
            courseRepository.searchById(courseId)
                .orElseThrow(() -> new Exception("COURSE ID " + courseId + " NOT FOUND"));

            log.info("Get section");
            sectionRepository.searchById(sectionId, courseId)
                .orElseThrow(() -> new Exception("SECTION ID " + sectionId + " WITH COURSE ID " + courseId +" NOT FOUND"));

            log.info("Get material by id");
            materialRepository.searchById(id, sectionId)
                .orElseThrow(() -> 
                new Exception("MATERIAL ID " + id + " WITH COURSE ID " + courseId + " AND SECTION ID " + sectionId + " NOT FOUND")
            );

            materialRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Delete material error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
