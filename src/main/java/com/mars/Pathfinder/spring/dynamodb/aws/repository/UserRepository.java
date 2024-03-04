package com.mars.Pathfinder.spring.dynamodb.aws.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.mars.Pathfinder.spring.dynamodb.aws.entity.User;

@Repository
public class UserRepository {

	@Autowired
	private DynamoDBMapper mapper;

	public User addUser(User user) {
		mapper.save(user);
		return user;
	}

	public User findUserByEmailId(String userEmailId) {
		return mapper.load(User.class, userEmailId);
	}

	public String deleteUser(User user) {
		mapper.delete(user);
		return "User Deleted";
	}

	public String editUser(User user) {
		// mapper.save(user);
		mapper.save(user, buildExpression(user));
		return "User Updated";
	}

	public List<User> getAllUsers() {
		return mapper.scan(User.class, new DynamoDBScanExpression());
	}

	private DynamoDBSaveExpression buildExpression(User user) {
		DynamoDBSaveExpression dynamoDBSaveExpression = new DynamoDBSaveExpression();
		Map<String, ExpectedAttributeValue> expectedMap = new HashMap<>();
		expectedMap.put("emailId", new ExpectedAttributeValue(new AttributeValue().withS(user.getEmailId())));
		dynamoDBSaveExpression.setExpected(expectedMap);
		return dynamoDBSaveExpression;
	}
}
