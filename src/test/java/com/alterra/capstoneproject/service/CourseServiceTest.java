package com.alterra.capstoneproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import com.alterra.capstoneproject.domain.dao.Specialization;
import com.alterra.capstoneproject.domain.dao.User;
import com.alterra.capstoneproject.domain.dto.CourseDto;
import com.alterra.capstoneproject.repository.CourseRepository;
import com.alterra.capstoneproject.repository.SpecializationRepository;
import com.alterra.capstoneproject.repository.UserRepository;

@SpringBootTest(classes = CourseService.class)
public class CourseServiceTest {
    private final EasyRandom EASY_RANDOM = new EasyRandom();
    private List<Course> courses;
    private CourseDto courseDto;
    private Course course;
    private User user;
    private Specialization specialization;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private SpecializationRepository specializationRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        courses = EASY_RANDOM.objects(Course.class, 2)
                    .collect(Collectors.toList());
        courseDto = EASY_RANDOM.nextObject(CourseDto.class);
        course = EASY_RANDOM.nextObject(Course.class);
        specialization = EASY_RANDOM.nextObject(Specialization.class);
        user = EASY_RANDOM.nextObject(User.class);
        courseDto.setSpecializationId(specialization.getId());
    }

    @Test
    void getCoursesSuccessTest() {
        when(courseRepository.findAll()).thenReturn(courses);
        
        var result = courseService.getCourses();

        assertEquals(courses, result);
    }

    @Test
    void getCoursesExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            courseService.getCourses();
        });
    }

    @Test
    void getCourseSuccessTest() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));

        var result = courseService.getCourse(course.getId());

        assertEquals(course, result);
    }

    @Test
    void getCourseExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            courseService.getCourse(course.getId());
        });
    }

    @Test
    void postCourseSuccessTest() {
        when(specializationRepository.searchById(specialization.getId()))
            .thenReturn(Optional.of(specialization));

        Course testCourse = new Course();

        testCourse.setName(courseDto.getName());
        testCourse.setDescription(courseDto.getDescription());
        testCourse.setBannerUrl(courseDto.getBannerUrl());
        testCourse.setTargetLearner(courseDto.getTargetLearner());
        testCourse.setObjectiveLearner(courseDto.getObjectiveLearner());
        testCourse.setCourseSpecialization(specialization);
        testCourse.setMethodologyLearnings(courseDto.getMethodologyLearnings());

        when(courseRepository.save(testCourse)).thenReturn(testCourse);

        var result = courseService.postCourse(courseDto);

        assertEquals(testCourse, result);
    }

    @Test
    void postCourseExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            courseService.postCourse(courseDto);
        });
    }

    @Test
    void updateCourseSuccessTest() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));

        when(specializationRepository.searchById(courseDto.getSpecializationId()))
            .thenReturn(Optional.of(specialization));
            
        when(courseRepository.save(course)).thenReturn(course);

        var result = courseService.updateCourse(course.getId(), courseDto);

        assertEquals(course, result);
    }

    @Test
    void updateCourseExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            courseService.updateCourse(course.getId(), courseDto);
        });
    }

    @Test
    void updateCourseException2Test() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));

        assertThrows(RuntimeException.class, () -> {
            courseService.updateCourse(course.getId(), courseDto);
        });
    }

    @Test
    void deleteCourseSuccessTest() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));

        courseService.deleteCourse(course.getId());

        verify(courseRepository).deleteById(course.getId());
    }

    @Test
    void deleteCourseExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            courseService.deleteCourse(course.getId());
        });
    }

    @Test
    void getCourseByUserSpecSuccessTest() {
        when(userRepository.findUsername(user.getUsername()))
            .thenReturn(user);

        when(courseRepository.searchByUserSpec(user.getUserSpecialization().getId()))
            .thenReturn(courses);

        var result = courseService.getCourseByUserSpec(user.getUsername());

        assertEquals(courses, result);
    }

    @Test
    void getCourseByUserSpecExceptionTest() {
        when(userRepository.findUsername(user.getUsername()))
            .thenReturn(user);
        
        user.setUserSpecialization(null);

        assertThrows(RuntimeException.class, () -> {
            courseService.getCourseByUserSpec(user.getUsername());
        });
    }

    @Test
    void getCourseByUserSpecException2Test() {
        when(userRepository.findUsername(user.getUsername()))
            .thenReturn(user);

        List<Course> testCourses = new ArrayList<>();

        when(courseRepository.searchByUserSpec(user.getUserSpecialization().getId()))
            .thenReturn(testCourses);

        assertTrue(testCourses.isEmpty());

        assertThrows(RuntimeException.class, () -> {
            courseService.getCourseByUserSpec(user.getUsername());
        });
    }

    @Test
    void getCourseByNameSuccessTest() {
        when(courseRepository.findByName(course.getName()))
            .thenReturn(courses);

        var result = courseService.getCourseByName(course.getName());

        assertEquals(courses, result);
    }

    @Test
    void getCourseByNameExceptionTest() {        
        assertThrows(RuntimeException.class, () -> {
            courseService.getCourseByName(course.getName());
        });
    }
}
