package com.gomoob.aws;

import java.io.File;

import software.amazon.awssdk.services.s3.model.PutObjectResponse;

/**
 * Interface which defines operations on Amazon S3.
 *
 * @author Jiaming LIANG (jiaming.liang@gomoob.com)
 */
public interface IS3 {
    /**
     * Adds an object to a bucket.
     *
     * @param bucket the name of the bucket.
     * @param key the key of the bucket.
     * @param file the file to add to the bucket.
     *
     * @return
     */
    public PutObjectResponse putObject(final String bucket, final String key, final File file);

}
