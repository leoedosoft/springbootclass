package com.teoresi.blogwebapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.teoresi.blogwebapp.model.Blog;
import com.teoresi.blogwebapp.model.User;
import com.teoresi.blogwebapp.repository.IUserRepository;

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
