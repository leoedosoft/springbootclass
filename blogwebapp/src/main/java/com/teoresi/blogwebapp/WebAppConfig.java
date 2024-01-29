package com.teoresi.blogwebapp;

import java.util.function.BiFunction;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

	/**
	 * CONFIGURAZIONE: Internazionalizzazione di Spring Boot
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		registry.addInterceptor(localeChangeInterceptor);
	}

	@Bean
	public LocaleResolver localeResolver() {
		return new CookieLocaleResolver();
	}

	@Bean("messageSource")
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("lang/messages");
		return messageSource;
	}

	/**
	 * VALIDAZIONE: Necessario per registrare i messaggi personalizzati per la view 
	 */
	@Bean
	public LocalValidatorFactoryBean getValidator() {
	    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
	    bean.setValidationMessageSource(messageSource());
	    return bean;
	}
	
	/**
	 * Call tramite sintassi Thymeleaf gestione parametro lingua lang=it/en
	 * add parameter to current url
	 */
	@Bean
	public BiFunction<String, String, String> replaceOrAddParam() {
	  return (paramName, newValue) -> ServletUriComponentsBuilder.fromCurrentRequest()
	        .replaceQueryParam(paramName, newValue)
	        .toUriString();
	}
}
