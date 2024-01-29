package com.teoresi.blogwebapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.teoresi.blogwebapp.model.User;

public interface IUserRepository extends JpaRepository<User, String> {
	
	@Query(value = "SELECT U FROM User U WHERE U.mail =:mail")
	public User selectUserByMailAndIdUser(@Param("mail") String mail);
	
	@Query(value = "SELECT * FROM user u inner join role r on r.user_id = u.iduser where r.tipo is not null LIMIT ?,?", nativeQuery = true)
	public List<User> findAllUserWithRoles(int start, int length);
	
	@Query(value="SELECT COUNT(*) FROM user u inner join role r on r.user_id = u.iduser where r.tipo is not null", nativeQuery = true)
	public int totalUsers();
}
