package com.lunaticcart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunaticcart.entity.User;

public interface UserRepository extends JpaRepository<User,Long>{
	
	Optional<User> findByEmail(String Email);
	
	Boolean existsByEmail(String email);

}
