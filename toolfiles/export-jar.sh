#!/bin/sh
R=/Volumes/fisme-sites

if ! test -d $R/www-dev
then
	echo "MOUNT FISME-SITES"
	exit 1;
else
	echo "fisme-sites mounted"
fi
APPLET=kladje
cp ../target/$APPLET.jar $R/www-dev/dwo/jars/
cp ../target/$APPLET.jar.pack.gz $R/www-dev/dwo/jars/
cp ../target/$APPLET.jar $R/www-dev/javaclasses/jars/
cp ../target/$APPLET.jar.pack.gz $R/www-dev/javaclasses/jars/
