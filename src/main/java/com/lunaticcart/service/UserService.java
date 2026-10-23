package com.lunaticcart.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lunaticcart.entity.User;
import com.lunaticcart.repository.UserRepository;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	
	@Transactional
	public User registerUser(String name , String email , String password)
	{
		if(name ==null || email.isBlank())
		{
			throw new IllegalArgumentException("name is required");
		}
		if(name ==null || email.isBlank())
		{
			throw new IllegalArgumentException("Email is required");
		}
		if(password == null || password.length()<8)
		{
			throw new IllegalArgumentException("Password must contail atleast 8 charcters");
		}
		
		String normalizedEmail = email.trim().toLowerCase();
		
		if(userRepository.existsByEmail(normalizedEmail)) {
			throw new IllegalArgumentException("Email already registered");
		}
		
		User user = new User();
		user.setName(name.trim());
		user.setEmail(normalizedEmail);
		user.setPassword(passwordEncoder.encode(password));
		
		
		user.setRole("CUSTOMER");
		
		return userRepository.save(user);
		
		
	}

}
