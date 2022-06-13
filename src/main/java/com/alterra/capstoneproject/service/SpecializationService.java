package com.alterra.capstoneproject.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alterra.capstoneproject.domain.dao.Specialization;
import com.alterra.capstoneproject.domain.dto.SpecializationDto;
import com.alterra.capstoneproject.repository.CourseRepository;
import com.alterra.capstoneproject.repository.SpecializationRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
@NoArgsConstructor
@Transactional
public class SpecializationService {
    @Autowired
    private SpecializationRepository specializationRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<Specialization> getSpecializations() {
        try {
            log.info("Get all specialization");
            List<Specialization> specializations = specializationRepository.findAll();

            if(specializations.isEmpty()) throw new Exception("SPECIALIZATION IS EMPTY");

            return specializations;
        } catch (Exception e) {
            log.error("Get all specialization error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Specialization getSpecialization(Long id) {
        try {
            log.info("Get specialization by id");
            Specialization specialization = specializationRepository.findById(id)
                .orElseThrow(() -> new Exception("SPECIALIZATION ID " + id + " NOT FOUND"));

            return specialization;
        } catch (Exception e) {
            log.error("Get specialization by id error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Specialization postSpecialization(SpecializationDto request) {
        try {
            log.info("Post specialization");
            Specialization specialization = new Specialization();
            
            specialization.setName(request.getName());

            specializationRepository.save(specialization);
            return specialization;
        } catch (Exception e) {
            log.error("Post specialization error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Specialization updateSpecialization(Long id, SpecializationDto request) {
        try {
            Specialization specialization = specializationRepository.findById(id)
                .orElseThrow(() -> new Exception("SPECIALIZATION ID " + id + " NOT FOUND"));

            log.info("Update specialization");
            
            specialization.setName(request.getName());

            specializationRepository.save(specialization);
            return specialization;
        } catch (Exception e) {
            log.error("Update specialization error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void deleteSpecialization(Long id) {
        try {
            specializationRepository.findById(id)
                .orElseThrow(() -> new Exception("SPECIALIZATION ID " + id + " NOT FOUND"));

            specializationRepository.deleteById(id);
            courseRepository.deleteCourseBySpecialization(id);
        } catch (Exception e) {
            log.error("Delete specialization error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
