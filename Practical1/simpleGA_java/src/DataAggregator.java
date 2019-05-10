//package geneticalgorithm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;



public class DataAggregator {

    File directory = new File("experiments");

    //init new file names
    String gotStuck = "experiments/gotStuck.txt";
    String foundGlobalOptimum = "experiments/foundGlobalOptimum.txt";
    List<String> files = new ArrayList<>(Arrays.asList(gotStuck, foundGlobalOptimum));

    public void aggregateM() {
        int stuckCount = 0;
        int foundGlobalCount = 0;
        List<Integer> evaluations = new ArrayList<>();
        List<Integer> generations = new ArrayList<>();

        int[] mValues = {1,2,4,8,16};
        
        // Key = value of m
        //int[] = [stuckCount, foundGlobalCount, evalTotal, genTotal]
        Map<Integer, int[]> counters = new HashMap<>();

        for (Integer m : mValues) {
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(foundGlobalOptimum));
                String line = reader.readLine();
                while (line != null) {
                    String[] linesplit = line.split("_");
                    int mVal = Integer.valueOf(linesplit[2].substring(1));

                    if (m == mVal) {
                        String[] evalGen = linesplit[6].split("-")[1].split(":");
                        int numEvals = Integer.valueOf(evalGen[0]);
                        int numGens = Integer.valueOf(evalGen[1]);
                        foundGlobalCount ++;
                        // evaluationsTotal += numEvals;
                        evaluations.add(numEvals);
                        // generationsTotal += numGens;
                        generations.add(numGens);
                    }


                    line = reader.readLine();
                }
                
                reader.close();
            } catch (Exception e) {
                
            }
            try {
                reader = new BufferedReader(new FileReader(gotStuck));
                String line = reader.readLine();
                while (line != null) {
                    String[] linesplit = line.split("_");
                    int mVal = Integer.valueOf(linesplit[2].substring(1));

                    if (m == mVal) {
                        stuckCount ++;
                    }
                    line = reader.readLine();
                }
                
                reader.close();
            } catch (Exception e) {
                
            }
            int evaluationsAvg = 0;
            int generationAvg = 0;
            if (foundGlobalCount> 0) {
                evaluationsAvg = evaluations.stream().mapToInt(a->a).sum()/foundGlobalCount;
                generationAvg = generations.stream().mapToInt(a->a).sum()/foundGlobalCount;
            }

            int[] aggregate = {stuckCount, foundGlobalCount, evaluationsAvg, generationAvg};
            counters.put(m, aggregate);
            stuckCount = 0;
            foundGlobalCount = 0;
            evaluations.clear();
            generations.clear();
        }

        System.out.println("Stuck count, Global count, eval avg, gen. avg");
        for (Integer key : counters.keySet()) {
            System.out.println("M value: " + key);
            for (int i = 0; i < 4; i++) {
                System.out.print(counters.get(key)[i] + " ");
            }
            System.out.println();
        }
    }

    public void aggregateD() {

    }

    
    public void aggregatePop() {
        int stuckCount = 0;
        int foundGlobalCount = 0;
        List<Integer> evaluations = new ArrayList<>();
        List<Integer> generations = new ArrayList<>();

        // int[] popValues = {50, 100, 200, 300, 400, 500};
        int[] popValues = {50, 100, 200, 300, 400, 500, 1000, 1500, 2000, 2500, 3000, 3500, 4000};
        
        // Key = value of m
        //int[] = [stuckCount, foundGlobalCount, evalTotal, genTotal]
        Map<Integer, int[]> counters = new HashMap<>();

        for (Integer p : popValues) {
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(foundGlobalOptimum));
                String line = reader.readLine();
                while (line != null) {
                    String[] linesplit = line.split("_");
                    int pVal = Integer.valueOf(linesplit[1].substring(1));

                    if (p == pVal) {
                        String[] evalGen = linesplit[6].split("-")[1].split(":");
                        int numEvals = Integer.valueOf(evalGen[0]);
                        int numGens = Integer.valueOf(evalGen[1]);
                        foundGlobalCount ++;
                        evaluations.add(numEvals);
                        // generationsTotal += numGens;
                        generations.add(numGens);
                    }


                    line = reader.readLine();
                }
                
                reader.close();
            } catch (Exception e) {
                
            }
            try {
                reader = new BufferedReader(new FileReader(gotStuck));
                String line = reader.readLine();
                while (line != null) {
                    String[] linesplit = line.split("_");
                    int pVal = Integer.valueOf(linesplit[1].substring(1));

                    if (p == pVal) {
                        stuckCount ++;
                    }
                    line = reader.readLine();
                }
                
                reader.close();
            } catch (Exception e) {
                
            }
            int evaluationsAvg = 0;
            int generationAvg = 0;
            if (foundGlobalCount> 0) {
                evaluationsAvg = evaluations.stream().mapToInt(a->a).sum()/foundGlobalCount;
                generationAvg = generations.stream().mapToInt(a->a).sum()/foundGlobalCount;
                
            }

            int[] aggregate = {stuckCount, foundGlobalCount, evaluationsAvg, generationAvg};
            counters.put(p, aggregate);
            stuckCount = 0;
            foundGlobalCount = 0;
        }

        System.out.println("Stuck count, Global count, eval avg, gen. avg");
        for (Integer key : counters.keySet()) {
            System.out.println("P value: " + key);
            for (int i = 0; i < 4; i++) {
                System.out.print(counters.get(key)[i] + " ");
            }
            System.out.println();
        }
        
    }
    
    public void aggregateType() {

        int stuckCount = 0;
        int foundGlobalCount = 0;
        List<Integer> evaluations = new ArrayList<>();
        List<Integer> generations = new ArrayList<>();

        // int[] popValues = {50, 100, 200, 300, 400, 500};
        String[] typeValues = {"Uniform",  "OnePoint"};
        
        // Key = value of m
        //int[] = [stuckCount, foundGlobalCount, evalTotal, genTotal]
        Map<String, int[]> counters = new HashMap<>();

        for (String t : typeValues) {
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(foundGlobalOptimum));
                String line = reader.readLine();
                while (line != null) {
                    String[] linesplit = line.split("_");
                    String tVal = linesplit[5].substring(1);

                    if (t.equals(tVal)) {
                        String[] evalGen = linesplit[6].split("-")[1].split(":");
                        int numEvals = Integer.valueOf(evalGen[0]);
                        int numGens = Integer.valueOf(evalGen[1]);
                        foundGlobalCount ++;
                        evaluations.add(numEvals);
                        // generationsTotal += numGens;
                        generations.add(numGens);
                    }


                    line = reader.readLine();
                }
                
                reader.close();
            } catch (Exception e) {
                
            }
            try {
                reader = new BufferedReader(new FileReader(gotStuck));
                String line = reader.readLine();
                while (line != null) {
                    String[] linesplit = line.split("_");
                    String tVal = linesplit[5].substring(1);

                    if (t.equals(tVal)) {
                        stuckCount ++;
                    }
                    line = reader.readLine();
                }
                
                reader.close();
            } catch (Exception e) {
                
            }
            int evaluationsAvg = 0;
            int generationAvg = 0;
            if (foundGlobalCount> 0) {
                evaluationsAvg = evaluations.stream().mapToInt(a->a).sum()/foundGlobalCount;
                generationAvg = generations.stream().mapToInt(a->a).sum()/foundGlobalCount;
                
            }

            int[] aggregate = {stuckCount, foundGlobalCount, evaluationsAvg, generationAvg};
            counters.put(t, aggregate);
            stuckCount = 0;
            foundGlobalCount = 0;
        }

        System.out.println("Stuck count, Global count, eval avg, gen. avg");
        for (String key : counters.keySet()) {
            System.out.println("Type value: " + key);
            for (int i = 0; i < 4; i++) {
                System.out.print(counters.get(key)[i] + " ");
            }
            System.out.println();
        }
        
    }
    public static void main(String[] args) throws IOException {

        DataAggregator agg = new DataAggregator();
        agg.aggregateM();
        agg.aggregatePop();
        agg.aggregateType();

        
    }
    
}
