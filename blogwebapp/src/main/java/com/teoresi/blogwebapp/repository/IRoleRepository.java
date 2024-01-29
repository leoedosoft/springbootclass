package com.teoresi.blogwebapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teoresi.blogwebapp.model.Role;

public interface IRoleRepository extends JpaRepository<Role, String> {

}
