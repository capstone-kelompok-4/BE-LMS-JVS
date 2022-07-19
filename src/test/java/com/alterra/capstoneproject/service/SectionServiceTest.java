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
import com.alterra.capstoneproject.domain.dao.Section;
import com.alterra.capstoneproject.domain.dto.SectionDto;
import com.alterra.capstoneproject.repository.CourseRepository;
import com.alterra.capstoneproject.repository.CourseTakenRepository;
import com.alterra.capstoneproject.repository.MaterialRepository;
import com.alterra.capstoneproject.repository.ReportRepository;
import com.alterra.capstoneproject.repository.SectionRepository;

@SpringBootTest(classes = SectionService.class)
public class SectionServiceTest {
    private final EasyRandom EASY_RANDOM = new EasyRandom();
    private List<Section> sections;
    private SectionDto sectionDto;
    private Section section;
    private Course course;

    @MockBean
    private SectionRepository sectionRepository;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private CourseTakenRepository courseTakenRepository;

    @MockBean
    private MaterialRepository materialRepository;

    @MockBean
    private ReportRepository reportRepository;

    @Autowired
    private SectionService sectionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);        
        course = EASY_RANDOM.nextObject(Course.class);
        sections = EASY_RANDOM.objects(Section.class, 2)
                            .collect(Collectors.toList());
        section = EASY_RANDOM.nextObject(Section.class);
        sectionDto = EASY_RANDOM.nextObject(SectionDto.class);

        section.setCourseSection(course);
        sectionDto.setCourseId(course.getId());
        sections.forEach(section -> {
            section.setCourseSection(course);;
        });
    }

    @Test
    void getSectionsSuccessTest() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));

        when(sectionRepository.searchAll(course.getId()))
            .thenReturn(sections);

        var result = sectionService.getSections(course.getId());

        assertEquals(sections, result);
    }

    @Test
    void getSectionsExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            sectionService.getSections(course.getId());
        });
    }

    @Test
    void getSectionsException2Test() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));

        assertThrows(RuntimeException.class, () -> {
            sectionService.getSections(course.getId());
        });
    }

    @Test
    void getSectionSuccessTest() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));
        
        when(sectionRepository.searchById(section.getId(), course.getId()))
            .thenReturn(Optional.of(section));

        var result = sectionService.getSection(course.getId(), section.getId());

        assertEquals(section, result);
    }

    @Test
    void getSectionExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            sectionService.getSection(course.getId(), section.getId());
        });
    }

    @Test
    void getSectionException2Test() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));
        
        assertThrows(RuntimeException.class, () -> {
            sectionService.getSection(course.getId(), section.getId());
        });
    }

    @Test
    void postSectionSuccessTest() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));

        Section testSection = new Section();

        testSection.setNumber(sectionDto.getNumber());
        testSection.setName(sectionDto.getName());
        testSection.setCourseSection(course);

        when(sectionRepository.save(testSection))
            .thenReturn(testSection);

        var result = sectionService.postSection(sectionDto);

        assertEquals(testSection, result);
    }

    @Test
    void postSectionExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            sectionService.postSection(sectionDto);
        });
    }

    @Test
    void updateSectionSuccessTest() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));

        when(sectionRepository.searchById(section.getId(), course.getId()))
            .thenReturn(Optional.of(section));

        when(sectionRepository.save(section))
            .thenReturn(section);

        var result = sectionService.updateSection(section.getId(), sectionDto);

        assertEquals(section, result);
    }

    @Test
    void updateSectionExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            sectionService.updateSection(section.getId(), sectionDto);
        });
    }

    @Test
    void updateSectionException2Test() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));

        assertThrows(RuntimeException.class, () -> {
            sectionService.updateSection(section.getId(), sectionDto);
        });
    }

    @Test
    void deleteSectionSuccessTest() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));
        
        when(sectionRepository.searchById(section.getId(), course.getId()))
            .thenReturn(Optional.of(section));  
            
        sectionService.deleteSection(course.getId(), section.getId());

        verify(sectionRepository).deleteById(section.getId());
    }

    @Test
    void deleteSectionExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            sectionService.deleteSection(course.getId(), section.getId());
        });
    }

    @Test
    void deleteSectionException2Test() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));

        assertThrows(RuntimeException.class, () -> {
            sectionService.deleteSection(course.getId(), section.getId());
        });
    }
}
