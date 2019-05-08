#!/usr/bin/env bash
cd ../simpleGA_java/
make
java -classpath build/classes geneticalgorithm.Launcher 8 5 1.0 Uniform 
