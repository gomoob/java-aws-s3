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
package com.gomoob.aws.s3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.gomoob.aws.AbstractS3Test;
import com.gomoob.aws.IS3;

import cucumber.api.java.After;
import software.amazon.awssdk.http.AbortableInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.sync.RequestBody;
import software.amazon.awssdk.sync.StreamingResponseHandler;
import software.amazon.awssdk.utils.IoUtils;

/**
 * Test case for the {@link S3} class.
 *
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public class S3Test extends AbstractS3Test {

    /**
     * An instance of the class to test.
     */
    private IS3 s3;

    /**
     * Test method executed after each test method.
     */
    @After
    public void after() throws Exception {

        // Cleanup the testing bucket
        this.cleanupTestingBucket();

    }

    /**
     * Test method executed before each test method.
     */
    @Before
    public void before() throws Exception {

        // Cleanup the testing bucket
        this.cleanupTestingBucket();

        // Creates an instance of the component to test
        this.s3 = new S3(this.getTestingS3Client());

    }

    /**
     * Test method for
     * {@link S3#getObject(software.amazon.awssdk.services.s3.model.GetObjectRequest, software.amazon.awssdk.sync.StreamingResponseHandler)}.
     */
    @Test
    public void testGetObject() {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(this.testingBucket())
                .key("java-aws-s3/OBJECT_1").build();

        Object object = this.s3.getObject(getObjectRequest, this.createStringStreamingResponseHandler());

        assertTrue(object instanceof String);
        assertEquals(object, "OBJECT_1");
    }

    /**
     * Test method for
     * {@link S3#putObject(software.amazon.awssdk.services.s3.model.PutObjectRequest, software.amazon.awssdk.sync.RequestBody)}.
     */
    @Test
    public void testPutObject() {

        // At the beginning no "java-aws-s3/NEW_OBJECT" key exists on our bucket
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(this.testingBucket())
                .key("java-aws-s3/NEW_OBJECT").build();
        try {
            this.s3.getObject(getObjectRequest, this.createStringStreamingResponseHandler());
        } catch (S3Exception s3ex) {
            assertTrue(s3ex.getMessage().contains("The specified key does not exist."));
        }

        // Put our new object
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(this.testingBucket())
                .key("java-aws-s3/NEW_OBJECT").build();
        this.s3.putObject(putObjectRequest, RequestBody.of("NEW_OBJECT"));

        // Get our new object now exists
        Object object = this.s3.getObject(getObjectRequest, this.createStringStreamingResponseHandler());

        assertTrue(object instanceof String);
        assertEquals(object, "NEW_OBJECT");
    }

    /**
     * Creates an Amazon SDK streaming response handler which returns the response as a string.
     *
     * @return the create Amazon SDK streaming response handler.
     */
    private StreamingResponseHandler<GetObjectResponse, String> createStringStreamingResponseHandler() {
        return new StreamingResponseHandler<GetObjectResponse, String>() {
            @Override
            public String apply(GetObjectResponse response, AbortableInputStream inputStream) throws Exception {
                return IoUtils.toString(inputStream);
            }
        };
    }

}
