#!/bin/sh
PATH=$PATH:/usr/local/bin
cd ../target
S="wiskopdr.jar wiskopdr.jar.pack.gz graphtool.jar graphtool.jar.pack.gz balansfruitapplet.jar balansfruitapplet.jar.pack.gz geodefiner.jar geodefiner.jar.pack.gz statistiek.jar statistiek.jar.pack.gz mathscratch.jar mathscratch.jar.pack.gz geogebra4widget.jar geogebra4widget.jar.pack.gz geogebra3widget.jar geogebra3widget.jar.pack.gz"
for i in $S; do 
azcopy sync $i https://numworxacc.blob.core.windows.net/test/jars/$i?"$SAS"
done
