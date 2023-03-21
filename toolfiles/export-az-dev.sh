#!/bin/sh
PATH=$PATH:/usr/local/bin
cd ../target
PW=/usr/local/etc/azssh.txt
S="previewhtml.jar"
D='numworxcontentdev.content.content@numworxcontentdev.blob.core.windows.net:jars'
for i in $S; do 
sshpass -f $PW sftp $D <<EOF
put -r $i
EOF
done
