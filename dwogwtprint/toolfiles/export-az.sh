#!/bin/sh
PATH=$PATH:/usr/local/bin
cd ../target
S="dwogwtprint.jar dwogwtprint.jar.pack.gz"
for i in $S; do 
azcopy sync $i https://numworxacc.blob.core.windows.net/test/jars/$i?"$SAS"
done
