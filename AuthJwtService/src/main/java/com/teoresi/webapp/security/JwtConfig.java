package com.teoresi.webapp.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties("sicurezza")
public class JwtConfig
{
	private String uri;
	private String refresh;
	private String header;
	private String prefix;
	private int expiration;
	private String secret;
	private Boolean noexpiration;
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getRefresh() {
		return refresh;
	}
	public void setRefresh(String refresh) {
		this.refresh = refresh;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public int getExpiration() {
		return expiration;
	}
	public void setExpiration(int expiration) {
		this.expiration = expiration;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public Boolean getNoexpiration() {
		return noexpiration;
	}
	public void setNoexpiration(Boolean noexpiration) {
		this.noexpiration = noexpiration;
	}
	
	
	
}
