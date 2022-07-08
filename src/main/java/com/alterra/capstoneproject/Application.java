package com.alterra.capstoneproject;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alterra.capstoneproject.domain.dao.MethodologyEnum;
import com.alterra.capstoneproject.domain.dao.MethodologyLearning;
import com.alterra.capstoneproject.domain.dao.Role;
import com.alterra.capstoneproject.domain.dao.RoleEnum;
import com.alterra.capstoneproject.domain.dao.User;
import com.alterra.capstoneproject.domain.dto.AddressDto;
import com.alterra.capstoneproject.domain.dto.Register;
import com.alterra.capstoneproject.repository.MethodologyRepository;
import com.alterra.capstoneproject.repository.RoleRepository;
import com.alterra.capstoneproject.repository.UserRepository;
import com.alterra.capstoneproject.service.AuthService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableAutoConfiguration
public class Application {
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private MethodologyRepository methodologyRepository;

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner run(AuthService authService) {
		return args -> {
			List<Role> checkRoles = roleRepository.findAll();
			if(checkRoles.isEmpty()) {
				log.info("Set roles");
				for(RoleEnum roleEnum : RoleEnum.values()) {
					Role role = new Role();
					role.setName(roleEnum);
					roleRepository.save(role);
				}
			}

			List<User> users = userRepository.findAll();
			if(users.isEmpty()) {
				log.info("Set admin");
				List<RoleEnum> roles = new ArrayList<>();
				AddressDto address = new AddressDto();
				Register register = new Register();

				register.setName("admin");
				register.setEmail("admin@gmail.com");
				register.setPassword("admin_123");
				roles.add(RoleEnum.ROLE_ADMIN);
				register.setRoles(roles);
				register.setAddress(address);

				authService.register(register);
			}
		};
	}

	@Bean
	InitializingBean sendDatabase() {
    return () -> {
		List<MethodologyLearning> checkMathodologies = methodologyRepository.findAll();
		if(checkMathodologies.isEmpty()) {
			log.info("Set methodology learnings");
			for(MethodologyEnum methodEnum : MethodologyEnum.values()) {
				MethodologyLearning method = new MethodologyLearning();
				method.setName(methodEnum);
				methodologyRepository.save(method);
			}
		}
      };	  
   }

   @Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**").allowedOrigins("http://localhost:3000");
			}
		};
	}
}
