package com.alcon.extraction.service.util.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.alcon.extraction.config.AtlasPropReader;
import com.alcon.extraction.config.SectionAttributesReader;
import com.alcon.extraction.dto.KeyValue;
import com.alcon.extraction.dto.PatientDemographics;

@RunWith(MockitoJUnitRunner.class)
public class PatientDTOHelperTest {
	
	@InjectMocks
    private PatientDTOHelper patientDTOHelper;

	@InjectMocks
    private AtlasPropReader atlasPropReader;

	@InjectMocks
    private SectionAttributesReader sectionAttributesReader;


    private List<String> sections = new ArrayList<String>();

    private List<Integer> positions = new ArrayList<Integer>();

    private List<Integer> sectionsInRow = new ArrayList<Integer>();

    private List<String> eyeType = new ArrayList<String>();

    Map<String, List<KeyValue>> mapOfSectionsRowWise = new LinkedHashMap<String, List<KeyValue>>();

    Map<Integer, List<KeyValue>> mapOfRows = new LinkedHashMap<Integer, List<KeyValue>>();

    @Before
    public void setUp() {
  //      MockitoAnnotations.initMocks(this);
    }
    
    
    @Ignore
    @Test
    public void checkAndAddDemographicDetailsTest() {
    	// PatientDemographics patientDemographicsResponse = patientDTOHelper.checkAndAddDemographicDetails(myList, myCompany, patientDemographics, keyValues);
        // Assert.assertNotNull(patientDemographicsResponse);
    }
}
