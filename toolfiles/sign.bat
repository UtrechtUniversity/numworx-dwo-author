%JAVA_HOME%\bin\pack200 --repack ../output/jar/kladje.jar
%JAVA_HOME%\bin\jarsigner -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/kladje.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/kladje.jar.pack.gz ../output/jar/kladje.jar