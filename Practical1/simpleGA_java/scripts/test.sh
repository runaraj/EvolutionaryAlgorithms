#!/usr/bin/env bash
cd ../simpleGA_java/
make
# args: m k d crossovertype
java -classpath build/classes geneticalgorithm.Launcher 1 5 0.2 Uniform 
