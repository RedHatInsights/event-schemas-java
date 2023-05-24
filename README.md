console.redhat.com CloudEvents for Java
=======================================

This repo contains Java bindings of the JSON schema definitions for events
generated by console.redhat.com

Getting started
---------------

This library is released on [maven central](https://central.sonatype.com/artifact/com.redhat.cloud.event/event-schemas).
You can include the following in your `pom.xml` if you are using Maven:

<!--- x-release-please-start-version -->

```xml
<dependency>
  <groupId>com.redhat.cloud.event</groupId>
  <artifactId>event-schemas</artifactId>
  <version>1.4.7</version>
</dependency>
```

<!--- x-release-please-end -->

The library contains the [ConsoleCloudEventParser](https://redhatinsights.github.io/event-schemas-java/com/redhat/cloud/event/parser/ConsoleCloudEventParser.html) 
with methods to transform a String to a [ConsoleCloudEvent](https://redhatinsights.github.io/event-schemas-java/com/redhat/cloud/event/parser/ConsoleCloudEvent.html)
or any other class that extends [GenericConsoleCloudEvent](https://redhatinsights.github.io/event-schemas-java/com/redhat/cloud/event/parser/GenericConsoleCloudEvent.html) and vice versa.

```java
package acme.parse;

import com.redhat.cloud.event.parser.ConsoleCloudEvent;
import com.redhat.cloud.event.parser.ConsoleCloudEventParser;

class ParseMyConsoleCloudEvent {
    public string main(String... args) {
        ConsoleCloudEventParser parser = new ConsoleCloudEventParser();
        ConsoleCloudEvent event = parser.fromJsonString(args[0]);
        
        System.out.println("Got a console cloud event with type: " + event.getType());
    }
}
```

You can check the file [ConsoleCloudEventParseTest](./src/test/java/com/redhat/cloud/event/parser/ConsoleCloudEventParserTest.java)
for additional usage.

Layout
------

* `api`: Defines a submodule with the current version of console.redhat.com 
  CloudEvents
* `examples`: Contains examples on how to use this library.
* `src/main/java/parser`: Contains a custom parser to load, save and validate
  console.redhat.com cloud events.

Generating code
---------------

The [generate.js](./scripts/generate.js) script generates the Java code.
It overwrites the default Java language config by using 
[CloudEventJavaLanguage](./scripts/CloudEventJavaLanguage.js) class to append additional
data to the code, such as extra annotations.

This script is executed on any push to our main branch, ensuring it never goes out 
of sync.

Releasing a new version
-----------------------

The release process works with 
[release-please-action](https://github.com/google-github-actions/release-please-action).
You can head to their 
[documentation](https://github.com/google-github-actions/release-please-action) 
to learn more about it, but in a nutshell:
 - Uses [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) 
 - Creates a PR when there is something to be released (patch, minor or major).
   Once that PR is merged, a new release occurs. 

The release is published to [maven central](https://search.maven.org/), but it goes first
through [Red Hat's nexus repository](https://repository.jboss.org/nexus).
It has been observed that it takes roughly a day for the new version to land on 
[maven central](https://search.maven.org/).

Docs
----

Java docs are automatically generated and hosted on GitHub pages: 
https://redhatinsights.github.io/event-schemas-java/.
