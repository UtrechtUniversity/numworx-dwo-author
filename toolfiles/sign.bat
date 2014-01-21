%JAVA_HOME%\bin\pack200 --repack ../output/jar/stroomdiagrammen.jar
%JAVA_HOME%\bin\jarsigner -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/stroomdiagrammen.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/stroomdiagrammen.jar.pack.gz ../output/jar/stroomdiagrammen.jar