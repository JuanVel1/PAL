package com.example.pal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.pal.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
	@Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles")
	List<User> findAllWithRoles();
}
