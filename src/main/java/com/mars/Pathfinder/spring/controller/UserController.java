package com.mars.Pathfinder.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mars.Pathfinder.spring.dynamodb.aws.entity.User;
import com.mars.Pathfinder.spring.dynamodb.aws.repository.UserRepository;

@RestController
public class UserController {

	@Autowired
	public UserRepository userRepo;

	@PostMapping("/save")
	public User saveUser(@RequestBody User user) {
		// get the password
		// use BcryptPasswordEncoder to encode password before storing
		return userRepo.addUser(user);
	}

	@GetMapping("/get/{userEmailId}")
	public User findUser(@PathVariable String userEmailId) {
		return userRepo.findUserByEmailId(userEmailId);
	}

	@DeleteMapping("/delete")
	public String deleteUser(@RequestBody User user) {
		return userRepo.deleteUser(user);
	}

	@PutMapping("/edit")
	public String updateUser(@RequestBody User user) {
		return userRepo.editUser(user);
	}
}
