#!/bin/sh
set -xe
sh -xe proguard.sh
sh -xe sign.sh
sh -xe export-jar.sh
