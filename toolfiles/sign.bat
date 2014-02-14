%JAVA_HOME%\bin\pack200 --repack ../output/jar/draaibank.jar
%JAVA_HOME%\bin\jarsigner -sigfile UU -tsa https://timestamp.geotrust.com/tsa -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/draaibank.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/draaibank.jar.pack.gz ../output/jar/draaibank.jar