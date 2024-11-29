package is.lessons.spring.pizzeria.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		http.authorizeHttpRequests()

		.requestMatchers("/pizze/crea-pizza", "/pizze/modifica-pizza/**", "/pizze/crea-offerta/**", "/pizze/modifica-offerta/**").hasAuthority("ADMIN")
		.requestMatchers(HttpMethod.POST, "/pizze/**").hasAuthority("ADMIN")
		.requestMatchers("/ingredienti/**").hasAuthority("ADMIN")
		.requestMatchers(HttpMethod.POST,"/ingredient/**").hasAuthority("ADMIN")
		.requestMatchers(HttpMethod.POST, "/coupons/**").hasAuthority("ADMIN")
		.requestMatchers("/**", "/pizze").permitAll()
		.requestMatchers("/pizze/**", "/ingredienti/**").hasAnyAuthority("USER", "ADMIN")
		.and().formLogin()
		.and().logout()
		.and().exceptionHandling();
		
		return http.build();

	}
	
	@Bean
	DatabaseUserDetailsService userDetailsService() {
		return new DatabaseUserDetailsService();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
}
