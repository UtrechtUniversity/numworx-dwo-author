%JAVA_HOME%\bin\pack200 --repack ../output/jar/statistiek.jar
%JAVA_HOME%\bin\jarsigner -sigfile UU -tsa https://timestamp.geotrust.com/tsa -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/statistiek.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/statistiek.jar.pack.gz ../output/jar/statistiek.jar