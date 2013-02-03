Ant
===
Quick Start
---
Add this to your build.xml:

    <project name="vbox-ant-tasks" default="build" xmlns:vbox="antlib:com.alexecollins.vbox.ant">
        <target name="build">
            <vbox:list-definitions/>
            <vbox:create-definition name="CentOS_6_3" dir="src/vbox/app1"/>
            <vbox:clean dir="src/vbox/app1" work="build"/>
            <vbox:create dir="src/vbox/app1" work="build" cacheDir="${user.home}/.vbox"/>
            <vbox:provision dir="src/vbox/app1" work="build"/>
            <vbox:start dir="src/vbox/app1"/>
            <!-- ... -->
            <vbox:stop dir="src/vbox/app1"/>
        </target>
    </project>

Add the vbox-ant-tasks-*.jar to Ant's class path.

Ant tasks do not currently allow you to do multiple VMs in a single command. You'll need to use multiple ones.

