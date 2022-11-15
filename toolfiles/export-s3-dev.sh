#!/bin/sh
PATH=$PATH:/usr/local/bin
cd ../target
D=s3://ebs-dev-dwo-nl/jars
aws s3 cp  --acl public-read statistiek-jar-with-dependencies.jar $D/statistiek.jar 
aws s3 cp  --acl public-read statistiek-jar-with-dependencies.jar.pack.gz $D/statistiek.jar.pack.gz 
