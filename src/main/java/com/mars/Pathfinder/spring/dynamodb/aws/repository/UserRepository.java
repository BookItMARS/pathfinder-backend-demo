package com.mars.Pathfinder.spring.dynamodb.aws.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDeleteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.mars.Pathfinder.spring.dynamodb.aws.entity.User;

/**
 * @author tjspitz
 */
@Repository
public class UserRepository {

    @Autowired
    private DynamoDBMapper mapper;

    /**
     * @param user
     * @return User
     */
    public User addUser(User user) {
        mapper.save(user);
        return user;
    }

    /**
     * @param userEmailId
     * @return User
     */
    public User findUserByEmailId(String userEmailId) {
        return mapper.load(User.class, userEmailId, "user");
    }

    /**
     * @param user
     * @return String
     */
    public String deleteUser(User user) {
        mapper.delete(user, buildDeleteExpression(user));
        return "User Deleted";
    }

    /**
     * @param user
     * @return String
     */
    public String editUser(User user) {
        mapper.save(user, buildEditExpression(user));
        return "User Updated";
    }

    /**
     * @return List<User>
     */
    public List<User> getAllUsers() {
        return mapper.scan(User.class, new DynamoDBScanExpression());
    }

    private DynamoDBSaveExpression buildEditExpression(User user) {
        DynamoDBSaveExpression dynamoDBSaveExpression = new DynamoDBSaveExpression();
        Map<String, ExpectedAttributeValue> expectedMap = new HashMap<>();
        expectedMap.put("ID",
                new ExpectedAttributeValue(new AttributeValue().withS(user.getEmailId())));
        expectedMap.put("DocType",
                new ExpectedAttributeValue(new AttributeValue().withS(user.getDocType())));
        dynamoDBSaveExpression.setExpected(expectedMap);
        return dynamoDBSaveExpression;
    }

    private DynamoDBDeleteExpression buildDeleteExpression(User user) {
        DynamoDBDeleteExpression dynamoDBDeleteExpression = new DynamoDBDeleteExpression();
        Map<String, ExpectedAttributeValue> expectedMap = new HashMap<>();
        expectedMap.put("ID",
                new ExpectedAttributeValue(new AttributeValue().withS(user.getEmailId())));
        expectedMap.put("DocType",
                new ExpectedAttributeValue(new AttributeValue().withS(user.getDocType())));
        dynamoDBDeleteExpression.setExpected(expectedMap);
        return dynamoDBDeleteExpression;
    }
}
