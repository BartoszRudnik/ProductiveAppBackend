#!/bin/bash
set -e
command_exists() {
	command -v "$@" > /dev/null 2>&1
}

echo "Productivity app REST Server prerequisites installer"
echo "WARNING! We do not provide any warranty for script to work correctly. Use at your own risk."
read -p "Are you wish to continue [Y/n]? " answer
if [ "$answer" != "Y" ]; then
    echo "Exiting.."; [[ "$0" = "$BASH_SOURCE" ]] && exit 1 || return 1
fi
if [ "$(uname -m)" != "x86_64" ]; then
	echo "Using this script on architectures other than amd64 is not supported"
	echo "Exiting.."; [[ "$0" = "$BASH_SOURCE" ]] && exit 1 || return 1
fi
dist_name=""
if [ -r /etc/os-release ]; then
	dist_name="$(. /etc/os-release && echo "$ID")"
fi
echo ""
case "$dist_name" in
		ubuntu)
			if command_exists lsb_release; then
				dist_version="$(lsb_release --codename | cut -f2)"
			fi
			if [ -z "$dist_version" ] && [ -r /etc/lsb-release ]; then
				dist_version="$(. /etc/lsb-release && echo "$DISTRIB_CODENAME")"
			fi
			declare -A ubuntu_versions=([xenial]=1 [bionic]=1 [focal]=1 [groovy]=1)
			if [[ ! "${ubuntu_versions[$dist_version]}" ]]; then
				echo "Found unsupported Ubuntu version!"
				echo "Install prequesites manually or use one of following Ubuntu versions:"
				echo "- 20.10 Groovy\n- 20.04 Focal\n- 18.04 Bionic\n- 16.04 Xenial"
				echo "Exiting.."; [[ "$0" = "$BASH_SOURCE" ]] && exit 1 || return 1
			fi
			echo "Detected OS: Ubuntu"
		;;
		debian)
			dist_version="$(sed 's/\/.*//' /etc/debian_version | sed 's/\..*//')"
			if [ -z "$dist_version" ] && [ -r /etc/os-release ]; then
				dist_version="$(. /etc/os-release && echo "$VERSION_ID")"
			fi
			if [ "$dist_version" != "10" -a "$dist_version" != "9" ]; then
				echo "Found unsupported Debian version!"
				echo "Install prequesites manually or use one of following Debian versions:"
				echo "- 10 Buster\n- 9 Stretch"
				echo "Exiting.."; [[ "$0" = "$BASH_SOURCE" ]] && exit 1 || return 1
			fi
			echo "Detected OS: Debian"
		;;
		fedora)
			if command_exists lsb_release; then
				dist_version="$(lsb_release --release | cut -f2)"
			fi
			if [ -z "$dist_version" ] && [ -r /etc/os-release ]; then
				dist_version="$(. /etc/os-release && echo "$VERSION_ID")"
			fi
			if [ "$dist_version" != "33" -a "$dist_version" != "32" ]; then
				echo "Found unsupported Fedora version!"
				echo "Install prequesites manually or use one of following Fedora versions:"
				echo "- 33\n- 32"
				echo "Exiting.."; [[ "$0" = "$BASH_SOURCE" ]] && exit 1 || return 1
			fi
			echo "Detected OS: Fedora"
		;;
		centos)
			if [ -r /etc/os-release ]; then
				dist_version="$(. /etc/os-release && echo "$VERSION_ID")"
			fi
			if [ "$dist_version" != "8" -a "$dist_version" != "7" ]; then
				echo "Found unsupported CentOS version!"
				echo "Install prequesites manually or use one of following CentOS versions:"
				echo "- 8\n- 7"
				echo "Exiting.."; [[ "$0" = "$BASH_SOURCE" ]] && exit 1 || return 1
			fi
			echo "Detected OS: CentOS"
		;;
		*)
			echo "Unrecognized/unsupported distribution"
			echo "Exiting.."; [[ "$0" = "$BASH_SOURCE" ]] && exit 1 || return 1
		;;
esac

cd "sh/$dist_name"
echo "Installing Docker Engine.."
. setup-docker.sh
cd ".."
echo "Installing Docker Compose.."
. setup-docker-compose.sh
