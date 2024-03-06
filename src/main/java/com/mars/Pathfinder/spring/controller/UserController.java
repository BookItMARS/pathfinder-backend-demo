package com.mars.Pathfinder.spring.controller;

import java.util.Random;

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
     * @description generates a temporary but strong password for new users
     * @return String
     */
    private String makeFirstPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~!@#$%^&*?";
        return RandomStringUtils.random(8, characters);
    }

    /**
     * @author tjspitz
     * @description generates a password with randomized set of characters;
     * includes at least 1 char from each group (A-Z), (a-z), (0-9), (~!@#$%^&*)
     * @return String
     */
    private String makeFirstPasswordV2() {
        String password = "";
        Random random = new Random();
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercase = uppercase.toLowerCase();
        String nums = "1234567890";
        String specials = "~!@#$%^&*";

        int i;
        int upperLength = uppercase.length();
        int lowerLength = lowercase.length();
        int numsLength = nums.length();
        int specialsLength = specials.length();

        while (password.length() < 8) {
            i = random.nextInt(upperLength);
            password += uppercase.charAt(i);

            i = random.nextInt(lowerLength);
            password += lowercase.charAt(i);

            i = random.nextInt(numsLength);
            password += nums.charAt(i);

            i = random.nextInt(specialsLength);
            password += specials.charAt(i);
        }
        return password;
    }
}
