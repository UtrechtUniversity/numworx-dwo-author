#!/bin/sh
PATH=$PATH:/usr/local/bin
cd ../output/jar
S="wiskopdr.jar wiskopdr.jar.pack.gz graphtool.jar graphtool.jar.pack.gz balansfruitapplet.jar balansfruitapplet.jar.pack.gz geodefiner.jar geodefiner.jar.pack.gz"
D=s3://test-dwo-nl/jars/
s3cmd -P -M --no-mime-magic put $S $D
