Definitions
===
Definitions can be found in src/test/vbox. Typically you'd create a series of definitions in src/main/vbox, alongside supporting files, for example an Ubuntu server might be named "UbuntuServer":

* src/main/vbox/
    * UbuntuServer/         - The name of the server.
        * MediaRegistry.xml - A list of media to get (e.g. from a URL or fileshare). Similar to a fragment of VirtualBox.xml file.
            * VirtualBox.xml    - The configuration of the server (e.g. disk etc.). Intentionally similar to one of Virtual Box's .vbox XML files.
            * Manifest.xml      - A list of all files used by the server (e.g. preseed.cfg, AutoUnattend.xml etc.). Optional.
            * Provisioning.xml  - The steps required to get the box ready (e.g. install Apache, set-up DNS etc.). Intentionally similar to an Ant script.
            * Profile.xml       - Information about the box, such as if it is headless, and how to determine if it's stared successfully.

The Ubuntu example downloads (by setting the DVDImage location to the URL) and attaches it. It then uses a preseed.cfg to create the VM.

You'll want to include an additional files, either a preseed.cfg for an Ubuntu VM, or an AutoUnattend.xml for a Windows. These files tell the installer how to set-up the OS.  To expose them to the VM you can either:

* Mount a floppy (esp. for Windows).
* Access the files by HTTP. When provisioning starts up, all the files in your definition dir are available on http://${server.ip}:${server.port}/.

Typically you'll want to make sure you VMs has:

* SSH access (or similar).
* ACPI support for graceful shutdown (many minimal installs don't).

Tokens
---
The following tokens are recognised in some XML documents:

* ${vbox.name} - Then name of the guest OS.
* ${server.ip} - The IP of the host.
* ${server.port} - The port the web server is running on.
* ${vbox.additions} - The path the VirtualBox Guest Additions on the host OS.

Authentication
---
By default the username is "vbox" and the default password "123456".

Supported Host OS Types
---
* Mac OS-X
* Windows 7

Unlisted OSs should all work.

Supported Guest OS Types/Supplied Definitions
---
* CentOS_6_5
* UbuntuServer_12_10
* WindowsServer2008

Unlisted OSs may work.
