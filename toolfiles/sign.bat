%JAVA_HOME%\bin\pack200 --repack ../output/jar/mozarch.jar
%JAVA_HOME%\bin\jarsigner -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/mozarch.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/mozarch.jar.pack.gz ../output/jar/mozarch.jar