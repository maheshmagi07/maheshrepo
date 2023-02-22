package com.alcon.extraction.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.alcon.extraction.config.AWSConfiguration;
import com.alcon.extraction.dto.DeviceData;
import com.alcon.extraction.service.S3Service;
import com.alcon.extraction.service.TextractService;
import com.alcon.extraction.service.util.service.DeviceIdentifier;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Principal;
import com.amazonaws.auth.policy.Resource;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.Statement.Effect;
import com.amazonaws.auth.policy.actions.SQSActions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.AmazonSNSException;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.QueueAttributeName;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.model.AmazonTextractException;
import com.amazonaws.services.textract.model.Block;
import com.amazonaws.services.textract.model.DocumentLocation;
import com.amazonaws.services.textract.model.GetDocumentAnalysisRequest;
import com.amazonaws.services.textract.model.GetDocumentAnalysisResult;
import com.amazonaws.services.textract.model.NotificationChannel;
import com.amazonaws.services.textract.model.S3Object;
import com.amazonaws.services.textract.model.StartDocumentAnalysisRequest;
import com.amazonaws.services.textract.model.StartDocumentAnalysisResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Primary
public class TextractServiceImpl implements TextractService {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private S3Service s3Service;

	@Autowired
	AWSConfiguration awsConfig;

	@Autowired
	DeviceIdentifier deviceIdentifierService;

	public enum ProcessType {
		DETECTION, ANALYSIS
	}

	private static String sqsQueueName = "extraction-queue-sqs";
	private static String snsTopicName = "extraction-queue";
	private static String snsTopicArn = "arn:aws:sns:us-east-1:644440541798:extraction-queue";
	private static String roleArn = "arn:aws:iam::644440541798:role/textract-role-3";
	private static String sqsQueueUrl = "arn:aws:iam::644440541798:role/textract-sns-role-2";
	private static String sqsQueueArn = "arn:aws:sqs:us-east-1:644440541798:extraction-queue-sqs";
	private static String startJobId = null;
	private static String bucket = "extraction-data-2";
	private static String document = "32022-12-12T15:31:09.798853900.pdf";
	private static AmazonSQS sqs = null;
	private static AmazonSNS sns = null;
	private static AmazonTextract textract = null;

	@Override
	public DeviceData analyzeDoc(byte[] bytes) throws IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException, InterruptedException, ExecutionException {

		return null;
	}

	// NEW METHoD//
	private DeviceData analyzeDocumentContents(List<Block> docInfo)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		return deviceIdentifierService.identifyreportTypeAndAnalyze(docInfo);
	}

	@Override
	public DeviceData analyzeDoc(String name) throws Exception {
		log.info("Method analyzeDoc starts");
	    String document = name;
		String bucket = awsConfig.bucketName();
		String roleArn = StringUtils.defaultIfEmpty(awsConfig.getRoleArn(),
				"arn:aws:iam::644440541798:role/textract-role-3");
		sns = awsConfig.getSNSClient();
		sqs = awsConfig.getSQSClient();
		textract = awsConfig.textractClient();
		this.CreateTopicandQueue();
		DeviceData deviceData = ProcessDocument(bucket, document, roleArn, ProcessType.ANALYSIS);
		this.DeleteTopicandQueue();
		log.info("Method analyzeDoc ends");
		return deviceData;
	}

	private DeviceData ProcessDocument(String inBucket, String inDocument, String inRoleArn, ProcessType type)
			throws Exception {
		log.info("Method ProcessDocument starts");
		bucket = inBucket;
		document = inDocument;
		roleArn = inRoleArn;

		switch (type) {

		case ANALYSIS:
			StartDocumentAnalysis(bucket, document);
			break;
		default:
			log.info("Invalid processing type. Choose Detection or Analysis");
			throw new Exception("Invalid processing type");

		}

		// Poll queue for messages
		List<Message> messages = null;
		int dotLine = 0;
		boolean jobFound = false;
		DeviceData deviceData = null;
		log.info("Waiting for job: " + startJobId);
		// loop until the job status is published. Ignore other messages in queue.
		do {
			messages = sqs.receiveMessage(sqsQueueUrl).getMessages();
			if (dotLine++ < 40) {
			} else {
				dotLine = 0;
			}

			if (!messages.isEmpty()) {
				// Loop through messages received.
				for (Message message : messages) {
					String notification = message.getBody();

					// Get status and job id from notification.
					ObjectMapper mapper = new ObjectMapper();
					JsonNode jsonMessageTree = mapper.readTree(notification);
					JsonNode messageBodyText = jsonMessageTree.get("Message");
					ObjectMapper operationResultMapper = new ObjectMapper();
					JsonNode jsonResultTree = operationResultMapper.readTree(messageBodyText.textValue());
					JsonNode operationJobId = jsonResultTree.get("JobId");
					JsonNode operationStatus = jsonResultTree.get("Status");
					// Found job. Get the results and display.
					if (operationJobId.asText().equals(startJobId)) {
						jobFound = true;
						log.info("Status :::: " + operationStatus.toString());
						if (operationStatus.asText().equals("SUCCEEDED")) {
							switch (type) {

							case ANALYSIS:
								deviceData = GetDocumentAnalysisResults();
								break;
							default:
								log.info("Invalid processing type. Choose Detection or Analysis:::");
								throw new Exception("Invalid processing type");

							}
						} else {
							log.info("Document analysis failed:::::");
						}

						sqs.deleteMessage(sqsQueueUrl, message.getReceiptHandle());
					}

					else {
						log.info("Job received was not job " + startJobId);
						// Delete unknown message. Consider moving message to dead letter queue
						sqs.deleteMessage(sqsQueueUrl, message.getReceiptHandle());
					}
				}
			} else {
				Thread.sleep(5000);
			}
		} while (!jobFound);

		log.info("Finished processing document");
		log.info("Method ProcessDocument ends");
		return deviceData;

	}

	private DeviceData  GetDocumentAnalysisResults()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException,AmazonTextractException {
		log.info("Method GetDocumentAnalysisResults starts");
		int maxResults = 1000;
		String paginationToken = null;
		GetDocumentAnalysisResult response = null;
		Boolean finished = false;
		List<Block> blocks = new ArrayList<Block>();
		// loops until pagination token is null
		while (finished == false) {
			GetDocumentAnalysisRequest documentAnalysisRequest = new GetDocumentAnalysisRequest().withJobId(startJobId)
					.withMaxResults(maxResults).withNextToken(paginationToken);
			response = textract.getDocumentAnalysis(documentAnalysisRequest);
			blocks.addAll(response.getBlocks());
			paginationToken = response.getNextToken();
			if (paginationToken == null)
			{
				finished = true;
			}
		}
		log.info("Method GetDocumentAnalysisResults ends");
		return this.analyzeDocumentContents(blocks);

	}

	private void StartDocumentAnalysis(String bucket2, String document2) throws AmazonTextractException{
		log.info("Method StartDocumentAnalysis starts");
		// Create notification channel
		NotificationChannel channel = new NotificationChannel().withSNSTopicArn(snsTopicArn).withRoleArn(roleArn);

		StartDocumentAnalysisRequest req = new StartDocumentAnalysisRequest().withFeatureTypes("TABLES", "FORMS")
				.withDocumentLocation(
						new DocumentLocation().withS3Object(new S3Object().withBucket(bucket).withName(document)))
				.withJobTag("AnalyzingText").withNotificationChannel(channel);

		StartDocumentAnalysisResult startDocumentAnalysisResult = textract.startDocumentAnalysis(req);
		startJobId = startDocumentAnalysisResult.getJobId();
		log.info("Method StartDocumentAnalysis ends");
	}

	private void DeleteTopicandQueue() throws AmazonSQSException,AmazonSNSException{
		log.info("Method DeleteTopicandQueue starts");
		if (sqs != null) {
			sqs.deleteQueue(sqsQueueUrl);
		}

		if (sns != null) {
			sns.deleteTopic(snsTopicArn);
		}
		log.info("Method DeleteTopicandQueue end");
	}

	private void CreateTopicandQueue() throws AmazonSQSException, AmazonSNSException{
		// create a new SNS topic
		log.info("Method CreateTopicandQueue starts");
		snsTopicName = "AmazonTextractTopic" + Long.toString(System.currentTimeMillis());
		CreateTopicRequest createTopicRequest = new CreateTopicRequest(snsTopicName);
		CreateTopicResult createTopicResult = sns.createTopic(createTopicRequest);
		snsTopicArn = createTopicResult.getTopicArn();

		// Create a new SQS Queue
		sqsQueueName = "AmazonTextractQueue" + Long.toString(System.currentTimeMillis());
		final CreateQueueRequest createQueueRequest = new CreateQueueRequest(sqsQueueName);
		sqsQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
		sqsQueueArn = sqs.getQueueAttributes(sqsQueueUrl, Arrays.asList("QueueArn")).getAttributes().get("QueueArn");

		// Subscribe SQS queue to SNS topic
		String sqsSubscriptionArn = sns.subscribe(snsTopicArn, "sqs", sqsQueueArn).getSubscriptionArn();

		// Authorize queue
		Policy policy = new Policy().withStatements(new Statement(Effect.Allow).withPrincipals(Principal.AllUsers)
				.withActions(SQSActions.SendMessage).withResources(new Resource(sqsQueueArn))
		);

		Map<String, String> queueAttributes = new HashMap<String, String>();
		queueAttributes.put(QueueAttributeName.Policy.toString(), policy.toJson());
		sqs.setQueueAttributes(new SetQueueAttributesRequest(sqsQueueUrl, queueAttributes));
		log.info("Method CreateTopicandQueue ends");
	}
}