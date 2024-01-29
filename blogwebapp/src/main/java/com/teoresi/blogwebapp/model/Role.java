package com.teoresi.blogwebapp.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="role")
public class Role implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "idrole")
	private String idRole;
	
	
	@Column(name = "tipo", length = 10)
	private String tipo;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
	@JsonBackReference
	private User user;
	
	@PrePersist
	public void prePersist() {
		this.idRole = UUID.randomUUID().toString();
	}

	public String getIdRole() {
		return idRole;
	}

	public void setIdRole(String idRole) {
		this.idRole = idRole;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
