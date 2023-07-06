%JAVA_HOME%\bin\pack200 --repack ../output/jar/heks.jar
%JAVA_HOME%\bin\jarsigner -sigfile UU -tsa https://timestamp.geotrust.com/tsa -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/heks.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/heks.jar.pack.gz ../output/jar/heks.jar