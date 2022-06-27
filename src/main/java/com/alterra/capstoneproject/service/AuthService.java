package com.alterra.capstoneproject.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alterra.capstoneproject.domain.dao.Role;
import com.alterra.capstoneproject.domain.dao.RoleEnum;
import com.alterra.capstoneproject.domain.dao.Specialization;
import com.alterra.capstoneproject.domain.dao.User;
import com.alterra.capstoneproject.domain.dto.TokenResponse;
import com.alterra.capstoneproject.domain.dto.Login;
import com.alterra.capstoneproject.domain.dto.Register;
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
    private final SpecializationRepository specializationRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public Register register(Register req) {
        try {
            log.info("Search username in database");
            if (userRepository.findUsername(req.getEmail()) != null) {
                throw new Exception("USER WITH EMAIL " + req.getEmail() + " IS ALREADY EXIST");
            }
            
            User user = new User();
            user.setName(req.getName());
            user.setUsername(req.getEmail());
            user.setPassword(passwordEncoder.encode(req.getPassword()));

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

            userRepository.save(user);

            req.setPassword("*".repeat(req.getPassword().length()));
            log.info("User {} saved", req.getEmail());
            return req;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public TokenResponse generateToken(Login req) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtTokenProvider.generateToken(authentication);

            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setToken(jwt);
            log.info("Token created");
            return tokenResponse;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
