package com.teoresi.blogdataservice.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="blog")
public class Blog implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid4")
    @Column(name = "blogid")
    private String blogid;
	
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

	@Column(name = "userid")
	private String userid;


	@PrePersist
	public void prePersist() {
		this.blogid = UUID.randomUUID().toString();
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

	public String getBlogid() {
		return blogid;
	}

	public void setBlogid(String blogid) {
		this.blogid = blogid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
}
