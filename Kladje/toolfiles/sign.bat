%JAVA_HOME%\bin\pack200 --repack ../output/jar/kladje.jar
%JAVA_HOME%\bin\jarsigner -sigfile UU -tsa https://timestamp.geotrust.com/tsa -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/kladje.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/kladje.jar.pack.gz ../output/jar/kladje.jar