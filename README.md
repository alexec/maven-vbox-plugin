Overview
===
This plugin provides support for creating, starting and stopping VirtualBox VMs. This is aimed at development and integration testing of projects by allowing you to package a complete software stack onto a single machine, install your code and perform your tests.

Some typical scenarios would be:

* Provide a dev stack and a touch of a button.
* Install all apps onto a VBox and test it.

Goals
===
To provide support for:

* Multiple host and guest OS, not least including Linux, Windows and OS-X
* Unattended install and provioning of guest OSs.
* Multiple VMs per project.
* Not to be a replacement for VeeWee, Vargant, Chef or Puppet.

Usage
===
The main mojos are:

* clean - deletes all VMs
* create - creates all VMs
* provision - provisions all VMs

Additionally, a mojo for creating VM templates:

* create-definition - creates a VM template definition

An example template can be found in src/test/vbox/UbuntuServer. Typically you'd create a series of definitions in src/main/vbox, alongside supporting files, for example an Ubuntu server might be name "UbuntuServer":

    src/main/vbox/
        UbuntuServer/      - The name of the server.
            VirtualBox.xml - The configuration of the server (e.g. disk etc.)
            Manifest.xml   - A list of all files used by the server (e.g. preseed.cfg, Unattended.xml etc.).
            Provisions.xml - The steps required to get the box ready (e.g. install Apache, set-up DNS etc.)

The example I use downloads (by setting the DVDImage location to the URL) and attaches it. It then uses a preseed.cfg to create the VM.

You'll want to include an additional files, either a preseed.cfg for a Ubuntu VM, or an AutoUnatted.xml for a Windows. These files tell the installer how to set-up the OS. List these in the Manifest.

You can access those files from the guest by:

* HTTP only.

When provisioning starts up, all the files in your definition dir are available on http://%IP%:%PORT%/, e.g. for preseed.cfg.

Supported Host OS Types
===
* Mac OS-X
* Windows 7

Supported Guest OS Types
===
* Ubuntu
* Windows2008

Known Issues
===
* Only support for one definition ATM.
* US keyboard layouts only.

Troubleshooting
===
* Sometimes VirtualBox gets in a state if VMs are not cleanly removed. Kill all VBox processes you can see.
* Host and guest firewalls can prevent OSs getting web resources. Disable the firewall.

References
===
* [VBoxManage|http://www.virtualbox.org/manual/ch08.html]
* [Ubuntu/Debian preseed.cfg|https://help.ubuntu.com/10.04/installation-guide/i386/preseed-using.html]
* [http://www.perkin.org.uk/posts/create-virtualbox-vm-from-the-command-line.html]
* [VeeWee|https://github.com/jedi4ever/veewee]
