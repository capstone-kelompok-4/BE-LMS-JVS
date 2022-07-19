package com.alterra.capstoneproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.alterra.capstoneproject.domain.dao.Specialization;
import com.alterra.capstoneproject.domain.dto.SpecializationDto;
import com.alterra.capstoneproject.repository.SpecializationRepository;

@SpringBootTest(classes = SpecializationService.class)
public class SpecializationServiceTest {
    private final EasyRandom EASY_RANDOM = new EasyRandom();
    private List<Specialization> specializations;
    private Specialization specialization;
    private SpecializationDto specializationDto;
    
    @MockBean
    private SpecializationRepository specializationRepository;

    @Autowired
    private SpecializationService specializationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        specializations = EASY_RANDOM.objects(Specialization.class, 2)
                            .collect(Collectors.toList());
        specialization = EASY_RANDOM.nextObject(Specialization.class);
        specializationDto = EASY_RANDOM.nextObject(SpecializationDto.class);
    }

    @Test
    void getSpecializationsSuccessTest() {
        when(specializationRepository.findAll()).thenReturn(specializations);

        var result = specializationService.getSpecializations();

        assertEquals(specializations, result);
    }

    @Test
    void getSpecializationsExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            specializationService.getSpecializations();
        });
    }

    @Test
    void getSpecializationSuccessTest() {
        when(specializationRepository.findById(specialization.getId()))
            .thenReturn(Optional.of(specialization));

        var result = specializationService.getSpecialization(specialization.getId());

        assertEquals(specialization, result);
    }

    @Test
    void getSpecializationExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            specializationService.getSpecialization(specialization.getId());
        });
    }

    @Test
    void postSpecializationSuccessTest() {
        Specialization testSpecialization = new Specialization();

        testSpecialization.setName(specializationDto.getName());

        when(specializationRepository.save(testSpecialization))
            .thenReturn(testSpecialization);

        var result = specializationService.postSpecialization(specializationDto);

        assertEquals(testSpecialization, result);
    }

    @Test
    void postSpecializationExceptionTest() {
        Specialization testSpecialization = new Specialization();

        testSpecialization.setName(specializationDto.getName());

        when(specializationRepository.save(testSpecialization))
            .thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> {
            specializationService.postSpecialization(specializationDto);
        });
    }

    @Test
    void updateSpecializationSuccessTest() {
        when(specializationRepository.findById(specialization.getId()))
            .thenReturn(Optional.of(specialization));

        when(specializationRepository.save(specialization))
            .thenReturn(specialization);

        var result = specializationService.updateSpecialization(specialization.getId(), specializationDto);

        assertEquals(specialization, result);
    }  
    
    @Test
    void updateSpecializationExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            specializationService.updateSpecialization(specialization.getId(), specializationDto);
        });
    }

    @Test
    void deleteSpecializationSuccessTest() {
        when(specializationRepository.findById(specialization.getId()))
            .thenReturn(Optional.of(specialization));

        specializationService.deleteSpecialization(specialization.getId());

        verify(specializationRepository).deleteById(specialization.getId());
    }

    @Test
    void deleteSpecializationExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            specializationService.deleteSpecialization(specialization.getId());
        });
    }
}
