%JAVA_HOME%\bin\pack200 --repack ../output/jar/nabouwenaanzichten.jar
%JAVA_HOME%\bin\jarsigner -sigfile UU -tsa https://timestamp.geotrust.com/tsa -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/nabouwenaanzichten.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/nabouwenaanzichten.jar.pack.gz ../output/jar/nabouwenaanzichten.jar