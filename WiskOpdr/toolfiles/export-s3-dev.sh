#!/bin/sh
PATH=$PATH:/usr/local/bin
cd ../target
S="wiskopdr.jar wiskopdr.jar.pack.gz graphtool.jar graphtool.jar.pack.gz balansfruitapplet.jar balansfruitapplet.jar.pack.gz geodefiner.jar geodefiner.jar.pack.gz mathscratch.jar mathscratch.jar.pack.gz"
D=s3://ebs-dev-dwo-nl/jars/
for i in $S  
do
	aws s3 cp --acl public-read $i $D
done
