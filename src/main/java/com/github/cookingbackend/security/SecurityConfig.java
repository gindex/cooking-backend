package com.github.cookingbackend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			.cors()
			.and()
			.headers().frameOptions().sameOrigin()  //Needed for Swagger and Graphiql iFrames.
			.and()
			.csrf().disable()
			.authorizeRequests(authorize ->
				{
					authorize.mvcMatchers("/api-docs").permitAll();
					authorize.mvcMatchers("/**").permitAll();
				}
			);
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration(
			"/**",
			new CorsConfiguration().applyPermitDefaultValues()
		);
		return urlBasedCorsConfigurationSource;
	}

}
