/**
 * {Purpose of File}
 * In case of Extension : Use - Please see the {@link com.alcon.} class for true identity
 */
package com.alcon.extraction.dto;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author AbhilashaP
 *
 */
@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PatientDemographics{

	private KeyValue firstName;
	
	private KeyValue lastName;
	
	private KeyValue patientMRN;
	
	private KeyValue patientDOB;

	public KeyValue getFirstName() {
		
		if(this.firstName == null)
		{
			this.firstName = new KeyValue();
		}
		return firstName;
	}

	public void setFirstName(KeyValue firstName) {
		this.firstName = firstName;
	}

	public KeyValue getLastName() {
		
		if(this.lastName == null)
		{
			this.lastName = new KeyValue();
		}
		return lastName;
	}

	public void setLastName(KeyValue lastName) {
		this.lastName = lastName;
	}

	public KeyValue getPatientMRN() {
		if(this.patientMRN == null)
		{
			this.patientMRN = new KeyValue();
		}
		return patientMRN;
	}

	public void setPatientMRN(KeyValue patientMRN) {
		this.patientMRN = patientMRN;
	}

	public KeyValue getPatientDOB() {
		if(this.patientDOB == null)
		{
			this.patientDOB = new KeyValue();
		}
		return patientDOB;
	}

	public void setPatientDOB(KeyValue patientDOB) {
		this.patientDOB = patientDOB;
	}

	@Override
	public String toString() {
		return "PatientDemographics [firstName=" + firstName + ", lastName=" + lastName + ", patientMRN=" + patientMRN
				+ ", patientDOB=" + patientDOB + "]";
	}
}
