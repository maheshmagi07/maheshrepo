/**
 * {Purpose of File}
 * In case of Extension : Use - Please see the {@link com.alcon.} class for true identity
 */
package com.alcon.extraction.service.util.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alcon.extraction.config.PropertyConfig;
import com.alcon.extraction.dto.DeviceData;
import com.alcon.extraction.service.util.Constants;
import com.amazonaws.services.textract.model.Block;



/**
 * @author AbhilashaP
 *
 */
@Service
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeviceIdentifierImpl implements DeviceIdentifier {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PropertyConfig propConfig;
	
	@Autowired
	private ApplicationContext appContext;
	
	@Autowired
	private DeviceData devicedata;
	
	List<String> listOfLineValues = new ArrayList<String>();
	

	//NEW METHoD//
	@Override
	public DeviceData analyseReport(List<Block> docInfo)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		log.info("Method analyseReport starts");
		Device identifierObj = null;
		String deviceName = this.readBlocks(docInfo);
		switch (deviceName) {
		case Constants.DEVICE_ATLAS:
			identifierObj = (Device) appContext.getBean(AtlasIdentifiers.class);
			break;
		default:
			break;
		}

		if (identifierObj != null) {

			devicedata = identifierObj.analyseReport(deviceName, docInfo);
			
		}
		log.info("Method analyseReport ends");
		return devicedata;
	}

	//NEW METHoD//
	private String readBlocks(List<Block> docInfo) {
		return this.identifyDeviceName(docInfo);
	}

	//NEW METHoD//
	@Override
	public String identifyDeviceName(List<Block> docInfo) {
		log.info("Method  identifyDeviceName starts");
		Iterator<Block> blockIterator = docInfo.iterator();
		listOfLineValues = new ArrayList<>();
		while (blockIterator.hasNext()) {
			Block block = blockIterator.next();
			if (block.getBlockType().equalsIgnoreCase("LINE")) {
				listOfLineValues.add(block.getText());
			}
		}
		
		//Identify device from Footer
		int size = listOfLineValues.size();
		AtomicReference<String> device = new AtomicReference<String>(Constants.DEVICE_NAME_NOT_FOUND);
		List<String> devices = propConfig.getDevices().stream().map(deviceName -> deviceName.toUpperCase()).collect(Collectors.toList());
		
		Predicate<String> checkDevicePresent = (value) -> {
			return Arrays.stream(value.split(Constants.BLANK_SPACE)).filter(val ->  devices.contains(val)).findFirst().isPresent() ;
		};
		
		listOfLineValues.stream().skip(size-10).map(value -> value.toUpperCase()).filter(checkDevicePresent).findFirst().ifPresent(deviceText -> {
			Arrays.stream(deviceText.split(Constants.BLANK_SPACE)).filter(val ->  devices.contains(val)).findFirst().ifPresent(device::set);
		});
		log.info("Method identifyDeviceName ends");
		return device.get();
	}

}
