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

import com.alcon.extraction.config.PropertyConfig;
import com.alcon.extraction.dto.DeviceData;
import com.alcon.extraction.dto.KeyValue;

@RunWith(MockitoJUnitRunner.class)
public class AtlasIdentifiersTest {

	@Mock
	private PropertyConfig propConfig;

	@Mock
	private DeviceData device;

	@Mock
	private PatientDTOHelper patientDTOHelper;
	
	@InjectMocks
	private AtlasIdentifiers atlasIdentifiers;
	
	List<String> listOfLineValues = new ArrayList<String>();
	
	List<Float> heightsArray = new ArrayList<Float>();
	
	List<Float> xAxisValue = new ArrayList<Float>();
	
	List<KeyValue> listOfValueWithConfidence = new ArrayList<KeyValue>();
	 @Before
	    public void setUp() {
	        MockitoAnnotations.initMocks(this);
	    }
	 
	 @Ignore
	 @Test
	    public void analyseReportTest() {
		// atlasIdentifiers.analyseReport(null, null);
	 }
}
