package com.alterra.capstoneproject.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alterra.capstoneproject.domain.dao.Course;
import com.alterra.capstoneproject.domain.dao.MethodologyLearning;
import com.alterra.capstoneproject.domain.dao.Specialization;
import com.alterra.capstoneproject.domain.dao.User;
import com.alterra.capstoneproject.domain.dto.CourseDto;
import com.alterra.capstoneproject.repository.CourseRepository;
import com.alterra.capstoneproject.repository.MethodologyRepository;
import com.alterra.capstoneproject.repository.SpecializationRepository;
import com.alterra.capstoneproject.repository.UserRepository;

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
    private UserRepository userRepository;

    @Autowired
    private MethodologyRepository methodologyRepository;
    
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
            course.setDescription(request.getDescription());
            course.setBannerUrl(request.getBannerUrl());
            course.setTargetLearner(request.getTargetLearner());
            course.setObjectiveLearner(request.getObjectiveLearner());
            course.setCourseSpecialization(spec);

            log.info("methodology");
            Set<MethodologyLearning> methodologyLearnings = new HashSet<>();
            request.getMethodologyLearnings().forEach(inputMethodology -> {
                MethodologyLearning method = methodologyRepository.findByName(inputMethodology)
                    .orElseThrow(() -> new RuntimeException("METHODOLOGY NOT FOUND"));
                methodologyLearnings.add(method);
            });
            course.setMethodologyLearnings(methodologyLearnings);

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
            course.setDescription(request.getDescription());
            course.setBannerUrl(request.getBannerUrl());
            course.setTargetLearner(request.getTargetLearner());
            course.setObjectiveLearner(request.getObjectiveLearner());
            course.setCourseSpecialization(spec);

            Set<MethodologyLearning> methodologyLearnings = new HashSet<>();
            request.getMethodologyLearnings().forEach(inputMethodology -> {
                MethodologyLearning method = methodologyRepository.findByName(inputMethodology)
                    .orElseThrow(() -> new RuntimeException("METHODOLOGY NOT FOUND"));
                methodologyLearnings.add(method);
            });
            course.setMethodologyLearnings(methodologyLearnings);

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
        } catch (Exception e) {
            log.error("Delete course error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public List<Course> getCourseByUserSpec(String email) {
        try {
            log.info("Get user");
            User user = userRepository.findUsername(email);

            if(user.getUserSpecialization() == null) throw new Exception("USER DO NOT HAVE SPECIALIZATION");
            
            log.info("Get courses by user specialization");
            List<Course> courses = courseRepository.searchByUserSpec(user.getUserSpecialization().getId());

            if(courses.isEmpty()) throw new Exception("COURSE WITH SPECIALIZATION " + user.getUserSpecialization().getName() + " IS EMPTY");

            return courses;
        } catch (Exception e) {
            log.error("Get course by user specialization error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public List<Course> getCourseByName(String name) {
        try {
            log.info("Get course by name {}", name);
            List<Course> courses = courseRepository.findByName(name);

            if(courses.isEmpty()) throw new Exception("COURSE IS EMPTY");

            return courses;
        } catch (Exception e) {
            log.error("Get course by name error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
