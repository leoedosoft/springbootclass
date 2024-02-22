package com.teoresi.webapp.security;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService
{
	
	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	@Autowired
	private UserConfig Config;
	
	@Override
	public UserDetails loadUserByUsername(String mail) 
	{
		String ErrMsg = "";
		
		if (mail == null || mail.length() < 2) 
		{
			ErrMsg = "Nome utente assente o non valido";
			
			logger.warn(ErrMsg);
			
	    	throw new UsernameNotFoundException(ErrMsg); 
		} 
		
		User utente = this.GetHttpValue(mail);
		
		if (utente == null)
		{
			ErrMsg = String.format("Utente %s non Trovato!!", mail);
			
			logger.warn(ErrMsg);
			
			throw new UsernameNotFoundException(ErrMsg);
		}
		
		UserBuilder userBuilder = null;
		userBuilder = org.springframework.security.core.userdetails.User.withUsername(utente.getMail());
		userBuilder.disabled(false);
		userBuilder.password(utente.getPassword());
		userBuilder.authorities(getAuthorities(utente));
		
		return userBuilder.build();
		
		
	}
	
	private Collection<GrantedAuthority> getAuthorities(User user){
		List<Role> roles = user.getRoles();
		Collection<GrantedAuthority> authorities = new ArrayList<>(roles.size());
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority("ROLE_"+	role.getTipo()));
		}
		return authorities;
	}
	
	private User GetHttpValue(String mail)
	{
		URI url = null;

		try 
		{
			String SrvUrl = Config.getSrvUrl();

			url = new URI(SrvUrl + mail);
		} 
		catch (URISyntaxException e) 
		{
			 
			e.printStackTrace();
		}
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(Config.getMail(), Config.getPassword()));
		
		User utente = null;

		try 
		{
			utente = restTemplate.getForObject(url, User.class);	
		} 
		catch (Exception e) 
		{
			String ErrMsg = String.format("Connessione al servizio di autenticazione non riuscita!!");
			
			logger.warn(ErrMsg);
			
		}
		
		return utente;
		
	}
	
	
	
	
	
	
	
	
}
	