package com.teoresi.blogwebapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="blog")
public class Blog implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idblog")
    private String idblog;
	
	//@DateTimeFormat
	//@NotEmpty(message  = "{blogapp.msg.validation.blog.pubblicationDate}")
	@Temporal(TemporalType.DATE)
	@Column(name = "publicationdata")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date publicationData;
		
	@NotEmpty(message  = "{blogapp.msg.validation.blog.content}")
	@Column(name = "content", length = 300)
	private String content;
	
	@Column(name = "status", length = 10)
	private String status;
	
	@Column(name = "type", length = 50)
	private String type;
	
	@NotEmpty(message  = "{blogapp.msg.validation.blog.title}")
	@Basic(optional = false)//valore obbligatorio
	@Column(name = "title", length = 100)
	private String title;
	
	//Serve per il set tramite thymeleaf = th:field="*{user.name}"
	@ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
	@JsonBackReference
	private User user;	

	@PrePersist
	public void prePersist() {
		this.idblog = UUID.randomUUID().toString();
	}

	public String getIdblog() {
		return idblog;
	}

	public void setIdblog(String idblog) {
		this.idblog = idblog;
	}

	public Date getPublicationData() {
		return publicationData;
	}

	public void setPublicationData(Date publicationData) {
		this.publicationData = publicationData;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
