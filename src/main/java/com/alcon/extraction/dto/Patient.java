/**
 * {Purpose of File}
 * In case of Extension : Use - Please see the {@link com.alcon.} class for true identity
 */
package com.alcon.extraction.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author AbhilashaP
 *
 */
@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Patient{

	@Autowired
	private PatientDemographics demographics;
	
	@Autowired
	private Assessment eyeAssessment;

	public PatientDemographics getDemographics() {
		return demographics;
	}

	public void setDemographics(PatientDemographics demographics) {
		this.demographics = demographics;
	}

	public Assessment getEyeAssessment() {
		return eyeAssessment;
	}

	public void setEyeAssessment(Assessment eyeAssessment) {
		this.eyeAssessment = eyeAssessment;
	}
	
	
	
}
