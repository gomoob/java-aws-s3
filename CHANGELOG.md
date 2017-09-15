# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/) and this project adheres to
[Semantic Versioning](http://semver.org/).

## [1.0.2] - 2017-09-15
 * Revert changes of version `1.0.1` because the stream closing bug was not due to the AWS SDK library and bad stream
   closing in the `S3Mock` class.

## [1.0.1] - 2017-09-14
 * Fix stream not closed after calling `S3DocumentStore.createFromUploadedFile(...)`.

## [1.0.0] - 2017-09-14

* Initial release.
