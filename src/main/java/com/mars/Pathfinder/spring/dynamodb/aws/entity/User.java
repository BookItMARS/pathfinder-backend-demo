package com.mars.Pathfinder.spring.dynamodb.aws.entity;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.Data;

/**
 * @author tjspitz
 */
@Data
@DynamoDBTable(tableName = "pathfinder")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @DynamoDBHashKey(attributeName = "ID")
    private String emailId;

    @DynamoDBRangeKey(attributeName = "DocType")
    private String docType;

    private Integer roleId;
    private String firstName;
    private String lastName;
    private String password;
    private String accountStatus = "inactive";
    private Integer loginAttempts = 0;
    private Boolean isLocked = false;
}
