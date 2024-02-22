package com.teoresi.blogdataservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class JwtSecurityConfig {

	@Autowired
	//private JwtUnAuthorizedResponseAuthenticationEntryPoint jwtUnAuthorizedResponseAuthenticationEntryPoint;
	private JwtUnAuthorizedResponseAuthenticationEntryPoint jwtUnAuthorizedResponseAuthenticationEntryPoint;

	@Autowired
	@Qualifier("customUserDetailsService")
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenAuthorizationOncePerRequestFilter jwtAuthenticationTokenFilter;
	
	private static final String[] ADMIN_MATCHER = { "/api/articoli/inserisci/**", 
			"/api/articoli/modifica/**", "/api/articoli/elimina/**", "/api/blog/**" };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> 
                            session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS)) // (2)
                .authorizeHttpRequests(
                        auth -> 
                            auth.requestMatchers("/authenticate", "/actuator", "/actuator/*", "/api/blog/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.OPTIONS,"/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                        ) // (3)
                .exceptionHandling(
                        (ex) -> 
                            ex.authenticationEntryPoint(jwtUnAuthorizedResponseAuthenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(
                        Customizer.withDefaults()) // (5)
                .build();
        

    }
    
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception 
	{
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoderBean());
	}

	@Bean
	public static PasswordEncoder passwordEncoderBean() 
	{
		return new BCryptPasswordEncoder();
	}
    
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }
    /*
    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService) {
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("admin@test.com")
                                .password("{noop}password")
                                .authorities("read")
                                .roles("ADMIN")
                                .build();

        return new InMemoryUserDetailsManager(user);
    }
    /*
    
    @Bean
    public UserDetailsService userDetailsService(){

        User.UserBuilder users = User.builder();

        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        manager.createUser(users
                .username("user@test.com")
                .password(new BCryptPasswordEncoder().encode("password"))
                .roles("USER").build());

        manager.createUser(users
                .username("admin@test.com")
                .password(new BCryptPasswordEncoder().encode("password"))
                .roles("USER", "ADMIN").build());

        return manager;
    }
    
     */

    
    /*
	@Bean
	public void configure(WebSecurity webSecurity) throws Exception 
	{
		webSecurity.ignoring()
				.requestMatchers(HttpMethod.OPTIONS, "/**")
				.and().ignoring()
				.requestMatchers(HttpMethod.GET, "/");	
	}
	
	*/
    
}


