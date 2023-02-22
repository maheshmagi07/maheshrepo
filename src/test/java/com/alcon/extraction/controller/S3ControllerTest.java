package com.alcon.extraction.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.alcon.extraction.config.AWSConfiguration;
import com.alcon.extraction.service.S3Service;
import com.alcon.extraction.service.TextractService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = S3Controller.class)
public class S3ControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	AWSConfiguration awsConfig;

	@MockBean
	TextractService textractservice;

	@MockBean
	S3Service s3service;

	@InjectMocks
	TextractController textractController;
	
          //Testcase for uploading a file successfully 
	@Test
	public final void testSingleFileUpload() throws Exception {
		final MockMultipartFile jsonfile = new MockMultipartFile("file", "2_1Page_AC2023-02-01T11:18:50.995.pdf",
				"application/json", "{\"json\": \"someValue\"}".getBytes());

		final RequestBuilder requestBuilder = MockMvcRequestBuilders.multipart("/upload").file(jsonfile);

		MvcResult mvcResult = this.mockMvc.perform(requestBuilder).andReturn();
		assertEquals(200, mvcResult.getResponse().getStatus());

	}
	
	//Testcase for not uploading file successfully as file is taken as an .xml file
	@Test
	public final void testSingleFileUploadWithException() throws Exception {
		final MockMultipartFile jsonfile = new MockMultipartFile("file", "2_1Page_AC2023-02-01T11:18:50.995.xml",
				"application/json", "{\"json\": \"someValue\"}".getBytes());

		final RequestBuilder requestBuilder = MockMvcRequestBuilders.multipart("/upload").file(jsonfile);

		MvcResult mvcResult = this.mockMvc.perform(requestBuilder).andReturn();
		assertEquals(true, mvcResult.getResponse().getContentAsString().contains("Invalide file extension"));

	}

}
