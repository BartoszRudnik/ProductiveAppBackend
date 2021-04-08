#!/bin/bash
set -e
command_exists() {
	command -v "$@" > /dev/null 2>&1
}

echo "WARNING! We do not provide any warranty for script to work correctly. Use at your own risk."
read -p "Are you wish to continue [Y/n]? " answer
if [ "$answer" != "Y" ]; then
    echo "Exiting.."; [[ "$0" = "$BASH_SOURCE" ]] && exit 1 || return 1
fi
if [ "$(uname -m)" != "x86_64" ]; then
	echo "Using this script on architectures other than amd64 is not supported"
	echo "Exiting.."; [[ "$0" = "$BASH_SOURCE" ]] && exit 1 || return 1
fi
lsb_dist=""
if [ -r /etc/os-release ]; then
	lsb_dist="$(. /etc/os-release && echo "$ID")"
fi
echo ""
case "$lsb_dist" in
		ubuntu)
			if command_exists lsb_release; then
				dist_version="$(lsb_release --codename | cut -f2)"
			fi
			if [ -z "$dist_version" ] && [ -r /etc/lsb-release ]; then
				dist_version="$(. /etc/lsb-release && echo "$DISTRIB_CODENAME")"
			fi
			declare -A ubuntu_versions=([xenial]=1 [bionic]=1 [focal]=1 [groovy]=1)
			if [[ ! "${ubuntu_versions[$dist_version]}" ]]; then
				echo "Unsupported Ubuntu version"
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
				echo "Unsupported Debian version"
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
				echo "Unsupported Fedora version"
				echo "Exiting.."; [[ "$0" = "$BASH_SOURCE" ]] && exit 1 || return 1
			fi
			echo "Detected OS: Fedora"
		;;
		centos)
			if [ -z "$dist_version" ] && [ -r /etc/os-release ]; then
				dist_version="$(. /etc/os-release && echo "$VERSION_ID")"
			fi
			if [ "$dist_version" != "7" -a "$dist_version" != "8" ]; then
				echo "Unsupported CentOS version"
				echo "Exiting.."; [[ "$0" = "$BASH_SOURCE" ]] && exit 1 || return 1
			fi
			echo "Detected OS: CentOS"
		;;
		*)
			echo "Unrecognized/unsupported distribution"
			echo "Exiting.."
			[[ "$0" = "$BASH_SOURCE" ]] && exit 1 || return 1
		;;
esac

cd "sh/$lsb_dist"
echo "Installing Docker Engine.."
. setup-docker.sh
cd ".."
echo "Installing Docker Compose.."
. setup-docker-compose.sh
