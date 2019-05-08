#!/usr/bin/env bash
cd ../simpleGA_java/
make
# args: m k d n runs crossovertype
java -classpath build/classes geneticalgorithm.Launcher 3 5 0.2 100 5 Onepoint
