#!/bin/sh
PATH=$PATH:/usr/local/bin
cd ../target
S="kladje.jar kladje.jar.pack.gz"
D=s3://ebs-dev-dwo-nl/jars/
for i in $S; do aws s3 cp --acl public-read $i $D; done
