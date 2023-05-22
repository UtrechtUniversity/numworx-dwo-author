#!/bin/sh
PATH=$PATH:/usr/local/bin
cd ../target
S="wiskopdr.jar wiskopdr.jar.pack.gz graphtool.jar graphtool.jar.pack.gz balansfruitapplet.jar balansfruitapplet.jar.pack.gz geodefiner.jar geodefiner.jar.pack.gz mathscratch.jar mathscratch.jar.pack.gz"
PW=/usr/local/etc/azssh.txt
for i in $S  
do
	sshpass -f $PW sftp numworxcontentdev.content.content@numworxcontentdev.blob.core.windows.net:jars <<EOF
put $i
EOF

done
