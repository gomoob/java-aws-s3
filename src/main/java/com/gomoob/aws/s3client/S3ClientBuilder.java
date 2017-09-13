/**
 * // * Copyright 2017 ATK EVENTS SAS. All rights reserved. //
 */
package com.gomoob.aws.s3client;

import org.springframework.stereotype.Component;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.sync.RequestBody;

/**
 * An implementation for the <tt>IS3</tt> interface which uses acts as a mock and allow to write unit test on Amazon S3
 * JAVA SDK functions without being connected to S3.
 *
 * @author Jiaming LIANG (jiaming.liang@gomoob.com)
 */
@Component
public class S3ClientBuilder {

    public void putObject(PutObjectRequest putObjectRequest) {
        // TODO Auto-generated method stub
        System.out.println("In the method `putObject` of the S3Client.");
        S3Client client = S3Client.create();
        client.putObject(PutObjectRequest.builder().bucket("gomoob-staging").key("AKIAJAKQPBJFJIP5OWMQ").build(),
                RequestBody.empty());
    }
}