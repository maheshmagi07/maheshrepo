/**
 * S3Service Interface lists all the methods required to Put Get Objects in S3 bucket
 */
package com.alcon.extraction.service;

import java.util.List;

/**
 * @author AbhilashaP
 */
public interface S3Service {

	byte[] getObjectBytes(String bucketname, String keyName);

	String putObject(byte[] bytes, String bucketName, String name);
	
	List<String> getFolder(String bucketName, String folderName);

}
