package com.mars.Pathfinder.spring.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mars.Pathfinder.spring.dynamodb.aws.entity.User;
import com.mars.Pathfinder.spring.dynamodb.aws.repository.UserRepository;

/**
 * @author tjspitz
 */
@RestController
public class UserController {

    /**
     * @see com.mars.Pathfinder.spring.dynamodb.aws.repository.UserRepository
     */
    @Autowired
    public UserRepository userRepo;

    /**
     * @param user
     * @return User
     */
    @PostMapping("/save") // [X]
    public User saveUser(@RequestBody User user) {
        String tempPassword = new BCryptPasswordEncoder().encode(makeFirstPassword());
        user.setPassword(tempPassword);

//      don't want to send the encrypted password back to the frontend
//      return userRepo.addUser(user);

        User sanitizedUser = userRepo.addUser(user);
        sanitizedUser.setPassword("encrypted for your protection");
        return sanitizedUser;
    }

    /**
     * @param userEmailId
     * @return User
     */
    @GetMapping("/get/{userEmailId}") // [X]
    public User findUser(@PathVariable String userEmailId) {
        return userRepo.findUserByEmailId(userEmailId);
    }

    /**
     * @param user
     * @return String
     */
    @DeleteMapping("/delete") // [X]
    public String deleteUser(@RequestBody User user) {
        // TODO: refactor so we don't need to pass the entire user object
        return userRepo.deleteUser(user);
    }

    /**
     * @param user
     * @return String
     */
    @PutMapping("/edit") //
    public String updateUser(@RequestBody User user) {
        // fetch the user
        // verify that the plain password matches the encrypted password
        // now we can save the changes (including new, encrypted password)
        return userRepo.editUser(user);
    }

    /**
     * @author Saibhavithra
     * @description generates a temporary but strong password for new usersß
     * @return String
     */
    private String makeFirstPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
        return RandomStringUtils.random(8, characters);
    }
}
