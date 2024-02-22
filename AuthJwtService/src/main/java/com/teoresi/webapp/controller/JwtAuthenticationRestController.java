package com.teoresi.webapp.controller;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.teoresi.webapp.security.JwtTokenUtil;

 
@RestController
public class JwtAuthenticationRestController 
{
	
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationRestController.class);


	@Value("${sicurezza.header}")
	private String tokenHeader;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	@Qualifier("customUserDetailsService")
	private UserDetailsService userDetailsService;
	
	@PostMapping(value = "${sicurezza.uri}")
	//@PostMapping("/authenticate") 
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtTokenRequest authenticationRequest) 
	{
		logger.info("Autenticazione e Generazione Token");

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);
		
		logger.warn(String.format("Token %s", token));

		return ResponseEntity.ok(token);
	}

	@GetMapping(value = "${sicurezza.refresh}")
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) throws Exception 
	{
		logger.info("Tentativo Refresh Token");
		String authToken = request.getHeader(tokenHeader);
		
		if (authToken == null)
		{
			throw new Exception("Token assente o non valido!");
		}
		
		final String token = authToken; 
		
		if (jwtTokenUtil.canTokenBeRefreshed(token)) 
		{
			String refreshedToken = jwtTokenUtil.refreshToken(token);
			
			logger.warn(String.format("Refreshed Token %s", refreshedToken));
			
			return ResponseEntity.ok(refreshedToken);
		} 
		else 
		{
			return ResponseEntity.badRequest().body(null);
		}
	}

	@ExceptionHandler({ AuthenticationException.class })
	public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) 
	{
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	private void authenticate(String username, String password) 
	{
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try 
		{
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} 
		catch (DisabledException e) 
		{
			logger.warn("UTENTE DISABILITATO");
			throw new AuthenticationException("UTENTE DISABILITATO", e);
		} 
		catch (BadCredentialsException e) 
		{
			logger.warn("CREDENZIALI NON VALIDE");
			throw new AuthenticationException("CREDENZIALI NON VALIDE", e);
		}
	}
}
