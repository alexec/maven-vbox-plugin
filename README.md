Overview
===
This project provides support for creating, starting and stopping VirtualBox VMs. This is aimed at development and integration testing of projects by allowing you to package a complete software stack onto a single machine, install your code and perform your tests.

Some typical scenarios would be:

* Provide a dev stack and a touch of a button.
* Install all apps onto a VBox and test it.

Goals
===
To provide support for:

* Multiple host and guest OS, not least including Linux, Windows and OS-X
* Unattended install and provisioning of guest OSs.
* Multiple VMs per project.
* Not to be a replacement for VeeWee, Vagrant, Chef or Puppet.

Usage
===
The main mojos/tasks are:

* clean - deletes VMs
* create - creates VMs
* provision - provisions VMs

Additionally, a mojo for creating VM templates:

* create-definition - creates a VM template definition

Ant
===
Quick Start
---
Add this to your build.xml:

    <project name="vbox-ant-tasks" default="use">
        <target name="use">
            <taskdef name="create-definition" classname="com.alexecollins.vbox.ant.CreateDefinitionTask"/>
            <taskdef name="clean" classname="com.alexecollins.vbox.ant.CleanTask"/>
            <taskdef name="create" classname="com.alexecollins.vbox.ant.CreateTask"/>
            <taskdef name="provision" classname="com.alexecollins.vbox.ant.ProvisionTask"/>

            <create-definition name="CentOS_6_3" dir="src/vbox/CentOS_6_3"/>
            <clean dir="src/vbox/CentOS_6_3" work="build"/>
            <create dir="src/vbox/CentOS_6_3" work="build"/>
            <provision dir="src/vbox/CentOS_6_3" work="build"/>
        </target>
    </project>

Add the vbox-ant-tasks-*.jar to Ant's class path.

Ant tasks do not currently allow you to do multiple VMs in a single command. You'll need to use multiple ones.

Maven
===
Quick Start
---
Execute this:

    mvn com.alexecollins.maven.plugins.vbox:create-definition -Dvbox.name=UbuntuServer_12_10

Add this to your pom.xml:

    <plugin>
        <groupId>com.alexecollins.maven.plugins.vbox</groupId>
        <artifactId>maven-vbox-plugin</artifactId>
        <version>1.0.0-SNAPSHOT</version>
        <executions>
            <execution>
                <goals>
                    <goal>clean</goal>
                    <goal>create</goal>
                    <goal>provision</goal>
                </goals>
            </execution>
        </executions>
    </plugin>

Execute:

    mvn verify

Maven searches for VM definitions (see below) under src/main/vbox.

Definitions
===
Definitions can be found in src/test/vbox. Typically you'd create a series of definitions in src/main/vbox, alongside supporting files, for example an Ubuntu server might be named "UbuntuServer":

    src/main/vbox/
        UbuntuServer/         - The name of the server.
            MediaRegistry.xml - A list of media to get (e.g. from a URL or fileshare). Similar to a fragment of VirtualBox.xml file.
            VirtualBox.xml    - The configuration of the server (e.g. disk etc.). Intentionally similar to one of Virtual Box's .vbox XML files.
            Manifest.xml      - A list of all files used by the server (e.g. preseed.cfg, AutoUnattend.xml etc.). Optional.
            Provisioning.xml  - The steps required to get the box ready (e.g. install Apache, set-up DNS etc.). Intentionally similar to an Ant script.

The Ubuntu example downloads (by setting the DVDImage location to the URL) and attaches it. It then uses a preseed.cfg to create the VM.

You'll want to include an additional files, either a preseed.cfg for an Ubuntu VM, or an AutoUnattend.xml for a Windows. These files tell the installer how to set-up the OS.  To expose them to the VM you can either:

* Mount a floppy (esp. for Windows).
* Access the files by HTTP. When provisioning starts up, all the files in your definition dir are available on http://%IP%:%PORT%/.

Tokens
---
The following tokens are recognised in some XML documents:

* %NAME% - Then name of the guest OS.
* %IP% - The IP of the host.
* %PORT% - The port the web server is running on.
* %VBOX_ADDITIONS% - The path the VirtualBox Guest Additions on the host OS.

Authentication
---
By default the username is "tallquark" and the default password "keenbrick".

Supported Host OS Types
===
* Mac OS-X
* Windows 7

Unlisted OSs should all work.

Supported Guest OS Types/Supplied Definitions
===
* CentOS_6_3
* UbuntuServer_12_10
* WindowsServer2008

Unlisted OSs may work.

Known Issues
===
* US keyboard layouts only.

Troubleshooting
===
* Sometimes VirtualBox gets in a state if VMs are not cleanly removed. Kill all VBox processes you can see.
* Host and guest firewalls can prevent OSs getting web resources. Disable the firewall.

References
===
* [VBoxManage](http://www.virtualbox.org/manual/ch08.html)
* [Ubuntu/Debian preseed.cfg](https://help.ubuntu.com/12.10/installation-guide/i386/preseed-using.html)
* [VeeWee](https://github.com/jedi4ever/veewee)
