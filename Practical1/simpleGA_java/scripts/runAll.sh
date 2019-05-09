#!/usr/bin/env bash
cd ../simpleGA_java/
make
# args: m k d n runs crossovertype
run=1
populationSize=100
crossoverTypes='Uniform Onepoint'
for m in 1 2 4 8 16
do
    for d in 0.2 0.8
    do
        for k in 3 5 10
        do
            for type in $crossoverTypes
            do
                java -classpath build/classes geneticalgorithm.Launcher $m $k $d $populationSize $run $type
            done
        done
    done
done