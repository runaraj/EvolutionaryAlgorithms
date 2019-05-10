
package geneticalgorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Marco Virgolin, with the collaboration of Anton Bouter and Hoang Ngoc Luong and the supervision of Peter A.N. Bosman
 */
public class SimpleGeneticAlgorithm {

    int population_size, generation, genotype_length;
    long start_time;

    FitnessFunction fitness_function;
    Variation variation;
    Selection selection;

    ArrayList<Individual> population;

    SimpleGeneticAlgorithm(int population_size, int m, int k, double d, CrossoverType crossover_type) {
        this.population_size = population_size;
        if (population_size % 2 == 1) {
            System.out.println("Error: population size must be multiple of 2");
            System.exit(1);
        }
        this.fitness_function = new FitnessFunction(m, k, d);
        this.genotype_length = m * k;
        this.variation = new Variation(crossover_type);
        this.selection = new Selection();
        this.population = new ArrayList<Individual>();
    }

    public void run(int generation_limit, long evaluations_limit, long time_limit) throws FitnessFunction.OptimumFoundCustomException, IOException {
        
        // L1: Initialize generation and time variables (evaluations are stored in fitness_function)
        generation = 0;
        start_time = System.currentTimeMillis();

        // L2: Create and evaluate individuals
        for (int i = 0; i < population_size; i++) {
            // A new individual is automatically initialized randomly (see its constructor)
            Individual individual = new Individual(genotype_length);
            fitness_function.Evaluate(individual);
            population.add(individual);
        }
        
        // Log generation 0
        Utilities.logger.write(generation+" "+fitness_function.evaluations+" "+(System.currentTimeMillis() - start_time)+" "+fitness_function.elite.fitness+"\n");

        // L3: Evolutionary loop
        while (!CheckTerminationCondition(generation_limit, evaluations_limit, time_limit)) {
            
            // System.out.println("> Generation "+generation+" - best fitness found: "+fitness_function.elite.fitness);
            /*System.out.print("Individual: ");
            for (int j = 0; j < fitness_function.elite.genotype.length; j++) {
                System.out.print(fitness_function.elite.genotype[j]+ " ");
            }
            System.out.println();*/


            
            // L3.1: Prepare offspring
            ArrayList<Individual> offspring = new ArrayList<>();
            // L3.2: Create random permutation
            int[] perm = Utilities.CreateRandomPermutation(population_size);
            // L3.3: Offspring generation loop
            for (int i = 0; i < population_size / 2; i++) {
                // L3.3.1: Create 2 children and add to population (crossover is always used, and mutation is never used)
                offspring.addAll(variation.PerformCrossover(population.get(perm[2 * i]), population.get(perm[2 * i + 1])));
            }
            // L3.4: evaluate offspring
            for (Individual o : offspring) {
                fitness_function.Evaluate(o);
            }
            // L3.5: join parents and offspring 
            ArrayList<Individual> p_and_o = new ArrayList<>();
            p_and_o.addAll(population);
            p_and_o.addAll(offspring);
            // L3.6 (and follownig): selection
            population = selection.TournamentSelect(p_and_o);

            /*for (int i = 0; i < population.size();i++) {
                System.out.println(population.get(i).toString());
            }*/


            // L3.8: increment generation counter
            generation += 1;
            
            // Log generation
            Utilities.logger.write(generation+" "+fitness_function.evaluations+" "+(System.currentTimeMillis() - start_time)+" "+fitness_function.elite.fitness+"\n");
        }
    }

    private int[] prevElite = {1};

    private boolean CheckTerminationCondition(int generation_limit, long evaluations_limit, long time_limit) {

        if (generation_limit > 0 && generation >= generation_limit) {
            return true;
        }
        if (evaluations_limit > 0 && fitness_function.evaluations >= evaluations_limit) {
            return true;
        }
        long elapsed_time = System.currentTimeMillis() - start_time;
        if (time_limit > 0 && elapsed_time >= time_limit) {
            return true;
        }
        // If the elite is all 0 two times in a row we are stuck
        if (Arrays.stream(prevElite).sum() == 0){
            return true;
        } else {
            prevElite = fitness_function.elite.genotype;
        }
        return false;
    }

}
