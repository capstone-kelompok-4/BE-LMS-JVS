package com.alterra.capstoneproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.alterra.capstoneproject.domain.dao.User;
import com.alterra.capstoneproject.repository.UserRepository;

@SpringBootTest(classes = UserService.class)
public class UserServiceTest {
    private final EasyRandom EASY_RANDOM = new EasyRandom();
    private User user;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = EASY_RANDOM.nextObject(User.class); 
    }
    
    @Test
    void successTest() {
        when(userRepository.findByUsername(user.getUsername()))
            .thenReturn(Optional.of(user));

        var result = userService.loadUserByUsername(user.getUsername());

        assertEquals(UserDetailsImpl.build(user), result);
    }

    @Test
    void exceptionTest() {
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(user.getUsername());
        });
    }
}
