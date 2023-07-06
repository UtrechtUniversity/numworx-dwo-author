%JAVA_HOME%\bin\pack200 --repack ../output/jar/statsim.jar
%JAVA_HOME%\bin\jarsigner -sigfile UU -tsa https://timestamp.geotrust.com/tsa -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/statsim.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/statsim.jar.pack.gz ../output/jar/statsim.jar