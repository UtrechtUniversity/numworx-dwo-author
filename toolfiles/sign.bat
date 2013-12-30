%JAVA_HOME%\bin\pack200 --repack ../output/jar/calculatordwo.jar
%JAVA_HOME%\bin\jarsigner -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/calculatordwo.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/calculatordwo.jar.pack.gz ../output/jar/calculatordwo.jar