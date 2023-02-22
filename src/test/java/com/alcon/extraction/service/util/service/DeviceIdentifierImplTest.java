package com.alcon.extraction.service.util.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.alcon.extraction.config.PropertyConfig;
import com.alcon.extraction.dto.DeviceData;

@RunWith(MockitoJUnitRunner.class)
public class DeviceIdentifierImplTest {
	
	
	@Mock
	private PropertyConfig propConfig;
	
	@Mock
	private ApplicationContext appContext;
	
	@Mock
	private DeviceData devicedata;
	
	@Mock
	private DeviceIdentifier deviceIdentifier;
	
	List<String> listOfLineValues = new ArrayList<String>();
	
	 @Before
	    public void setUp() {
	        MockitoAnnotations.initMocks(this);
	    }
	 
	 @Ignore
	 @Test
	 public void analyseReportTest()
	 {
		// deviceIdentifier.analyseReport(null); 
	 }

}
