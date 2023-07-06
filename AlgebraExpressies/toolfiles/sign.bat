%JAVA_HOME%\bin\pack200 --repack ../output/jar/algebraexpressies.jar
%JAVA_HOME%\bin\jarsigner -sigfile UU -tsa https://timestamp.geotrust.com/tsa -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/algebraexpressies.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/algebraexpressies.jar.pack.gz ../output/jar/algebraexpressies.jar
