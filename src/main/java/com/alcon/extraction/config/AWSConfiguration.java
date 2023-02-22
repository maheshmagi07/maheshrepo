/**
 * AWS Configurations/ Connections file 
 */
package com.alcon.extraction.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClientBuilder;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * @author AbhilashaP
 *
 */
@Configuration
public class AWSConfiguration {

	@Value("${aws.service.s3.bucket.name}")
	private String bucketName;

	@Value("${aws.service.accesskey}")
	private String accessKey;

	@Value("${aws.service.secretkey}")
	private String secretKey;

	@Value("${aws.service.region}")
	private String region;

	@Value("${aws.service.roleArn}")
	private String roleArn;

	public String getRoleArn() {
		return roleArn;
	}

	public void setRoleArn(String roleArn) {
		this.roleArn = roleArn;
	}

	private AWSCredentials credentials() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		return credentials;
	}

	private AwsBasicCredentials getCredentials() {
		AwsBasicCredentials awsCreds = AwsBasicCredentials.create(this.accessKey, this.secretKey);
		return awsCreds;
	}

	public String bucketName() {
		return bucketName;
	}

	@Bean
	public AmazonTextract textractClient() {
		return AmazonTextractClientBuilder.defaultClient();
	}

	@Bean
	public AmazonS3 amazonS3() {
		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(region).build();
		return s3client;
	}

	@Bean
	public S3Client getClient() {

		S3Client s3 = S3Client.builder().region(Region.of(region)).build();
		return s3;
	}

	public GetObjectRequest getObjectRequest(String fileName) {
		GetObjectRequest requestJson = new GetObjectRequest(this.bucketName, fileName);
		return requestJson;
	}

	@Bean
	public AmazonSNS getSNSClient() {
		AmazonSNS snsClient = AmazonSNSClientBuilder.defaultClient();
		return snsClient;
	}

	@Bean
	public AmazonSQS getSQSClient() {
		AmazonSQS sqsClient = AmazonSQSClientBuilder.defaultClient();
		return sqsClient;
	}

	
	}

