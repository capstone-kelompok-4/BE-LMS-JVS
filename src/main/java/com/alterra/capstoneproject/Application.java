package com.alterra.capstoneproject;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.alterra.capstoneproject.domain.dao.Role;
import com.alterra.capstoneproject.domain.dao.RoleEnum;
import com.alterra.capstoneproject.repository.RoleRepository;

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
		Role roleAdmin = new Role();
		roleAdmin.setName(RoleEnum.ROLE_ADMIN);
        roleRepository.save(roleAdmin);

		Role roleUser = new Role();
		roleUser.setName(RoleEnum.ROLE_USER);
        roleRepository.save(roleUser);
      };
   }

}
