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
public class KeyValue {
	
	private String value;
	private Float confidence;
	private Float polygonXaxisValue;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Float getConfidence() {
		return confidence;
	}

	public void setConfidence(Float confidence) {
		this.confidence = confidence;
	}
	
	public Float getPolygonXaxisValue() {
		return polygonXaxisValue;
	}

	public void setPolygonXaxisValue(Float polygonXaxisValue) {
		this.polygonXaxisValue = polygonXaxisValue;
	}

	public KeyValue() {
		super();
	}

	public KeyValue(String value, Float confidence) {
		super();
		this.value = value;
		this.confidence = confidence;
	}

	public KeyValue(String value, Float confidence, Float polygonXaxisValue) {
		super();
		this.value = value;
		this.confidence = confidence;
		this.polygonXaxisValue = polygonXaxisValue;
	}

	@Override
	public String toString() {
		return "KeyValue [value=" + value + ", confidence=" + confidence + ", polygonXaxisValue=" + polygonXaxisValue
				+ "]";
	}
}
