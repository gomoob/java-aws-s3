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

import java.util.Date;

/**
 * Interface which representes a document store file.
 *
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public interface IDocumentStoreFile {

    /**
     * Gets the key name which indicate where the file is stored in the document store.
     *
     * @return the key name which indicate where the file is stored in the document store.
     */
    public String getKeyName();

    /**
     * Gets the date of the last access to the file.
     *
     * @return the date of the last access to the file.
     */
    public Date getLastAccessDate();

    /**
     * Gets the date of the last update of the file.
     *
     * @return the date of the last update of the file.
     */
    public Date getLastUpdateDate();

    /**
     * Gets the name of the file.
     *
     * @return the name of the file.
     */
    public String getName();

    /**
     * Gets the size of the file in bytes.
     *
     * @return the size of the file in bytes.
     */
    public int getSize();

    /**
     * Sets the key name which indicate where the file is stored in the document store.
     *
     * @param keyName the key name which indicate where the file is stored in the document store.
     */
    public void setKeyName(final String keyName);

    /**
     * Sets the date of the last access to the file.
     *
     * @param lastAccessDate the date of the last access to the file.
     */
    public void setLastAccessDate(final Date lastAccessDate);

    /**
     * Sets the date of the last update of the file.
     *
     * @param lastUpdateDate the date of the last update of the file.
     */
    public void setLastUpdateDate(final Date lastUpdateDate);

    /**
     * Sets the name of the file.
     *
     * @param name the name of the file.
     */
    public void setName(final String name);

    /**
     * Sets the size of the file in bytes.
     *
     * @param size the size of the file in bytes.
     */
    public void setSize(final int size);
}
