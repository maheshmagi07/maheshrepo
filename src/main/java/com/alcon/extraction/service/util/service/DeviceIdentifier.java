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
public interface DeviceIdentifier {


	//NEW METHoD//
	default DeviceData identifyreportTypeAndAnalyze(List<Block> docInfo) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		return this.analyseReport(docInfo);

	}

	//NEW METHoD//
	DeviceData analyseReport( List<Block> docInfo) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException ;

	//NEW METHoD//
	String identifyDeviceName(List<Block> docInfo);

}
