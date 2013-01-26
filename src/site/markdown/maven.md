Maven
===
Quick Start
---
Add this to your pom.xml:

    <plugin>
        <groupId>com.alexecollins.vbox</groupId>
        <artifactId>vbox-maven-plugin</artifactId>
        <version>1.0.0-SNAPSHOT</version>
        <executions>
            <execution>
                <goals>
                    <goal>clean</goal>
                    <goal>provision</goal>
                    <goal>start</goal>
                    <goal>stop</goal>
                </goals>
            </execution>
        </executions>
    </plugin>

Execute this:

    mvn vbox:create-definition -Dvbox.name=CentOS_6_3

Execute:

    mvn verify

Maven searches for VM definitions (see below) under src/main/vbox.

