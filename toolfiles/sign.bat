%JAVA_HOME%\bin\pack200 --repack ../output/jar/verknippen.jar
%JAVA_HOME%\bin\jarsigner -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/verknippen.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/verknippen.jar.pack.gz ../output/jar/verknippen.jar