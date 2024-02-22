package com.teoresi.webapp.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;

@Component
public class JwtTokenUtil implements Serializable {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

	static final String CLAIM_KEY_USERNAME = "sub";
	static final String CLAIM_KEY_CREATED = "iat";
	private static final long serialVersionUID = -3301605591108950415L;
	private Clock clock = DefaultClock.INSTANCE;

	@Autowired
	private JwtConfig jwtConfig;


	public String getUsernameFromToken(String token) 
	{
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getIssuedAtDateFromToken(String token) 
	{
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) 
	{
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) 
	{
		final Claims claims = getAllClaimsFromToken(token);
		
		if (claims != null)
		{
			logger.info(String.format("Emissione Token:  %s", claims.getIssuedAt().toString()));
			logger.info(String.format("Scadenza Token:  %s", claims.getExpiration().toString()));
			
			return claimsResolver.apply(claims);
		}
		else 
			return null;
	}

	private Claims getAllClaimsFromToken(String token) 
	{
		Claims retVal = null;
		
		try
		{
			retVal = Jwts.parser()
					.setSigningKey(jwtConfig.getSecret().getBytes())
					.parseClaimsJws(token)
					.getBody();
		}
		catch (Exception ex)
		{
			logger.warn(ex.getMessage());
		}
		
		return retVal;
	}

	private Boolean isTokenExpired(String token) 
	{
	
		final Date expiration = getExpirationDateFromToken(token);
		
		boolean retVal = (expiration != null) ? true : false;
		
		if (retVal)
		{
			logger.info("Token Ancora Valido!");
		}
		else 
		{
			logger.warn("Token Scaduto o non Valido!");
		}
		
		return retVal;
	}

	public String generateToken(UserDetails userDetails) 
	{
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails);
	}

	private String doGenerateToken(Map<String, Object> claims, UserDetails userDetails) 
	{
		final Date createdDate = clock.now();
		final Date expirationDate = calculateExpirationDate(createdDate);
		
		final String secret = jwtConfig.getSecret();

		return Jwts.builder()
				.setClaims(claims)
				.setSubject(userDetails.getUsername())
				.claim("authorities", userDetails.getAuthorities()
						.stream()
							.map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setIssuedAt(createdDate)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact();
	}

	public Boolean canTokenBeRefreshed(String token) 
	{
		return (isTokenExpired(token));
	}

	public String refreshToken(String token) 
	{
		final Date createdDate = clock.now();
		final Date expirationDate = calculateExpirationDate(createdDate);
		
		final String secret = jwtConfig.getSecret();

		final Claims claims = getAllClaimsFromToken(token);
		claims.setIssuedAt(createdDate);
		claims.setExpiration(expirationDate);

		return Jwts.builder()
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) 
	{
		//JwtUserDetails user = (JwtUserDetails) userDetails;
		
		final String username = getUsernameFromToken(token);
		
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private Date calculateExpirationDate(Date createdDate) 
	{
		return new Date(createdDate.getTime() + jwtConfig.getExpiration() * 1000);
	}
}
