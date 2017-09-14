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
package com.gomoob.aws.s3.documentstore;

import java.io.File;
import java.io.IOException;

import com.gomoob.aws.IDocumentStore;
import com.gomoob.aws.IS3;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.sync.RequestBody;

/**
 * An Amazon S3 Implementation for the GOMOOB document store.
 *
 * @author Jiaming LIANG (jiaming.liang@gomoob.com)
 */
public class DocumentStore implements IDocumentStore {

    /**
     * The name of the Amazon S3 Bucket to use.
     */
    private String bucket;

    /**
     * A prefix to be placed before all the key names passed as arguments to the functions of this class. The purpose of
     * the prefix is to manage files inside a subdirectory of an Amazon S3 bucket.
     */
    private String keyNamePrefix = "";

    /**
     * An instance of the GOMOOB Amazon S3 facade.
     */
    private IS3 s3;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createFromUploadedFile(final String serverFilePath, final String keyName) throws IOException {

        // Create the prefixed key name
        String prefixedKeyName = this.createKeyNameWithPrefix(keyName);

        // Checks the file
        File file = new File(serverFilePath);
        if (!file.exists()) {
            throw new IOException("Fail to open file with path '" + serverFilePath + "' !");
        }

        // Puts the file on Amazon S3
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(this.bucket).key(prefixedKeyName).build();
        this.s3.putObject(putObjectRequest, RequestBody.of(file));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBucket() {
        if (this.bucket == null) {
            throw new IllegalStateException("No bucket name has been configured !");
        }

        return this.bucket;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBucket(final String bucket) {
        this.bucket = bucket;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setKeyNamePrefix(final String keyNamePrefix) {
        this.keyNamePrefix = keyNamePrefix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setS3(final IS3 s3) {
        this.s3 = s3;
    }

    /**
     * Function used to create a key name with the key name prefixed configured in the document store.
     *
     * @param keyName the provided key name.
     *
     * @return the key name modified with the configured prefix.
     */
    private String createKeyNameWithPrefix(final String keyName) {
        String prefix = this.keyNamePrefix;
        String keyNameWithPrefix;

        // If the first character is `/`, delete it.
        if (prefix.indexOf('/') == 0) {
            prefix = prefix.substring(1);
        }

        // If the key name prefix is null or empty we simply return the provided key name
        if (prefix == null || prefix.isEmpty()) {
            keyNameWithPrefix = keyName;
        } else {
            if (keyName.indexOf('/') == 0) {
                keyNameWithPrefix = prefix + keyName;
            } else {
                keyNameWithPrefix = prefix + '/' + keyName;
            }
        }

        return keyNameWithPrefix;
    }

}
