%JAVA_HOME%\bin\pack200 --repack ../output/jar/binomverdeling.jar
%JAVA_HOME%\bin\jarsigner -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/binomverdeling.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/binomverdeling.jar.pack.gz ../output/jar/binomverdeling.jar