#!/usr/bin/env bash
cd ../simpleGA_java/
make
# args: m k d n crossovertype
java -classpath build/classes geneticalgorithm.Launcher 2 5 0.2 100 Onepoint
