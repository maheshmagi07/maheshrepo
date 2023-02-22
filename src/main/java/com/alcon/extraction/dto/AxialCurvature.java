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
public class AxialCurvature {

	private KeyValue steepK;
	private KeyValue flatK;
	private KeyValue recorDdate;
	private KeyValue instrumentANSIStandard;
	private KeyValue astigmatism;
	private KeyValue eccentricity;
	private KeyValue q;
	private KeyValue shapeFactor;
	private KeyValue hvid;

	public KeyValue getSteepK() {
		if (this.steepK == null) {
			this.steepK = new KeyValue();
		}
		return steepK;
	}

	public void setSteepK(KeyValue steepK) {
		this.steepK = steepK;
	}

	public KeyValue getFlatK() {
		if (this.flatK == null) {
			this.flatK = new KeyValue();
		}
		return flatK;
	}

	public void setFlatK(KeyValue flatK) {
		this.flatK = flatK;
	}

	public KeyValue getRecorDdate() {
		if (this.recorDdate == null) {
			this.recorDdate = new KeyValue();
		}
		return recorDdate;
	}

	public void setRecorDdate(KeyValue recorDdate) {
		this.recorDdate = recorDdate;
	}

	public KeyValue getInstrumentANSIStandard() {
		if (this.instrumentANSIStandard == null) {
			this.instrumentANSIStandard = new KeyValue();
		}
		return instrumentANSIStandard;
	}

	public void setInstrumentANSIStandard(KeyValue instrumentANSIStandard) {
		this.instrumentANSIStandard = instrumentANSIStandard;
	}

	public KeyValue getAstigmatism() {
		if (this.astigmatism == null) {
			this.astigmatism = new KeyValue();
		}
		return astigmatism;
	}

	public void setAstigmatism(KeyValue astigmatism) {
		this.astigmatism = astigmatism;
	}

	public KeyValue getEccentricity() {
		if (this.eccentricity == null) {
			this.eccentricity = new KeyValue();
		}
		return eccentricity;
	}

	public void setEccentricity(KeyValue eccentricity) {
		this.eccentricity = eccentricity;
	}

	public KeyValue getQ() {
		if (this.q == null) {
			this.q = new KeyValue();
		}
		return q;
	}

	public void setQ(KeyValue q) {
		this.q = q;
	}

	public KeyValue getShapeFactor() {
		if (this.shapeFactor == null) {
			this.shapeFactor = new KeyValue();
		}
		return shapeFactor;
	}

	public void setShapeFactor(KeyValue shapeFactor) {
		this.shapeFactor = shapeFactor;
	}

	public KeyValue getHvid() {
		if (this.hvid == null) {
			this.hvid = new KeyValue();
		}
		return hvid;
	}

	public void setHvid(KeyValue hvid) {
		this.hvid = hvid;
	}

}
