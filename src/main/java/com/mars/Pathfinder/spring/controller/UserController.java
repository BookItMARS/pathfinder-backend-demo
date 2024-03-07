package com.mars.Pathfinder.spring.controller;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
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
        // does the user already exist?
            // give an error code (status code 4xx)

        String tempPassword = generatePassayPassword();
        user.setPassword(new BCryptPasswordEncoder().encode(tempPassword));

        // the encoded password
        System.out.println(user.getPassword());

        User sanitizedUser = userRepo.addUser(user);

//      don't want to send the encrypted password back to the frontend
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
        return userRepo.deleteUser(user);
    }

    /**
     * @param user
     * @return String
     */
    @PutMapping("/edit") // [X]
    public String updateUser(@RequestBody User user) {
        // is the user.getPassword() matching what is in the db?
        // might have to fetch user from the DB to get the encoded password
            // yes => leave password alone
            // no => encode this password and then save the user

        // addtil db col for temp password(?)

        return userRepo.editUser(user);
    }

    /**
     * @return String
     */
    private String generatePassayPassword() {
        PasswordGenerator gen = new PasswordGenerator();

        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return "ERROR_CODE";
            }
            public String getCharacters() {
                return "@#$%&*";
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(1);

        return gen.generatePassword(10, splCharRule, lowerCaseRule, upperCaseRule, digitRule);
    }
}
