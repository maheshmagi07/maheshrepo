package com.alcon.extraction.service.impl;

import com.alcon.extraction.config.AWSConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Fanish Singh
 */

@RunWith(MockitoJUnitRunner.class)
public class S3ServiceImplTest {

    @InjectMocks
    private S3ServiceImpl s3ServiceImpl;

    @Mock
    private AWSConfiguration awsConfig;
    
    @Mock
    private S3Client s3Client;

    private static final String bucketName = "extraction-data-2";

    private static final String FILE_NAME = "fileName";
    private static final String FOLDER_NAME = "folderName";
    private static final String BUCKET_NAME = "bucketName";


    @Before
    public void setUp() {
    }

    @Test
    public void testGetObjectBytes() {
        Mockito.when(awsConfig.bucketName()).thenReturn(bucketName);
        ResponseBytes<GetObjectResponse> responseTResponseBytes = ResponseBytes
                .fromByteArray(GetObjectResponse.builder().build(), new byte[1]);
        Mockito.when(s3Client.getObjectAsBytes(Mockito.any(GetObjectRequest.class))).thenReturn(responseTResponseBytes);
        byte[] result = s3ServiceImpl.getObjectBytes(FILE_NAME, FOLDER_NAME);
        Assert.assertNotNull(result);

    }

    @Test
    public void testGetFolder() {
        S3Object s3Object = S3Object.builder().build();
        ListObjectsResponse listObjectsResponse = ListObjectsResponse.builder()
                .contents(s3Object).build();
        Mockito.when(s3Client.listObjects(Mockito.any(ListObjectsRequest.class))).thenReturn(listObjectsResponse);
        List<String> result = s3ServiceImpl.getFolder(BUCKET_NAME, FOLDER_NAME);
        Assert.assertNotNull(result);
        Assert.assertTrue("Size of folder list", result.size() == 1);
    }

    @Test
    public void testPutObject_when_folderName_not_present() {
        PutObjectResponse putObjectResponse = PutObjectResponse.builder().eTag("SuccessFully Put Object").build();
        Mockito.when(awsConfig.bucketName()).thenReturn(BUCKET_NAME);
        S3Object s3Object = S3Object.builder().key(BUCKET_NAME).build();
        ListObjectsResponse listObjectsResponse = ListObjectsResponse.builder()
                .contents(s3Object).build();
        Mockito.when(s3Client.listObjects(Mockito.any(ListObjectsRequest.class))).thenReturn(listObjectsResponse);
        Mockito.when(s3Client.putObject(Mockito.any(PutObjectRequest.class), Mockito.any(RequestBody.class))).thenReturn(putObjectResponse);
        String result = s3ServiceImpl.putObject(new byte[]{(byte) 0}, BUCKET_NAME, FILE_NAME);
        Assert.assertNotNull(result);
        Assert.assertTrue("E-tag Message", StringUtils.equals(result,"SuccessFully Put Object"));
    }

    @Test
    public void testPutObject_when_FolderName_consist() {
        PutObjectResponse putObjectResponse = PutObjectResponse.builder().eTag("SuccessFully Put Object").build();
        S3Object s3Object = S3Object.builder().key(LocalDate.now()+"/").build();
        ListObjectsResponse listObjectsResponse = ListObjectsResponse.builder()
                .contents(s3Object).build();
        Mockito.when(s3Client.listObjects(Mockito.any(ListObjectsRequest.class))).thenReturn(listObjectsResponse);
        Mockito.when(s3Client.putObject(Mockito.any(PutObjectRequest.class), Mockito.any(RequestBody.class))).thenReturn(putObjectResponse);
        String result = s3ServiceImpl.putObject(new byte[]{(byte) 0}, BUCKET_NAME, FILE_NAME);
        Assert.assertNotNull(result);
        Assert.assertTrue("E-tag Message", StringUtils.equals(result,"SuccessFully Put Object"));
    }


}
