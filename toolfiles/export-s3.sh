#!/bin/sh
PATH=$PATH:/usr/local/bin
cd ../target
S="wiskopdr.jar wiskopdr.jar.pack.gz graphtool.jar graphtool.jar.pack.gz balansfruitapplet.jar balansfruitapplet.jar.pack.gz geodefiner.jar geodefiner.jar.pack.gz"
D=s3://test-dwo-nl/jars/
for i in $S; do aws --profile prod s3 cp  --acl public-read $i $D; done
