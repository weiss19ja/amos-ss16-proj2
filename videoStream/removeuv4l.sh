#!/bin/bash

if [ "$EUID" -ne 0 ]
then
   echo "This script must be run as root" 1>&2
   exit 1
fi

echo 'Removing uv4l and all related components'

sudo service uv4l_raspicam stop

sudo apt-get remove -y uv4l-webrtc uv4l-raspicam-extras uv4l-raspicam uv4l

sudo rm -rf /etc/uv4l

cd ~

sudo head -n -1 /etc/apt/sources.list >> sources.list
sudo mv sources.list /etc/apt/
