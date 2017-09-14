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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gomoob.aws.AbstractS3Test;
import com.gomoob.aws.s3.S3;
import com.gomoob.documentstore.IDocumentStoreFile;

import software.amazon.awssdk.utils.IoUtils;

/**
 * Test case for the {@link S3DocumentStore} class.
 *
 * @author Baptiste GAILLARD (baptiste.gaillard@gomoob.com)
 */
public class S3DocumentStoreTest extends AbstractS3Test {

    /**
     * An instance of the class to test.
     */
    private S3DocumentStore s3DocumentStore;

    /**
     * Test method executed after each test method.
     */
    @After
    public void after() throws Exception {

        // Cleanup the testing bucket
        this.cleanupTestingBucket();

        // Cleanup the temporary folder used to write test files
        this.deleteFolder(new File("target/tmp"));

    }

    /**
     * Test method executed before each test method.
     */
    @Before
    public void before() throws Exception {

        // Cleanup the temporary folder used to write test files
        File tmpFolder = new File("target/tmp");
        this.deleteFolder(tmpFolder);
        tmpFolder.mkdirs();

        // Cleanup the testing bucket
        this.cleanupTestingBucket();

        // Creates an instance of the class to test
        this.s3DocumentStore = new S3DocumentStore();
        this.s3DocumentStore.setBucket(this.testingBucket());
        this.s3DocumentStore.setKeyNamePrefix("java-aws-s3");
        this.s3DocumentStore.setS3(new S3(getTestingS3Client()));

    }

    /**
     * Test method for {@link S3DocumentStore#createFromUploadedFile(String, String)}.
     *
     * @throws Exception if an unexpected error occurs in the test.
     */
    @Test
    public void testCreateFromUploadedFile() throws Exception {

        // At the beginning no "java-aws-s3/NEW_OBJECT" key exists on our bucket
        assertNull(this.s3DocumentStore.find("NEW_OBJECT"));

        // Put our new object
        this.s3DocumentStore.createFromUploadedFile("target/test-classes/NEW_OBJECT", "NEW_OBJECT");

        // Check our new object now exists and has the right content
        IDocumentStoreFile documentStoreFile = this.s3DocumentStore.find("NEW_OBJECT");
        assertNotNull(documentStoreFile);
        assertEquals("NEW_OBJECT", documentStoreFile.getKeyName());
        assertNull(documentStoreFile.getLastAccessDate());
        assertNotNull(documentStoreFile.getLastUpdateDate());
        assertEquals("NEW_OBJECT", documentStoreFile.getName());
        assertEquals(0, documentStoreFile.getSize());

        String destination = this.s3DocumentStore.download("NEW_OBJECT", "target/tmp/NEW_OBJECT");
        assertNotNull(destination);
        InputStream is = new FileInputStream(destination);
        assertEquals("NEW_OBJECT", IoUtils.toString(is));
        is.close();
    }

    /**
     * Test method for {@link S3DocumentStore#delete(String)}.
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testDelete() throws Exception {

        // Put a new object
        this.s3DocumentStore.createFromUploadedFile("target/test-classes/NEW_OBJECT", "NEW_OBJECT");
        IDocumentStoreFile documentStoreFile = this.s3DocumentStore.find("NEW_OBJECT");
        assertNotNull(documentStoreFile);

        // Delete the object
        this.s3DocumentStore.delete("NEW_OBJECT");
        documentStoreFile = this.s3DocumentStore.find("NEW_OBJECT");
        assertNull(documentStoreFile);
    }

    /**
     * Test method for {@link S3DocumentStore#download(String, String)}.
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testDownload() throws Exception {

        String destination = this.s3DocumentStore.download("OBJECT_1", "target/tmp/OBJECT_1");

        assertNotNull(destination);

        InputStream is = new FileInputStream(destination);

        assertEquals("OBJECT_1", IoUtils.toString(is));

        is.close();
    }

    /**
     * Test method for {@link S3DocumentStore#find(String)}.
     */
    @Test
    public void testFind() {

        // Test with an object which does not exist
        assertNull(this.s3DocumentStore.find("DOES_NOT_EXIST"));

        // Test with an object which exist
        IDocumentStoreFile documentStoreFile = this.s3DocumentStore.find("OBJECT_1");
        assertNotNull(documentStoreFile);
        assertEquals("OBJECT_1", documentStoreFile.getKeyName());
        assertNull(documentStoreFile.getLastAccessDate());
        assertNotNull(documentStoreFile.getLastUpdateDate());
        assertEquals("OBJECT_1", documentStoreFile.getName());
        assertEquals(0, documentStoreFile.getSize());
    }
}
