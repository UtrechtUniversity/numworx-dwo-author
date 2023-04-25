%JAVA_HOME%\bin\pack200 --repack ../output/jar/grafiek3dtest.jar
%JAVA_HOME%\bin\jarsigner -sigfile UU -tsa https://timestamp.geotrust.com/tsa -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/grafiek3dtest.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/grafiek3dtest.jar.pack.gz ../output/jar/grafiek3dtest.jar