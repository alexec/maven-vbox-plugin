Virtual Box Java API
===
Overview
===
This project provides support for creating, starting and stopping VirtualBox VMs. This is aimed at development and integration testing of projects by allowing you to package a complete software stack onto a single machine, install your code and perform your tests.

Some typical scenarios would be:

* Provide a dev stack and a touch of a button.
* Install all apps onto a VBox and test it.

It provides:

 1. A Java API for programatic control of boxes.
 2. A set of Maven Mojos.
 3. A set of matching Ant tasks.

Goals
===
To provide support for:

* Multiple host and guest OS, not least including Linux, Windows and OS-X
* Unattended install and provisioning of guest OSs.
* Multiple VMs per project.
* *Not* to be a replacement for VeeWee, Vagrant, Chef or Puppet.

Usage
===
The main mojos/tasks are:

* clean - deletes VMs
* create - creates VMs, generally not used as provision will create
* provision - provisions VMs, creating them if needs be
* start - start VMs
* stop - stops VMs

Additionally, a mojo for creating VM templates:

* list-definitions - list available template definitions
* create-definition - creates a VM template definition

Maven
===
Quick Start
---
Execute this:

    mvn com.alexecollins.vbox:vbox-maven-plugin:create-definition -Dvbox.name=UbuntuServer_12_10

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

Execute:

    mvn verify

Maven searches for VM definitions (see below) under src/main/vbox.

Ant
===
Quick Start
---
Add this to your build.xml:

    <project name="vbox-ant-tasks" default="use">
        <target name="use">
            <taskdef name="list-definitions" classname="com.alexecollins.vbox.ant.ListDefinitionsTask"/>
            <taskdef name="create-definition" classname="com.alexecollins.vbox.ant.CreateDefinitionTask"/>
            <taskdef name="clean" classname="com.alexecollins.vbox.ant.CleanTask"/>
            <taskdef name="provision" classname="com.alexecollins.vbox.ant.ProvisionTask"/>
            <taskdef name="start" classname="com.alexecollins.vbox.ant.StartTask"/>
            <taskdef name="stop" classname="com.alexecollins.vbox.ant.StopTask"/>

            <list-definitions/>
            <create-definition name="CentOS_6_3" dir="src/vbox/CentOS_6_3"/>
            <clean dir="src/vbox/CentOS_6_3" work="build"/>
            <provision dir="src/vbox/CentOS_6_3" work="build"/>
            <start dir="src/vbox/CentOS_6_3"/>
            <!-- ... -->
            <stop dir="src/vbox/CentOS_6_3"/>
        </target>
    </project>

Add the vbox-ant-tasks-*.jar to Ant's class path.

Ant tasks do not currently allow you to do multiple VMs in a single command. You'll need to use multiple ones.

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

Typically you'll want to make sure you VMs has:

* SSH access (or similar).
* ACPI support for graceful shutdown (many minimal installs don't).

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

32 Bit vs 64 Bit
---
Currently the definitions are all 32 bit. I _think_ you'll want to use the same bits on the guest as the host. It'll be faster.

If you want use 64 bit you typically need to:

- Append "_64" to the OS type, e.g. "RedHat_64".
- Enable IO ACPI (as a side-effect, it'll be much faster, if you're host OS is 64 bit).

Known Issues
===
* US keyboard layouts only.
* Limit sub-set of vbox set-up supported, e.g. Internal Network no supported.

Troubleshooting
===
* Sometimes VirtualBox gets in a state if VMs are not cleanly removed. Kill all VBox processes you can see.
* Host and guest firewalls can prevent OSs getting web resources. Disable the firewall.

References
===
* [VBoxManage](http://www.virtualbox.org/manual/ch08.html)
* [Ubuntu/Debian preseed.cfg](https://help.ubuntu.com/12.10/installation-guide/i386/preseed-using.html)
* [VeeWee](https://github.com/jedi4ever/veewee)
* [Oracle blog on VirtualBox networking](https://blogs.oracle.com/fatbloke/entry/networking_in_virtualbox1)
