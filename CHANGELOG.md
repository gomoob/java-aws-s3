# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/) and this project adheres to
[Semantic Versioning](http://semver.org/).

## [1.4.0] - 2018-03-22
 * Add `setCacheControl(...)` / `getCacheControl()` and `setContentType(...)` / `getContentType()` methods in the
   `S3UploadConfig` class.

## [1.3.0] - 2018-03-21
 * Allow the `IDocumentStore` to accept an `IUploadConfig` file to pass additional configuration properties
   while uploading a file.

## [1.2.0] - 2017-10-06
 * Add a new `S3DocumentStore.getUrl(final String keyName)` function to create absolute Amazon S3 urls ;
 * Add an implementation for `S3Mock.deleteObject(final DeleteObjectRequest deleteObjectRequest)`.

## [1.1.0] - 2017-09-29
 * Now the `IDocumentStore.createFromUploadedFile(...)` methods return an `IDocumentStoreFile` which describes the
  file uploaded on the document store.

## [1.0.2] - 2017-09-15
 * Revert changes of version `1.0.1` because the stream closing bug was not due to the AWS SDK library and bad stream
   closing in the `S3Mock` class.

## [1.0.1] - 2017-09-14
 * Fix stream not closed after calling `S3DocumentStore.createFromUploadedFile(...)`.

## [1.0.0] - 2017-09-14

* Initial release.
