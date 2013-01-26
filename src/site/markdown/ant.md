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

