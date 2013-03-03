Usage
===
The main mojos/tasks are split into three groups:

Managing Definitions
---
Task that work on a single definition:

* list-definitions - list available template definitions
* create-definition - creates a box from template definition
* delete-definition - delete the definition
* list-predefined-patches - list built-in patches
* patch-definition - patch a definition with one or more patches

Provisioning Tasks
---
Task related to setting up one or more boxes:

* clean - deletes boxes
* create - creates boxes, generally not used as provision will create the box if it doesn't exist
* provision - provisions boxes, creating them if needs be

Runtime Tasks
---
Tasks related to the usage of boxes:

* start - start boxes
* stop - stops boxes
* suspend - suspend the boxes
* resume - resume boxes


