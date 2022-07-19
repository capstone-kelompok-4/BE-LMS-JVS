package com.alterra.capstoneproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.alterra.capstoneproject.domain.dao.CourseTaken;
import com.alterra.capstoneproject.domain.dao.Material;
import com.alterra.capstoneproject.domain.dao.Report;
import com.alterra.capstoneproject.domain.dao.RequestEnum;
import com.alterra.capstoneproject.domain.dao.RoleEnum;
import com.alterra.capstoneproject.domain.dao.Section;
import com.alterra.capstoneproject.domain.dao.StatusEnum;
import com.alterra.capstoneproject.domain.dao.User;
import com.alterra.capstoneproject.domain.dto.CourseTakenDto;
import com.alterra.capstoneproject.domain.dto.RateCourse;
import com.alterra.capstoneproject.domain.dto.StatusCourse;
import com.alterra.capstoneproject.repository.CourseRepository;
import com.alterra.capstoneproject.repository.CourseTakenRepository;
import com.alterra.capstoneproject.repository.ReportRepository;
import com.alterra.capstoneproject.repository.UserRepository;

@SpringBootTest(classes = CourseTakenService.class)
public class CourseTakenServiceTest {
    private final EasyRandom EASY_RANDOM = new EasyRandom();
    private List<CourseTaken> courseTakens;
    private CourseTaken courseTaken;
    private CourseTakenDto courseTakenDto;
    private User user;
    private Course course;
    private RateCourse rateCourse;
    private Material material;
    private String message;
    private Integer totalMaterial;
    private Boolean check;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    @MockBean
    private CourseTakenRepository courseTakenRepository;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ReportRepository reportRepository;

    @MockBean
    private ReportService reportService;

    @Autowired
    private CourseTakenService courseTakenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        courseTakens = EASY_RANDOM.objects(CourseTaken.class, 2)
                            .collect(Collectors.toList());
        courseTaken = EASY_RANDOM.nextObject(CourseTaken.class);
        courseTakenDto = EASY_RANDOM.nextObject(CourseTakenDto.class);
        user = EASY_RANDOM.nextObject(User.class); 
        course = EASY_RANDOM.nextObject(Course.class);
        rateCourse = EASY_RANDOM.nextObject(RateCourse.class);

        courseTakenDto.setCourseId(course.getId());
        courseTakenDto.setEmail(user.getUsername());
        rateCourse.setCourseTakenId(courseTaken.getId());
    }

    @Test
    void getCourseTakensSuccessTest() {
        when(courseTakenRepository.findAll())
            .thenReturn(courseTakens);

        var result = courseTakenService.getCourseTakens();

        assertEquals(courseTakens, result);
    }

    @Test
    void getCourseTakensExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            courseTakenService.getCourseTakens();
        });
    }

    @Test
    void getCourseTakenSuccessTest() {
        when(courseTakenRepository.findById(courseTaken.getId()))
            .thenReturn(Optional.of(courseTaken));

        var result = courseTakenService.getCourseTaken(courseTaken.getId(), user.getUsername());

        assertEquals(courseTaken, result);
    }

    @Test
    void getCourseTakenSuccess2Test() {
        when(courseTakenRepository.findById(courseTaken.getId()))
            .thenReturn(Optional.of(courseTaken));

        courseTaken.getUser().setUsername(user.getUsername());

        var result = courseTakenService.getCourseTaken(courseTaken.getId(), user.getUsername());
        courseTaken.setLastAccessCourse(result.getLastAccessCourse());

        assertEquals(courseTaken, result);
    }

    @Test
    void getCourseTakenExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            courseTakenService.getCourseTaken(courseTaken.getId(), user.getUsername());
        });
    }

    @Test
    void getCourseTakenByUserSuccessTest() {
        when(userRepository.findUsername(user.getUsername()))
            .thenReturn(user);

        when(courseTakenRepository.findCourseTakenByUser(user.getId()))
            .thenReturn(courseTakens);

        var result = courseTakenService.getCourseTakenByUser(user.getUsername());

        assertEquals(courseTakens, result);
    }

    @Test
    void getCourseTakenByUserExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            courseTakenService.getCourseTakenByUser(user.getUsername());
        });
    }

    @Test
    void getCourseTakenByUserException2Test() {
        when(userRepository.findUsername(user.getUsername()))
            .thenReturn(user);

        assertThrows(RuntimeException.class, () -> {
            courseTakenService.getCourseTakenByUser(user.getUsername());
        });
    }

    @Test
    void getCourseTakenByCourseSuccessTest() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));

        when(courseTakenRepository.findCourseTakenByCourse(course.getId()))
            .thenReturn(courseTakens);

        var result = courseTakenService.getCourseTakenByCourse(course.getId());

        assertEquals(courseTakens, result);
    }

    @Test
    void getCourseTakenByCourseExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            courseTakenService.getCourseTakenByCourse(course.getId());
        });
    }

    @Test
    void getCourseTakenByCourseException2Test() {
        when(courseRepository.findById(course.getId()))
            .thenReturn(Optional.of(course));

        assertThrows(RuntimeException.class, () -> {
            courseTakenService.getCourseTakenByCourse(course.getId());
        });
    }

    @Test
    void updateStatusAcceptedTest() {
        StatusCourse statusCourse = new StatusCourse();
        statusCourse.setStatus(StatusEnum.ACCEPTED);
        statusCourse.setCertificateCode("CPKB97ES");
        when(courseTakenRepository.findById(courseTaken.getId()))
            .thenReturn(Optional.of(courseTaken));

        courseTaken.setStatus(StatusEnum.PENDING);
        courseTaken.setCertificateCode(statusCourse.getCertificateCode());

        when(courseTakenRepository.save(courseTaken))
            .thenReturn(courseTaken);

        var result = courseTakenService.updateStatus(courseTaken.getId(), statusCourse);

        assertEquals(courseTaken, result);
    }

    @Test
    void updateStatusRejectedTest() {
        StatusCourse statusCourse = new StatusCourse();
        statusCourse.setStatus(StatusEnum.REJECTED);
        when(courseTakenRepository.findById(courseTaken.getId()))
            .thenReturn(Optional.of(courseTaken));

        courseTaken.setStatus(StatusEnum.PENDING);

        when(courseTakenRepository.save(courseTaken))
            .thenReturn(courseTaken);

        var result = courseTakenService.updateStatus(courseTaken.getId(), statusCourse);

        assertEquals(courseTaken, result);
    }

    @Test
    void updateStatusExceptionTest() {
        StatusCourse statusCourse = new StatusCourse();
        statusCourse.setStatus(StatusEnum.REJECTED);
        
        assertThrows(RuntimeException.class, () -> {
            courseTakenService.updateStatus(courseTaken.getId(), statusCourse);
        });
    }

    @Test
    void updateStatusException2Test() {
        StatusCourse statusCourse = new StatusCourse();
        statusCourse.setStatus(StatusEnum.REJECTED);
        when(courseTakenRepository.findById(courseTaken.getId()))
            .thenReturn(Optional.of(courseTaken));
        
        courseTaken.setStatus(StatusEnum.ACCEPTED);

        assertThrows(RuntimeException.class, () -> {
            courseTakenService.updateStatus(courseTaken.getId(), statusCourse);
        });
    }

    @Test
    void updateStatusException3Test() {
        StatusCourse statusCourse = new StatusCourse();
        statusCourse.setStatus(StatusEnum.PENDING);

        when(courseTakenRepository.findById(courseTaken.getId()))
            .thenReturn(Optional.of(courseTaken));
        
        courseTaken.setStatus(StatusEnum.PENDING);

        assertThrows(RuntimeException.class, () -> {
            courseTakenService.updateStatus(courseTaken.getId(), statusCourse);
        });
    }

    @Test
    void deleteCourseTakenSuccessTest() {
        when(courseTakenRepository.findById(courseTaken.getId()))
            .thenReturn(Optional.of(courseTaken));

        when(courseRepository.findById(courseTaken.getCourseTake().getId()))
            .thenReturn(Optional.of(course));

        courseTakenService.deleteCourseTaken(courseTaken.getId());

        verify(courseTakenRepository).deleteById(courseTaken.getId());
    }

    @Test
    void deleteCourseTakenExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            courseTakenService.deleteCourseTaken(courseTaken.getId());
        });
    }

    @Test
    void deleteCourseTakenException2Test() {
        when(courseTakenRepository.findById(courseTaken.getId()))
            .thenReturn(Optional.of(courseTaken));

        assertThrows(RuntimeException.class, () -> {
            courseTakenService.deleteCourseTaken(courseTaken.getId());
        });
    }

    @Test
    void postCourseTakenSuccessTest() {
        when(courseRepository.findById(courseTakenDto.getCourseId()))
            .thenReturn(Optional.of(course));

        when(userRepository.findUsername(courseTakenDto.getEmail()))
            .thenReturn(user);

        List<CourseTaken> testListCourseTaken = new ArrayList<>();

        when(courseTakenRepository.findCourseTakenByCourseUser(course.getId(), user.getId()))
            .thenReturn(testListCourseTaken);

        CourseTaken testCourseTaken = new CourseTaken();

        testCourseTaken.setCourseTake(course);
        testCourseTaken.setUser(user);
        testCourseTaken.setRequestType(courseTakenDto.getRequestType());
        testCourseTaken.setRequestDetail(courseTakenDto.getRequestDetail());

        var result = courseTakenService.postCourseTaken(courseTakenDto);

        assertEquals(testCourseTaken, result);
    }

    @Test
    void postCourseTakenSameSpecializationSuccessTest() {
        when(courseRepository.findById(courseTakenDto.getCourseId()))
            .thenReturn(Optional.of(course));

        when(userRepository.findUsername(courseTakenDto.getEmail()))
            .thenReturn(user);

        List<CourseTaken> testListCourseTaken = EASY_RANDOM.objects(CourseTaken.class, 2)
                                                    .collect(Collectors.toList());

        when(courseTakenRepository.findCourseTakenByCourseUser(course.getId(), user.getId()))
            .thenReturn(testListCourseTaken);

        testListCourseTaken.forEach(testList -> {
            if(testList.getRequestType() == RequestEnum.COURSE) {
                courseTakenDto.setRequestType(RequestEnum.TRAINING);
            }  else {
                courseTakenDto.setRequestType(RequestEnum.COURSE);
            }

        });

        CourseTaken testCourseTaken = new CourseTaken();

        testCourseTaken.setCourseTake(course);
        testCourseTaken.setUser(user);
        testCourseTaken.setRequestType(courseTakenDto.getRequestType());
        testCourseTaken.setRequestDetail(courseTakenDto.getRequestDetail());

        user.setUserSpecialization(course.getCourseSpecialization());

        testCourseTaken.setStatus(StatusEnum.ACCEPTED);
        testCourseTaken.setRequestDetail("SAME SPECIALIZATION");

        var result = courseTakenService.postCourseTaken(courseTakenDto);
        testCourseTaken.setCertificateCode(result.getCertificateCode());

        testCourseTaken.setTakenAt(result.getTakenAt());

        assertEquals(testCourseTaken, result);
    }

    @Test
    void postCourseTakenExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            courseTakenService.postCourseTaken(courseTakenDto);
        });
    }

    @Test
    void postCourseTakenException2Test() {
        when(courseRepository.findById(courseTakenDto.getCourseId()))
            .thenReturn(Optional.of(course));

        when(userRepository.findUsername(courseTakenDto.getEmail()))
            .thenReturn(user);

        when(courseTakenRepository.findCourseTakenByCourseUser(course.getId(), user.getId()))
            .thenReturn(courseTakens);

        courseTakens.forEach(courseTaken -> {
            courseTaken.setRequestType(courseTakenDto.getRequestType());
            message = courseTakenDto.getRequestType() + " " + courseTaken.getCourseTake().getName() + " IS ALREADY TAKEN BY USER " + courseTakenDto.getEmail();
        });

        assertThrows(RuntimeException.class, () -> {
            courseTakenService.postCourseTaken(courseTakenDto);
        });
    }

    @Test
    void updateRatingSuccessTest() {
        when(courseTakenRepository.findById(courseTaken.getId()))
            .thenReturn(Optional.of(courseTaken));

        courseTaken.setStatus(StatusEnum.ACCEPTED);

        courseTaken.setUser(user);

        when(courseRepository.findById(courseTaken.getCourseTake().getId()))
            .thenReturn(Optional.of(course));

        Integer rateInCourseTaken = 4;

        when(courseTakenRepository.countRateByCourse(course.getId()))
            .thenReturn(rateInCourseTaken);

        var result = courseTakenService.updateRating(user.getUsername(), rateCourse);

        assertEquals(courseTaken, result);
    }

    @Test
    void updateRatingSuccess2Test() {
        when(courseTakenRepository.findById(courseTaken.getId()))
            .thenReturn(Optional.of(courseTaken));

        courseTaken.setStatus(StatusEnum.ACCEPTED);

        courseTaken.setUser(user);

        when(courseRepository.findById(courseTaken.getCourseTake().getId()))
            .thenReturn(Optional.of(course));

        var result = courseTakenService.updateRating(user.getUsername(), rateCourse);

        assertEquals(courseTaken, result);
    }


    @Test
    void updateRatingExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            courseTakenService.updateRating(user.getUsername(), rateCourse);
        });
    }

    @Test
    void updateRatingException2Test() {
        when(courseTakenRepository.findById(courseTaken.getId()))
            .thenReturn(Optional.of(courseTaken));

        courseTaken.setStatus(StatusEnum.PENDING);

        assertThrows(RuntimeException.class, () -> {
            courseTakenService.updateRating(user.getUsername(), rateCourse);
        });
    }

    @Test
    void updateRatingException3Test() {
        when(courseTakenRepository.findById(courseTaken.getId()))
            .thenReturn(Optional.of(courseTaken));

        courseTaken.setStatus(StatusEnum.ACCEPTED);

        User randomUser = EASY_RANDOM.nextObject(User.class);

        courseTaken.setUser(randomUser);

        assertThrows(RuntimeException.class, () -> {
            courseTakenService.updateRating(user.getUsername(), rateCourse);
        });
    }

    @Test
    void updateRatingException4Test() {
        when(courseTakenRepository.findById(courseTaken.getId()))
            .thenReturn(Optional.of(courseTaken));

        courseTaken.setStatus(StatusEnum.ACCEPTED);

        courseTaken.setUser(user);

        assertThrows(RuntimeException.class, () -> {
            courseTakenService.updateRating(user.getUsername(), rateCourse);
        });
    }

    @Test
    void updateProgressUserSuccessTest() {
        totalMaterial = 0;

        when(courseTakenRepository.findById(courseTaken.getId()))
            .thenReturn(Optional.of(courseTaken));

        courseTaken.setStatus(StatusEnum.ACCEPTED);

        courseTaken.setUser(user);
        when(userRepository.findUsername(courseTakenDto.getEmail()))
            .thenReturn(user);

        user.getRoles().forEach(role -> {
            role.setName(RoleEnum.ROLE_USER);
        });

        List<Section> sections = courseTaken.getCourseTake().getSections();

        sections.forEach(section -> {
            List<Material> materials = section.getMaterials();
            materials.forEach(cMaterial -> {
                cMaterial.setId(courseTakenDto.getMaterialId());
                if(cMaterial.getId().equals(courseTakenDto.getMaterialId())) {
                    material = cMaterial;
                    check = true;
                }
                cMaterial.setDeleted(false);
                if(cMaterial.getDeleted() == false) 
                    totalMaterial++;
            });
        });

        Report report = new Report();

        when(reportRepository.getReport(courseTaken.getId(), material.getId()))
            .thenReturn(report);
    
        var result = courseTakenService.updateProgress(courseTaken.getId(), courseTakenDto);

        assertEquals(courseTaken, result);
    }

    @Test
    void updateProgressAdminSuccessTest() {
        totalMaterial = 0;

        when(courseTakenRepository.findById(courseTaken.getId()))
            .thenReturn(Optional.of(courseTaken));

        courseTaken.setStatus(StatusEnum.ACCEPTED);

        courseTaken.setUser(user);
        when(userRepository.findUsername(courseTakenDto.getEmail()))
            .thenReturn(user);

        user.getRoles().forEach(role -> {
            role.setName(RoleEnum.ROLE_ADMIN);
        });

        List<Section> sections = courseTaken.getCourseTake().getSections();

        sections.forEach(section -> {
            List<Material> materials = section.getMaterials();
            materials.forEach(cMaterial -> {
                cMaterial.setId(courseTakenDto.getMaterialId());
                if(cMaterial.getId().equals(courseTakenDto.getMaterialId())) {
                    material = cMaterial;
                    check = true;
                }
                cMaterial.setDeleted(false);
                if(cMaterial.getDeleted() == false) 
                    totalMaterial++;
            });
        });

        Report report = new Report();

        when(reportRepository.getReport(courseTaken.getId(), material.getId()))
            .thenReturn(report);

        report.setCompleted(true);
    
        var result = courseTakenService.updateProgress(courseTaken.getId(), courseTakenDto);

        assertEquals(courseTaken, result);
    }

    @Test
    void updateProgressExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            courseTakenService.updateProgress(courseTaken.getId(), courseTakenDto);
        });
    }

    @Test
    void updateProgressException2Test() {
        when(courseTakenRepository.findById(courseTaken.getId()))
            .thenReturn(Optional.of(courseTaken));

        courseTaken.setStatus(StatusEnum.PENDING);
        
        assertThrows(RuntimeException.class, () -> {
            courseTakenService.updateProgress(courseTaken.getId(), courseTakenDto);
        });
    }

    @Test
    void updateProgressException3Test() {
        when(courseTakenRepository.findById(courseTaken.getId()))
            .thenReturn(Optional.of(courseTaken));

        courseTaken.setStatus(StatusEnum.ACCEPTED);

        when(userRepository.findUsername(courseTakenDto.getEmail()))
            .thenReturn(user);

        user.getRoles().forEach(role -> {
            role.setName(RoleEnum.ROLE_USER);
        });

        assertThrows(RuntimeException.class, () -> {
            courseTakenService.updateProgress(courseTaken.getId(), courseTakenDto);
        });
    }

    @Test
    void updateProgressException4Test() {
        when(courseTakenRepository.findById(courseTaken.getId()))
            .thenReturn(Optional.of(courseTaken));

        courseTaken.setStatus(StatusEnum.ACCEPTED);

        when(userRepository.findUsername(courseTakenDto.getEmail()))
            .thenReturn(user);

        user.getRoles().forEach(role -> {
            role.setName(RoleEnum.ROLE_ADMIN);
        });

        List<Section> sections = courseTaken.getCourseTake().getSections();

        sections.forEach(section -> {
            List<Material> materials = section.getMaterials();
            materials.forEach(cMaterial -> {
                cMaterial.setId(courseTakenDto.getMaterialId());
                if(cMaterial.getId().equals(courseTakenDto.getMaterialId())) {
                    material = cMaterial;
                    check = true;
                }
                cMaterial.setDeleted(true);
            });
        });

        assertThrows(RuntimeException.class, () -> {
            courseTakenService.updateProgress(courseTaken.getId(), courseTakenDto);
        });
    }

    @Test
    void updateProgressException5Test() {
        when(courseTakenRepository.findById(courseTaken.getId()))
            .thenReturn(Optional.of(courseTaken));

        courseTaken.setStatus(StatusEnum.ACCEPTED);

        when(userRepository.findUsername(courseTakenDto.getEmail()))
            .thenReturn(user);

        user.getRoles().forEach(role -> {
            role.setName(RoleEnum.ROLE_ADMIN);
        });

        check = false;

        assertThrows(RuntimeException.class, () -> {
            courseTakenService.updateProgress(courseTaken.getId(), courseTakenDto);
        });
    }

    @Test
    void updateProgressException6Test() {
        totalMaterial = 0;

        when(courseTakenRepository.findById(courseTaken.getId()))
            .thenReturn(Optional.of(courseTaken));

        courseTaken.setStatus(StatusEnum.ACCEPTED);

        courseTaken.setUser(user);
        when(userRepository.findUsername(courseTakenDto.getEmail()))
            .thenReturn(user);

        user.getRoles().forEach(role -> {
            role.setName(RoleEnum.ROLE_ADMIN);
        });

        List<Section> sections = courseTaken.getCourseTake().getSections();

        sections.forEach(section -> {
            List<Material> materials = section.getMaterials();
            materials.forEach(cMaterial -> {
                cMaterial.setId(courseTakenDto.getMaterialId());
                if(cMaterial.getId().equals(courseTakenDto.getMaterialId())) {
                    material = cMaterial;
                    check = true;
                }
                cMaterial.setDeleted(false);
                if(cMaterial.getDeleted() == false) 
                    totalMaterial++;
            });
        });

        Report report = new Report();

        when(reportRepository.getReport(courseTaken.getId(), material.getId()))
            .thenReturn(report);
        
        report.setCompleted(false);

        assertThrows(RuntimeException.class, () -> {
            courseTakenService.updateProgress(courseTaken.getId(), courseTakenDto);
        });
    }

    @Test
    void getCourseTakenByCertificateCodeSuccessTest() {
        when(courseTakenRepository.findCourseTakenByCertificateCode(courseTaken.getCertificateCode().toUpperCase()))
            .thenReturn(Optional.of(courseTaken));

        courseTaken.setUser(user);

        var result = courseTakenService.getCourseTakenByCertificateCode(courseTaken.getCertificateCode().toUpperCase(), user.getUsername());

        assertEquals(courseTaken, result);
    }

    @Test
    void getCourseTakenByCertificateCodeExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            courseTakenService.getCourseTakenByCertificateCode(courseTaken.getCertificateCode().toUpperCase(), user.getUsername());
        });
    } 

    @Test
    void getCourseTakenByCertificateCodeException2Test() {
        when(courseTakenRepository.findCourseTakenByCertificateCode(courseTaken.getCertificateCode().toUpperCase()))
            .thenReturn(Optional.of(courseTaken));
            
        assertThrows(RuntimeException.class, () -> {
            courseTakenService.getCourseTakenByCertificateCode(courseTaken.getCertificateCode().toUpperCase(), user.getUsername());
        });
    } 
}
