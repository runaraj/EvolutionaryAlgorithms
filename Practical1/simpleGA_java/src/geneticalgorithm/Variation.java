
package geneticalgorithm;

import java.util.ArrayList;

/**
 *
 * @author Marco Virgolin, with the collaboration of Anton Bouter and Hoang Ngoc Luong and the supervision of Peter A.N. Bosman
 */
enum CrossoverType {
    Uniform,
    OnePoint
}

public class Variation {

    CrossoverType crossover_type;

    public Variation(CrossoverType crossover_type) {
        this.crossover_type = crossover_type;
    }

    public ArrayList<Individual> PerformCrossover(Individual parent1, Individual parent2) {

        if (crossover_type == CrossoverType.OnePoint) {
            return OnePointCrossover(parent1, parent2);
        } else {
            return UniformCrossover(parent1, parent2);
        }

    }

    private ArrayList<Individual> UniformCrossover(Individual parent1, Individual parent2) {

        Individual child1 = parent1.Clone(); // explicit call to clone because otherwise Java copies the reference
        Individual child2 = parent2.Clone();
        

        //perform crossover
        for(int i = 0; i < parent1.genotype.length; i++){
            //select random bit from either parent 1 or parent 2
            int val1 = Utilities.rng.nextDouble() < 0.5 ? 0 : 1;
            //int val2 = Utilities.rng.nextDouble() < 0.5 ? 0 : 1;
            if (val1 == 0){
                child1.genotype[i]=parent1.genotype[i];
                child2.genotype[i]=parent2.genotype[i];
            }
            if (val1 == 1){
                child1.genotype[i]=parent2.genotype[i];
                child2.genotype[i]=parent1.genotype[i];
            }
            /*}
            if (val2 == 0){
            }
            if (val2 == 1){
                child2.genotype[i]=parent2.genotype[i];
            }*/
        }   

        ArrayList<Individual> result = new ArrayList<Individual>();
        result.add(child1);
        result.add(child2);

        return result;
    }

    private ArrayList<Individual> OnePointCrossover(Individual parent1, Individual parent2) {

        Individual child1 = parent1.Clone();
        Individual child2 = parent2.Clone();

        // select point to do crossover with.
        int crossoverPoint1 = Utilities.rng.nextInt(parent1.genotype.length);
        //int crossoverPoint2 = Utilities.rng.nextInt(parent1.genotype.length);




        // do crossover
        for(int i = 0; i < parent1.genotype.length; i++){
            if(i < crossoverPoint1){
                child1.genotype[i] = parent1.genotype[i];
                child2.genotype[i] = parent2.genotype[i];
            }else if (i > crossoverPoint1){
                child1.genotype[i] = parent2.genotype[i];
                child2.genotype[i] = parent1.genotype[i];
            }
        }
        ArrayList<Individual> result = new ArrayList<Individual>();

        result.add(child1);
        result.add(child2);

        return result;
    }

}
