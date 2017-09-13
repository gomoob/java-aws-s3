package com.gomoob.aws.s3;

import java.io.File;

import org.springframework.stereotype.Component;

import com.gomoob.aws.IS3;

import software.amazon.awssdk.auth.AwsCredentials;
import software.amazon.awssdk.auth.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.sync.RequestBody;

@Component
public class S3 implements IS3 {

    /**
     * An AWS Client Amazon PHP SDK class instance used to manage calls to an Amazon AWS Web Service.
     */
    protected S3Client awsClient;

    /**
     * Creates a new <tt>S3</tt> instance.
     *
     * @param accessKeyId the access key id used to connect to Amazon S3.
     * @param secretAccessKey the secret access key used to connect to Amazon S3.
     * @param regionString the region of configuration the S3 client in format String.
     */
    public S3(final String accessKeyId, final String secretAccessKey, final String regionString) {

        AwsCredentials awsCreds = new AwsCredentials(accessKeyId, secretAccessKey);
        Region region = Region.of(regionString);

        this.awsClient = S3Client.builder().region(region).credentialsProvider(new StaticCredentialsProvider(awsCreds))
                .build();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PutObjectResponse putObject(final String bucket, final String key, final File file) {

        return this.awsClient.putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(),
                RequestBody.of(file));
    }

}
