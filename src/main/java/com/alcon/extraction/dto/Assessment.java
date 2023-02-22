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
public class Assessment{

	@Autowired
	private EyeDetails od;
	
	@Autowired
	private EyeDetails os;
	
	private String facility;

	public EyeDetails getOd() {
		return od;
	}

	public void setOd(EyeDetails od) {
		this.od = od;
	}

	public EyeDetails getOs() {
		return os;
	}

	public void setOs(EyeDetails os) {
		this.os = os;
	}
	

	public String getFacility() {
		return facility;
	}

	public void setFacility(String facility) {
		this.facility = facility;
	}

	@Override
	public String toString() {
		return "Assessment [od=" + od + ", os=" + os + "]";
	}
	
	
	
}
