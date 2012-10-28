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
* Not to be a replacment for VeeWee, Vargant, Chep or Puppet.

Usage
===
The main mojos are:

* clean - deletes all VMs
* create - creates all VMs
* provision - provisions all VMs

An example template can be found in src/test/vbox/UbuntuServer.

When provisioning starts up, all the files in your definition dir are available on http://%IP%:%PORT%/, e.g. for preseed.cfg.

Supported OS Types
===
* Ubuntu

Known Issues
===
* Only support for one definition ATM.
* US keyboard layouts.
