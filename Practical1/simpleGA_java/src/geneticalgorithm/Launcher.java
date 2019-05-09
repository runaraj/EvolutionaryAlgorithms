package geneticalgorithm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.file.Files;

/**
 *
 * @author Marco Virgolin, with the collaboration of Anton Bouter and Hoang Ngoc Luong and the supervision of Peter A.N. Bosman
 */
public class Launcher {

    public static void main(String[] args) throws IOException {
        //System.out.println(args[0]);
        // termination condition parameters ( 0 or negatives are ignored )
        int m = Integer.parseInt(args[0]);
        int k = Integer.parseInt(args[1]);
        double d = Double.parseDouble(args[2]);
        int population_size = Integer.parseInt(args[3]);
        int numberOfRuns = Integer.parseInt(args[4]);
        String type = args[5];

        CrossoverType ct;
        if (type.equals("Uniform")){
            ct = CrossoverType.Uniform;
        }else{
            ct = CrossoverType.OnePoint;
        }


        long time_limit = 3 * 20; // in milliseconds
        int generations_limit = -1;
        long evaluations_limit = -1;
        File directory = new File("experiments");
        if (!directory.exists()) {
            directory.mkdir();
        }

        //init new file names
        String gotStuck = "experiments/gotStuck.txt";
        String foundGlobalOptimum = "experiments/foundGlobalOptimum.txt";


        // TODO: this runs one experiment, you may want to script a pipeline here.


        //int m = 8; int k = 5; double d = 1.0;

        for (int i = 0; i< numberOfRuns; i++){
            // Set up logging
            Utilities.rng.setSeed(42424242+i);
            String output_file_name = "experiments/log_p" + population_size + "_m" + m + "_k" + k + "_d" + d + "_c" + ct + "_run" + i + ".txt";
            Files.deleteIfExists(new File(output_file_name).toPath());
            Utilities.logger = new BufferedWriter(new FileWriter(output_file_name, true));
            Utilities.logger.write("gen evals time best_fitness\n");

            // Run GA
            System.out.println("Starting run " + i + " with pop_size=" + population_size + ", m=" + m + ", k=" + k + ", d=" + d + ", crossover_type=" + ct);
            SimpleGeneticAlgorithm ga = new SimpleGeneticAlgorithm(population_size, m, k, d, ct);
            try {
                ga.run(generations_limit, evaluations_limit, time_limit);

                System.out.println("Best fitness " + ga.fitness_function.elite.fitness + " found at\n"
                        + "generation\t" + ga.generation + "\nevaluations\t" + ga.fitness_function.evaluations + "\ntime (ms)\t" + (System.currentTimeMillis() - ga.start_time + "\n")
                        + "elite\t\t" + ga.fitness_function.elite.toString());
                Utilities.logger.write("\n");


                Utilities.logger.close();
                //writes all files that got stuck

                BufferedWriter logger3 = new BufferedWriter(new FileWriter(gotStuck,true));
                logger3.write("log_p" + population_size + "_m" + m + "_k" + k + "_d" + d + "_c" + ct + "_run" + i + ".txt"+"\n");
                logger3.close();

            } catch (FitnessFunction.OptimumFoundCustomException ex) {
                System.out.println("Optimum " + ga.fitness_function.elite.fitness + " found at\n"
                        + "generation\t" + ga.generation + "\nevaluations\t" + ga.fitness_function.evaluations + "\ntime (ms)\t" + (System.currentTimeMillis() - ga.start_time + "\n")
                        + "elite\t\t" + ga.fitness_function.elite.toString());
                Utilities.logger.write(ga.generation + " " + ga.fitness_function.evaluations + " " + (System.currentTimeMillis() - ga.start_time) + " " + ga.fitness_function.elite.fitness + "\n");


                Utilities.logger.close();
                //writes all files with parameters that found global optimum
                BufferedWriter logger2 = new BufferedWriter(new FileWriter(foundGlobalOptimum,true));
                logger2.write("log_p" + population_size + "_m" + m + "_k" + k + "_d" + d + "_c" + ct + "_run" + i + ".txt" +"\n");
                logger2.close();
            }
        }


    }
}
