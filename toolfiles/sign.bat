%JAVA_HOME%\bin\pack200 --repack ../output/jar/algebrapijlenopdr.jar
%JAVA_HOME%\bin\jarsigner -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/algebrapijlenopdr.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/algebrapijlenopdr.jar.pack.gz ../output/jar/algebrapijlenopdr.jar
