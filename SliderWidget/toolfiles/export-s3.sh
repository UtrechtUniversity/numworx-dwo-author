#!/bin/sh
PATH=$PATH:/usr/local/bin
cd ../target
S="sliderwidget.jar sliderwidget.jar.pack.gz"
D=s3://test-dwo-nl/jars/
for i in $S; do aws --profile prod s3 cp  --acl public-read $i $D; done
