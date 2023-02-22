/**
 * {Purpose of File}
 * In case of Extension : Use - Please see the {@link com.alcon.} class for true identity
 */
package com.alcon.extraction.config;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author AbhilashaP
 *
 */
@Configuration
@ConfigurationProperties(prefix = "atlas")
public class AtlasPropReader {

	private Map<String, Integer> position;

	private Map<String, Integer> range;

	@Value("${atlas.available.quadrants}")
	private List<String> quadrants;

	@Value("${available.eye}")
	private List<String> eyes;

	@Value("${atlas.range.sections.in.row}")
	private Integer row;

	@Value("${atlas.range.sections.in.row.start}")
	private Integer rowStart;

	public Map<String, Integer> getPosition() {
		return position;
	}

	public void setPosition(Map<String, Integer> position) {
		this.position = position;
	}

	public List<String> getQuadrants() {
		return quadrants;
	}

	public void setQuadrants(List<String> quadrants) {
		this.quadrants = quadrants;
	}

	public List<String> getEyes() {
		return eyes;
	}

	public Integer getRow() {
		return row;
	}

	public void setRow(Integer row) {
		this.row = row;
	}

	public void setEyes(List<String> eyes) {
		this.eyes = eyes;
	}

	public Map<String, Integer> getRange() {
		return range;
	}

	public void setRange(Map<String, Integer> range) {
		this.range = range;
	}

	public Integer getRowStart() {
		return rowStart;
	}

	public void setRowStart(Integer rowStart) {
		this.rowStart = rowStart;
	}

}
