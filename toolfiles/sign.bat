%JAVA_HOME%\bin\pack200 --repack ../output/jar/tegels.jar
%JAVA_HOME%\bin\jarsigner -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/tegels.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/tegels.jar.pack.gz ../output/jar/tegels.jar