package com.alterra.capstoneproject.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alterra.capstoneproject.domain.dao.Course;
import com.alterra.capstoneproject.domain.dao.Section;
import com.alterra.capstoneproject.domain.dto.SectionDto;
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
public class SectionService {
    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private MaterialRepository materialRepository;

    public List<Section> getSections(Long courseId) {
        try {
            log.info("Get course");
            courseRepository.searchById(courseId)
                .orElseThrow(() -> new Exception("COURSE ID " + courseId + " NOT FOUND"));

            log.info("Get all sections");
            List<Section> sections = sectionRepository.searchAll(courseId);

            if(sections.isEmpty()) throw new Exception("SECTION IS EMPTY");

            return sections;
        } catch (Exception e) {
            log.error("Get all sections error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Section getSection(Long courseId, Long id) {
        try {
            log.info("Get course");
            courseRepository.searchById(courseId)
                .orElseThrow(() -> new Exception("COURSE ID " + courseId + " NOT FOUND"));

            log.info("Get section by id");
            Section section = sectionRepository.searchById(id, courseId)
                .orElseThrow(() -> new Exception("SECTION ID " + id + " WITH COURSE ID " + courseId +" NOT FOUND"));

            return section;
        } catch (Exception e) {
            log.error("Get section by id error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Section postSection(SectionDto request) {
        try {
            log.info("Get course");
            Course course = courseRepository.searchById(request.getCourseId())
                .orElseThrow(() -> new Exception("COURSE ID " + request.getCourseId() + " NOT FOUND"));

            log.info("Post section");
            Section section = new Section();

            section.setName(request.getName());
            section.setCourseSection(course);

            sectionRepository.save(section);
            return section;
        } catch (Exception e) {
            log.error("Post section error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Section updateSection(Long id, SectionDto request) {
        try {
            log.info("Get course");
            Course course = courseRepository.searchById(request.getCourseId())
                .orElseThrow(() -> new Exception("COURSE ID " + request.getCourseId() + " NOT FOUND"));

            log.info("Get section by id");
            Section section = sectionRepository.searchById(id, request.getCourseId())
                .orElseThrow(() -> new Exception("SECTION ID " + id + " WITH COURSE ID " + request.getCourseId() +" NOT FOUND"));

            log.info("Post section");

            section.setName(request.getName());
            section.setCourseSection(course);

            sectionRepository.save(section);
            return section;
        } catch (Exception e) {
            log.error("Post section error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void deleteSection(Long courseId, Long id) {
        try {
            log.info("Get course id {}", courseId);
            courseRepository.searchById(courseId)
                .orElseThrow(() -> new Exception("COURSE ID " + courseId + " NOT FOUND"));

            log.info("Get section by id");
            sectionRepository.searchById(id, courseId)
                .orElseThrow(() -> new Exception("SECTION ID " + id + " WITH COURSE ID " + courseId +" NOT FOUND"));

            sectionRepository.deleteById(id);
            materialRepository.deleteMaterialBySection(id);
        } catch (Exception e) {
            log.error("Delete section error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
