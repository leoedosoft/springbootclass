package com.teoresi.blogwebapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.teoresi.blogwebapp.model.Blog;
import com.teoresi.blogwebapp.repository.IBlogRepository;

@Service
public class BlogService {
	
	@Autowired
	private IBlogRepository blogRepository;

	public List<Blog> listAll(){
		return blogRepository.findAll();
	}
	
	public void save(Blog blog) {
		blogRepository.save(blog);
	}

	public Blog get(String id) {
		return blogRepository.findById(id).get();
	}
	
	public void delete(String id) {
		blogRepository.deleteById(id);
	}
	
	public Page<Blog> findByTitleLikeOrContentLike(String title, String content, Pageable pageable){
		return blogRepository.findByTitleLikeOrContentLike(title, content, pageable);
	}
}
