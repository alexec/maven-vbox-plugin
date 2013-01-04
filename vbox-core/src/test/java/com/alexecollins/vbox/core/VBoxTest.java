package com.alexecollins.vbox.core;

import org.junit.Test;

import java.util.Collections;
import java.util.Properties;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * @author alex.e.c@gmail.com
 */
public class VBoxTest {

	@Test
	public void testVersion() throws Exception {
		final VBox.Version v = new VBox.Version("4.1.22r80657");
		assertThat(v.major, is(equalTo(4)));
		assertThat(v.minor, is(equalTo(1)));
		assertThat(v.build, is(equalTo(22)));
		assertThat(v.revision, is(equalTo(80657)));
	}

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

	@Test
	public void testParseVms() throws Exception {
		assertEquals(Collections.singleton("oeuaoeuaoe"), VBox.parseVms("\"oeuaoeuaoe\" {78c15dce-cac4-4b61-96b1-4f85e1cbadab}"));
	}

	@Test
	public void testParseOSTypes() throws Exception {
		String s ="ID:          Other\n" +
				"Description: Other/Unknown\n" +
				"\n" +
				"ID:          Windows31\n" +
				"Description: Windows 3.1\n" +
				"\n" +
				"ID:          Windows95\n" +
				"Description: Windows 95\n" +
				"\n" +
				"ID:          Windows98\n" +
				"Description: Windows 98\n" +
				"\n" +
				"ID:          WindowsMe\n" +
				"Description: Windows Me\n" +
				"\n" +
				"ID:          WindowsNT4\n" +
				"Description: Windows NT 4\n" +
				"\n" +
				"ID:          Windows2000\n" +
				"Description: Windows 2000\n" +
				"\n" +
				"ID:          WindowsXP\n" +
				"Description: Windows XP\n" +
				"\n" +
				"ID:          WindowsXP_64\n" +
				"Description: Windows XP (64 bit)\n" +
				"\n" +
				"ID:          Windows2003\n" +
				"Description: Windows 2003\n" +
				"\n" +
				"ID:          Windows2003_64\n" +
				"Description: Windows 2003 (64 bit)\n" +
				"\n" +
				"ID:          WindowsVista\n" +
				"Description: Windows Vista\n" +
				"\n" +
				"ID:          WindowsVista_64\n" +
				"Description: Windows Vista (64 bit)\n" +
				"\n" +
				"ID:          Windows2008\n" +
				"Description: Windows 2008\n" +
				"\n" +
				"ID:          Windows2008_64\n" +
				"Description: Windows 2008 (64 bit)\n" +
				"\n" +
				"ID:          Windows7\n" +
				"Description: Windows 7\n" +
				"\n" +
				"ID:          Windows7_64\n" +
				"Description: Windows 7 (64 bit)\n" +
				"\n" +
				"ID:          Windows8\n" +
				"Description: Windows 8\n" +
				"\n" +
				"ID:          Windows8_64\n" +
				"Description: Windows 8 (64 bit)\n" +
				"\n" +
				"ID:          WindowsNT\n" +
				"Description: Other Windows\n" +
				"\n" +
				"ID:          Linux22\n" +
				"Description: Linux 2.2\n" +
				"\n" +
				"ID:          Linux24\n" +
				"Description: Linux 2.4\n" +
				"\n" +
				"ID:          Linux24_64\n" +
				"Description: Linux 2.4 (64 bit)\n" +
				"\n" +
				"ID:          Linux26\n" +
				"Description: Linux 2.6\n" +
				"\n" +
				"ID:          Linux26_64\n" +
				"Description: Linux 2.6 (64 bit)\n" +
				"\n" +
				"ID:          ArchLinux\n" +
				"Description: Arch Linux\n" +
				"\n" +
				"ID:          ArchLinux_64\n" +
				"Description: Arch Linux (64 bit)\n" +
				"\n" +
				"ID:          Debian\n" +
				"Description: Debian\n" +
				"\n" +
				"ID:          Debian_64\n" +
				"Description: Debian (64 bit)\n" +
				"\n" +
				"ID:          OpenSUSE\n" +
				"Description: openSUSE\n" +
				"\n" +
				"ID:          OpenSUSE_64\n" +
				"Description: openSUSE (64 bit)\n" +
				"\n" +
				"ID:          Fedora\n" +
				"Description: Fedora\n" +
				"\n" +
				"ID:          Fedora_64\n" +
				"Description: Fedora (64 bit)\n" +
				"\n" +
				"ID:          Gentoo\n" +
				"Description: Gentoo\n" +
				"\n" +
				"ID:          Gentoo_64\n" +
				"Description: Gentoo (64 bit)\n" +
				"\n" +
				"ID:          Mandriva\n" +
				"Description: Mandriva\n" +
				"\n" +
				"ID:          Mandriva_64\n" +
				"Description: Mandriva (64 bit)\n" +
				"\n" +
				"ID:          RedHat\n" +
				"Description: Red Hat\n" +
				"\n" +
				"ID:          RedHat_64\n" +
				"Description: Red Hat (64 bit)\n" +
				"\n" +
				"ID:          Turbolinux\n" +
				"Description: Turbolinux\n" +
				"\n" +
				"ID:          Turbolinux\n" +
				"Description: Turbolinux (64 bit)\n" +
				"\n" +
				"ID:          Ubuntu\n" +
				"Description: Ubuntu\n" +
				"\n" +
				"ID:          Ubuntu_64\n" +
				"Description: Ubuntu (64 bit)\n" +
				"\n" +
				"ID:          Xandros\n" +
				"Description: Xandros\n" +
				"\n" +
				"ID:          Xandros_64\n" +
				"Description: Xandros (64 bit)\n" +
				"\n" +
				"ID:          Oracle\n" +
				"Description: Oracle\n" +
				"\n" +
				"ID:          Oracle_64\n" +
				"Description: Oracle (64 bit)\n" +
				"\n" +
				"ID:          Linux\n" +
				"Description: Other Linux\n" +
				"\n" +
				"ID:          Solaris\n" +
				"Description: Oracle Solaris 10 5/09 and earlier\n" +
				"\n" +
				"ID:          Solaris_64\n" +
				"Description: Oracle Solaris 10 5/09 and earlier (64 bit)\n" +
				"\n" +
				"ID:          OpenSolaris\n" +
				"Description: Oracle Solaris 10 10/09 and later\n" +
				"\n" +
				"ID:          OpenSolaris_64\n" +
				"Description: Oracle Solaris 10 10/09 and later (64 bit)\n" +
				"\n" +
				"ID:          FreeBSD\n" +
				"Description: FreeBSD\n" +
				"\n" +
				"ID:          FreeBSD_64\n" +
				"Description: FreeBSD (64 bit)\n" +
				"\n" +
				"ID:          OpenBSD\n" +
				"Description: OpenBSD\n" +
				"\n" +
				"ID:          OpenBSD_64\n" +
				"Description: OpenBSD (64 bit)\n" +
				"\n" +
				"ID:          NetBSD\n" +
				"Description: NetBSD\n" +
				"\n" +
				"ID:          NetBSD_64\n" +
				"Description: NetBSD (64 bit)\n" +
				"\n" +
				"ID:          OS2Warp3\n" +
				"Description: OS/2 Warp 3\n" +
				"\n" +
				"ID:          OS2Warp4\n" +
				"Description: OS/2 Warp 4\n" +
				"\n" +
				"ID:          OS2Warp45\n" +
				"Description: OS/2 Warp 4.5\n" +
				"\n" +
				"ID:          OS2eCS\n" +
				"Description: eComStation\n" +
				"\n" +
				"ID:          OS2\n" +
				"Description: Other OS/2\n" +
				"\n" +
				"ID:          MacOS\n" +
				"Description: Mac OS X Server\n" +
				"\n" +
				"ID:          MacOS_64\n" +
				"Description: Mac OS X Server (64 bit)\n" +
				"\n" +
				"ID:          DOS\n" +
				"Description: DOS\n" +
				"\n" +
				"ID:          Netware\n" +
				"Description: Netware\n" +
				"\n" +
				"ID:          L4\n" +
				"Description: L4\n" +
				"\n" +
				"ID:          QNX\n" +
				"Description: QNX\n" +
				"\n" +
				"ID:          JRockitVE\n" +
				"Description: JRockitVE\n";


		final Set<VBox.OSType> osTypes = VBox.parseOSTypes(s);
		assertTrue(osTypes.size() > 10);
		assertTrue(osTypes.contains(new VBox.OSType("Windows2008")));
		assertFalse(osTypes.contains(new VBox.OSType("Windows 2008")));
	}

    @Test
    public void testPrettyDuration() throws Exception {
        assertEquals("1 minute(s)", VBox.prettyDuration(60000));
        assertEquals("30 second(s)", VBox.prettyDuration(30000));

    }
}
