%JAVA_HOME%\bin\pack200 --repack ../output/jar/tekenveelvlakopdr.jar
%JAVA_HOME%\bin\jarsigner -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/tekenveelvlakopdr.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/tekenveelvlakopdr.jar.pack.gz ../output/jar/tekenveelvlakopdr.jar