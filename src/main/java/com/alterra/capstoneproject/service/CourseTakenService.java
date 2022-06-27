package com.alterra.capstoneproject.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alterra.capstoneproject.domain.dao.Course;
import com.alterra.capstoneproject.domain.dao.CourseTaken;
import com.alterra.capstoneproject.domain.dao.User;
import com.alterra.capstoneproject.domain.dto.CourseTakenDto;
import com.alterra.capstoneproject.repository.CourseRepository;
import com.alterra.capstoneproject.repository.CourseTakenRepository;
import com.alterra.capstoneproject.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
@NoArgsConstructor
@Transactional
public class CourseTakenService {
    @Autowired
    private CourseTakenRepository courseTakenRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    public List<CourseTaken> getCourseTakens() {
        try {
            log.info("Get all course taken");
            List<CourseTaken> courseTakens = courseTakenRepository.findAll();

            if(courseTakens.isEmpty()) throw new Exception("COURSE TAKEN IS EMPTY");

            return courseTakens;
        } catch (Exception e) {
            log.error("Get all course taken error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public CourseTaken getCourseTaken(Long id) {
        try {
            log.info("Get course taken by id");
            CourseTaken courseTaken = courseTakenRepository.findById(id)
                .orElseThrow(() -> new Exception("COURSE TAKEN ID " + id + " NOT FOUND"));

            return courseTaken;
        } catch (Exception e) {
            log.error("Get course taken by id error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public CourseTaken postCourseTaken(CourseTakenDto request) {
        try {
            log.info("Get course");
            Course course = courseRepository.searchById(request.getCourseId())
                .orElseThrow(() -> new Exception("COURSE ID " + request.getCourseId() + " NOT FOUND"));

            log.info("Get user");
            User user = userRepository.findUsername(request.getEmail());

            log.info("Post course taken");
            CourseTaken courseTaken = new CourseTaken();

            courseTaken.setCourseTake(course);
            courseTaken.setUser(user);
            courseTaken.setTakenAt(LocalDateTime.now());

            courseTakenRepository.save(courseTaken);
            return courseTaken;
        } catch (Exception e) {
            log.error("Post course taken error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public CourseTaken updateCourseTaken(Long id, CourseTakenDto request) {
        try {
            log.info("Get course taken by id");
            CourseTaken courseTaken = courseTakenRepository.findById(id)
                .orElseThrow(() -> new Exception("COURSE TAKEN ID " + id + " NOT FOUND"));

            log.info("Get course");
            Course course = courseRepository.searchById(request.getCourseId())
                .orElseThrow(() -> new Exception("COURSE ID " + request.getCourseId() + " NOT FOUND"));

            log.info("Get user");
            User user = userRepository.findByUsername(request.getEmail())
                .orElseThrow(() -> new Exception("EMAIL " + request.getEmail() + " NOT FOUND"));

            log.info("Post course taken");

            courseTaken.setCourseTake(course);
            courseTaken.setUser(user);

            courseTakenRepository.save(courseTaken);
            return courseTaken;
        } catch (Exception e) {
            log.error("Post course taken error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void deleteCourseTaken(Long id) {
        try {
            courseTakenRepository.findById(id)
                .orElseThrow(() -> new Exception("COURSE TAKEN ID " + id + " NOT FOUND"));

            courseTakenRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Delete course taken error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public List<CourseTaken> getCourseTakenByUser(String email) {
        try {
            log.info("Get user");
            User user = userRepository.findUsername(email);

            List<CourseTaken> courseTakens = courseTakenRepository.findCourseTakenByUser(user.getId());
                 
            if(courseTakens.isEmpty()) throw new Exception("COURSE TAKEN BY USER " + user.getUsername() + " IS EMPTY");

            return courseTakens;
        } catch (Exception e) {
            log.error("Get course taken by user error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public List<CourseTaken> getCourseTakenByCourse(Long id) {
        try {
            log.info("Get course by id");
            courseRepository.findById(id)
                .orElseThrow(() -> new Exception("COURSE ID " + id + " NOT FOUND"));

            List<CourseTaken> courseTakens = courseTakenRepository.findCourseTakenByCourse(id);

            return courseTakens;
        } catch (Exception e) {
            log.error("Get course taken by course error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
