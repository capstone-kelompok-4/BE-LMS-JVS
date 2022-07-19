package com.alterra.capstoneproject.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alterra.capstoneproject.domain.dao.Course;
import com.alterra.capstoneproject.domain.dao.CourseTaken;
import com.alterra.capstoneproject.domain.dao.Material;
import com.alterra.capstoneproject.domain.dao.Report;
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

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportService reportService;

    private Material material;
    private Boolean check, checkAdmin;
    private Integer totalMaterial;
    private String message;

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

    public CourseTaken getCourseTaken(Long id, String username) {
        try {
            log.info("Get course taken by id");
            CourseTaken courseTaken = courseTakenRepository.findById(id)
                .orElseThrow(() -> new Exception("COURSE TAKEN ID " + id + " NOT FOUND"));

            if(username.equals(courseTaken.getUser().getUsername())) {
                courseTaken.setLastAccessCourse(LocalDateTime.now());
            }

            return courseTaken;
        } catch (Exception e) {
            log.error("Get course taken by id error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public CourseTaken postCourseTaken(CourseTakenDto request) {
        try {
            message = "";
            log.info("Get course");
            Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new Exception("COURSE ID " + request.getCourseId() + " NOT FOUND"));

            log.info("Get user");
            User user = userRepository.findUsername(request.getEmail());

            log.info("Check course taken");
            List<CourseTaken> courseTakens = courseTakenRepository.findCourseTakenByCourseUser(request.getCourseId(), user.getId());

            if(!courseTakens.isEmpty()) {
                courseTakens.forEach(courseTake -> {
                    if(courseTake.getRequestType().equals(request.getRequestType())) {
                        message = request.getRequestType() + " " + courseTake.getCourseTake().getName() + " IS ALREADY TAKEN BY USER " + request.getEmail();
                    }
                });
            }

            if(!message.isBlank()) throw new Exception(message);

            log.info("Post course taken");
            CourseTaken courseTaken = new CourseTaken();

            courseTaken.setCourseTake(course);
            courseTaken.setUser(user);
            courseTaken.setRequestType(request.getRequestType());
            courseTaken.setRequestDetail(request.getRequestDetail());
            if(course.getCourseSpecialization().equals(user.getUserSpecialization())) {
                courseTaken.setTakenAt(LocalDateTime.now());
                courseTaken.setStatus(StatusEnum.ACCEPTED);
                courseTaken.setRequestDetail("SAME SPECIALIZATION");
                courseTaken.setCertificateCode(request.getCertificateCode().toUpperCase());

                log.info("Add report");
                reportService.postReport(courseTaken);
            }

            courseTakenRepository.save(courseTaken);
           
            return courseTaken;
        } catch (Exception e) {
            log.error("Post course taken error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void deleteCourseTaken(Long id) {
        try {
            CourseTaken courseTaken = courseTakenRepository.findById(id)
                .orElseThrow(() -> new Exception("COURSE TAKEN ID " + id + " NOT FOUND"));

            courseTakenRepository.deleteById(id);

            log.info("Update rate in course");

            log.info("Get course by id");
            Course course = courseRepository.findById(courseTaken.getCourseTake().getId())
                .orElseThrow(() -> new Exception("COURSE ID " + courseTaken.getCourseTake().getId() + " NOT FOUND"));


            log.info("Save rate course");
            Integer rateInCourseTaken = courseTakenRepository.countRateByCourse(course.getId());
            
            if(rateInCourseTaken >= 1) {
                Double sumRate = courseTakenRepository.sumRateByCourse(course.getId());
                Double rateCourse = sumRate/rateInCourseTaken;
                course.setRate(rateCourse);
                courseRepository.save(course);
            }else {
                course.setRate(0.0);
                courseRepository.save(course);
            }
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

            if(courseTakens.isEmpty()) throw new Exception("COURSE TAKEN BY COURSE " + id + " IS EMPTY");

            return courseTakens;
        } catch (Exception e) {
            log.error("Get course taken by course error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public CourseTaken updateRating(String email, RateCourse request) {
        try {
            log.info("Get course taken by id");
            CourseTaken courseTaken = courseTakenRepository.findById(request.getCourseTakenId())
                .orElseThrow(() -> new Exception("COURSE TAKEN ID " + request.getCourseTakenId() + " NOT FOUND"));

            if(courseTaken.getStatus() != StatusEnum.ACCEPTED) 
                throw new Exception("COURSE TAKEN ID " + request.getCourseTakenId() +" IS " + courseTaken.getStatus());

            log.info("Check user with the course taken user");            
            if(!courseTaken.getUser().getUsername().equals(email)) 
                throw new Exception("COURSE TAKEN ID " + request.getCourseTakenId() +" WITH EMAIL " + email + " NOT FOUND");

            log.info("Update rate in course taken");
            
            courseTaken.setRate(request.getRate());

            courseTakenRepository.save(courseTaken);

            log.info("Update rate in course");

            log.info("Get course by id");
            Course course = courseRepository.findById(courseTaken.getCourseTake().getId())
                .orElseThrow(() -> new Exception("COURSE ID " + courseTaken.getCourseTake().getId() + " NOT FOUND"));


            log.info("Save rate course");
            Integer rateInCourseTaken = courseTakenRepository.countRateByCourse(course.getId());

            if(rateInCourseTaken > 1) {
                Double sumRate = courseTakenRepository.sumRateByCourse(course.getId());
                Double rateCourse = sumRate/rateInCourseTaken;
                course.setRate(rateCourse);
                courseRepository.save(course);
            }else {
                course.setRate(request.getRate());
                courseRepository.save(course);
            }

            return courseTaken;
        } catch (Exception e) {
            log.error("Update rating course taken error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public CourseTaken updateProgress(Long id, CourseTakenDto request) {
        try {
            check = false; totalMaterial = 0; checkAdmin = false;

            log.info("Get course taken by id");
            CourseTaken courseTaken = courseTakenRepository.findById(id)
                .orElseThrow(() -> new Exception("COURSE TAKEN ID " + id + " NOT FOUND"));

            if(courseTaken.getStatus() != StatusEnum.ACCEPTED) 
                throw new Exception("COURSE TAKEN ID " + id +" IS " + courseTaken.getStatus());

            log.info("Check user with the course taken user {}", request.getEmail());   
            User user = userRepository.findUsername(request.getEmail());
            user.getRoles().forEach(checkRole -> {
                if(checkRole.getName().equals(RoleEnum.ROLE_ADMIN)) {
                    checkAdmin = true;
                }
            });  
            
            if(checkAdmin == false) {
                if(!courseTaken.getUser().getUsername().equals(request.getEmail())) 
                    throw new Exception("COURSE TAKEN ID " + id +" WITH EMAIL " + request.getEmail() + " NOT FOUND");
            }

            log.info("Get section course by course taken");
            List<Section> sections = courseTaken.getCourseTake().getSections();

            sections.forEach(checkSection -> {
                List<Material> materials = checkSection.getMaterials();
                materials.forEach(checkMaterial -> {
                    if(checkMaterial.getId().equals(request.getMaterialId())) {
                        material = checkMaterial;
                        check = true;
                    }
                    if(checkMaterial.getDeleted() == false) totalMaterial++; 
                });
            });

            log.info("check {}", check);
            if(!check || material.getDeleted() == true) 
                throw new Exception("MATERIAL ID " + request.getMaterialId() + " NOT FOUND");

            log.info("Update report");            
            Report report = reportRepository.getReport(id, request.getMaterialId());

            if(checkAdmin == false) {
                report.setCompleted(true);
            } else {
                if(report.getCompleted() == true) {
                    report.setScore(request.getScore());
                } else {
                    throw new Exception("PROGRESS IS NOT COMPLETED");
                }
            }

            reportRepository.save(report);

            Integer completedCourse = reportRepository.findByCompleted(id).size();            

            Integer progress = (completedCourse*100)/totalMaterial;

            courseTaken.setProgress(progress);

            courseTakenRepository.save(courseTaken);
            
            return courseTaken;
        } catch (Exception e) {
            log.error("Update report course taken error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public CourseTaken updateStatus(Long id, StatusCourse request) {
        try {
            log.info("Get course taken by id");
            CourseTaken courseTaken = courseTakenRepository.findById(id)
                .orElseThrow(() -> new Exception("COURSE TAKEN ID " + id + " NOT FOUND"));

            if(courseTaken.getStatus() != StatusEnum.PENDING)
                throw new Exception("COURSE TAKEN ID " + id + " IS ALREADY " + courseTaken.getStatus());

            if(request.getStatus() == StatusEnum.PENDING) 
                throw new Exception("CAN NOT UPDATE STATUS OF COURSE TAKEN ID " + id);

            log.info("Update status course taken");
            
            courseTaken.setStatus(request.getStatus());

            log.info("Check if status request is accepted");
            if(request.getStatus() == StatusEnum.ACCEPTED) {
                courseTaken.setTakenAt(LocalDateTime.now());
                courseTaken.setCertificateCode(request.getCertificateCode().toUpperCase());

                log.info("Add report");
                reportService.postReport(courseTaken);
            } 
            courseTakenRepository.save(courseTaken);

            return courseTaken;
        } catch (Exception e) {
            log.error("Update status course taken error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public CourseTaken getCourseTakenByCertificateCode(String code, String username) {
        try {
            log.info("Get course taken certificate by certificate code");
            CourseTaken courseTaken = courseTakenRepository.findCourseTakenByCertificateCode(code.toUpperCase())
                .orElseThrow(() -> new Exception("CERTIFICATE CODE " + code + " IS NOT FOUND"));

            log.info("Check username with the certificate");
            if(!username.equals(courseTaken.getUser().getUsername())) {
                throw new Exception("CERTIFICATE CODE IS NOT VALID FOR USER " + username);
            }

            return courseTaken;
        } catch (Exception e) {
            log.error("Get course taken by certificate code error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
