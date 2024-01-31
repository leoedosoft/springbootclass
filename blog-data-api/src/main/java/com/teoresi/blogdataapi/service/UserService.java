package com.teoresi.blogdataapi.service;

import com.teoresi.blogdataapi.model.User;
import com.teoresi.blogdataapi.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
	
	@Autowired
	private IUserRepository userRepository;
	
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
