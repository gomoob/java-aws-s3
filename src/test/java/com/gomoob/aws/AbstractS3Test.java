/**
 * BSD 3-Clause License
 *
 * Copyright (c) 2017, GOMOOB All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gomoob.aws;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import software.amazon.awssdk.auth.AwsCredentials;
import software.amazon.awssdk.auth.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

/**
 * Abstract class which contain utility function to test Amazon S3 components.
 *
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public abstract class AbstractS3Test {

    /**
     * Properties used to configure the unit and integration tests.
     */
    protected Properties testingProperties;

    /**
     * A testing Amazon S3 client to use in the tests.
     */
    protected S3Client testingS3Client;

    /**
     * Creates a new instance of the {@link AbstractS3Test} class.
     */
    public AbstractS3Test() {
        this.testingProperties = new Properties();

        // If the test is executed in Travis get properties from special environment variables
        if (System.getenv("TRAVIS").equals("true")) {
            this.testingProperties.put("aws.key", System.getenv("AWS_KEY"));
            this.testingProperties.put("aws.secret", System.getenv("AWS_SECRET"));
            this.testingProperties.put("aws.region", System.getenv("AWS_REGION"));
            this.testingProperties.put("aws.s3.bucket", System.getenv("AWS_S3_BUCKET"));
        }

        // Otherwise get properties from a file
        else {
            try {
                InputStream is = new FileInputStream(new File(".testing.properties"));
                this.testingProperties.load(is);
            } catch (IOException ioex) {
                System.err.println("You must create a '.testing.properties' file at the root of the project ! "
                        + "The content of this file should be the following.");
                System.err.println("aws.key=XXXXXXXX");
                System.err.println("aws.secret=XXXXXXXX");
                System.err.println("aws.region=us-east-1");
                System.err.println("aws.s3.bucket=my-bucket");
                System.err.println("");
                throw new RuntimeException("Fail to load '.testing.properties' file !", ioex);
            }
        }
    }

    /**
     * Creates a testing Amazon S3 client.
     *
     * @return the testing Amazon S3 client.
     */
    protected S3Client createTestingS3Client() {
        return S3Client.builder().region(Region.of(this.testingProperties.getProperty("aws.region")))
                .credentialsProvider(new AwsCredentialsProvider() {

                    @Override
                    public AwsCredentials getCredentials() {
                        return new AwsCredentials(testingProperties.getProperty("aws.key"),
                                testingProperties.getProperty("aws.secret"));
                    }
                }).build();
    }

    /**
     * Cleanup the testing Amazon S3 bucket by reinitializing the initial firxtures set.
     */
    protected void cleanupTestingBucket() {
        S3Client s3Client = this.getTestingS3Client();

        // Clear our Amazon S3 bucket by removing all Amazon S3 keys which are not in our initial fixtures set
        List<String> fixtureKeys = Arrays.asList("java-aws-s3/OBJECT_1", "java-aws-s3/OBJECT_2",
                "java-aws-s3/OBJECT_3");
        ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder().bucket(this.testingBucket())
                .prefix("java-aws-s3").build();
        ListObjectsResponse listObjectResponse = s3Client.listObjects(listObjectsRequest);

        for (S3Object s3Object : listObjectResponse.contents()) {
            if (!fixtureKeys.contains(s3Object.key())) {
                s3Client.deleteObject(
                        DeleteObjectRequest.builder().bucket(this.testingBucket()).key(s3Object.key()).build());
            }
        }
    }

    /**
     * Gets the testing Amazon S3 client.
     *
     * <p>
     * If the testing Amazon S3 client is not initialize this function automatically creates it.
     * </p>
     *
     * @return the testing Amazon S3 client.
     */
    protected S3Client getTestingS3Client() {
        if (this.testingS3Client == null) {
            this.testingS3Client = this.createTestingS3Client();
        }

        return testingS3Client;
    }

    /**
     * Gest the name of the testing Amazon S3 bucket to use in the tests.
     *
     * @return the name of the testing Amazon S3 bucket to use in the tests.
     */
    protected String testingBucket() {
        return this.testingProperties.getProperty("aws.s3.bucket");
    }
}
