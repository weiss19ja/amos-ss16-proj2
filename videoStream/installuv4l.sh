#!/bin/bash

if [ "$EUID" -ne 0 ] 
then
   echo "This script must be run as root" 1>&2
   exit 1
fi

while true; do
    read -p "After installation of the required packages a reboot will be executed. Do you want to continue? [y/n] " yn
    case $yn in
        [Yy]* ) break;;
        [Nn]* ) exit;;
        * ) echo "Please answer yes or no.";;
    esac
done

# Fetch and add key for uv4l repository
curl http://www.linux-projects.org/listing/uv4l_repo/lrkey.asc | sudo apt-key add -

# Add repository to apt-get
sudo echo 'deb http://www.linux-projects.org/listing/uv4l_repo/raspbian/ wheezy main' >> /etc/apt/sources.list


# Install required software
sudo apt-get update
sudo apt-get install uv4l uv4l-raspicam

sudo apt-get install uv4l-raspicam-extras

sudo apt-get -y install uv4l-webrtc

# Copy config file to expected directory
sudo cp /home/pi/uv4l-raspicam.conf /etc/uv4l/

# Reboot for applying the changes
sudo reboot
