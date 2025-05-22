#!/bin/sh
R=/Volumes/fisme-sites
R=$USER@gemini.science.uu.nl:/science/wwwprojects/FI-Sites

APPLET=statsim
scp ../target/$APPLET.jar $R/www-dev/dwo/jars/
scp ../target/$APPLET.jar.pack.gz $R/www-dev/dwo/jars/
