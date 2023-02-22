/**
 * Controller Class has path to read/ analyze PDF documents 
 */
package com.alcon.extraction.controller;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alcon.extraction.config.AWSConfiguration;
import com.alcon.extraction.service.S3Service;
import com.alcon.extraction.service.TextractService;

/**
 * @author AbhilashaP
 *
 */
@RestController
public class S3Controller {

	@Autowired
	TextractService textractService;

	@Autowired
	S3Service s3Service;

	@Autowired
	AWSConfiguration awsConfig;

	// Upload a Document to analyze.
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> singleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {

		byte[] bytes = file.getBytes();
		String name = null;
		if(file.getOriginalFilename().lastIndexOf(".pdf") != -1) {
			name = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf(".pdf"))+LocalDateTime.now()+".pdf";
		}else {
			throw new IOException("Invalide file extension");
		}
		// Put the posted PDF file into the bucket.
		s3Service.putObject(bytes, awsConfig.bucketName(), name);

		return new ResponseEntity<String>("FileName::" + name, HttpStatus.OK);

	}

}