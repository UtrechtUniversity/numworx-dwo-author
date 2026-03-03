#!/bin/sh
R=/Volumes/fisme-sites
R=$USER@gemini.science.uu.nl:/science/wwwprojects/FI-Sites

APPLET=geodefull
scp ../target/$APPLET.jar $R/www/dwo/jars/
scp ../target/$APPLET.jar.pack.gz $R/www/dwo/jars/
