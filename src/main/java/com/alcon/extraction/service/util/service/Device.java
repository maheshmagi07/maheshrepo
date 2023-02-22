/**
 * {Purpose of File}
 * In case of Extension : Use - Please see the {@link com.alcon.} class for true identity
 */
package com.alcon.extraction.service.util.service;

import java.util.List;

import com.alcon.extraction.dto.DeviceData;
import com.amazonaws.services.textract.model.Block;



/**
 * @author AbhilashaP
 *
 */
public interface Device {

	//DeviceData analyseReport(String deviceName, List<String> myList, List<Double> heights) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException ;

	//NEW METHoD//
	DeviceData analyseReport(String deviceName, List<Block> docInfo) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException ;
	
}
