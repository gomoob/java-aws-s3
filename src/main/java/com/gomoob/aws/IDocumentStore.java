package com.gomoob.aws;

import java.io.IOException;

/**
 * Interface which acts as a facade to a document store.
 *
 * <p>
 * This interface has to be very generic to allow developers to create multiple implementations.
 * </p>
 *
 * @author Jiaming LIANG (jiaming.liang@gomoob.com)
 */
public interface IDocumentStore {

    /**
     * Creates a new file in the document store by copying an uploaded file.
     *
     * <p>
     * This function is used to create NEW file and throw an exception if the file to create already exists in the
     * store.
     * </p>
     *
     * @param serverFilePath the path to the uploaded file to copy into the document store.
     * @param keyName the key name to be given to the new file to create. The keyname is a string which has a format
     *            which is the same as a relative file path.
     */
    public void createFromUploadedFile(final String serverFilePath, final String keyName) throws IOException;

    /**
     * Gets the name of the Amazon S3 Bucket to use.
     *
     * @return the name of the Amazon S3 Bucket to use.
     */
    public String getBucket();

    /**
     * Sets the name of the bucket.
     *
     * @param bucket the name of the bucket.
     */
    public void setBucket(final String bucket);

    /**
     * Sets the instance of the GOMOOB Amazon S3 facade.
     *
     * @param s3 the instance of the GOMOOB Amazon S3 facade.
     */
    public void setS3(final IS3 s3);

    /**
     * Sets the prefix of key name of the bucket.
     *
     * @param keyNamePrefix the prefix of key name of the bucket.
     */
    public void setKeyNamePrefix(final String keyNamePrefix);

}
