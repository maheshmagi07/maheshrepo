package com.alcon.extraction.service.impl;


import com.alcon.extraction.config.AWSConfiguration;
import com.alcon.extraction.dto.DeviceData;
import com.alcon.extraction.service.util.service.DeviceIdentifier;
import com.amazonaws.AmazonClientException;
import com.amazonaws.http.AmazonHttpClient;
import com.amazonaws.http.ExecutionContext;
import com.amazonaws.http.HttpResponseHandler;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClientBuilder;
import com.amazonaws.services.textract.model.StartDocumentAnalysisRequest;
import com.amazonaws.services.textract.model.StartDocumentAnalysisResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

@RunWith(MockitoJUnitRunner.class)
public class TextractServiceImplTest {
    
	 @Spy
	@InjectMocks
    private TextractServiceImpl textractService;

	
    @InjectMocks
    private AWSConfiguration awsConfig;

    @Mock
    private DeviceIdentifier deviceIdentifierService;

    @Mock
    private AmazonHttpClient amazonHttpClient;

    @Mock
    ExecutionContext executionContext;
    @Mock
    HttpResponseHandler<AmazonClientException> handler;
    @Mock
    HttpResponseHandler<String> responseHandler;



    private AmazonSQS amazonSQS;
    private AmazonSNS amazonSNS;
    private AmazonTextract amazonTextract;

    private CreateTopicResult createTopicResult = new CreateTopicResult().withTopicArn(snsTopicArn);

	/*
	 * private static final String BUCKET_NAME = "bucketName"; private static final
	 * String bucketName = "extraction-data-2";
	 */
    
    private static final String BUCKET_NAME = "bucketName";
    private static final String bucketName = "extraction-data-2";

    private static String sqsQueueName = "extraction-queue-sqs";
    private static String snsTopicName = "extraction-queue";
    private static String snsTopicArn = "arn:aws:sns:us-east-1:644440541798:extraction-queue";
    private static String roleArn = "arn:aws:iam::644440541798:role/textract-role-3";
    private static String sqsQueueUrl = "arn:aws:iam::644440541798:role/textract-sns-role-2";
    private static String sqsQueueArn = "arn:aws:sqs:us-east-1:644440541798:extraction-queue-sqs";


	/*
	 * @Before public void setUp() {
	 * 
	 * MockitoAnnotations.initMocks(this); StartDocumentAnalysisResult
	 * startDocumentAnalysisResult = new StartDocumentAnalysisResult();
	 * startDocumentAnalysisResult.setJobId("abc"); CreateTopicRequest
	 * createTopicRequest = new CreateTopicRequest(snsTopicName); // amazonSQS =
	 * AmazonSQSClientBuilder.defaultClient(); // amazonSNS =
	 * AmazonSNSClientBuilder.defaultClient(); // amazonTextract =
	 * AmazonTextractClientBuilder.defaultClient();
	 * Mockito.when(awsConfig.bucketName()).thenReturn(bucketName);
	 * Mockito.when(awsConfig.getSQSClient()).thenReturn(amazonSQS);
	 * Mockito.when(awsConfig.getSNSClient()).thenReturn(amazonSNS);
	 * Mockito.when(awsConfig.textractClient()).thenReturn(amazonTextract);
	 * Mockito.doNothing().when(amazonSQS).deleteQueue(Mockito.anyString());
	 * Mockito.doNothing().when(amazonSQS).deleteMessage(Mockito.anyString(),
	 * Mockito.anyString());
	 * Mockito.doNothing().when(amazonSNS).deleteTopic(Mockito.anyString());
	 * 
	 * Mockito.when(amazonTextract.startDocumentAnalysis(Mockito.any(
	 * StartDocumentAnalysisRequest.class))).thenReturn(startDocumentAnalysisResult)
	 * ;
	 * Mockito.doNothing().when(amazonSNS.createTopic(Mockito.any(CreateTopicRequest
	 * .class)));
	 * Mockito.when(amazonSNS.createTopic(createTopicRequest)).thenReturn(
	 * createTopicResult); }
	 */
    
    
    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        StartDocumentAnalysisResult startDocumentAnalysisResult = new StartDocumentAnalysisResult();
        startDocumentAnalysisResult.setJobId("abc");
        CreateTopicRequest createTopicRequest = new CreateTopicRequest(snsTopicName);
       // amazonSQS = AmazonSQSClientBuilder.defaultClient();
       // amazonSNS = AmazonSNSClientBuilder.defaultClient();
       // amazonTextract = AmazonTextractClientBuilder.defaultClient();
        Mockito.when(awsConfig.bucketName()).thenReturn(bucketName);
        Mockito.when(awsConfig.getSQSClient()).thenReturn(amazonSQS);
        Mockito.when(awsConfig.getSNSClient()).thenReturn(amazonSNS);
        Mockito.when(awsConfig.textractClient()).thenReturn(amazonTextract);
        Mockito.doNothing().when(amazonSQS).deleteQueue(Mockito.anyString());
        Mockito.doNothing().when(amazonSQS).deleteMessage(Mockito.anyString(), Mockito.anyString());
        Mockito.doNothing().when(amazonSNS).deleteTopic(Mockito.anyString());

        Mockito.when(amazonTextract.startDocumentAnalysis(Mockito.any(StartDocumentAnalysisRequest.class))).thenReturn(startDocumentAnalysisResult);
        Mockito.doNothing().when(amazonSNS.createTopic(Mockito.any(CreateTopicRequest.class)));
        Mockito.when(amazonSNS.createTopic(createTopicRequest)).thenReturn(createTopicResult);
    }

    
	
	  @Test 
	  public void testAnalyzeDoc() throws Exception { 
	  Mockito.when(awsConfig.bucketName()).thenReturn(bucketName); 
	  DeviceData docName = textractService.analyzeDoc("docName");
	  Assert.assertNotNull(docName);
	 
	  }
   
      /*  @Test
        public void testAnalyzeDoc() throws Exception {
        	AWSConfiguration mahi=new AWSConfiguration();
           Mockito.when(mahi.bucketName()).thenReturn(bucketName);
            DeviceData docName = textractService.analyzeDoc("mahi");
            Assert.assertNotNull(docName);*/
            
            
          //  @Test
            
           /* public void testAnalyzeDoc() throws Exception  {
              PowerMockito.mockStatic(TextractServiceImpl.class);
              Mockito.when(AWSConfiguration.bucketName()).thenReturn(bucketName);
              //Mockito.when(awsConfig.bucketName()).thenReturn(bucketName); 
              
         	 DeviceData docName = textractService.analyzeDoc("docName");
         	  Assert.assertNotNull(docName);  */  
                   
    }


