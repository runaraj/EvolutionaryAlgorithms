
package geneticalgorithm;
import java.util.Arrays;

/**
 *
 * @author Marco Virgolin, with the collaboration of Anton Bouter and Hoang Ngoc Luong and the supervision of Peter A.N. Bosman
 */
public class FitnessFunction {

    int m, k;
    double d;
    long evaluations;
    double optimum;

    Individual elite = null;

    FitnessFunction(int m, int k, double d) {
        this.m = m;
        this.k = k;
        this.d = d;
        this.evaluations = 0;
        
        //this.optimum = m * k;   // This is the optimum for OneMax, not for your function
        this.optimum = m; // The optimum is a summation of m elements, where each element is max 1
    }
    
    // The purpose of this custom exception is to perform a naughty trick: halt the GA as soon as the optimum is found
    // do not modify it
    class OptimumFoundCustomException extends Exception {
        public OptimumFoundCustomException(String message) {
            super(message);
        }
    }

    // This is the f function
    public void Evaluate(Individual individual) throws OptimumFoundCustomException, OptimumFoundCustomException {

        evaluations++;
        
        double result = 0.0;
        

        // we want to sum f-sub m times
        for (int i=0; i<m; i++) {
            int[] subgenes = Arrays.copyOfRange(individual.genotype, i*k, (i*k)+k);
            result += subfunction(subgenes,k,d);
        }

        /*System.out.print("Individual: ");
        for (int j = 0; j < individual.genotype.length; j++) {
            System.out.print(individual.genotype[j]+ " ");
        }


        /*for (int i = 0; i < individual.genotype.length; i++) {
            result += individual.genotype[i];
        }*/

        //System.out.println("Individual fitness" + result);
        // set the fitness of the individual
        individual.fitness = result;
        // update elite
        if (elite == null || elite.fitness < individual.fitness) {
            elite = individual.Clone();
        }

        // check if optimum has been found
        if (result == optimum) {
            // naughty trick in action: throw our custom exception
            throw new OptimumFoundCustomException("GG EZ");
        }
    }

    // This is the f-sub function
    private Double subfunction(int[] subgenes, int k, double d) {

        // genesum: The u(b) function
        double genesum = Arrays.stream(subgenes).sum();
        if (genesum == Double.valueOf(k)) {
            return 1.0;
        }
        return (1-d)*((Double.valueOf(k)-1-genesum)/(Double.valueOf(k)-1));
    }



    public static void main(String[] args) {

        FitnessFunction f = new FitnessFunction(1, 5, 0.2);
        Individual i1 = new Individual(5);
        i1.genotype = new int[]{0,0,0,0,1};
        Individual i2 = new Individual(5);
        i2.genotype = new int[]{1,0,0,0,0};
        Individual i3 = new Individual(5);
        i3.genotype = new int[]{1,1,0,0,0};
        Individual i4 = new Individual(5);
        i4.genotype = new int[]{0,1,1,0,0};
        Individual i5 = new Individual(5);
        i5.genotype = new int[]{1,1,1,1,0};
        Individual i6 = new Individual(5);
        i6.genotype = new int[]{1,1,1,1,1};
        try {
            f.Evaluate(i1);
            f.Evaluate(i2);
            f.Evaluate(i3);
            f.Evaluate(i4);
            f.Evaluate(i5);
            
        } catch (Exception e) {
            //TODO: handle exception
        }
        try {
            f.Evaluate(i6);
            
        } catch (Exception e) {
            //TODO: handle exception
        }
        
        System.out.println(i1+ " " + i1.fitness);
        System.out.println(i2+ " " + i2.fitness);    
        System.out.println(i3+ " " + i3.fitness);
        System.out.println(i4+ " " + i4.fitness);
        System.out.println(i5+ " " + i5.fitness);
        System.out.println(i6+ " " + i6.fitness);
        System.out.println("\n");

        f = new FitnessFunction(1, 5, 0.8);
        try {
            f.Evaluate(i1);
            f.Evaluate(i2);
            f.Evaluate(i3);
            f.Evaluate(i4);
            f.Evaluate(i5);
            
        } catch (Exception e) {
            //TODO: handle exception
        }
        try {
            f.Evaluate(i6);
            
        } catch (Exception e) {
            //TODO: handle exception
        }
        
        System.out.println(i1+ " " + i1.fitness);
        System.out.println(i2+ " " + i2.fitness);    
        System.out.println(i3+ " " + i3.fitness);
        System.out.println(i4+ " " + i4.fitness);
        System.out.println(i5+ " " + i5.fitness);
        System.out.println(i6+ " " + i6.fitness);
        
    }



    

}
