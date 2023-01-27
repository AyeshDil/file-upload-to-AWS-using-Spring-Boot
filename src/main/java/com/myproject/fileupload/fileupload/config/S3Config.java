package com.myproject.fileupload.fileupload.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class S3Config {

    // S3 Bucket configuration
    // Amazon S3
    // Amazon S3 Client

    @Value("${accessKey}")
    private String accessKey; // Assign S3 bucket AccessKey to the accessKey variable.

    @Value("${secrete}")
    private String secreteKey; // Assign S3 bucket SecreteKey to the secreteKey variable.

    @Value("${region}")
    private String region; // Assign S3 bucket Region to the region variable.

    @Bean
    public AmazonS3 s3() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secreteKey);
        return AmazonS3ClientBuilder.standard().withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
    }

    @Bean
    @Primary
    public AmazonS3Client s3Client() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secreteKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard().withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
    }
}
