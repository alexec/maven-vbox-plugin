Ant
===
Quick Start
---
Add this to your build.xml:

    <project name="vbox-ant-tasks" default="build" xmlns:vbox="antlib:com.alexecollins.vbox.ant">
        <target name="build">
            <property name="context" value="ant-project:1.0.0"/>
            <property name="app" location="src/vbox/app1"/>
            <vbox:purge-local-repository/>
            <vbox:list-definitions/>
            <vbox:delete-definition dir="${app}"/>
            <vbox:create-definition name="CentOS_6_5" dir="${app}"/>
            <vbox:patch-definition dir="${app}">
                <archPatch/>
                <predefinedPatch name="CentOS_6_5--tomcat6"/>
            </vbox:patch-definition>
            <vbox:clean dir="${app}" context="${context}"/>
            <vbox:create dir="${app}" context="${context}"/>
            <vbox:provision dir="${app}" context="${context}"/>
            <vbox:start dir="${app}"/>
            <!-- ... -->
            <vbox:stop dir="${app}"/>
        </target>
    </project>

Add the vbox-ant-tasks-*.jar to Ant's class path.

Ant tasks do not currently allow you to do multiple VMs in a single command. You'll need to use multiple ones.

An example can be [found here](https://github.com/alexec/maven-vbox-plugin/tree/master/vbox-examples/ant).
