$JAVA_HOME/bin/pack200 --repack ../output/jar/wiskopdr.jar
$JAVA_HOME/bin/jarsigner -sigfile UU -tsa https://timestamp.geotrust.com/tsa -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/wiskopdr.jar pboon
$JAVA_HOME/bin/pack200 ../output/jar/wiskopdr.jar.pack.gz ../output/jar/wiskopdr.jar
$JAVA_HOME/bin/pack200 --repack ../output/jar/graphtool.jar
$JAVA_HOME/bin/jarsigner -sigfile UU -tsa https://timestamp.geotrust.com/tsa -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/graphtool.jar pboon
$JAVA_HOME/bin/pack200 ../output/jar/graphtool.jar.pack.gz ../output/jar/graphtool.jar
$JAVA_HOME/bin/pack200 --repack ../output/jar/balansfruitapplet.jar
$JAVA_HOME/bin/jarsigner -sigfile UU -tsa https://timestamp.geotrust.com/tsa -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/balansfruitapplet.jar pboon
$JAVA_HOME/bin/pack200 ../output/jar/balansfruitapplet.jar.pack.gz ../output/jar/balansfruitapplet.jar
$JAVA_HOME/bin/pack200 --repack ../output/jar/geodefiner.jar
$JAVA_HOME/bin/jarsigner -sigfile UU -tsa https://timestamp.geotrust.com/tsa -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/geodefiner.jar pboon
$JAVA_HOME/bin/pack200 ../output/jar/geodefiner.jar.pack.gz ../output/jar/geodefiner.jar