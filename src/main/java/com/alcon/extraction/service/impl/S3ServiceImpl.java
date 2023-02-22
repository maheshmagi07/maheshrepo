/**
 * Implementation class of S3Service Interface
 * Please see the {@link com.alcon.extraction.service.S3Service} class for true identity
 */
package com.alcon.extraction.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alcon.extraction.config.AWSConfiguration;
import com.alcon.extraction.service.S3Service;
import com.amazonaws.services.s3.model.AmazonS3Exception;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

/**
 * @author AbhilashaP
 */
@Service
public class S3ServiceImpl implements S3Service {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	AWSConfiguration awsConfig;

	@Autowired
	S3Client s3;

	// getting byte[] from s3 bucket
		@Override
		public byte[] getObjectBytes(String fileName, String folderName) throws S3Exception{
			log.info("Method getObjectBytes starts");
			GetObjectRequest objectRequest = GetObjectRequest.builder().key(folderName+fileName).bucket(awsConfig.bucketName()).build();
			ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(objectRequest);
			byte[] data = objectBytes.asByteArray();
			log.info("Method getObjectBytes end");
			return data;

		}

	// put object into s3
		@Override
		public String putObject(byte[] data, String bucketName, String name) throws S3Exception{
			log.info("Method putObject Starts");
			String folderName = LocalDate.now() + "/";
			PutObjectResponse response = null;
			if (this.getFolder(bucketName, folderName).contains(folderName)) {
				response = s3.putObject(
						PutObjectRequest.builder().bucket(awsConfig.bucketName()).key(folderName + name).build(),
						RequestBody.fromBytes(data));
			} else {
				PutObjectRequest request = PutObjectRequest.builder().bucket(bucketName).key(folderName).build();
				s3.putObject(request, RequestBody.empty());

				response = s3.putObject(
						PutObjectRequest.builder().bucket(awsConfig.bucketName()).key(folderName + name).build(),
						RequestBody.fromBytes(data));
			}
			log.info("Method putObject End");
			return response.eTag();
		}
	
	@Override
	public List<String> getFolder(String bucketName, String folderName) {
		log.info("Method getfolder starts");
		ArrayList<String> listOfFolderList = new ArrayList<String>();
		ListObjectsRequest request = ListObjectsRequest.builder().bucket(bucketName).build();
		ListObjectsResponse response = s3.listObjects(request);
		List<S3Object> objects = response.contents();
		objects.stream().forEach(folder -> {
			S3Object object = folder;
			listOfFolderList.add(object.key());
		});
		log.info("Method getfolder end");
		return listOfFolderList;
	}

}
