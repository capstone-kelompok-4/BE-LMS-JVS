package com.alterra.capstoneproject.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alterra.capstoneproject.domain.dao.CourseTaken;
import com.alterra.capstoneproject.domain.dao.Material;
import com.alterra.capstoneproject.domain.dao.Report;
import com.alterra.capstoneproject.domain.dao.Section;
import com.alterra.capstoneproject.domain.dto.MaterialDto;
import com.alterra.capstoneproject.repository.CourseRepository;
import com.alterra.capstoneproject.repository.CourseTakenRepository;
import com.alterra.capstoneproject.repository.MaterialRepository;
import com.alterra.capstoneproject.repository.ReportRepository;
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

    @Autowired
    private CourseTakenRepository courseTakenRepository;

    @Autowired
    private ReportRepository reportRepository;

    public List<Material> getMaterials(Long courseId, Long sectionId) {
        try {
            log.info("Get course");
            courseRepository.findById(courseId)
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
            courseRepository.findById(courseId)
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
            courseRepository.findById(request.getCourseId())
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

            log.info("Update material report and progress");
            List<CourseTaken> courseTakens = courseTakenRepository.findCourseTakenByCourse(request.getCourseId());            

            Integer totalMaterial = materialRepository.countMaterial(request.getCourseId()).size();
            
            courseTakens.forEach(courseTake -> {
                log.info("Add new report");
                Report report = new Report();
                report.setCourseTaken(courseTake);
                report.setMaterial(material);
                reportRepository.save(report);

                log.info("Update progress course taken");
                Integer completedCourse = reportRepository.findByCompleted(courseTake.getId()).size();
                Integer progress = (completedCourse*100)/totalMaterial;
                courseTake.setProgress(progress);
                courseTakenRepository.save(courseTake);
            });

            return material;
        } catch (Exception e) {
            log.error("Post material error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Material updateMaterial(Long id, MaterialDto request) {
        try {
            log.info("Get course");
            courseRepository.findById(request.getCourseId())
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
            courseRepository.findById(courseId)
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

            log.info("Update material progress");
            List<CourseTaken> courseTakens = courseTakenRepository.findCourseTakenByCourse(courseId);            

            Integer totalMaterial = materialRepository.countMaterial(courseId).size();
            
            courseTakens.forEach(courseTake -> {
                log.info("Update progress course taken");
                Integer completedCourse = reportRepository.findByCompleted(courseTake.getId()).size();
                Integer progress = (completedCourse*100)/totalMaterial;
                courseTake.setProgress(progress);
                courseTakenRepository.save(courseTake);
            });
        } catch (Exception e) {
            log.error("Delete material error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
