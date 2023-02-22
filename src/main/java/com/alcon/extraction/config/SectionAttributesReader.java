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
@ConfigurationProperties(prefix = "sections")
public class SectionAttributesReader {

	@Value("${sections.axial.curvature.attributes}")
	private List<String> axialCurvatureAttributes;

	@Value("${sections.keratometry.attributes}")
	private List<String> keratometryAttributes;

	@Value("${sections.image.simulation.attributes}")
	private List<String> imageSimulationAttributes;

	private Map<String, List<String>> validator;

	public List<String> getAxialCurvatureAttributes() {
		return axialCurvatureAttributes;
	}

	public void setAxialCurvatureAttributes(List<String> axialCurvatureAttributes) {
		this.axialCurvatureAttributes = axialCurvatureAttributes;
	}

	public Map<String, List<String>> getValidator() {
		return validator;
	}

	public void setValidator(Map<String, List<String>> validator) {
		this.validator = validator;
	}

	public List<String> getKeratometryAttributes() {
		return keratometryAttributes;
	}

	public void setKeratometryAttributes(List<String> keratometryAttributes) {
		this.keratometryAttributes = keratometryAttributes;
	}

	public List<String> getImageSimulationAttributes() {
		return imageSimulationAttributes;
	}

	public void setImageSimulationAttributes(List<String> imageSimulationAttributes) {
		this.imageSimulationAttributes = imageSimulationAttributes;
	}

}
