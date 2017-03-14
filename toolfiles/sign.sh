#!/bin/sh
PATH=$JAVA_HOME/bin:$PATH
pack200 --repack ../output/jar/wiskopdr.jar
jarsigner -sigfile UU -tsa https://timestamp.geotrust.com/tsa -keystore ../../../tools/pb.keystore -storepass passww -keypass passww ../output/jar/wiskopdr.jar pboon
pack200 ../output/jar/wiskopdr.jar.pack.gz ../output/jar/wiskopdr.jar
pack200 --repack ../output/jar/graphtool.jar
jarsigner -sigfile UU -tsa https://timestamp.geotrust.com/tsa -keystore ../../../tools/pb.keystore -storepass passww -keypass passww ../output/jar/graphtool.jar pboon
pack200 ../output/jar/graphtool.jar.pack.gz ../output/jar/graphtool.jar
pack200 --repack ../output/jar/balansfruitapplet.jar
jarsigner -sigfile UU -tsa https://timestamp.geotrust.com/tsa -keystore ../../../tools/pb.keystore -storepass passww -keypass passww ../output/jar/balansfruitapplet.jar pboon
pack200 ../output/jar/balansfruitapplet.jar.pack.gz ../output/jar/balansfruitapplet.jar
pack200 --repack ../output/jar/geodefiner.jar
jarsigner -sigfile UU -tsa https://timestamp.geotrust.com/tsa -keystore ../../../tools/pb.keystore -storepass passww -keypass passww ../output/jar/geodefiner.jar pboon
pack200 ../output/jar/geodefiner.jar.pack.gz ../output/jar/geodefiner.jar