package com.alterra.capstoneproject;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.alterra.capstoneproject.domain.dao.Role;
import com.alterra.capstoneproject.domain.dao.RoleEnum;
import com.alterra.capstoneproject.repository.RoleRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class Application {
	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	InitializingBean sendDatabase() {
    return () -> {
		Role checkAdmin = roleRepository.findByName(RoleEnum.ROLE_ADMIN).orElse(null);
		if(checkAdmin == null) {
			log.info("Set role admin");
			Role roleAdmin = new Role();
			roleAdmin.setName(RoleEnum.ROLE_ADMIN);
			roleRepository.save(roleAdmin);
		}			

		Role checkUser = roleRepository.findByName(RoleEnum.ROLE_USER).orElse(null);
		if(checkUser == null) {
			log.info("Set role user");
			Role roleUser = new Role();
			roleUser.setName(RoleEnum.ROLE_USER);
        	roleRepository.save(roleUser);
		}
      };
   }

}
