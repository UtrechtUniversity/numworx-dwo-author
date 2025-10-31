#!/bin/sh
PATH=$PATH:/usr/local/bin
cd ../target
S="wiskopdrbeans.jar"
D=s3://cds.dwo.nl/jars/
for i in $S; do aws --profile prod s3 cp  --acl public-read $i $D; done
