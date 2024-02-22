package com.teoresi.webapp.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component 
@ConfigurationProperties("gestuser")
public class UserConfig 
{
	private String srvUrl;
	private String mail;
	private String password;
	public String getSrvUrl() {
		return srvUrl;
	}
	public void setSrvUrl(String srvUrl) {
		this.srvUrl = srvUrl;
	}
	
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
