package com.the.menu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Configuration
@EnableWebSecurity
class SecurityConfig {
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers("/admin/**").authenticated()
						.anyRequest().permitAll())
				.formLogin(Customizer.withDefaults())
				.build();
	}

	@Bean
	UserDetailsService userDetailsService() {
		UserDetails admin = User.builder()
				.username(System.getenv("MENU_ADMIN_USERNAME"))
				.password(new BCryptPasswordEncoder().encode(System.getenv("MENU_ADMIN_PASSWORD")))
				.build();
		return new InMemoryUserDetailsManager(admin);
	}
}

@Controller
@SpringBootApplication
public class MenuApplication {
	@GetMapping
	public String index() {
		return "index";
	}

	public static void main(String[] args) {
		SpringApplication.run(MenuApplication.class, args);
	}
}
