package com.teoresi.blogwebapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.teoresi.blogwebapp.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(customUserDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
	

	//Autentificazione che si basa sulla lettura dei dati sul database
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.authenticationProvider(authenticationProvider());
	}
	
	private static final String[] USER_BLOGS_MATCHER = 
	{
		"/users/**",
		"/deleteUser/**",
		"/editUser/**",
		"/new/**",
		"/delete/**"
	};

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/resources/**").permitAll()
		.antMatchers("/register/**").permitAll()
		.antMatchers("/bloglist").permitAll()
		.antMatchers("/").permitAll()
		.antMatchers(USER_BLOGS_MATCHER).hasAnyRole("ADMIN")
		.antMatchers("/blogs").hasAnyRole("ADMIN","USER","MODERATOR")
		.antMatchers("/edit/**").hasAnyRole("ADMIN","MODERATOR")
		.antMatchers("/detail/**").hasAnyRole("ADMIN","USER","MODERATOR")
		.and()
		.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.formLogin()
				.loginPage("/login")
				.permitAll()
		.and()
			.logout()
			.invalidateHttpSession(true)
			.clearAuthentication(true)
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/login?logout")
			.permitAll();
	}
	
	public AuthenticationFilter authenticationFilter() 
			throws Exception 
	{
		 AuthenticationFilter filter = new AuthenticationFilter();
		 filter.setAuthenticationManager(authenticationManagerBean());
		 filter.setAuthenticationFailureHandler(failureHandler());
		 filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
		 return filter;
		 
	}
	
	public SimpleUrlAuthenticationFailureHandler failureHandler() 
	{ 
        return new SimpleUrlAuthenticationFailureHandler("/login?error"); 
    } 
	
	@Bean
	public SavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler() 
	{
        SavedRequestAwareAuthenticationSuccessHandler auth = new SavedRequestAwareAuthenticationSuccessHandler();
        //True: indirizza alla pagina indicata false:indirizza alla pagina protetta che si voleva accedere
		auth.setAlwaysUseDefaultTargetUrl(true);
		auth.setDefaultTargetUrl("/blogs");
		return auth;
	}
}
