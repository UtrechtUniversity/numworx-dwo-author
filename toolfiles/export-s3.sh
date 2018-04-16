#!/bin/sh
PATH=$PATH:/usr/local/bin
cd ../target

D=s3://test-dwo-nl/jars/
aws --profile prod s3 cp  --acl public-read statistiek-jar-with-dependencies.jar $D/statistiek.jar 
aws --profile prod s3 cp  --acl public-read statistiek-jar-with-dependencies.jar.pack.gz $D/statistiek.jar.pack.gz 
