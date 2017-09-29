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
package com.gomoob.documentstore;

import java.io.IOException;
import java.io.InputStream;

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
     * @param keyName the key name to be given to the new file to create. The keyn ame is a string which has a format
     *            which is the same as a relative file path.
     *
     * @return an object which describes the file which have been uploaded on the document store.
     *
     * @throws IOException if an input / output error occurs while downloading or creating the file.
     */
    public IDocumentStoreFile createFromUploadedFile(final String serverFilePath, final String keyName)
            throws IOException;

    /**
     * Creates a new file in the document store by copying an uploaded file.
     *
     * <p>
     * This function is used to create NEW file and throw an exception if the file to create already exists in the
     * store.
     * </p>
     *
     * @param serverFileInputStream the input stream of the file to copy into the document store.
     * @param keyName the key name to be given to the new file to create. The keyn ame is a string which has a format
     *            which is the same as a relative file path.
     * @param fileSize the size of file.
     *
     * @return an object which describes the file which have been uploaded on the document store.
     *
     * @throws IOException if an input / output error occurs while downloading or creating the file.
     */
    public IDocumentStoreFile createFromUploadedFile(final InputStream serverFileInputStream, final String keyName,
            final long fileSize) throws IOException;

    /**
     * Deletes a file associated to a specified key name.
     *
     * @param keyName the key name used to find the file to delete from the store.
     */
    public void delete(final String keyName);

    /**
     * Download a file associated to a specified key name to a generic destination. The destination is expressed using a
     * normal file path or a custom URL to "download" the file using SFTP or an other mechanism (the supported
     * destinations depends on the implementations of the document store in use).
     *
     * <p>
     * NOTE: Please note that this function has not be named 'copy' because the 'copy' function is reserved to document
     * copying on the document store itself and not outside the document store.
     * </p>
     *
     * @param keyName the key name used to find the file to download to the destination.
     * @param destination the place where to download the file.
     *
     * @return string the provided <tt>destination</tt> parameter if the download is successful.
     *
     * @throws IOException If the download operation has failed.
     */
    public String download(final String keyName, final String destination) throws IOException;

    /**
     * Find a file associated to a specified key name.
     *
     * @param keyName the key name used to find the file in the document store.
     *
     * @return the found file or <code>null</code>.
     */
    public IDocumentStoreFile find(final String keyName);
}
