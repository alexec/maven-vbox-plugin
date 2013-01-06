#! /bin/sh
set -ue

yum -y install acpid kernel-headers kernel-devel gcc gcc-c++ make kernel-devel-$(uname -r)
mkdir /media/cdrom
mount /dev/sr0 /media/cdrom
/media/cdrom/VBoxLinuxAdditions.run
umount /dev/sr0
rm -R /media/cdrom
poweroff now
