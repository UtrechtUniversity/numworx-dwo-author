#!/bin/sh
R=/Volumes/fisme-sites
R=$USER@gemini.science.uu.nl:/science/wwwprojects/FI-Sites

APPLET=javalogoweb
scp ../target/$APPLET.jar $R/www/dwo/jars/
scp ../target/$APPLET.jar.pack.gz $R/www/dwo/jars/
