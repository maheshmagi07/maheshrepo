/**
 * {Purpose of File}
 * In case of Extension : Use - Please see the {@link com.alcon.} class for true identity
 */
package com.alcon.extraction.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author AbhilashaP
 *
 */
@JsonInclude(Include.NON_NULL)
public class ImageSimulation {
	private String aperture;
	private String order;
	private String recorDdate;
	private String index;
	private String scWeight;
	private String coeffEdit;
	public String getAperture() {
		return aperture;
	}
	public void setAperture(String aperture) {
		this.aperture = aperture;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getRecorDdate() {
		return recorDdate;
	}
	public void setRecorDdate(String recorDdate) {
		this.recorDdate = recorDdate;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getScWeight() {
		return scWeight;
	}
	public void setScWeight(String scWeight) {
		this.scWeight = scWeight;
	}
	public String getCoeffEdit() {
		return coeffEdit;
	}
	public void setCoeffEdit(String coeffEdit) {
		this.coeffEdit = coeffEdit;
	}

	
	
}
