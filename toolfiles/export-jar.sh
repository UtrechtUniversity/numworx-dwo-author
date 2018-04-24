R=/Volumes/fisme-sites

if ! test -d $R/www-dev
then
	echo "MOUNT FISME-SITES"
	exit 1;
else
	echo "fisme-sites mounted"
fi

cp ../target/wiskopdr.jar $R/www-dev/dwo/jars/wiskopdr.jar
cp ../target/wiskopdr.jar.pack.gz $R/www-dev/dwo/jars/wiskopdr.jar.pack.gz
cp ../target/graphtool.jar $R/www-dev/dwo/jars/graphtool.jar
cp ../target/graphtool.jar.pack.gz $R/www-dev/dwo/jars/graphtool.jar.pack.gz
cp ../target/balansfruitapplet.jar $R/www-dev/dwo/jars/balansfruitapplet.jar
cp ../target/balansfruitapplet.jar.pack.gz $R/www-dev/dwo/jars/balansfruitapplet.jar.pack.gz
cp ../target/geodefiner.jar $R/www-dev/dwo/jars/geodefiner.jar
cp ../target/geodefiner.jar.pack.gz $R/www-dev/dwo/jars/geodefiner.jar.pack.gz
cp ../target/statistiek.jar $R/www-dev/dwo/jars/statistiek.jar
cp ../target/statistiek.jar.pack.gz $R/www-dev/dwo/jars/statistiek.jar.pack.gz
