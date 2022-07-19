package com.alterra.capstoneproject.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alterra.capstoneproject.domain.dao.Address;
import com.alterra.capstoneproject.domain.dao.Course;
import com.alterra.capstoneproject.domain.dao.Role;
import com.alterra.capstoneproject.domain.dao.RoleEnum;
import com.alterra.capstoneproject.domain.dao.Specialization;
import com.alterra.capstoneproject.domain.dao.User;
import com.alterra.capstoneproject.domain.dto.TokenResponse;
import com.alterra.capstoneproject.domain.dto.ChangePassword;
import com.alterra.capstoneproject.domain.dto.Login;
import com.alterra.capstoneproject.domain.dto.Register;
import com.alterra.capstoneproject.repository.CourseRepository;
import com.alterra.capstoneproject.repository.CourseTakenRepository;
import com.alterra.capstoneproject.repository.RoleRepository;
import com.alterra.capstoneproject.repository.SpecializationRepository;
import com.alterra.capstoneproject.repository.UserRepository;
import com.alterra.capstoneproject.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AddressService addressService;
    private final CourseRepository courseRepository;
    private final CourseTakenRepository courseTakenRepository;
    private final SpecializationRepository specializationRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private Boolean check;

    public User register(Register req) {
        try {
            log.info("Search username in database");
            if (userRepository.findUsername(req.getEmail()) != null) {
                throw new Exception("USER WITH EMAIL " + req.getEmail() + " IS ALREADY EXIST");
            }

            if(req.getPassword().isBlank() || req.getPassword().contains(" ") || req.getPassword().length() < 8) 
                throw new Exception("INVALID PASSWORD");         

            log.info("Post user");
            User user = new User();

            user.setName(req.getName());
            user.setUsername(req.getEmail().toLowerCase());   
            user.setPassword(passwordEncoder.encode(req.getPassword()));
            user.setImageUrl(req.getImageUrl());
            user.setPhoneNumber(req.getPhoneNumber());

            Set<Role> roles = new HashSet<>();
            if(req.getRoles() == null) {
                Role role = roleRepository.findByName(RoleEnum.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("ROLE NOT FOUND"));
                
                roles.add(role);
            }else {
                req.getRoles().forEach(inputRole -> {
                    Role role = roleRepository.findByName(inputRole)
                        .orElseThrow(() -> new RuntimeException("ROLE NOT FOUND"));
                    roles.add(role);
                });
            }
            user.setRoles(roles);

            if(req.getSpecializationId() != null) {
                Specialization specialization = specializationRepository.findById(req.getSpecializationId())
                .orElseThrow(() -> new RuntimeException("SPECIALIZATION NOT FOUND"));
                 user.setUserSpecialization(specialization);
            }          

            log.info("Setting address"); 
            addressService.postAddress(user, req.getAddress());            

            log.info("User {} saved", req.getEmail());
            return user;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public TokenResponse generateToken(Login req) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail().toLowerCase(), req.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtTokenProvider.generateToken(authentication);

            User user = userRepository.findUsername(req.getEmail().toLowerCase());

            user.setLastAccess(LocalDateTime.now());

            userRepository.save(user);

            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setToken(jwt);
            log.info("Token created");
            return tokenResponse;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public List<User> getUsers() {
        try {
            log.info("Get all user");
            List<User> users = userRepository.findAll();

            if(users.isEmpty()) throw new Exception("USER IS EMPTY");

            return users;
        } catch (Exception e) {
            log.error("Get all user error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public User getUser(Long id) {
        try {
            log.info("Get user");
            User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("USER ID " + id + " NOT FOUND"));

            return user;
        } catch (Exception e) {
            log.error("Get user error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public User getUser(String email) {
        try {
            log.info("Get user");
            User user = userRepository.findByUsername(email)
                .orElseThrow(() -> new Exception("USER WITH EMAIL " + email + " NOT FOUND"));

            return user;
        } catch (Exception e) {
            log.error("Get user error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public User updateUser(Register request) {
        try {
            log.info("Get user");
            User user = userRepository.findByUsername(request.getEmail())
                .orElseThrow(() -> new Exception("USER WITH EMAIL " + request.getEmail() + " NOT FOUND"));

            log.info("Update user");

            user.setName(request.getName());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setImageUrl(request.getImageUrl());

            log.info("Update user address");
            request.getAddress().setEmail(request.getEmail());
            Address address = addressService.updateAddress(request.getAddress());
            
            user.setAddress(address);

            if(request.getSpecializationId() != null) {
                Specialization specialization = specializationRepository.findById(request.getSpecializationId())
                    .orElseThrow(() -> new RuntimeException("SPECIALIZATION NOT FOUND"));
                user.setUserSpecialization(specialization);
            }

            log.info("Save user");
            userRepository.save(user);

            return user;
        } catch (Exception e) {
            log.error("Update user error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public User changePassword(ChangePassword request) {
        try {
            log.info("Get user");
            User user = userRepository.findByUsername(request.getEmail())
                .orElseThrow(() -> new Exception("USER WITH EMAIL " + request.getEmail() + " NOT FOUND"));

            log.info("Check password in database");
            Boolean isMatch = passwordEncoder.matches(request.getCurrentPassword(), user.getPassword());

            if(!isMatch) throw new Exception("PASSWORD DID NOT MATCH");

            log.info("Check new password");
            if(request.getNewPassword().isBlank() || request.getNewPassword().contains(" ") || request.getNewPassword().length() < 8) 
                throw new Exception("INVALID PASSWORD");    

            log.info("Save new password");
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));

            userRepository.save(user);

            return user;
        } catch (Exception e) {
            log.error("Change password error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void deleteUser(Long id) {
        try {
            log.info("Check user");
            check = false;
            User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("USER WITH ID " + id + " NOT FOUND"));

            user.getRoles().forEach(role -> {
                if(role.getName().equals(RoleEnum.ROLE_ADMIN)) check = true;
            });

            if(check == true) throw new Exception("CAN NOT DELETE ADMIN");

            log.info("Delete user");
            userRepository.deleteById(id);
            
            log.info("Update rate in course");
            List<Course> courses = courseRepository.findAll();

            log.info("courses size {}", courses.size());
            courses.forEach(course -> {
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
            });
        } catch (Exception e) {
            log.error("Delete user error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
