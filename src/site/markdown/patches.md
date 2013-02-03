Patches
===
A patch is a way of modifying a definition. Typically a patch will take a base definition and add support for new features. An example would be installing Tomcat onto a server. Patches are applied after a definition in created, but before the machine is created (in fact, applying a patch after a machine is created will change its (signature)[signatures.md] and result in its rebuild.

There are two types of patch _predefined_, _user defined_ and _custom_.

Predefined Patches
---
Predefined patches can be listed with the Maven plugin:

    mvn vbox:list-predefined-patches

Typically a predefined patch has a name which is the concatenation of the template used to create it, two dashes, and a short description. E.g.:

    CentOS_6_3--tomcat6

To apply a patch you need to add it to your XML. For example, you can get it to create patches as follows:

    <execution>
        <id>create-definition</id>
        <goals><goal>create-definition</goal></goals>
        <configuration>
            <templateName>CentOS_6_3</templateName>
            <name>app1</name>
        </configuration>
    </execution>
    <execution>
        <id>patch-definition</id>
        <goals><goal>patch-definition</goal></goals>
        <configuration>
            <patches>
                <predefinedPatch>
                    <name>CentOS_6_3--tomcat6</name>
                </predefinedPatch>
            </patches>
        </configuration>
    </execution>

User Defined Patches
---
As pre-defined patches might not cover all cases you can also use user defined ones. The format is unified diff, so you can use diff to create the patch, e.g:

    $ diff -ruN app1 app2 patches/user-defined.patch
    diff -ruN app1/Provisioning.xml app2/Provisioning.xml
    --- app1/Provisioning.xml	2013-02-03 14:54:29.000000000 +0000
    +++ app2/Provisioning.xml	2013-02-03 14:33:34.000000000 +0000
    @@ -22,6 +22,5 @@
         </Target>
         <Target name="cleanup">
             <Exec>vboxmanage storageattach %NAME% --storagectl "Floppy Controller" --port 0 --device 0 --medium none</Exec>
    -        <PortForward hostport="10022" guestport="22"/>
         </Target>
     </Provisioning>

And XML to configure it.

    <patches>
        <userDefinedPatch>
            <file>src/vbox/patches/user-defined.patch</file>
            <!-- default level is 1 -->
            <level>1</level>
        </userDefinedPatch>
    </patches>

Note that patches are level 1 by default.

Custom Patches
---
You can create a custom patch if you want. This is an advanced topic. Simple implement com.alexecollins.vbox.core.patch.Patch and add that and the appropriate information to your POM. E.g.

    package com.alexecollins.vbox.maven.patch;

    import com.alexecollins.vbox.core.VBox;
    import com.alexecollins.vbox.core.patch.Patch;

    public class NoopPatch implements Patch {
        public void apply(VBox box) throws Exception {
            // nop
        }

        public String getName() {
            return "NoopPatch";
        }
    }

And add the standard Maven implementation detail:

    <patches>
        <noopPatch implementation="com.alexecollins.vbox.patch.demo.NoopPatch"/>
    </patches>