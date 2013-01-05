#! /bin/sh
set -ue

apt-get -y install acpid linux-headers-$(uname -r) build-essential
mount /dev/cdrom /media/cdrom
/media/cdrom/VBoxLinuxAdditions.run
eject /dev/cdrom
poweroff now
