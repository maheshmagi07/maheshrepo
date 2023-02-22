/**
 * {Purpose of File}
 * In case of Extension : Use - Please see the {@link com.alcon.} class for true identity
 */
package com.alcon.extraction.service.util.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alcon.extraction.dto.DeviceData;
import com.amazonaws.services.textract.model.Block;
/**
 * @author AbhilashaP
 *
 */
@Component
public class IOLMaster700Identifiers implements Device{


//	@Override
//	public DeviceData analyseReport(String deviceName, List<String> myList, List<Double> heights) {
//		return null;
//	}

	@Override
	public DeviceData analyseReport(String deviceName, List<Block> docInfo)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		// TODO Auto-generated method stub
		return null;
	}


}
