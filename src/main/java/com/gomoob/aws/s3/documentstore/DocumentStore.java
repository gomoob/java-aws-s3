package com.gomoob.aws.s3.documentstore;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gomoob.aws.IDocumentStore;
import com.gomoob.aws.IS3;

/**
 * An Amazon S3 Implementation for the GOMOOB document store.
 *
 * @author Jiaming LIANG (jiaming.liang@gomoob.com)
 */
@Component
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
    @Autowired
    private IS3 s3;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createFromUploadedFile(String serverFilePath, String keyName) throws IOException {

        // Create the prefixed key name
        String prefixedKeyName = this.createKeyNameWithPrefix(keyName);

        // Checks the file
        File file = new File(serverFilePath);
        if (!file.exists()) {
            throw new IOException("Fail to open file with path '" + serverFilePath + "' !");
        }

        // Puts the file on Amazon S3
        this.s3.putObject(this.getBucket(), prefixedKeyName, file);
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
