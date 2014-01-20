%JAVA_HOME%\bin\pack200 --repack ../output/jar/spot_problems_dwo.jar
%JAVA_HOME%\bin\jarsigner -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/spot_problems_dwo.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/spot_problems_dwo.jar.pack.gz ../output/jar/spot_problems_dwo.jar