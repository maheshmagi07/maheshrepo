/**
 * Controller Class has path to read/ analyze PDF documents 
 */
package com.alcon.extraction.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alcon.extraction.config.AWSConfiguration;
import com.alcon.extraction.dto.DeviceData;
import com.alcon.extraction.service.S3Service;
import com.alcon.extraction.service.TextractService;

/**
 * @author AbhilashaP
 *
 */
@RestController
public class TextractController {

	@Autowired
	TextractService textractService;

	@Autowired
	S3Service s3Service;
	
	@Autowired
	AWSConfiguration awsConfig;
	
	@Value("${aws.service.s3.bucket.name}")
	private String bucketName;

	@RequestMapping(value = "/analyzeDoc", method = RequestMethod.POST)
	DeviceData getImages(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String name = request.getParameter("name");
		return textractService.analyzeDoc(name);
		
	}

	@RequestMapping(value = "/analyzeFile", method = RequestMethod.POST)
	DeviceData getImages(@RequestParam("file") MultipartFile file) throws Exception {

		byte[] bytes = file.getBytes();
		String name = null;
		if(file.getOriginalFilename().lastIndexOf(".pdf") != -1) {
			name = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf(".pdf"))+LocalDateTime.now()+".pdf";
		}else {
			throw new IOException("Invalide file extension");
		}
		// Put the posted PDF file into the bucket.
    	s3Service.putObject( bytes , awsConfig.bucketName(), name);
    	return textractService.analyzeDoc(LocalDate.now() + "/"+name);
	//	return textractService.analyzeDoc(file.getBytes());
	}
	
	

	
}