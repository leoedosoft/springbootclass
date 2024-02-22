package com.teoresi.webapp.service;

import com.teoresi.webapp.model.Role;
import com.teoresi.webapp.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
