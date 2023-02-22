package com.alcon.extraction.exception;

public class PDFExtractionExceptionResponse {

	private  String message;
	private  int errorCode;

	public PDFExtractionExceptionResponse(String message, int errorCode) {
		super();
		this.message = message;
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	

}
