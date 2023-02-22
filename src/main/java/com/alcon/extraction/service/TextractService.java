/**
 * TextractService Interface lists all the methods required to Read/ analyse documents using AWS Textract
 */
package com.alcon.extraction.service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.alcon.extraction.dto.DeviceData;

/**
 * @author AbhilashaP
 */
public interface TextractService {

	DeviceData analyzeDoc(String name) throws IOException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InterruptedException, ExecutionException, Exception;

	 DeviceData analyzeDoc(byte[] bytes) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InterruptedException, ExecutionException;

}
