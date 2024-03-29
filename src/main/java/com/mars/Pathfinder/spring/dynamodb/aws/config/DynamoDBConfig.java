package com.mars.Pathfinder.spring.dynamodb.aws.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

/**
 * @author tjspitz
 */
@Configuration
public class DynamoDBConfig {

//	static Logger logger = Logger.getLogger(PathfinderController.class.getName());

	@Value("${aws.dynamodb.endpoint}")
	private String awsDynamoDBEndpoint;

	@Value("${aws.dynamodb.region}")
	private String awsDynamoDBRegion;

	@Value("${aws.dynamodb.accesskey}")
	private String accessKey;

	@Value("${aws.dynamodb.secretkey}")
	private String secretKey;

	/**
	 * @return DynamoDBMapper
	 */
	@Bean
	public DynamoDBMapper mapper() {
		return new DynamoDBMapper(amazonDynamoDBConfig());
	}

	/**
	 * @return AmazonDynamoDB
	 */
	@Bean
	public AmazonDynamoDB amazonDynamoDBConfig() {
		return AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(
						new AwsClientBuilder.EndpointConfiguration(awsDynamoDBEndpoint, awsDynamoDBRegion))
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
				.build();
	}
}
