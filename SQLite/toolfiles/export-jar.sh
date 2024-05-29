#!/bin/sh
R=/Volumes/fisme-sites
R=$USER@gemini.science.uu.nl:/science/wwwprojects/FI-Sites
scp ../target/*.jar $R/www-dev/dwo/jars/
scp ../target/*.jar.pack.gz $R/www-dev/dwo/jars/
