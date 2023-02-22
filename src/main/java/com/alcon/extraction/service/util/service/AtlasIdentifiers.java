/**
 * {Purpose of File}
 * In case of Extension : Use - Please see the {@link com.alcon.} class for true identity
 */
package com.alcon.extraction.service.util.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alcon.extraction.config.PropertyConfig;
import com.alcon.extraction.dto.DeviceData;
import com.alcon.extraction.dto.KeyValue;
import com.amazonaws.services.textract.model.Block;
 

/**
 * @author AbhilashaP
 *
 */
@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AtlasIdentifiers implements Device {

	@Autowired
	private PropertyConfig propConfig;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DeviceData device;

	@Autowired
	private PatientDTOHelper patientDTOHelper;
	
	List<String> listOfLineValues = new ArrayList<String>();
	
	List<Float> heightsArray = new ArrayList<Float>();
	
	List<Float> xAxisValue = new ArrayList<Float>();
	
	List<KeyValue> listOfValueWithConfidence = new ArrayList<KeyValue>();
	
	//NEW METHoD//
	@Override
	public DeviceData analyseReport(String deviceName, List<Block> docInfo)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		log.info("Method analyseReport starts");
		this.fetchDetails(docInfo);
		device.setDeviceName(deviceName);
		device.setReportTitle(identifyReport(docInfo));
		identifyPatientDemographics(docInfo);
		log.info(device.getPatient().getDemographics().toString());
		identifyAllQuadrants(docInfo);
		log.info("Method analyseReport ends");
		return device;
	}

	//NEW METHoD//
	private void fetchDetails(List<Block> docInfo) {
		log.info("Method fetchDetails starts");
		Iterator<Block> blockIterator = docInfo.iterator();
		
		listOfLineValues = new ArrayList<>();
		heightsArray = new ArrayList<Float>();
		xAxisValue = new ArrayList<Float>();
		listOfValueWithConfidence = new ArrayList<KeyValue>();
		
		while (blockIterator.hasNext()) {
			Block block = blockIterator.next();
			if (block.getBlockType().equalsIgnoreCase("LINE")) {
				KeyValue keyValueObj = new KeyValue(block.getText(),block.getConfidence(),
						block.getGeometry().getPolygon().get(0).getX());
				listOfLineValues.add(block.getText());
				heightsArray.add(block.getGeometry().getBoundingBox().getHeight());
				listOfValueWithConfidence.add(keyValueObj);
				////////ADD CHECKS HERE
				xAxisValue.add(block.getGeometry().getPolygon().get(0).getX());
			}
		}
		log.info("Method fetchDetails ends");
	}

	//NEW METHoD//
	private void identifyAllQuadrants(List<Block> docInfo) {
	
		patientDTOHelper.identifyQuadrants(listOfValueWithConfidence,xAxisValue,device);
		
	}

	//NEW METHoD//
	private void identifyPatientDemographics(List<Block> docInfo) {

		patientDTOHelper.checkAndAddDemographicDetails(listOfLineValues, propConfig.getCompanies(),
				device.getPatient().getDemographics(),listOfValueWithConfidence);

		
	}

	//NEW METHoD//
	private String identifyReport(List<Block> docInfo) {
		
		Double maxHeight = heightsArray.stream().mapToDouble(x -> Double.valueOf(x.toString())).max().getAsDouble();
		int index = 0;

		for (int i = 0; i < heightsArray.size(); i++) {
			Double currentNumber = Double.valueOf(heightsArray.get(i).toString());
			if (currentNumber.equals(maxHeight)) {
				index = i;
			}
		}
		return listOfLineValues.get(index);
	}

	

}
