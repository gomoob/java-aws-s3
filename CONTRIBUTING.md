# Building on a development machine

The project is based on Maven, to build the project you first have to create a special `.testing.properties` file at
the root of the project.

The purpose of this file is to provide secret Amazon AWS credentials to let integration tests work, this file must
contain the following informations.

```
aws.key=XXXXXXXX
aws.secret=XXXXXXXX
aws.region=us-east-1
aws.s3.bucket=my-bucket
```

After having creating this file you should ensure your testing Amazon S3 bucket is available avec has the following
keys.

* `java-aws-s3/OBJECT_1` (plain file with string `OBJECT_1` inside)
* `java-aws-s3/OBJECT_2` (plain file with string `OBJECT_2` inside)
* `java-aws-s3/OBJECT_3` (plain file with string `OBJECT_3` inside)

Then the `mvn test` or `mvn install` command should work.

# Building on Travis

Travis builds are slightly different because they do not use the `.testing.properties` file, instead they use the
following environment variables which are configured as a variable in the Travis repository (see
[Defining Variables in Repository Settings](https://docs.travis-ci.com/user/environment-variables/#Defining-Variables-in-Repository-Settings)).

* `AWS_KEY`
* `AWS_SECRET`
* `AWS_REGION`
* `AWS_S3_BUCKET`

# Prepare a new release

The release process is described at http://central.sonatype.org/pages/apache-maven.html.

First create the release and deploy the artifacts to the Central Maven Repository.

```
mvn clean deploy -P release
```

Then creates a GIT tag with release log comments on Github.
