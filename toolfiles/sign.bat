%JAVA_HOME%\bin\pack200 --repack ../output/jar/draaibank.jar
%JAVA_HOME%\bin\jarsigner -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/draaibank.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/draaibank.jar.pack.gz ../output/jar/draaibank.jar