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
package com.gomoob.aws.mock;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.gomoob.aws.IS3;

import software.amazon.awssdk.SdkClientException;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.sync.RequestBody;
import software.amazon.awssdk.sync.StreamingResponseHandler;
import software.amazon.awssdk.utils.IoUtils;

/**
 * An implementation for the {@link IS3} interface which acts as a mock and allow to write unit test on Amazon S3 JAVA
 * SDK functions without being connected to S3.
 *
 * @author Jiaming LIANG (jiaming.liang@gomoob.com)
 */
public class S3Mock implements IS3 {

    /**
     * Utility map which allows to emulate Amazon S3 buckets.
     *
     * <p>
     * This map maps bucket names to bucket contents.
     * </p>
     * <p>
     * The value associated to the parent map represents a whole Amazon S3 bucket where keys represents Amazon S3
     * keynames and values the associated object content.
     * </p>
     */
    private Map<String, Map<String, Object>> buckets = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public DeleteObjectResponse deleteObject(final DeleteObjectRequest deleteObjectRequest) {

        // Creates an empty fake bucket if it does not exists
        this.initBucket(deleteObjectRequest.bucket());

        // Deletes the object from the bucket
        this.buckets.get(deleteObjectRequest.bucket()).remove(deleteObjectRequest.key());

        // Create a fake Amazon S3 response
        return DeleteObjectResponse.builder().build();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public <ReturnT> ReturnT getObject(final GetObjectRequest getObjectRequest,
            final StreamingResponseHandler streamingHandler) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PutObjectResponse putObject(final PutObjectRequest putObjectRequest, final RequestBody requestBody) {

        // this.mockAddMethodCall(__FUNCTION__, ['options' => $options]);

        // Creates an empty fake bucket if it does not exists
        this.initBucket(putObjectRequest.bucket());

        // Put the content of the file into the fake bucket
        try {
            byte[] byteArray = IoUtils.toByteArray(requestBody.asStream());
            this.buckets.get(putObjectRequest.bucket()).put(putObjectRequest.key(), byteArray);
        } catch (IOException ioex) {
            throw new SdkClientException(ioex);
        } finally {
            try {
                requestBody.asStream().close();
            } catch (IOException ioex) {
                // TODO Auto-generated catch block
                ioex.printStackTrace();
            }
        }

        // Create a fake Amazon S3 response
        return PutObjectResponse.builder().build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws Exception {
        // Empty
    }

    /**
     * Initialize a fake Amazon S3 Bucket.
     * 
     * @param bucket the name of the fake Amazon S3 Bucket to initialize.
     */
    private void initBucket(final String bucket) {
        if (!this.buckets.containsKey(bucket)) {
            this.buckets.put(bucket, new HashMap<String, Object>());
        }
    }
}
