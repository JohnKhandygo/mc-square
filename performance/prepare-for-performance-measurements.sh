#!/bin/bash

echo "going to rebuild the project."
cd ..
mvn clean install -DskipTests
if (( "$?" != 0 ))
then
	exit 1
fi

rm -f mc-square-1.0-SNAPSHOT-jar-with-dependencies.jar
cp target/mc-square-1.0-SNAPSHOT-jar-with-dependencies.jar performance/
if (( "$?" != 0 ))
then
	echo "copy executable failed."
	exit 1
fi
echo "copy executable succeed."

cd performance/