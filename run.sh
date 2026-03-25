#!/bin/zsh

mvn -q clean compile
mvn dependency:build-classpath -Dmdep.outputFile=.classpath
java -XstartOnFirstThread --enable-native-access=ALL-UNNAMED  -cp target/classes:$(cat .classpath) com.asciugano.Main
