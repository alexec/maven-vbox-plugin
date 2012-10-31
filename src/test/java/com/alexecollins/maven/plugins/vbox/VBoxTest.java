package com.alexecollins.maven.plugins.vbox;

import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * @author alex.e.c@gmail.com
 */
public class VBoxTest {

	@Test
	public void test() throws Exception {
		final Properties p = VBox.getPropertiesFromString("name=\"Ubuntu\"\n" +
				"ostype=\"Ubuntu\"\n" +
				"UUID=\"cc094854-cb59-4316-8de3-bcb4e86eb69c\"\n" +
				"CfgFile=\"C:\\Users\\alex.collins\\VirtualBox VMs\\Ubuntu\\Ubuntu\n" +
				"SnapFldr=\"C:\\Users\\alex.collins\\VirtualBox VMs\\Ubuntu\\Snaps\n" +
				"LogFldr=\"C:\\Users\\alex.collins\\VirtualBox VMs\\Ubuntu\\Logs\"\n" +
				"hardwareuuid=\"cc094854-cb59-4316-8de3-bcb4e86eb69c\"\n" +
				"memory=512\n" +
				"pagefusion=\"off\"\n" +
				"vram=12\n" +
				"cpuexecutioncap=100\n" +
				"hpet=\"off\"\n" +
				"chipset=\"piix3\"\n" +
				"firmware=\"BIOS\"\n" +
				"cpus=2\n" +
				"synthcpu=\"off\"\n" +
				"bootmenu=\"messageandmenu\"\n" +
				"boot1=\"floppy\"\n" +
				"boot2=\"dvd\"\n" +
				"boot3=\"disk\"\n" +
				"boot4=\"none\"\n" +
				"acpi=\"on\"\n" +
				"ioapic=\"on\"\n" +
				"pae=\"on\"\n" +
				"biossystemtimeoffset=0\n" +
				"rtcuseutc=\"on\"\n" +
				"hwvirtex=\"on\"\n" +
				"hwvirtexexcl=\"off\"\n" +
				"nestedpaging=\"on\"\n" +
				"largepages=\"on\"\n" +
				"vtxvpid=\"on\"\n" +
				"VMState=\"poweroff\"\n" +
				"VMStateChangeTime=\"2012-10-29T18:51:56.810000000\"\n" +
				"monitorcount=1\n" +
				"accelerate3d=\"off\"\n" +
				"accelerate2dvideo=\"off\"\n" +
				"teleporterenabled=\"off\"\n" +
				"teleporterport=0\n" +
				"teleporteraddress=\"\"\n" +
				"teleporterpassword=\"\"\n" +
				"storagecontrollername0=\"IDE Controller\"\n" +
				"storagecontrollertype0=\"PIIX4\"\n" +
				"storagecontrollerinstance0=\"0\"\n" +
				"storagecontrollermaxportcount0=\"2\"\n" +
				"storagecontrollerportcount0=\"2\"\n" +
				"storagecontrollerbootable0=\"on\"\n" +
				"storagecontrollername1=\"SATA Controller\"\n" +
				"storagecontrollertype1=\"IntelAhci\"\n" +
				"storagecontrollerinstance1=\"0\"\n" +
				"storagecontrollermaxportcount1=\"30\"\n" +
				"storagecontrollerportcount1=\"1\"\n" +
				"storagecontrollerbootable1=\"on\"\n" +
				"\"IDE Controller-0-0\"=\"none\"\n" +
				"\"IDE Controller-0-1\"=\"none\"\n" +
				"\"IDE Controller-1-0\"=\"C:\\Users\\alex.collins\\Desktop\\install\n" +
				"\"IDE Controller-ImageUUID-1-0\"=\"5cdc0de2-7822-4316-822e-754\n" +
				"\"IDE Controller-tempeject\"=\"off\"\n" +
				"\"IDE Controller-IsEjected\"=\"off\"\n" +
				"\"IDE Controller-1-1\"=\"none\"\n" +
				"\"SATA Controller-0-0\"=\"C:\\Users\\alex.collins\\VirtualBox VMs\n" +
				"\"SATA Controller-ImageUUID-0-0\"=\"69043504-6578-44e0-b1d1-45\n" +
				"natnet1=\"nat\"\n" +
				"macaddress1=\"08002718FDF2\"\n" +
				"cableconnected1=\"on\"\n" +
				"nic1=\"nat\"\n" +
				"mtu=\"0\"\n" +
				"sockSnd=\"64\"\n" +
				"sockRcv=\"64\"\n" +
				"tcpWndSnd=\"64\"\n" +
				"tcpWndRcv=\"64\"\n" +
				"nic2=\"none\"\n" +
				"nic3=\"none\"\n" +
				"nic4=\"none\"\n" +
				"nic5=\"none\"\n" +
				"nic6=\"none\"\n" +
				"nic7=\"none\"\n" +
				"nic8=\"none\"\n" +
				"hidpointing=\"usbtablet\"\n" +
				"hidkeyboard=\"ps2kbd\"\n" +
				"uart1=\"off\"\n" +
				"uart2=\"off\"\n" +
				"audio=\"dsound\"\n" +
				"clipboard=\"bidirectional\"\n" +
				"vrde=\"off\"\n" +
				"usb=\"on\"\n" +
				"VRDEActiveConnection=\"off\"\n" +
				"VRDEClients=0\n" +
				"GuestMemoryBalloon=0\n" +
				"GuestOSType=\"Ubuntu\"\n" +
				"GuestAdditionsRunLevel=0\n" +
				"GuestAdditionsVersion=\"4.1.12\"");

		assertEquals("poweroff", p.getProperty("VMState"));
	}
}
