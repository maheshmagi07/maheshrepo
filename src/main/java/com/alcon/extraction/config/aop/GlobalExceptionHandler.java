/**
 * Defines Global exception class
 */
package com.alcon.extraction.config.aop;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.alcon.extraction.exception.PDFExtractionExceptionResponse;
import com.amazonaws.services.codepipeline.model.ErrorDetails;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.sns.model.AmazonSNSException;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.textract.model.AmazonTextractException;

import software.amazon.awssdk.services.s3.model.S3Exception;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<?> amazonS3Exception(S3Exception ex, WebRequest request) {
    	 PDFExtractionExceptionResponse peer = new PDFExtractionExceptionResponse(ex.getMessage(), ex.statusCode());
         return new ResponseEntity<>(peer, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> ioException(IOException ex, WebRequest request) {
         ErrorDetails errorDetails = new ErrorDetails();
         errorDetails.setMessage(ex.getLocalizedMessage());
         errorDetails.setCode(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
         return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
   // @ExceptionHandler(TextractException.class)
  //  public ResponseEntity<?> textractException(TextractException ex, WebRequest request) {
  //       ErrorDetails errorDetails = new ErrorDetails();
 //        errorDetails.setMessage(ex.getLocalizedMessage());
  //       errorDetails.setCode(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
  //       return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
  //  }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage(ex.getLocalizedMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(AmazonSNSException.class)
    public ResponseEntity<?> amazonSNSException(AmazonSNSException ex, WebRequest request) {
    	 PDFExtractionExceptionResponse peer = new PDFExtractionExceptionResponse(ex.getErrorMessage(), ex.getStatusCode());
         return new ResponseEntity<>(peer, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(AmazonSQSException.class)
    public ResponseEntity<?> amazonSQSException(AmazonSQSException ex, WebRequest request) {
    	 PDFExtractionExceptionResponse peer = new PDFExtractionExceptionResponse(ex.getErrorMessage(), ex.getStatusCode());
         return new ResponseEntity<>(peer, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(AmazonTextractException.class)
    public ResponseEntity<?> amazonTextractException(AmazonTextractException ex, WebRequest request) {
    	 PDFExtractionExceptionResponse peer = new PDFExtractionExceptionResponse(ex.getErrorMessage(), ex.getStatusCode());
         return new ResponseEntity<>(peer, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    
}
