#! /bin/sh
set -ue

apt-get -y install linux-headers-$(uname -r) build-essentials
if [ ! -e /media/cdrom/VBoxLinuxAdditions-x86.run ] ; then
    mount /dev/cdrom /media/cdrom
fi
/media/cdrom/VBoxLinuxAdditions-x86.run
poweroff now
