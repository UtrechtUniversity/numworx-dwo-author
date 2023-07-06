#!/bin/sh
if ! test -d /Volumes/fisme-sites/www-dev
then
	echo "MOUNT FISME-SITES"
	exit 1;
else
	echo "fisme-sites mounted"
fi
set -xe
jh=$(/usr/libexec/java_home -v 1.8)
export JAVA_HOME=$jh
(cd ..; mvn install)
sh -xe export-jar.sh
