#!/bin/sh
set -xe
jh=$(/usr/libexec/java_home -v 1.8)
export JAVA_HOME=$jh
sh -xe proguard.sh
(cd ../output/jar;sh -xe jarindex.sh)
sh -xe sign.sh
sh -xe export-jar.sh
