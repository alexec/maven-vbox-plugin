#! /bin/sh
set -ue

apt-get -y install linux-headers-$(uname -r) build-essential
if [ ! -e /media/cdrom/VBoxLinuxAdditions-x86.run ] ; then
    mount /dev/cdrom /media/cdrom
fi
/media/cdrom/VBoxLinuxAdditions.run
eject /dev/cdrom
poweroff now
