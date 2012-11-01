#! /bin/sh
set -ue

ifup eth0
sed -i 's/ONBOOT="no"/ONBOOT="yes"/' /etc/sysconfig/network-scripts/ifcfg-eth0

yum -y install kernel-headers kernel-devel gcc gcc-c++ make kernel-devel-$(uname -r)
mkdir /media/cdrom
mount /dev/sr0 /media/cdrom
/media/cdrom/VBoxLinuxAdditions.run
umount /dev/sr0
rm -R /media/cdrom
poweroff now
