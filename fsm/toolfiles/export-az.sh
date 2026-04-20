#!/bin/sh
PATH=$PATH:/usr/local/bin
cd ../target
S="fsm.jar fsm.jar.pack.gz"
for i in $S; do 
azcopy sync $i https://numworxacc.blob.core.windows.net/test/jars/$i?"$SAS"
done
