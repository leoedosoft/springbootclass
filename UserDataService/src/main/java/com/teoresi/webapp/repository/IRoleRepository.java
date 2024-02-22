package com.teoresi.webapp.repository;

import com.teoresi.webapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, String> {

}
