/**
 * {Purpose of File}
 * In case of Extension : Use - Please see the {@link com.alcon.} class for true identity
 */
package com.alcon.extraction.service.util.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alcon.extraction.config.AtlasPropReader;
import com.alcon.extraction.config.SectionAttributesReader;
import com.alcon.extraction.dto.AxialCurvature;
import com.alcon.extraction.dto.DeviceData;
import com.alcon.extraction.dto.Keratometry;
import com.alcon.extraction.dto.KeyValue;
import com.alcon.extraction.dto.PatientDemographics;
import com.alcon.extraction.service.util.Constants;
import com.alcon.extraction.service.util.Helper;

/**
 * @author AbhilashaP
 *
 */
@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PatientDTOHelper {

	@Autowired
	private AtlasPropReader atlasPropReader;

	@Autowired
	private SectionAttributesReader sectionAttributesReader;

	private List<String> sections = new ArrayList<String>();

	private List<Integer> positions = new ArrayList<Integer>();

	private List<Integer> sectionsInRow = new ArrayList<Integer>();

	private List<String> eyeType = new ArrayList<String>();

	Map<String, List<KeyValue>> mapOfSectionsRowWise = new LinkedHashMap<String, List<KeyValue>>();

	Map<Integer, List<KeyValue>> mapOfRows = new LinkedHashMap<Integer, List<KeyValue>>();

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public PatientDemographics checkAndAddDemographicDetails(List<String> myList, List<String> companies,
			PatientDemographics patientDemographics, List<KeyValue> listOfValueWithConfidence) {
		log.info("Method checkAndAddDemographicDetails starts");
		
		String possibleName = myList.get(atlasPropReader.getPosition().get(Constants.PATIENT_NAME));
		String possibleCompany = myList.get(atlasPropReader.getPosition().get(Constants.DEVICE_COMPANY));
		KeyValue patientDetails = new KeyValue();
		
		if (possibleName != null && Helper.testContains(possibleName, Constants.COMMA_SEPERATOR)) {
			OptionalInt result =	IntStream.range(0,listOfValueWithConfidence.size()).filter(innerIndex -> listOfValueWithConfidence.get(innerIndex).getValue().equalsIgnoreCase(possibleName)).findFirst();
			patientDetails = listOfValueWithConfidence.get(result.getAsInt());
			
			patientDemographics.getLastName().setValue(Helper.subString(patientDetails.getValue().toString(), 0, patientDetails.getValue().toString().indexOf(Constants.COMMA_SEPERATOR)));
			patientDemographics.getLastName().setConfidence(patientDetails.getConfidence());
			
			patientDemographics.getFirstName().setValue(Helper.subString(patientDetails.getValue().toString(),patientDetails.getValue().toString().indexOf(Constants.COMMA_SEPERATOR) + 1, patientDetails.getValue().toString().length()));
			patientDemographics.getFirstName().setConfidence(patientDetails.getConfidence());
		} else {
			patientDemographics.getLastName().setValue(patientDetails.getValue().toString());
			patientDemographics.getLastName().setConfidence(patientDetails.getConfidence());
		}

		if (!Helper.testContains(companies, possibleCompany)) {
			myList.add(atlasPropReader.getPosition().get(Constants.DEVICE_COMPANY), Constants.BLANK);
		}

		String possibleMRN = myList.get(atlasPropReader.getPosition().get(Constants.PATIENT_MRN));
		String possibleDOB = myList.get(atlasPropReader.getPosition().get(Constants.PATIENT_DOB));
		if (possibleMRN.length() > 3) {
			OptionalInt result =	IntStream.range(0,listOfValueWithConfidence.size()).filter(innerIndex -> listOfValueWithConfidence.get(innerIndex).getValue().equalsIgnoreCase(possibleMRN)).findFirst();
			patientDetails = listOfValueWithConfidence.get(result.getAsInt());
			
			patientDemographics.getPatientMRN().setValue(patientDetails.getValue().toString());
			patientDemographics.getPatientMRN().setConfidence(patientDetails.getConfidence());
		}

		if (Helper.checkDOBPattern(possibleDOB)) {
			OptionalInt result =	IntStream.range(0,listOfValueWithConfidence.size()).filter(innerIndex -> listOfValueWithConfidence.get(innerIndex).getValue().equalsIgnoreCase(possibleDOB)).findFirst();
			patientDetails = listOfValueWithConfidence.get(result.getAsInt());
			
			patientDemographics.getPatientDOB().setValue(patientDetails.getValue().toString());
			patientDemographics.getPatientDOB().setConfidence(patientDetails.getConfidence());
		}
		log.info("Method checkAndAddDemographicDetails end");
		return patientDemographics;
	}


	private void checkDetailedSections(List<Float> xAxisValue, DeviceData device) {
		log.info("Method checkDetailedSections starts");
		AtomicInteger previousNumber = new AtomicInteger(0);
		// Think how to reset this
		device.getPatient().getEyeAssessment().getOd().setAxialCurvature(new ArrayList<>());
		device.getPatient().getEyeAssessment().getOs().setAxialCurvature(new ArrayList<>());

		device.getPatient().getEyeAssessment().getOd().setKeratometry(new ArrayList<>());
		device.getPatient().getEyeAssessment().getOs().setKeratometry(new ArrayList<>());
		
		device.getPatient().getEyeAssessment().getOd().setImageSimulation(new ArrayList<>());
		device.getPatient().getEyeAssessment().getOs().setImageSimulation(new ArrayList<>());
		
		AtomicInteger mapIndex = new AtomicInteger(0);
		
		mapOfRows.keySet().stream().forEach(rowIndex -> {
			AtomicReference<List<KeyValue>> ref = new AtomicReference<List<KeyValue>>();
			mapOfSectionsRowWise.keySet().stream()
					.filter(key -> getRowNumber(key) <= rowIndex && getRowNumber(key) >= previousNumber.get())
					.forEach(key -> {
						String eyeType = identifyEyeFromKey(key);
						String section = identifySectionFromKey(key);
						if (ref.get() == null) {
							ref.set(new ArrayList<>(mapOfSectionsRowWise.get(key)));
						}
						switch (section.toUpperCase()) {
						case Constants.SECTION_AXIAL_CURVATURE: {
							fillUpEyeTypeWithAxialC(ref.get(), device, eyeType.toUpperCase(),sectionsInRow.get(mapIndex.get()) );
							break;
						}
						case Constants.SECTION_KERATOMETRY: {
							fillUpEyeTypeWithKeret(ref.get(), device, eyeType.toUpperCase());
							break;
						}
						case Constants.SECTION_IMAGE_SIMULATION: {
							fillUpEyeTypeWithImageSim(ref.get(), device, eyeType.toUpperCase(),sectionsInRow.get(mapIndex.get()));
							break;
						}

						default:
							break;
						}

					});
			mapIndex.getAndAdd(1);
			previousNumber.set(rowIndex + 1);
		});
		log.info("Method checkDetailedSections ends");
	}

	private void fillUpEyeTypeWithImageSim(List<KeyValue> list, DeviceData device, String eyeType, Integer numberOfSections) {
	}

	private Integer getRowNumber(String key) {
		return key.lastIndexOf(Constants.UNDERSCORE) != -1
				? Integer.parseInt(key.substring(key.lastIndexOf(Constants.UNDERSCORE) + 1))
				: -1;
	}
	
	
	private double getPolygonValueForKeretometry(List<KeyValue> list, String eyeType)
	{
		log.info("Method getPolygonValueForKeretometry starts");
		List<KeyValue> filteredList = new ArrayList<KeyValue>();
		double polyGonValue = 0;
		filteredList = list.stream().filter(data -> data.getValue().toString().equalsIgnoreCase(Constants.KERATOMETRY_SIDE)).collect(Collectors.toList());
		if(filteredList.size() > 1)
		{
			filteredList = list.stream().filter(data -> data.getValue().toString().equalsIgnoreCase(eyeType)).collect(Collectors.toList());
			polyGonValue = filteredList.get(0).getPolygonXaxisValue();
		}
		else
		{
			polyGonValue = filteredList.get(0).getPolygonXaxisValue();
		}
		log.info("Method getPolygonValueForKeretometry ends");
		return polyGonValue;
	}
	
	private void fillUpEyeTypeWithKeret(List<KeyValue> list, DeviceData device, String eyeType) {
		log.info("Method fillUpEyeTypeWithKeret starts");
		double polyGonValue = 0;
		List<KeyValue> subList = new ArrayList<KeyValue>();
		Keratometry keratometryObj = new Keratometry();
		for (int index = 0; index < list.size(); index++) {
			// Checking Record date
			if (Helper.checkDOBPattern(list.get(index).getValue()) && keratometryObj.getRecorDdate().getValue() == null) {
				keratometryObj.setRecorDdate(list.get(index));

				IntStream.range(index, index + 15).filter(innerIndex -> Helper.checkTimePattern(list.get(innerIndex).getValue()))
						.findFirst().ifPresent(innerIndex -> {
							keratometryObj.getRecorDdate().setValue(keratometryObj.getRecorDdate().getValue()
									.concat(Constants.BLANK_SPACE + list.get(innerIndex).getValue()));
							keratometryObj.getRecorDdate().setConfidence( list.get(innerIndex).getConfidence());
							list.remove(innerIndex);

						});
				list.remove(index--);
				index--; // think abt logic to do this -- operation
				continue;
			}
			if (Helper.testContains(list.get(index).getValue(), Constants.TOTAL_ASTIGMATISM)
					&& keratometryObj.getTotalAstigmatism().getValue() == null) {
				if(list.get(index).getValue().contains(sectionAttributesReader.getValidator().get(Constants.ASTIGMATISM.toLowerCase()).get(0))){
					keratometryObj.getTotalAstigmatism().setValue(list.get(index).getValue().substring(list.get(index).getValue().indexOf(Constants.TOTAL_ASTIGMATISM)+18)); //think about case insensitive search
					keratometryObj.getTotalAstigmatism().setConfidence(list.get(index).getConfidence());
				}else {
					keratometryObj.getTotalAstigmatism().setValue(list.get(index+1).getValue());
					keratometryObj.getTotalAstigmatism().setConfidence(list.get(index+1).getConfidence());
					list.remove(index--);
				}
				list.remove(index--);
				continue;
			}
			
		}
		
		OptionalInt result =	IntStream.range(0,list.size()).filter(innerIndex -> list.get(innerIndex).getValue().equalsIgnoreCase(Constants.KERATOMETRY_END_SECTION)).findFirst();
		if(result.getAsInt() == 0)
		{
			result =	IntStream.range(1,list.size()).filter(innerIndex -> list.get(innerIndex).getValue().equalsIgnoreCase(Constants.KERATOMETRY_END_SECTION)).findFirst();
		}
		subList = list.subList(0, result.getAsInt());
		List<KeyValue> tempList = new LinkedList<KeyValue>();
		polyGonValue = getPolygonValueForKeretometry(subList,eyeType);
		
		if(polyGonValue < .50)
		{
			tempList = subList.stream().filter(data -> data.getPolygonXaxisValue() < .50).filter(data -> data.getValue().contains("(")).collect(Collectors.toList());
		}
		else if(polyGonValue > .50)
		{
			tempList = subList.stream().filter(data -> data.getPolygonXaxisValue() >= .50).filter(data -> data.getValue().contains("(")).collect(Collectors.toList());
		}
		else
		{
			tempList = subList;
		}
		
		for (int index = 0; index < tempList.size(); index++) {
			if (Helper.testContainsInLower(sectionAttributesReader.getKeratometryAttributes(), tempList.get(index).getValue())
					&& Helper.testContains(tempList.get(index).getValue(), Constants.SIM) && keratometryObj.getSimKsSteepK().getValue() == null && keratometryObj.getSimKsFlatK().getValue() == null)
			{
					keratometryObj.getSimKsSteepK().setValue(tempList.get(index+1).getValue());
					keratometryObj.getSimKsSteepK().setConfidence(tempList.get(index+1).getConfidence());
					keratometryObj.getSimKsFlatK().setValue(tempList.get(index+2).getValue());
					keratometryObj.getSimKsFlatK().setConfidence(tempList.get(index+2).getConfidence());
					continue;
			}	
			
			if (Helper.testContains(tempList.get(index).getValue(), Constants.CENTRAL) && keratometryObj.getCentralSteepKValue1().getValue() == null
					&& keratometryObj.getCentralSteepKValue2().getValue() == null && keratometryObj.getCentralFlatKValue1().getValue() == null
					&& keratometryObj.getCentralFlatKValue2().getValue() == null)
			{
					keratometryObj.getCentralSteepKValue1().setValue(tempList.get(index+1).getValue());
					keratometryObj.getCentralSteepKValue1().setConfidence(tempList.get(index+1).getConfidence());

					keratometryObj.getCentralSteepKValue2().setValue(tempList.get(index+2).getValue());
					keratometryObj.getCentralSteepKValue2().setConfidence(tempList.get(index+2).getConfidence());
					
					keratometryObj.getCentralFlatKValue1().setValue(tempList.get(index+3).getValue());
					keratometryObj.getCentralFlatKValue1().setConfidence(tempList.get(index+3).getConfidence());
					
					keratometryObj.getCentralFlatKValue2().setValue(tempList.get(index+4).getValue());
					keratometryObj.getCentralFlatKValue2().setConfidence(tempList.get(index+4).getConfidence());
					continue;
			}
			
			if (Helper.testContains(tempList.get(index).getValue(), Constants.MID_PERIPHERY)) {

				keratometryObj.getMidPeripherySteepKValue1().setValue(tempList.get(index+1).getValue());
				keratometryObj.getMidPeripherySteepKValue1().setConfidence(tempList.get(index+1).getConfidence());
				
				keratometryObj.getMidPeripherySteepKValue2().setValue(tempList.get(index+2).getValue());
				keratometryObj.getMidPeripherySteepKValue2().setConfidence(list.get(index+2).getConfidence());
				
				keratometryObj.getMidPeripheryFlatKValue1().setValue(tempList.get(index+3).getValue());
				keratometryObj.getMidPeripheryFlatKValue1().setConfidence(list.get(index+3).getConfidence());
				
				keratometryObj.getMidPeripheryFlatKValue2().setValue(tempList.get(index+4).getValue());
				keratometryObj.getMidPeripheryFlatKValue2().setConfidence(tempList.get(index+4).getConfidence());
				continue;
			}
			
			if (Helper.testStartsWith(tempList.get(index).getValue(), Constants.PERIPHERY) ) {
				
				keratometryObj.getPeripherySteepKValue1().setValue(tempList.get(index+1).getValue());
				keratometryObj.getPeripherySteepKValue1().setConfidence(tempList.get(index+1).getConfidence());
				
				keratometryObj.getPeripherySteepKValue2().setValue(tempList.get(index+2).getValue());
				keratometryObj.getPeripherySteepKValue2().setConfidence(tempList.get(index+2).getConfidence());
				
				keratometryObj.getPeripheryFlatKValue1().setValue(tempList.get(index+3).getValue());
				keratometryObj.getPeripheryFlatKValue1().setConfidence(tempList.get(index+3).getConfidence());

				keratometryObj.getPeripheryFlatKValue2().setValue(tempList.get(index+4).getValue());
				keratometryObj.getPeripheryFlatKValue2().setConfidence(tempList.get(index+4).getConfidence());	
				continue;
			}
		}
		
		if (eyeType.equalsIgnoreCase(Constants.EYE_TYPE_OD)) {
			device.getPatient().getEyeAssessment().getOd().getKeratometry().add(keratometryObj);
		}

		if (eyeType.equalsIgnoreCase(Constants.EYE_TYPE_OS)) {
			device.getPatient().getEyeAssessment().getOs().getKeratometry().add(keratometryObj);
		}
		log.info("Method fillUpEyeTypeWithKeret ends");
	}
	
	private void fillUpEyeTypeWithAxialC(List<KeyValue> list, DeviceData device, String eyeType, Integer numberOfSections) {
		log.info("Method fillUpEyeTypeWithAxialC starts");
		AxialCurvature axialCurvatureObj = new AxialCurvature();
		for (int index = 0; index < list.size(); index++) {
			// Checking ANSI Standard
			if (list.get(index).getValue().startsWith("ANSI") && axialCurvatureObj.getInstrumentANSIStandard().getValue() == null) {
				axialCurvatureObj.getInstrumentANSIStandard().setValue((list.get(index).getValue()));
				axialCurvatureObj.getInstrumentANSIStandard().setConfidence((list.get(index).getConfidence()));
				list.remove(index--);
				continue;
			}

			// Checking Record date
			if (Helper.checkDOBPattern(list.get(index).getValue()) && axialCurvatureObj.getRecorDdate().getValue() == null) {
				axialCurvatureObj.getRecorDdate().setValue(list.get(index).getValue());

				IntStream.range(index, index + 15).filter(innerIndex -> Helper.checkTimePattern(list.get(innerIndex).getValue()))
						.findFirst().ifPresent(innerIndex -> {
							axialCurvatureObj.getRecorDdate().setValue(axialCurvatureObj.getRecorDdate().getValue()
									.concat(Constants.BLANK_SPACE + list.get(innerIndex).getValue()));
							axialCurvatureObj.getRecorDdate().setConfidence(list.get(innerIndex).getConfidence());
							list.remove(innerIndex);

						});
				list.remove(index--);
				index--; // think abt logic to do this -- operation
				continue;
			}
			
			/////ADD CONFIDENCE LEVEL HERE
			if (Helper.testContainsInLower(sectionAttributesReader.getAxialCurvatureAttributes(), list.get(index).getValue())
					&& Helper.testContains(list.get(index).getValue(), Constants.STEEP) && axialCurvatureObj.getSteepK().getValue() == null) {
				IntStream.range(index, index + 3)
						.filter(currentIndex -> list.get(currentIndex).getValue()
								.contains(sectionAttributesReader.getValidator().get(Constants.STEEP_K).get(0)))
						.findFirst().ifPresent(currentIndex -> {
							axialCurvatureObj.getSteepK().setValue(list.get(currentIndex).getValue());
							axialCurvatureObj.getSteepK().setConfidence(list.get(currentIndex).getConfidence());
							list.remove(currentIndex);
						});
				;
				list.remove(index--);
				index--;// think abt logic to do this -- operation
				continue;
			}

			if (Helper.testContainsInLower(sectionAttributesReader.getAxialCurvatureAttributes(), list.get(index).getValue())
					&& Helper.testContains(list.get(index).getValue(), Constants.FLAT) && axialCurvatureObj.getFlatK().getValue() == null) {
				IntStream.range(index, index + 3)
						.filter(currentIndex -> list.get(currentIndex).getValue()
								.contains(sectionAttributesReader.getValidator().get(Constants.FLAT_K).get(0)))
						.findFirst().ifPresent(currentIndex -> {
							axialCurvatureObj.getFlatK().setValue(list.get(currentIndex).getValue());
							axialCurvatureObj.getFlatK().setConfidence(list.get(currentIndex).getConfidence());
							list.remove(currentIndex);
						});
				;
				list.remove(index--);
				index--;// think abt logic to do this -- operation
				continue;
			}

			if (Helper.testContainsInLower(sectionAttributesReader.getAxialCurvatureAttributes(), list.get(index).getValue())
					&& Helper.testContains(list.get(index).getValue(), Constants.ASTIGMATISM)
					&& axialCurvatureObj.getAstigmatism().getValue() == null) {
				IntStream.range(index, index + 3)
						.filter(currentIndex -> list.get(currentIndex).getValue().contains(
								sectionAttributesReader.getValidator().get(Constants.ASTIGMATISM.toLowerCase()).get(0)))
						.findFirst().ifPresent(currentIndex -> {
							axialCurvatureObj.getAstigmatism().setValue(list.get(currentIndex).getValue());
							axialCurvatureObj.getAstigmatism().setConfidence(list.get(currentIndex).getConfidence());
							list.remove(currentIndex);
						});
				;
				list.remove(index--);
				index--;// think abt logic to do this -- operation
				continue;
			}

			if (Helper.testContainsInLower(sectionAttributesReader.getAxialCurvatureAttributes(), list.get(index).getValue())
					&& Helper.testContains(list.get(index).getValue(), Constants.ECCENTRICITY)
					&& axialCurvatureObj.getEccentricity().getValue() == null) {
				axialCurvatureObj.getEccentricity().setValue(list.get(index + 1).getValue());
				axialCurvatureObj.getEccentricity().setConfidence(list.get(index + 1).getConfidence());
				list.remove(index);
				list.remove(index);
				index = index - 2;
				continue;
			}

			if (Helper.testContainsInLower(sectionAttributesReader.getAxialCurvatureAttributes(), list.get(index).getValue())
					&& Helper.testContains(list.get(index).getValue(), Constants.HVID) && axialCurvatureObj.getHvid().getValue() == null) {
				axialCurvatureObj.getHvid().setValue(list.get(index + 1).getValue());
				axialCurvatureObj.getHvid().setConfidence(list.get(index + 1).getConfidence());
				list.remove(index);
				list.remove(index);
				index = index - 2;
				continue;
			}

		}

		// Need one map that says row 1 has 2 sections and get the info of 2 sections in
		// single loop
		if (eyeType.equalsIgnoreCase(Constants.EYE_TYPE_OD)) {
			device.getPatient().getEyeAssessment().getOd().getAxialCurvature().add(axialCurvatureObj);
		}

		if (eyeType.equalsIgnoreCase(Constants.EYE_TYPE_OS)) {
			device.getPatient().getEyeAssessment().getOs().getAxialCurvature().add(axialCurvatureObj);
		}
		log.info("Method fillUpEyeTypeWithAxialC ends");
	}

	private String identifySectionFromKey(String key) {
		if (key.indexOf(Constants.UNDERSCORE) > -1
				&& key.lastIndexOf(Constants.UNDERSCORE) != key.indexOf(Constants.UNDERSCORE)) {
			return key.substring(key.indexOf(Constants.UNDERSCORE) + 1, key.lastIndexOf(Constants.UNDERSCORE));
		} else {
			return Constants.BLANK;
		}

	}

	private String identifyEyeFromKey(String key) {

		return key.indexOf(Constants.UNDERSCORE) > -1 ? key.substring(0, key.indexOf(Constants.UNDERSCORE))
				: Constants.BLANK;
	}

	private String createKey(int currentIndex) {
		StringBuilder keyBuilder = new StringBuilder(eyeType.get(currentIndex));
		keyBuilder.append(Constants.UNDERSCORE);
		keyBuilder.append(sections.get(currentIndex));
		keyBuilder.append(Constants.UNDERSCORE);
		keyBuilder.append(currentIndex);
		return keyBuilder.toString();
	}

	//NEW METHoD//
	public void identifyQuadrants(List<KeyValue> listOfValueWithConfidence, List<Float> xAxisValue, DeviceData device) {
		log.info("Method identifyQuadrants starts");
		sections = new ArrayList<String>(); // Identify Quadrants
		eyeType = new ArrayList<String>(); // Identify OD/OS Section
		positions = new ArrayList<Integer>();
		
		List<String> quadrants = atlasPropReader.getQuadrants().stream().map(name -> name.toUpperCase())
				.collect(Collectors.toList());
		// Eye type follows section name in 99% cases, identifying Quadrant, it's
		// position & eye type
		IntStream.range(0, listOfValueWithConfidence.size())
				.filter(quadrantindex -> quadrants.contains(listOfValueWithConfidence.get(quadrantindex).getValue().toUpperCase())).forEach(index -> {
					sections.add(listOfValueWithConfidence.get(index).getValue());
					positions.add(index);
					if (listOfValueWithConfidence.size() > index + 1
							&& atlasPropReader.getEyes().contains(listOfValueWithConfidence.get(index + 1).getValue().toUpperCase())) {
						eyeType.add(listOfValueWithConfidence.get(index + 1).getValue());
					} else {
						eyeType.add(Constants.BLANK);
					}
				});
		this.identifyIndividualSections(listOfValueWithConfidence, xAxisValue,device);
		log.info("Method identifyQuadrants ends");
	}

	//NEW METHoD//
	private void identifyIndividualSections(List<KeyValue> listOfValueWithConfidence, List<Float> xAxisValue, DeviceData device) {
		log.info("Method identifyIndividualSections starts");
		device.getPatient().getEyeAssessment().setFacility(listOfValueWithConfidence.get(4).getValue());
		sectionsInRow = new ArrayList<Integer>();
		// Map that will contain individual sections and data it holds in a block
		mapOfSectionsRowWise = new LinkedHashMap<String, List<KeyValue>>();
		// Identify number of sections in 1 row
		IntStream.range(1, positions.size())
				.filter(index -> positions.get(index) - positions.get(index - 1) > atlasPropReader.getRow())
				.forEach(sectionsInRow::add);

		AtomicInteger initialIndex = new AtomicInteger(0);
		mapOfRows = new LinkedHashMap<Integer, List<KeyValue>>();
		// Identify the sections in individual row and put it in a map
		// Think about 45 number logic
		IntStream.range(0, sectionsInRow.size()).forEach(index -> {
			int finalRange = listOfValueWithConfidence.size() > 500 * sectionsInRow.get(index) ? 500 * sectionsInRow.get(index)
					: listOfValueWithConfidence.size();
			List<KeyValue> finalList = listOfValueWithConfidence.subList(positions.get(index) - atlasPropReader.getRowStart(), finalRange);

			IntStream.range(initialIndex.get(), sectionsInRow.get(index)).forEach(currentIndex -> {
				mapOfSectionsRowWise.put(createKey(currentIndex), finalList);
				initialIndex.set(currentIndex + 1);
				if (currentIndex + 1 == sectionsInRow.get(index)) {
					mapOfRows.put(currentIndex, finalList);
				}

			});
		});
		AtomicInteger lastRow = new AtomicInteger(0);
		List<KeyValue> finalList = listOfValueWithConfidence.subList(positions.get(initialIndex.get()) - atlasPropReader.getRowStart(),
				listOfValueWithConfidence.size());
		IntStream.range(initialIndex.get(), sections.size()).forEach(currentIndex -> {
			mapOfSectionsRowWise.put(createKey(currentIndex), finalList);
			lastRow.addAndGet(1);
			if (currentIndex + 1 == sections.size()) {
				mapOfRows.put(currentIndex, finalList);// this could be problematic for 3-4 sections in a row
			}

		});
		sectionsInRow.add(lastRow.get());
		log.info("Method identifyIndividualSections ends");
		checkDetailedSections(xAxisValue,device);
	}
}
