# Prepare a new release

The release process is described at http://central.sonatype.org/pages/apache-maven.html.

First create the release and deploy the artifacts to the Central Maven Repository.

```
mvn clean deploy -P release
```

Then creates a GIT tag with release log comments on Github.
