/**
 * {Purpose of File}
 * In case of Extension : Use - Please see the {@link com.alcon.} class for true identity
 */
package com.alcon.extraction.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author AbhilashaP
 *
 */
@Configuration
@PropertySource(ignoreResourceNotFound = true, value = "classpath:rule.properties")
public class PropertyConfig {

	@Value("${available.devices}")
	private List<String> devices;
	
	@Value("${available.eye}")
	private List<String> eyes;
	
	@Value("${available.company}")
	private List<String> companies;

	public List<String> getDevices() {
		return devices;
	}

	public List<String> getEyes() {
		return eyes;
	}

	public List<String> getCompanies() {
		return companies;
	}
	
	
}
