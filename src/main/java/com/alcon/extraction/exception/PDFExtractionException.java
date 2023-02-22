package com.alcon.extraction.exception;

public class PDFExtractionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String errorCode;

	public PDFExtractionException() {
		super();
	}

	public PDFExtractionException(String errorCode) {
		super(errorCode);
		this.errorCode = errorCode;
	}

	public PDFExtractionException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public PDFExtractionException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public PDFExtractionException(Throwable cause) {
		super(cause.getMessage(), cause);
	}

	public PDFExtractionException(String errorCode, Throwable cause) {
		super(cause.getMessage(), cause);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
