"%JAVA_HOME%\bin\pack200" --repack ../output/jar/wiskopdr.jar
"%JAVA_HOME%\bin\jarsigner" -sigfile UU -tsa http://sha256timestamp.ws.symantec.com/sha256/timestamp -keystore ../../../tools/uu.keystore -storepass passww -keypass passww ../output/jar/wiskopdr.jar pboon
"%JAVA_HOME%\bin\pack200" ../output/jar/wiskopdr.jar.pack.gz ../output/jar/wiskopdr.jar
"%JAVA_HOME%\bin\pack200" --repack ../output/jar/graphtool.jar
"%JAVA_HOME%\bin\jarsigner" -sigfile UU -tsa http://sha256timestamp.ws.symantec.com/sha256/timestamp -keystore ../../../tools/uu.keystore -storepass passww -keypass passww ../output/jar/graphtool.jar pboon
"%JAVA_HOME%\bin\pack200" ../output/jar/graphtool.jar.pack.gz ../output/jar/graphtool.jar
"%JAVA_HOME%\bin\pack200" --repack ../output/jar/balansfruitapplet.jar
"%JAVA_HOME%\bin\jarsigner" -sigfile UU -tsa http://sha256timestamp.ws.symantec.com/sha256/timestamp -keystore ../../../tools/uu.keystore -storepass passww -keypass passww ../output/jar/balansfruitapplet.jar pboon
"%JAVA_HOME%\bin\pack200" ../output/jar/balansfruitapplet.jar.pack.gz ../output/jar/balansfruitapplet.jar
"%JAVA_HOME%\bin\pack200" --repack ../output/jar/geodefiner.jar
"%JAVA_HOME%\bin\jarsigner" -sigfile UU -tsa http://sha256timestamp.ws.symantec.com/sha256/timestamp -keystore ../../../tools/uu.keystore -storepass passww -keypass passww ../output/jar/geodefiner.jar pboon
"%JAVA_HOME%\bin\pack200" ../output/jar/geodefiner.jar.pack.gz ../output/jar/geodefiner.jar