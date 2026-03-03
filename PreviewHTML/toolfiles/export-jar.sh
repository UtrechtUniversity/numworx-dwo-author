#!/bin/sh
R=/Volumes/fisme-sites

if ! test -d $R/www
then
	echo "MOUNT FISME-SITES"
	exit 1;
else
	echo "fisme-sites mounted"
fi
APPLET=previewhtml
cp ../target/$APPLET.jar $R/www/dwo/jars/
