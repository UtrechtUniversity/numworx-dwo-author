R=/Volumes/fisme-sites

if ! test -d $R/www-dev
then
	echo "MOUNT FISME-SITES"
	exit 1;
else
	echo "fisme-sites mounted"
fi

cp ../target/statistiek-jar-with-dependencies.jar $R/www-dev/dwo/jars/statistiek.jar
cp ../target/statistiek-jar-with-dependencies.jar.pack.gz $R/www-dev/dwo/jars/statistiek.jar.pack.gz
