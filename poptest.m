pop = [
    50, 100, 200, 300, 400, 500, 1000, 1500, 2000, 2500, 3000, 3500, 4000];

eval = [
    95
284
773
1242
1936
2585
6267
10064
13255
17098
20557
23588
26889];

sol = [
    131
182
228
245
267
283
316
333
343
350
355
359
359];


figure
plot(pop, eval);
xlabel("Population size");
ylabel("Number of evaluations");

figure
plot(pop, sol);
hold on
y = 400;
x1 = 0;
x2 = 4000;
plot([x1, x2], [y,y])
ylim([0 450])
xlabel("Population size");
ylabel("Number of solutions found");