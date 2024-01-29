package com.teoresi.blogwebapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.teoresi.blogwebapp.model.Blog;

public interface IBlogRepository extends JpaRepository<Blog, String> {
	
	public Page<Blog> findByTitleLikeOrContentLike(String title, String content, Pageable pageable);
}
