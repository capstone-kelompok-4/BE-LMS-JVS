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

import com.alterra.capstoneproject.domain.dao.Course;
import com.alterra.capstoneproject.domain.dao.Material;
import com.alterra.capstoneproject.domain.dao.Section;
import com.alterra.capstoneproject.domain.dto.MaterialDto;
import com.alterra.capstoneproject.repository.CourseRepository;
import com.alterra.capstoneproject.repository.CourseTakenRepository;
import com.alterra.capstoneproject.repository.MaterialRepository;
import com.alterra.capstoneproject.repository.ReportRepository;
import com.alterra.capstoneproject.repository.SectionRepository;

@SpringBootTest(classes = MaterialService.class)
public class MaterialServiceTest {
    private final EasyRandom EASY_RANDOM = new EasyRandom();
    private List<Material> materials;
    private MaterialDto materialDto;
    private Material material;
    private Section section;
    private Course course;
    
    @MockBean
    private MaterialRepository materialRepository;

    @MockBean
    private SectionRepository sectionRepository;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private CourseTakenRepository courseTakenRepository;

    @MockBean
    private ReportRepository reportRepository;

    @Autowired
    private MaterialService materialService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);        
        course = EASY_RANDOM.nextObject(Course.class);        
        section = EASY_RANDOM.nextObject(Section.class);
        materials = EASY_RANDOM.objects(Material.class, 2)
                            .collect(Collectors.toList());
        material = EASY_RANDOM.nextObject(Material.class);
        materialDto = EASY_RANDOM.nextObject(MaterialDto.class);

        section.setCourseSection(course);
        materialDto.setCourseId(course.getId());
        materialDto.setSectionId(section.getId());
        materials.forEach(material -> {
            material.setSection(section);
        });
    }

    @Test
    void getMaterialsSuccessTest() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));
        
        when(sectionRepository.searchById(section.getId(), course.getId()))
            .thenReturn(Optional.of(section));

        when(materialRepository.searchAll(section.getId()))
            .thenReturn(materials);

        var result = materialService.getMaterials(course.getId(), section.getId());

        assertEquals(materials, result);
    }

    @Test
    void getMaterialsExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            materialService.getMaterials(course.getId(), section.getId());
        });
    }

    @Test
    void getMaterialsException2Test() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));

        assertThrows(RuntimeException.class, () -> {
            materialService.getMaterials(course.getId(), section.getId());
        });
    }

    @Test
    void getMaterialsException3Test() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));
        
        when(sectionRepository.searchById(section.getId(), course.getId()))
            .thenReturn(Optional.of(section));
            
        assertThrows(RuntimeException.class, () -> {
            materialService.getMaterials(course.getId(), section.getId());
        });
    }

    @Test
    void getMaterialSuccessTest() {
        when(courseRepository.findById(course.getId()))
        .thenReturn(Optional.of(course));
    
        when(sectionRepository.searchById(section.getId(), course.getId()))
            .thenReturn(Optional.of(section));

        when(materialRepository.searchById(material.getId(), section.getId()))
            .thenReturn(Optional.of(material));

        var result = materialService.getMaterial(course.getId(), section.getId(), material.getId());

        assertEquals(material, result);
    }

    @Test
    void getMaterialExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            materialService.getMaterial(course.getId(), section.getId(), material.getId());
        });
    }

    @Test
    void getMaterialException2Test() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));

        assertThrows(RuntimeException.class, () -> {
            materialService.getMaterial(course.getId(), section.getId(), material.getId());
        });
    }

    @Test
    void getMaterialException3Test() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));

        when(sectionRepository.searchById(section.getId(), course.getId()))
            .thenReturn(Optional.of(section));

        assertThrows(RuntimeException.class, () -> {
            materialService.getMaterial(course.getId(), section.getId(), material.getId());
        });
    }

    @Test
    void postMaterialSuccessTest() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));

        when(sectionRepository.searchById(section.getId(), course.getId()))
            .thenReturn(Optional.of(section));

        Material testMaterial = new Material();

        testMaterial.setName(materialDto.getName());
        testMaterial.setSection(section);
        testMaterial.setType(materialDto.getType());
        testMaterial.setUrl(materialDto.getUrl());

        when(materialRepository.save(testMaterial))
            .thenReturn(testMaterial);

        var result = materialService.postMaterial(materialDto);

        assertEquals(testMaterial, result);
    }

    @Test
    void postMaterialExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            materialService.postMaterial(materialDto);
        });
    }

    @Test
    void postMaterialException2Test() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));
        
        assertThrows(RuntimeException.class, () -> {
            materialService.postMaterial(materialDto);
        });
    }

    @Test
    void updateMaterialSuccessTest() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));
    
        when(sectionRepository.searchById(section.getId(), course.getId()))
            .thenReturn(Optional.of(section));

        when(materialRepository.searchById(material.getId(), section.getId()))
            .thenReturn(Optional.of(material));

        var result = materialService.updateMaterial(material.getId(), materialDto);

        assertEquals(material, result);
    }

    @Test
    void updateMaterialExceptionTest() {
       assertThrows(RuntimeException.class, () -> {
            materialService.updateMaterial(material.getId(), materialDto);
       });
    }

    @Test
    void updateMaterialException2Test() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));
    
        assertThrows(RuntimeException.class, () -> {
            materialService.updateMaterial(material.getId(), materialDto);
        });
    }

    @Test
    void updateMaterialException3Test() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));
        
        when(sectionRepository.searchById(section.getId(), course.getId()))
            .thenReturn(Optional.of(section));

        assertThrows(RuntimeException.class, () -> {
            materialService.updateMaterial(material.getId(), materialDto);
        });
    }

    @Test
    void deleteMaterialSuccessTest() {
        when(courseRepository.findById(course.getId()))
        .thenReturn(Optional.of(course));

        when(sectionRepository.searchById(section.getId(), course.getId()))
            .thenReturn(Optional.of(section));

        when(materialRepository.searchById(material.getId(), section.getId()))
            .thenReturn(Optional.of(material));

        materialService.deleteMaterial(course.getId(), section.getId(), material.getId());

        verify(materialRepository).deleteById(material.getId());
    }

    @Test
    void deleteMaterialExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            materialService.deleteMaterial(course.getId(), section.getId(), material.getId());
        });
    }

    @Test
    void deleteMaterialException2Test() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));

        assertThrows(RuntimeException.class, () -> {
            materialService.deleteMaterial(course.getId(), section.getId(), material.getId());
        });
    }

    @Test
    void deleteMaterialException3Test() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));

        when(sectionRepository.searchById(section.getId(), course.getId()))
            .thenReturn(Optional.of(section));

        assertThrows(RuntimeException.class, () -> {
            materialService.deleteMaterial(course.getId(), section.getId(), material.getId());
        });
    }
}
