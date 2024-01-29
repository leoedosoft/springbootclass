package com.teoresi.blogwebapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teoresi.blogwebapp.model.Role;
import com.teoresi.blogwebapp.repository.IRoleRepository;

@Service
public class RoleService {
	
	@Autowired
	private IRoleRepository roleRepository;
	
	public List<Role> listAll(){
		return roleRepository.findAll();
	}
	
	public void save(Role role) {
		roleRepository.save(role);
	}

	public Role get(String id) {
		return roleRepository.findById(id).get();
	}
	
	public void delete(String id) {
		roleRepository.deleteById(id);
	}
	
}
