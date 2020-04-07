#!/bin/sh
wget -O BuildTools.jar https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar
java -jar BuildTools.jar --rev 1.15.2
cd /WauzStarter
mvn clean install
cd /
mvn clean install
cd /WauzDiscord
mvn clean install
cd /WauzUnit
mvn clean install
