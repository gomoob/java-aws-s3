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

import com.gomoob.aws.IS3;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.sync.RequestBody;
import software.amazon.awssdk.sync.StreamingResponseHandler;

/**
 * An implementation for the {@link IS3} interface which uses the {@link S3Client} Amazon Java SDK class.
 *
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 *
 * @see <a href="https://github.com/aws/aws-sdk-java-v2">https://github.com/aws/aws-sdk-java-v2</a>
 */
public class S3 implements IS3 {

    /**
     * An Amazon S3 client used to request the Amazon Web Services.
     */
    protected S3Client s3Client;

    /**
     * Creates a new {@link S3} instance.
     *
     * @param s3Client the Amazon S3 client to which one to delegate calls and request the Amazon S3 Web Services.
     */
    public S3(final S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeleteObjectResponse deleteObject(final DeleteObjectRequest deleteObjectRequest) {
        return this.s3Client.deleteObject(deleteObjectRequest);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public <ReturnT> ReturnT getObject(GetObjectRequest getObjectRequest, StreamingResponseHandler streamingHandler) {
        return this.s3Client.getObject(getObjectRequest, streamingHandler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListObjectsResponse listObjects(ListObjectsRequest listObjectsRequest) {
        return this.s3Client.listObjects(listObjectsRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PutObjectResponse putObject(final PutObjectRequest putObjectRequest, final RequestBody requestBody) {
        return this.s3Client.putObject(putObjectRequest, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws Exception {
        // Empty
    }
}
