package com.teoresi.webapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name="role")
public class Role implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GenericGenerator(name = "uuid", strategy = "uuid4")
	private String roleid;
	
	
	@Column(name = "tipo", length = 10)
	private String tipo;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "userid")
	@JsonBackReference
	private User user;
	
	@PrePersist
	public void prePersist() {
		this.roleid = UUID.randomUUID().toString();
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
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
