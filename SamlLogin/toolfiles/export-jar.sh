#!/bin/sh
R=$USER@gemini.science.uu.nl:/science/wwwprojects/FI-Sites

APPLET=samllogin
scp ../target/$APPLET.jar $R/www/dwo/jars/
