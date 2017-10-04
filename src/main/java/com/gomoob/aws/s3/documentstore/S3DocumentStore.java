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
import java.io.InputStream;
import java.util.Date;

import com.gomoob.aws.IS3;
import com.gomoob.documentstore.IDocumentStore;
import com.gomoob.documentstore.IDocumentStoreFile;
import com.gomoob.documentstore.filesystem.DocumentStoreFile;

import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.sync.RequestBody;
import software.amazon.awssdk.sync.StreamingResponseHandler;

/**
 * An Amazon S3 Implementation for the GOMOOB document store.
 *
 * @author Jiaming LIANG (jiaming.liang@gomoob.com)
 */
public class S3DocumentStore implements IDocumentStore {

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
    public IDocumentStoreFile createFromUploadedFile(final InputStream serverFileInputStream, final String keyName,
            final long fileSize) throws IOException {

        // Creates the request body
        RequestBody requestBody = RequestBody.of(serverFileInputStream, fileSize);

        return this.uploadToS3(requestBody, keyName, fileSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IDocumentStoreFile createFromUploadedFile(final String serverFilePath, final String keyName)
            throws IOException {

        // Checks the file
        File file = new File(serverFilePath);
        if (!file.exists()) {
            throw new IOException("Fail to open file with path '" + serverFilePath + "' !");
        }

        // Creates the request body
        RequestBody requestBody = RequestBody.of(file);

        return this.uploadToS3(requestBody, keyName, file.length());
    }

    /**
     * Gets the name of the Amazon S3 Bucket to use.
     *
     * @return the name of the Amazon S3 Bucket to use.
     */
    public String getBucket() {
        if (this.bucket == null) {
            throw new IllegalStateException("No bucket name has been configured !");
        }

        return this.bucket;
    }

    /**
     * Gets the absolute url of the uploaded file having the key name.
     *
     * @param the key name of the uploaded file.
     *
     * @return the absolute url of the uploaded file.
     */
    public String getUrl(final String keyName) {

        return "https://s3.amazonaws.com/" + this.bucket + "/" + this.createKeyNameWithPrefix(keyName);
    }

    /**
     * Sets the name of the bucket.
     *
     * @param bucket the name of the bucket.
     */
    public void setBucket(final String bucket) {
        this.bucket = bucket;
    }

    /**
     * Sets the prefix of key name of the bucket.
     *
     * @param keyNamePrefix the prefix of key name of the bucket.
     */
    public void setKeyNamePrefix(final String keyNamePrefix) {
        this.keyNamePrefix = keyNamePrefix;
    }

    /**
     * Sets the instance of the GOMOOB Amazon S3 facade.
     *
     * @param s3 the instance of the GOMOOB Amazon S3 facade.
     */
    public void setS3(final IS3 s3) {
        this.s3 = s3;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final String keyName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(this.getBucket())
                .key(this.createKeyNameWithPrefix(keyName)).build();
        this.s3.deleteObject(deleteObjectRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String download(final String keyName, final String destination) throws IOException {
        File destinationFile = new File(destination);
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(this.getBucket())
                .key(this.createKeyNameWithPrefix(keyName)).build();
        this.s3.getObject(getObjectRequest, StreamingResponseHandler.toFile(destinationFile.toPath()));

        return destinationFile.getAbsolutePath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IDocumentStoreFile find(final String keyName) {

        IDocumentStoreFile documentStoreFile = null;

        ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder().bucket(this.getBucket())
                .prefix(this.createKeyNameWithPrefix(keyName)).build();
        ListObjectsResponse listObjectsResponse = this.s3.listObjects(listObjectsRequest);

        // If one file has been found
        if (listObjectsResponse.contents() != null && listObjectsResponse.contents().size() == 1) {
            S3Object s3Object = listObjectsResponse.contents().get(0);
            documentStoreFile = new DocumentStoreFile();
            documentStoreFile.setKeyName(this.extractKeyNameWithoutPrefix(s3Object.key()));
            documentStoreFile.setName(s3Object.key().substring(s3Object.key().lastIndexOf('/') + 1));
            documentStoreFile.setLastAccessDate(null);
            documentStoreFile.setLastUpdateDate(Date.from(s3Object.lastModified()));
        }

        return documentStoreFile;
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

    /**
     * Extract the key name with the prefix from a key name with a prefix.
     *
     * @param keyNameWithPrefix the key name without prefix.
     *
     * @return the key name without prefix.
     */
    private String extractKeyNameWithoutPrefix(final String keyNameWithPrefix) {
        String keyName = keyNameWithPrefix;

        if (this.keyNamePrefix != null && this.keyNamePrefix.length() > 0) {
            keyName = keyNameWithPrefix.substring(this.keyNamePrefix.length() + 1);
        }

        return keyName;
    }

    /**
     * Utility function used to put an object to S3.
     *
     * @param requestBody the request body to put.
     * @param keyName the key name of the file.
     * @param fileSize the size of the file.
     *
     * @return the document store file.
     */
    private IDocumentStoreFile uploadToS3(final RequestBody requestBody, final String keyName, final long fileSize) {

        // Create the prefixed key name
        String prefixedKeyName = this.createKeyNameWithPrefix(keyName);

        // Puts the file on Amazon S3
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(this.bucket).key(prefixedKeyName).build();

        this.s3.putObject(putObjectRequest, requestBody);

        // Creates the Document Store File description to return
        // FIXME: problem of key name and prefixed key name for the attributes keyName and name.
        Date currentDate = new Date();
        IDocumentStoreFile documentStoreFile = new DocumentStoreFile();
        documentStoreFile.setKeyName(prefixedKeyName);
        documentStoreFile.setLastAccessDate(currentDate);
        documentStoreFile.setLastUpdateDate(currentDate);
        documentStoreFile.setName(prefixedKeyName);
        documentStoreFile.setSize(fileSize);

        return documentStoreFile;
    }

}
