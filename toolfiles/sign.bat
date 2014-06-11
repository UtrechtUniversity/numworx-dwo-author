%JAVA_HOME%\bin\pack200 --repack ../output/jar/sliderwidget.jar
%JAVA_HOME%\bin\jarsigner -sigfile UU -tsa https://timestamp.geotrust.com/tsa -keystore ../../../tools/pboon.keystore -storepass passww -keypass passw ../output/jar/sliderwidget.jar pboon
%JAVA_HOME%\bin\pack200 ../output/jar/sliderwidget.jar.pack.gz ../output/jar/sliderwidget.jar
