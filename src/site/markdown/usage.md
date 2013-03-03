Usage
===
The main mojos/tasks are split into three groups:

Managing Definitions
---
Task that work on a single definition:

* list-definitions - list available template definitions
* create-definition - creates a VM template definition
* delete-definition - delete the definition
* list-predefined-patches - list built-in patches
* patch-definition - patch a definition with one or more patches

Provisioning Tasks
---
Task related to setting up one or more boxes:

* clean - deletes VMs
* create - creates VMs, generally not used as provision will create the VM if it doesn't exist
* provision - provisions VMs, creating them if needs be

Runtime Tasks
---
Tasks related to the usage of boxes:

* start - start VMs
* stop - stops VMs


