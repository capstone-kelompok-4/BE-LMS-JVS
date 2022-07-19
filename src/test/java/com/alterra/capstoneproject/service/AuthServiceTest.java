package com.alterra.capstoneproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.alterra.capstoneproject.domain.dao.Role;
import com.alterra.capstoneproject.domain.dao.RoleEnum;
import com.alterra.capstoneproject.domain.dao.Specialization;
import com.alterra.capstoneproject.domain.dao.User;
import com.alterra.capstoneproject.domain.dto.ChangePassword;
import com.alterra.capstoneproject.domain.dto.Login;
import com.alterra.capstoneproject.domain.dto.Register;
import com.alterra.capstoneproject.domain.dto.TokenResponse;
import com.alterra.capstoneproject.repository.CourseRepository;
import com.alterra.capstoneproject.repository.CourseTakenRepository;
import com.alterra.capstoneproject.repository.RoleRepository;
import com.alterra.capstoneproject.repository.SpecializationRepository;
import com.alterra.capstoneproject.repository.UserRepository;
import com.alterra.capstoneproject.security.JwtTokenProvider;

@SpringBootTest(classes = AuthService.class)
public class AuthServiceTest {
    private final EasyRandom EASY_RANDOM = new EasyRandom();
    private User user;
    private Register register;
    private Login login;
    private List<User> users;
    private ChangePassword changePassword;
    private Specialization specialization;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private CourseTakenRepository courseTakenRepository;

    @MockBean
    private SpecializationRepository specializationRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AddressService addressService;
    
    @Autowired
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = EASY_RANDOM.nextObject(User.class);
        register = EASY_RANDOM.nextObject(Register.class);
        login = EASY_RANDOM.nextObject(Login.class);
        users = EASY_RANDOM.objects(User.class, 2)
                .collect(Collectors.toList());
        specialization = EASY_RANDOM.nextObject(Specialization.class);
        changePassword = EASY_RANDOM.nextObject(ChangePassword.class);
    }

    @Test
    void registerUserSuccessTest() {
        User testUser = new User();

        testUser.setName(register.getName());
        testUser.setUsername(register.getEmail().toLowerCase());
        testUser.setPassword(passwordEncoder.encode(register.getPassword()));
        testUser.setImageUrl(register.getImageUrl());
        testUser.setPhoneNumber(register.getPhoneNumber());

        register.setRoles(null);

        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        when(roleRepository.findByName(RoleEnum.ROLE_USER))
            .thenReturn(Optional.of(role));
        
        roles.add(role);

        testUser.setRoles(roles);

        register.setSpecializationId(specialization.getId());

        when(specializationRepository.findById(specialization.getId()))
            .thenReturn(Optional.of(specialization));

        testUser.setUserSpecialization(specialization);

        var result = authService.register(register);

        assertEquals(testUser, result);
    }

    @Test
    void registerAdminSuccessTest() {
        User testUser = new User();

        testUser.setName(register.getName());
        testUser.setUsername(register.getEmail().toLowerCase());
        testUser.setPassword(passwordEncoder.encode(register.getPassword()));
        testUser.setImageUrl(register.getImageUrl());
        testUser.setPhoneNumber(register.getPhoneNumber());

        List<RoleEnum> listRoles = new ArrayList<>();
        listRoles.add(RoleEnum.ROLE_ADMIN);
        register.setRoles(listRoles);

        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        register.getRoles().forEach(registerRole -> {
            when(roleRepository.findByName(registerRole))
                .thenReturn(Optional.of(role));
        
            roles.add(role);
        });

        testUser.setRoles(roles);

        register.setSpecializationId(null);

        var result = authService.register(register);

        assertEquals(testUser, result);
    }

    @Test
    void registerExceptionTest() {
        when(userRepository.findUsername(register.getEmail()))
            .thenReturn(user);

        assertThrows(RuntimeException.class, () -> {
            authService.register(register);
        });
    }

    @Test
    void registerException2Test() {
        register.setPassword("");

        assertThrows(RuntimeException.class, () -> {
            authService.register(register);
        });
    }

    @Test
    void registerException3Test() {
        register.setPassword("cpk bgt");

        assertThrows(RuntimeException.class, () -> {
            authService.register(register);
        });
    }

    @Test
    void registerException4Test() {
        register.setPassword("cpk_bgt");

        assertThrows(RuntimeException.class, () -> {
            authService.register(register);
        });
    }

    @Test
    void registerException5Test() {
        User testUser = new User();

        testUser.setName(register.getName());
        testUser.setUsername(register.getEmail().toLowerCase());
        testUser.setPassword(passwordEncoder.encode(register.getPassword()));
        testUser.setImageUrl(register.getImageUrl());
        testUser.setPhoneNumber(register.getPhoneNumber());

        register.setRoles(null);

        assertThrows(RuntimeException.class, () -> {
            authService.register(register);
        });
    }

    @Test
    void registerException6Test() {
        User testUser = new User();

        testUser.setName(register.getName());
        testUser.setUsername(register.getEmail().toLowerCase());
        testUser.setPassword(passwordEncoder.encode(register.getPassword()));
        testUser.setImageUrl(register.getImageUrl());
        testUser.setPhoneNumber(register.getPhoneNumber());

        List<RoleEnum> listRoles = new ArrayList<>();
        listRoles.add(RoleEnum.ROLE_ADMIN);
        register.setRoles(listRoles);

        register.getRoles().forEach(registerRole -> {
            assertThrows(RuntimeException.class, () -> {
                authService.register(register);
            });
        });
    }

    @Test
    void registerException7Test() {
        User testUser = new User();

        testUser.setName(register.getName());
        testUser.setUsername(register.getEmail().toLowerCase());
        testUser.setPassword(passwordEncoder.encode(register.getPassword()));
        testUser.setImageUrl(register.getImageUrl());
        testUser.setPhoneNumber(register.getPhoneNumber());

        register.setRoles(null);

        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        when(roleRepository.findByName(RoleEnum.ROLE_USER))
            .thenReturn(Optional.of(role));
        
        roles.add(role);

        testUser.setRoles(roles);

        register.setSpecializationId(specialization.getId());

        assertThrows(RuntimeException.class, () -> {
            authService.register(register);
        });
    }

    @Test
    void loginSuccessTest() {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(login.getEmail().toLowerCase(), login.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);

        when(userRepository.findUsername(login.getEmail().toLowerCase()))
            .thenReturn(user);

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken(jwt);

        var result = authService.generateToken(login);

        assertEquals(tokenResponse, result);
    }

    @Test
    void loginExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            authService.generateToken(login);
        });
    }

    @Test
    void getUsersSuccessTest() {
        when(userRepository.findAll())
            .thenReturn(users);

        var result = authService.getUsers();

        assertEquals(users, result);
    }

    @Test
    void getUsersExceptionTest() {
        assertThrows(RuntimeException.class, () ->{
            authService.getUsers();
        });
    }

    @Test
    void getUserIdSuccessTest() {
        when(userRepository.findById(user.getId()))
            .thenReturn(Optional.of(user));

        var result = authService.getUser(user.getId());

        assertEquals(user, result);
    }

    @Test
    void getUserIdExceptionTest() {
        assertThrows(RuntimeException.class, () ->{
            authService.getUser(user.getId());
        });
    }

    @Test
    void getUserUsernameSuccessTest() {
        when(userRepository.findByUsername(user.getUsername()))
            .thenReturn(Optional.of(user));

        var result = authService.getUser(user.getUsername());

        assertEquals(user, result);
    }

    @Test
    void getUserUsernameExceptionTest() {
        assertThrows(RuntimeException.class, () ->{
            authService.getUser(user.getUsername());
        });
    }

    @Test
    void updateUserSuccessTest() {
        when(userRepository.findByUsername(register.getEmail()))
            .thenReturn(Optional.of(user));

        register.setSpecializationId(specialization.getId());

        when(specializationRepository.findById(register.getSpecializationId()))
            .thenReturn(Optional.of(specialization));

        var result = authService.updateUser(register);

        assertEquals(user, result);
    }  

    @Test
    void updateUserSpecializationNullTest() {
        when(userRepository.findByUsername(register.getEmail()))
            .thenReturn(Optional.of(user));

        register.setSpecializationId(null);

        var result = authService.updateUser(register);

        assertEquals(user, result);
    }

    @Test
    void updateUserExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            authService.updateUser(register);
        });
    }

    @Test
    void updateUserException2Test() {
        when(userRepository.findByUsername(register.getEmail()))
            .thenReturn(Optional.of(user));

        register.setSpecializationId(specialization.getId());

        assertThrows(RuntimeException.class, () -> {
            authService.updateUser(register);
        });
    }

    @Bean public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Test
    void changePasswordSuccessTest() {
        when(userRepository.findByUsername(changePassword.getEmail()))
            .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(changePassword.getCurrentPassword(), user.getPassword()))
            .thenReturn(true);

        var result = authService.changePassword(changePassword);

        assertEquals(user, result);
    }

    @Test
    void changePasswordExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            authService.changePassword(changePassword);
        });
    }

    @Test
    void changePasswordException2Test() {
        when(userRepository.findByUsername(changePassword.getEmail()))
            .thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> {
            authService.changePassword(changePassword);
        });
    }

    @Test
    void changePasswordException3Test() {
        when(userRepository.findByUsername(changePassword.getEmail()))
            .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(changePassword.getCurrentPassword(), user.getPassword()))
            .thenReturn(true);

        changePassword.setNewPassword("");

        assertThrows(RuntimeException.class, () -> {
            authService.changePassword(changePassword);
        });
    }

    @Test
    void changePasswordException4Test() {
        when(userRepository.findByUsername(changePassword.getEmail()))
            .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(changePassword.getCurrentPassword(), user.getPassword()))
            .thenReturn(true);

        changePassword.setNewPassword("cpk bgt");

        assertThrows(RuntimeException.class, () -> {
            authService.changePassword(changePassword);
        });
    }

    @Test
    void changePasswordException5Test() {
        when(userRepository.findByUsername(changePassword.getEmail()))
            .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(changePassword.getCurrentPassword(), user.getPassword()))
            .thenReturn(true);

        changePassword.setNewPassword("cpk_bgt");

        assertThrows(RuntimeException.class, () -> {
            authService.changePassword(changePassword);
        });
    }

    @Test
    void deleteUserSuccessTest() {
        when(userRepository.findById(user.getId()))
            .thenReturn(Optional.of(user));

        user.getRoles().forEach(role -> {
            if(role.getName().equals(RoleEnum.ROLE_ADMIN))
                role.setName(RoleEnum.ROLE_USER);
        });

        authService.deleteUser(user.getId());

        verify(userRepository).deleteById(user.getId());
    }

    @Test
    void deleteUserExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            authService.deleteUser(user.getId());
        });
    }

    @Test
    void deleteUserException2Test() {
        when(userRepository.findById(user.getId()))
            .thenReturn(Optional.of(user));

        user.getRoles().forEach(role -> {
            if(role.getName().equals(RoleEnum.ROLE_USER)) {
                role.setName(RoleEnum.ROLE_ADMIN);
            }
        });

        assertThrows(RuntimeException.class, () -> {
            authService.deleteUser(user.getId());
        });
    }
}
