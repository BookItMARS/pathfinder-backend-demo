package com.mars.Pathfinder.spring.dynamodb.aws.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.mars.Pathfinder.spring.dynamodb.aws.entity.User;

/**
 * @author tjspitz
 */
@Repository
public class UserRepository {

    @Autowired
    private AmazonDynamoDB db;

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
        mapper.delete(user);
        return "User Deleted";
    }

    /**
     * @param user
     * @return String
     */
    public String editUser(User user) {
        // mapper.save(user);
//        mapper.save(user, buildExpression(user));
        db.putItem("pathfinder", buildPutMap(user));

        return "User Updated";
    }

    /**
     * @return List<User>
     */
    public List<User> getAllUsers() {
        return mapper.scan(User.class, new DynamoDBScanExpression());
    }

//    TODO: how can we implement this? buildPutMap() works, but....
//    private DynamoDBSaveExpression buildExpression(User user) {
//        DynamoDBSaveExpression dynamoDBSaveExpression = new DynamoDBSaveExpression();
//        Map<String, ExpectedAttributeValue> expectedMap = new HashMap<>();
//        expectedMap.put("ID",
//                new ExpectedAttributeValue(new AttributeValue().withS(user.getEmailId())));
//        expectedMap.put("DocType",
//                new ExpectedAttributeValue(new AttributeValue().withS(user.getDocType())));
//        expectedMap.put("roleId",
//                new ExpectedAttributeValue(new AttributeValue().withN(user.getRoleId().toString())));
//        dynamoDBSaveExpression.setExpected(expectedMap);
//        return dynamoDBSaveExpression;
//    }

    private Map<String, AttributeValue> buildPutMap(User user) {
        Map<String, AttributeValue> expectedMap = new HashMap<>();
        expectedMap.put("ID", new AttributeValue().withS(user.getEmailId()));
        expectedMap.put("DocType", new AttributeValue().withS(user.getDocType()));
        expectedMap.put("roleId", new AttributeValue().withN(user.getRoleId().toString()));
        expectedMap.put("firstName", new AttributeValue().withS(user.getFirstName()));
        expectedMap.put("lastName", new AttributeValue().withS(user.getLastName()));
        expectedMap.put("firstName", new AttributeValue().withS(user.getFirstName()));
        
        // TODO: get the password encrypted in the controller so it's all set up in HERE
        expectedMap.put("password", new AttributeValue().withS(user.getPassword()));
        
        expectedMap.put("accountStatus", new AttributeValue().withS(user.getAccountStatus()));
        expectedMap.put("loginAttempts",
                new AttributeValue().withN(user.getLoginAttempts().toString()));
        expectedMap.put("isLocked", new AttributeValue().withBOOL(user.getIsLocked()));
        return expectedMap;
    }
}
