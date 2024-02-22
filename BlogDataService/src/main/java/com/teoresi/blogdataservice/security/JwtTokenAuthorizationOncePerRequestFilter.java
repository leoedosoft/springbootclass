package com.teoresi.blogdataservice.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenAuthorizationOncePerRequestFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenAuthorizationOncePerRequestFilter.class);

	@Autowired
	@Qualifier("customUserDetailsService")
	private UserDetailsService userDetailsService;

	
	@Value("${sicurezza.header}")
	private String tokenHeader;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException 
	{
		logger.info(String.format("SONO QUI .. Authentication Request For '{%s}'", request.getRequestURL()));

		final String requestTokenHeader = request.getHeader(this.tokenHeader);
		
		logger.warn("Token: " + requestTokenHeader);

		String username = null;
		String jwtToken = null;
		
		if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
			chain.doFilter(request, response);
		    return;
		}
		
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) 
		{
			jwtToken = requestTokenHeader.substring(7);
			
			try 
			{
				//String jwt = requestTokenHeader.substring(authenticationScheme.length());

				//then email is extracted from the token
				//String userEmail = JwtProvider.extractUsername(jwt);
				
				//Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				//UserDetails userDetail = (UserDetails) authentication.getPrincipal();
				//logger.info("USERNAME è: ", userDetail.getUsername());
				//username =  userDetail.getUsername();
				//userDetailsService.loadUserByUsername(userDetail.getUsername());
				

				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
				logger.info("USERNAME è: ", username);
			} 
			catch (IllegalArgumentException e) 
			{
				logger.error("IMPOSSIBILE OTTENERE LA USERID", e);
			} 
			catch (ExpiredJwtException e) 
			{
				logger.warn("TOKEN SCADUTO", e);
			}
		} 
		else 
		{
			logger.warn("TOKEN NON VALIDO");
		}

		logger.warn(String.format("JWT_TOKEN_USERNAME_VALUE '{%s}'", username));
		
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) 
		{

			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) 
			{
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}

		chain.doFilter(request, response);
	}
}
