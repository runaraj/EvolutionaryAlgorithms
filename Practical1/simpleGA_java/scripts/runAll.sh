#!/usr/bin/env bash
cd ../simpleGA_java/
make
# args: m k d n runs crossovertype
run=5
populationSize=100
crossoverTypes='Uniform Onepoint'
#remove old aggregate files at start

FILEgotStuck=experiments/gotStuck.txt
if [ -e "$FILEgotStuck" ]
then 
    rm experiments/gotStuck.txt
    rm experiments/foundGlobalOptimum.txt
fi

k=5

for populationSize in 10 100 500 1000
do
    for m in 1 2 4 8 16
    do
        for d in 0.2 0.8
        do
            for type in $crossoverTypes
            do
                java -classpath build/classes geneticalgorithm.Launcher $m $k $d $populationSize $run $type
            done
        done
    done
done


java -classpath build/classes DataAggregator