package com.alterra.capstoneproject.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alterra.capstoneproject.domain.dao.Course;
import com.alterra.capstoneproject.domain.dao.Specialization;
import com.alterra.capstoneproject.domain.dto.CourseDto;
import com.alterra.capstoneproject.repository.CourseRepository;
import com.alterra.capstoneproject.repository.CourseTakenRepository;
import com.alterra.capstoneproject.repository.SectionRepository;
import com.alterra.capstoneproject.repository.SpecializationRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
@NoArgsConstructor
@Transactional
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SpecializationRepository specializationRepository;

    @Autowired
    private CourseTakenRepository courseTakenRepository;

    @Autowired
    private SectionRepository sectionRepository;
    
    public List<Course> getCourses() {
        try {
            log.info("Get all courses");
            List<Course> courses = courseRepository.findAll();

            if(courses.isEmpty()) throw new Exception("COURSE IS EMPTY");

            return courses;
        } catch (Exception e) {
            log.error("Get all courses error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Course getCourse(Long id) {
        try {
            log.info("Get course by id");
            Course course = courseRepository.findById(id)
                .orElseThrow(() -> new Exception("COURSE ID " + id + " NOT FOUND"));

            return course;
        } catch (Exception e) {
            log.error("Get course by id error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Course postCourse(CourseDto request) {
        try {
            log.info("Get specialization");
            Specialization spec = specializationRepository.searchById(request.getSpecializationId())
                .orElseThrow(() -> new Exception("SPECIALIZATION ID " + request.getSpecializationId() + " NOT FOUND"));

            log.info("Post course");
            Course course = new Course();

            course.setName(request.getName());
            course.setSpecialization(spec);

            courseRepository.save(course);
            return course;
        } catch (Exception e) {
            log.error("Post course error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Course updateCourse(Long id, CourseDto request) {
        try {
            log.info("Get course by id");
            Course course = courseRepository.findById(id)
                .orElseThrow(() -> new Exception("COURSE ID " + id + " NOT FOUND"));

            log.info("Get specialization");
            Specialization spec = specializationRepository.searchById(request.getSpecializationId())
                .orElseThrow(() -> new Exception("SPECIALIZATION ID " + request.getSpecializationId() + " NOT FOUND"));

            log.info("Update course");

            course.setName(request.getName());
            course.setSpecialization(spec);

            courseRepository.save(course);
            return course;
        } catch (Exception e) {
            log.error("Update course error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void deleteCourse(Long id) {
        try {
            courseRepository.findById(id)
                .orElseThrow(() -> new Exception("COURSE ID " + id + " NOT FOUND"));

            courseRepository.deleteById(id);
            courseTakenRepository.deleteCourseTakenByCourse(id);
            sectionRepository.deleteSectionByCourse(id);
        } catch (Exception e) {
            log.error("Delete course error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
