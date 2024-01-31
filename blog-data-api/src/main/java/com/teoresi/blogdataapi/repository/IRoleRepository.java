package com.teoresi.blogdataapi.repository;

import com.teoresi.blogdataapi.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, String> {

}
