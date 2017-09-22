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
package com.gomoob.documentstore.filesystem;

import java.util.Date;

import com.gomoob.documentstore.IDocumentStoreFile;

/**
 * A filesystem implementation for the {@link IDocumentStoreFile} interface.
 *
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public class DocumentStoreFile implements IDocumentStoreFile {

    /**
     * The key name which indicate where the file is stored in the document store.
     */
    private String keyName;

    /**
     * The date of the last access to the file.
     */
    private Date lastAccessDate;

    /**
     * The date of the last update of the file.
     */
    private Date lastUpdateDate;

    /**
     * The name of the file.
     */
    private String name;

    /**
     * The size of the file in bytes.
     */
    private long size;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKeyName() {
        return this.keyName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getLastAccessDate() {
        return this.lastAccessDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getLastUpdateDate() {
        return this.lastUpdateDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getSize() {
        return this.size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setKeyName(final String keyName) {
        this.keyName = keyName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLastAccessDate(final Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLastUpdateDate(final Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSize(final long size) {
        this.size = size;
    }
}
