%JAVA_HOME%\bin\pack200 --repack ../output/jar/normaleverdeling.jar
%JAVA_HOME%\bin\jarsigner -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/normaleverdeling.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/normaleverdeling.jar.pack.gz ../output/jar/normaleverdeling.jar