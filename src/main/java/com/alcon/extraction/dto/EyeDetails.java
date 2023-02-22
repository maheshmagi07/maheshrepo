/**
 * {Purpose of File}
 * In case of Extension : Use - Please see the {@link com.alcon.} class for true identity
 */
package com.alcon.extraction.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author AbhilashaP
 *
 */
@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EyeDetails {

	List<AxialCurvature> axialCurvature = new ArrayList<>();

	List<Keratometry> keratometry = new ArrayList<>();

	List<ImageSimulation> imageSimulation = new ArrayList<>();

	public List<Keratometry> getKeratometry() {
		return keratometry;
	}

	public void setKeratometry(List<Keratometry> keratometry) {
		this.keratometry = keratometry;
	}

	public List<AxialCurvature> getAxialCurvature() {
		return axialCurvature;
	}

	public void setAxialCurvature(List<AxialCurvature> axialCurvature) {
		this.axialCurvature = axialCurvature;
	}

	public List<ImageSimulation> getImageSimulation() {
		return imageSimulation;
	}

	public void setImageSimulation(List<ImageSimulation> imageSimulation) {
		this.imageSimulation = imageSimulation;
	}

}
