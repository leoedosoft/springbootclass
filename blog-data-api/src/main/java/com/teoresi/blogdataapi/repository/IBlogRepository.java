package com.teoresi.blogdataapi.repository;

import com.teoresi.blogdataapi.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBlogRepository extends JpaRepository<Blog, String> {
	
	public Page<Blog> findByTitleLikeOrContentLike(String title, String content, Pageable pageable);
}
