%JAVA_HOME%\bin\pack200 --repack ../output/jar/geomalgebra.jar
%JAVA_HOME%\bin\jarsigner -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/geomalgebra.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/geomalgebra.jar.pack.gz ../output/jar/geomalgebra.jar