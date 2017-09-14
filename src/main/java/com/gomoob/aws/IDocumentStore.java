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
