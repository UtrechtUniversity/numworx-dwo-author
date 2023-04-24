%JAVA_HOME%\bin\pack200 --repack ../output/jar/doorziendwo.jar
%JAVA_HOME%\bin\jarsigner -sigfile UU -tsa https://timestamp.geotrust.com/tsa -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/doorziendwo.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/doorziendwo.jar.pack.gz ../output/jar/doorziendwo.jar