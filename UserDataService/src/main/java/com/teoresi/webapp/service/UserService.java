package com.teoresi.webapp.service;


import com.teoresi.webapp.model.User;
import com.teoresi.webapp.repository.IUtenteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
	

	private IUtenteRepository userRepository;

	public UserService(IUtenteRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> listAll(){
		return userRepository.findAll();
	}
	
	public void save(User user) {
		userRepository.save(user);
	}

	public User get(String id) {
		return userRepository.findById(id).get();
	}
	
	public void delete(String id) {
		userRepository.deleteById(id);
	}
	
	public User selectUserByMailAndIdUser(String mail) {
		return selectUserByMailAndIdUser(mail);
	}
	
	public List<User> findAllUserWithRoles(int start, int length){
		return userRepository.findAllUserWithRoles(start, length);
	}
	
	public int totalUsers() {
		return userRepository.totalUsers();
	}
}
