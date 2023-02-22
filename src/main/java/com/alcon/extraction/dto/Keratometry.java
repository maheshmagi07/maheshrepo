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
public class Keratometry {

	private KeyValue recorDdate;

	private KeyValue simKsSteepK;
	private KeyValue simKsFlatK;
	private KeyValue totalAstigmatism;

	private KeyValue centralSteepKValue1;
	private KeyValue centralSteepKValue2;
	private KeyValue centralFlatKValue1;
	private KeyValue centralFlatKValue2;

	private KeyValue midPeripherySteepKValue1;
	private KeyValue midPeripherySteepKValue2;
	private KeyValue midPeripheryFlatKValue1;
	private KeyValue midPeripheryFlatKValue2;

	private KeyValue peripherySteepKValue1;
	private KeyValue peripherySteepKValue2;
	private KeyValue peripheryFlatKValue1;
	private KeyValue peripheryFlatKValue2;

	public KeyValue getRecorDdate() {
		if (this.recorDdate == null) {
			this.recorDdate = new KeyValue();
		}
		return recorDdate;
	}

	public KeyValue getSimKsSteepK() {
		if (this.simKsSteepK == null) {
			this.simKsSteepK = new KeyValue();
		}
		return simKsSteepK;
	}

	public KeyValue getSimKsFlatK() {
		if (this.simKsFlatK == null) {
			this.simKsFlatK = new KeyValue();
		}
		return simKsFlatK;
	}

	public KeyValue getTotalAstigmatism() {
		if (this.totalAstigmatism == null) {
			this.totalAstigmatism = new KeyValue();
		}
		return totalAstigmatism;
	}

	public KeyValue getCentralSteepKValue1() {
		if (this.centralSteepKValue1 == null) {
			this.centralSteepKValue1 = new KeyValue();
		}
		return centralSteepKValue1;
	}

	public KeyValue getCentralSteepKValue2() {
		if (this.centralSteepKValue2 == null) {
			this.centralSteepKValue2 = new KeyValue();
		}
		return centralSteepKValue2;
	}

	public KeyValue getCentralFlatKValue1() {
		if (this.centralFlatKValue1 == null) {
			this.centralFlatKValue1 = new KeyValue();
		}
		return centralFlatKValue1;
	}

	public KeyValue getCentralFlatKValue2() {
		if (this.centralFlatKValue2 == null) {
			this.centralFlatKValue2 = new KeyValue();
		}
		return centralFlatKValue2;
	}

	public KeyValue getMidPeripherySteepKValue1() {
		if (this.midPeripherySteepKValue1 == null) {
			this.midPeripherySteepKValue1 = new KeyValue();
		}
		return midPeripherySteepKValue1;
	}

	public KeyValue getMidPeripherySteepKValue2() {
		if (this.midPeripherySteepKValue2 == null) {
			this.midPeripherySteepKValue2 = new KeyValue();
		}
		return midPeripherySteepKValue2;
	}

	public KeyValue getMidPeripheryFlatKValue1() {
		if (this.midPeripheryFlatKValue1 == null) {
			this.midPeripheryFlatKValue1 = new KeyValue();
		}
		return midPeripheryFlatKValue1;
	}

	public KeyValue getMidPeripheryFlatKValue2() {
		if (this.midPeripheryFlatKValue2 == null) {
			this.midPeripheryFlatKValue2 = new KeyValue();
		}
		return midPeripheryFlatKValue2;
	}

	public KeyValue getPeripherySteepKValue1() {
		if (this.peripherySteepKValue1 == null) {
			this.peripherySteepKValue1 = new KeyValue();
		}
		return peripherySteepKValue1;
	}

	public KeyValue getPeripherySteepKValue2() {
		if (this.peripherySteepKValue2 == null) {
			this.peripherySteepKValue2 = new KeyValue();
		}
		return peripherySteepKValue2;
	}

	public KeyValue getPeripheryFlatKValue1() {
		if (this.peripheryFlatKValue1 == null) {
			this.peripheryFlatKValue1 = new KeyValue();
		}
		return peripheryFlatKValue1;
	}

	public KeyValue getPeripheryFlatKValue2() {
		if (this.peripheryFlatKValue2 == null) {
			this.peripheryFlatKValue2 = new KeyValue();
		}
		return peripheryFlatKValue2;
	}

	public void setRecorDdate(KeyValue recorDdate) {
		this.recorDdate = recorDdate;
	}

	public void setSimKsSteepK(KeyValue simKsSteepK) {
		this.simKsSteepK = simKsSteepK;
	}

	public void setSimKsFlatK(KeyValue simKsFlatK) {
		this.simKsFlatK = simKsFlatK;
	}

	public void setTotalAstigmatism(KeyValue totalAstigmatism) {
		this.totalAstigmatism = totalAstigmatism;
	}

	public void setCentralSteepKValue1(KeyValue centralSteepKValue1) {
		this.centralSteepKValue1 = centralSteepKValue1;
	}

	public void setCentralSteepKValue2(KeyValue centralSteepKValue2) {
		this.centralSteepKValue2 = centralSteepKValue2;
	}

	public void setCentralFlatKValue1(KeyValue centralFlatKValue1) {
		this.centralFlatKValue1 = centralFlatKValue1;
	}

	public void setCentralFlatKValue2(KeyValue centralFlatKValue2) {
		this.centralFlatKValue2 = centralFlatKValue2;
	}

	public void setMidPeripherySteepKValue1(KeyValue midPeripherySteepKValue1) {
		this.midPeripherySteepKValue1 = midPeripherySteepKValue1;
	}

	public void setMidPeripherySteepKValue2(KeyValue midPeripherySteepKValue2) {
		this.midPeripherySteepKValue2 = midPeripherySteepKValue2;
	}

	public void setMidPeripheryFlatKValue1(KeyValue midPeripheryFlatKValue1) {
		this.midPeripheryFlatKValue1 = midPeripheryFlatKValue1;
	}

	public void setMidPeripheryFlatKValue2(KeyValue midPeripheryFlatKValue2) {
		this.midPeripheryFlatKValue2 = midPeripheryFlatKValue2;
	}

	public void setPeripherySteepKValue1(KeyValue peripherySteepKValue1) {
		this.peripherySteepKValue1 = peripherySteepKValue1;
	}

	public void setPeripherySteepKValue2(KeyValue peripherySteepKValue2) {
		this.peripherySteepKValue2 = peripherySteepKValue2;
	}

	public void setPeripheryFlatKValue1(KeyValue peripheryFlatKValue1) {
		this.peripheryFlatKValue1 = peripheryFlatKValue1;
	}

	public void setPeripheryFlatKValue2(KeyValue peripheryFlatKValue2) {
		this.peripheryFlatKValue2 = peripheryFlatKValue2;
	}

}
