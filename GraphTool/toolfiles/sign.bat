%JAVA_HOME%\bin\pack200 --repack ../output/jar/graphtool.jar
%JAVA_HOME%\bin\jarsigner -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/graphtool.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/graphtool.jar.pack.gz ../output/jar/graphtool.jar