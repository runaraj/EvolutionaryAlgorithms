#!/usr/bin/env bash
cd ../simpleGA_java/
make
java -classpath build/classes geneticalgorithm.Launcher 9 4 1.0 Uniform
