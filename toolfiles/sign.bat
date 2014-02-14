%JAVA_HOME%\bin\pack200 --repack ../output/jar/kansbomen.jar
%JAVA_HOME%\bin\jarsigner -sigfile UU -tsa https://timestamp.geotrust.com/tsa -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/kansbomen.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/kansbomen.jar.pack.gz ../output/jar/kansbomen.jar