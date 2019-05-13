numberOfOnes = [
    0,1,2,3,4,5];

fitnessD02 = [
    0.8
    0.6000000000000001
    0.4
    0.2
    0.0
    1.0
];

fitnessD08 = [
    0.19999999999999996
    0.14999999999999997
    0.09999999999999998
    0.04999999999999999
    0.0
    1.0
];

figure
plot(numberOfOnes, fitnessD02);
hold on
plot(numberOfOnes, fitnessD08);
legend("D=0.2", "D=0.8");
xticks(0:1:5);
xlabel("Number of 1s in individual");
ylabel("Fitness");

figure
plot(numberOfOnes, fitnessD08);
legend("D=0.8");
title("Fitness func.");
xlabel("Number of 1s in individual");
xticks(0:1:5);
ylabel("Fitness");