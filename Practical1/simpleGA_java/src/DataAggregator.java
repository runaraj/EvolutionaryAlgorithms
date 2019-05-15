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

    public static Map<Integer, int[]> mAggregates = new HashMap<>();
    public static Map<Integer, int[]> popAggregates = new HashMap<>();
    public static Map<String, int[]> typeAggregates = new HashMap<>();
    public static Map<Double, int[]> dAggregates = new HashMap<>();

    
    public Map<Integer, List<Integer>> aggregateM(String variationType, double dVal) {
        int stuckCount = 0;
        int foundGlobalCount = 0;
        List<Integer> evaluations = new ArrayList<>();
        List<Integer> generations = new ArrayList<>();

        int[] mValues = {1,2,4,8,16};
        
        // Key = value of m
        //int[] = [stuckCount, foundGlobalCount, evalTotal, genTotal]
        Map<Integer, int[]> counters = new HashMap<>();

        Map<Integer, List<Integer>> allEvals = new HashMap<>();

        for (Integer m : mValues) {
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(foundGlobalOptimum));
                String line = reader.readLine();
                while (line != null) {
                    String[] linesplit = line.split("_");
                    double d = Double.parseDouble(linesplit[4].substring(1,linesplit[4].length()));
                    int mVal = Integer.valueOf(linesplit[2].substring(1));
                    String tVal = linesplit[5].substring(1);

                    if (m == mVal && (tVal.equals(variationType) || variationType.equals("all")) && (d==dVal || dVal==0)) {
                        String[] evalGen = linesplit[6].split("-")[1].split(":");
                        int numEvals = Integer.valueOf(evalGen[0]);
                        int numGens = Integer.valueOf(evalGen[1]);
                        foundGlobalCount ++;
                        evaluations.add(numEvals);
                        generations.add(numGens);
                    }
                    line = reader.readLine();
                }
                
                reader.close();
            } catch (Exception e) {
                
            }
            // Get data from gotStuck
            try {
                reader = new BufferedReader(new FileReader(gotStuck));
                String line = reader.readLine();
                
                while (line != null) {
                    String[] linesplit = line.split("_");
                    double d = Double.parseDouble(linesplit[4].substring(1,linesplit[4].length()));
                    int mVal = Integer.valueOf(linesplit[2].substring(1));
                    String tVal = linesplit[5].substring(1);

                    if (m == mVal && (tVal.equals(variationType) || variationType.equals("all")) && (d==dVal || dVal==0)) {
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
            allEvals.put(m, new ArrayList<>(evaluations));
            evaluations.clear();
            generations.clear();
        }

        mAggregates = counters;

        // System.out.println("Stuck count, Global count, eval avg, gen. avg");
        for (Integer key : counters.keySet()) {
            // System.out.println("M value: " + key);
            for (int i = 0; i < 4; i++) {
                // System.out.print(counters.get(key)[i] + " ");
            }
            // System.out.println();
        }
        return allEvals;


    }

    public Map<Double, List<Integer>> aggregateD() {
        List<Integer> evaluationsList02 = new ArrayList<>();
        List<Integer> generationsList02 = new ArrayList<>();
        List<Integer> evaluationsList08 = new ArrayList<>();
        List<Integer> generationsList08 = new ArrayList<>();

        //int[] = [stuckCount, foundGlobalCount, evalAverage, genAverage]
        Map<Double, int[]> counters = new HashMap<>();
        BufferedReader reader;
        for (String f : files) {
            try {
                reader = new BufferedReader(new FileReader(f));
                String line = reader.readLine();
                while (line != null) {
                    String[] variableConfig = line.split("_");
                    double d = Double.parseDouble(variableConfig[4].substring(1,variableConfig[4].length()));
                    int[] count = {0,0};
                    if (f.equals(this.gotStuck)) {
                        count[0] = 1;
                    } else {
                        count[1] = 1;
                        String params = line.split("-")[1];
                        int evalCount = Integer.parseInt(params.split(":")[0]);
                        int genCount = Integer.parseInt(params.split(":")[1]);
                        if (d == 0.2){
                            evaluationsList02.add(evalCount);
                            generationsList02.add(genCount);
                        }
                        else {
                            evaluationsList08.add(evalCount);
                            generationsList08.add(genCount);
                        }
                    }
                    if (counters.containsKey(d)){
                        counters.get(d)[0] += count[0];
                        counters.get(d)[1] += count[1];
                        // int[] res = add(solutionCounts.get(solutionsCountKey), count);
                        // solutionCounts.replace(solutionsCountKey, res);
                    } else {
                        counters.put(d, new int[4]);
                    }

                    line = reader.readLine();
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int evalSum = 0;
        int genSum = 0;
        for(int i = 0; i<generationsList02.size(); i++){
            evalSum += evaluationsList02.get(i);
            genSum += generationsList02.get(i);
        }
        counters.get(0.2)[2] = evalSum/evaluationsList02.size();
        counters.get(0.2)[3] = genSum/generationsList02.size();

        evalSum = 0;
        genSum = 0;
        for(int i = 0; i<generationsList08.size(); i++){
            evalSum += evaluationsList08.get(i);
            genSum += generationsList08.get(i);
        }

        counters.get(0.8)[2] = evalSum/evaluationsList08.size();
        counters.get(0.8)[3] = genSum/generationsList08.size();

        for(Double key: counters.keySet()){
            System.out.println("D-value is equal to: " + key);
            for(int i = 0; i<4; i++){
                System.out.print(counters.get(key)[i]+" ");
            }
            System.out.println();
        }

        dAggregates = counters;

        Map<Double, List<Integer>> allEvals = new HashMap<>();
        allEvals.put(0.8, evaluationsList08);
        allEvals.put(0.2, evaluationsList02);

        // return counters;
        return allEvals;
    }

    
    public Map<Integer, List<Integer>> aggregatePop(double dVal) {
        int stuckCount = 0;
        int foundGlobalCount = 0;
        List<Integer> evaluations = new ArrayList<>();
        List<Integer> generations = new ArrayList<>();

        Map<Integer, List<Integer>> allEvals = new HashMap<>();

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
                    double d = Double.parseDouble(linesplit[4].substring(1,linesplit[4].length()));
                    int pVal = Integer.valueOf(linesplit[1].substring(1));

                    if (p == pVal && (d ==dVal||dVal==0)) {
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
                    double d = Double.parseDouble(linesplit[4].substring(1,linesplit[4].length()));
                    if (p == pVal && (dVal==d || dVal ==0)) {
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
            if (evaluations.size() > 0) {
                counters.put(p, aggregate);
                allEvals.put(p, new ArrayList<>(evaluations));
            }
            stuckCount = 0;
            foundGlobalCount = 0;
            evaluations.clear();
            generations.clear();
        }

        popAggregates = counters;
        /*
        System.out.println("Stuck count, Global count, eval avg, gen. avg");
        for (Integer key : counters.keySet()) {
            System.out.println("P value: " + key);
            for (int i = 0; i < 4; i++) {
                System.out.print(counters.get(key)[i] + " ");
            }
            System.out.println();
        }*/

        return allEvals;
        
        
    }

    
    public Map<String, List<Integer>> aggregateType() {

        int stuckCount = 0;
        int foundGlobalCount = 0;
        List<Integer> evaluations = new ArrayList<>();
        List<Integer> generations = new ArrayList<>();

        // int[] popValues = {50, 100, 200, 300, 400, 500};
        String[] typeValues = {"Uniform",  "OnePoint"};
        
        // Key = value of m
        //int[] = [stuckCount, foundGlobalCount, evalTotal, genTotal]
        Map<String, int[]> counters = new HashMap<>();

        Map<String, List<Integer>> allEvals = new HashMap<>();

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
            allEvals.put(t, new ArrayList<>(evaluations));
        }

        typeAggregates = counters;
        // System.out.println("Stuck count, Global count, eval avg, gen. avg");
        for (String key : counters.keySet()) {
            // System.out.println("Type value: " + key);
            for (int i = 0; i < 4; i++) {
                // System.out.print(counters.get(key)[i] + " ");
            }
            // System.out.println();
        }

        return allEvals;
        
    }


    // @data = list of #evaluations or fitness.
    // Returns the std of values in @data.
    public double getStandardDeviation(List<Integer> data){
        int sum = 0;
        for (Integer in: data) {
            sum+=in;
        }
        double mean = sum/data.size();
        double sumSquared = 0;
        for (Integer in : data) {
            sumSquared +=Math.pow(in-mean,2);
        }
        double std =Math.sqrt(sumSquared/data.size());
        return std;
    }

    public double getStandardDeviationDouble(List<Double> data){
        double sum = 0;
        for (Double in: data) {
            sum+=in;
        }
        double mean = sum/data.size();
        double sumSquared = 0;
        for (Double in : data) {
            sumSquared +=Math.pow(in-mean,2);
        }
        double std =Math.sqrt(sumSquared/data.size());
        return std;
    }





    public static void main(String[] args) throws IOException {

        DataAggregator agg = new DataAggregator();


        // START - create data for population sizes 50 to 4000
        //         Finds the average number of evaluations and
        //              the number of solutions found for a 
        //              given population size
        // first run the script: 'runPop50to4000'
        System.out.println("RESULTS FOR DIFFERENT POPULATION SIZES:");
        Map<Integer, List<Integer>> resPop = agg.aggregatePop(0);
        List<Integer> copPop = new ArrayList<Integer>(resPop.keySet());
        System.out.println(popAggregates.get(50)[0]);
        Collections.sort(copPop);
        for (Integer key : copPop) {
            System.out.println("    Population size: " + key + " Avg. Number of Evals: " + popAggregates.get(key)[2] + " Number of solutions: " + popAggregates.get(key)[1] + " Number of instances: " + (popAggregates.get(key)[1]+popAggregates.get(key)[0]));
        }
        System.out.println("\n");
        // END ---------------------------------------------------


        // The following prints show that:
        // The average number of evals increase with m
        // The std also increases at the same time
        // HOWEVER, for each value of m, the std of the resulting
        //          number of evaluations is relatively equal
        //          i.e. the Z-value has approx the same std
        // agg.aggregateM();
        //Map<Integer, List<Integer>> resPop = agg.aggregatePop();

        // START
        
        // The following code find evals by m for the different d values
        System.out.println("RESULTS FOR DIFFERENT D VALUES by M");
        Map<Integer,List<Integer>> resMu = agg.aggregateM("all", 0.2);
        List<Integer> copM = new ArrayList<Integer>(resMu.keySet());
        Collections.sort(copM);
        System.out.println("D = 0.2");
        for (Integer key : copM) {
            System.out.println("    M value: " + key + " Avg. Number of Evals: " + mAggregates.get(key)[2] + " Number of solutions found: " + mAggregates.get(key)[1] + " Number of instances: " + (mAggregates.get(key)[1]+mAggregates.get(key)[0]));
        }

        System.out.println("D = 0.8");
        resMu = agg.aggregateM("all", 0.8);
        copM = new ArrayList<Integer>(resMu.keySet());
        Collections.sort(copM);
        for (Integer key : copM) {
            System.out.println("    M value: " + key + " Avg. Number of Evals: " + mAggregates.get(key)[2] + " Number of solutions found: " + mAggregates.get(key)[1] + " Number of instances: " + (mAggregates.get(key)[1]+mAggregates.get(key)[0]));
        }
        // END --------------------------------------------

        
        // The following code find evals by population size for different d values
        System.out.println("RESULTS FOR DIFFERENT D VALUES by Population Size");
        Map<Integer,List<Integer>> res = agg.aggregatePop(0.2);
        List<Integer> cop = new ArrayList<Integer>(res.keySet());
        Collections.sort(cop);
        System.out.println("D = 0.2");
        for (Integer key : cop) {
            System.out.println("    Population size: " + key + " Number of Evals: " + popAggregates.get(key)[2] + " Number of solutions: " + popAggregates.get(key)[1] + " Number of instances: " + (popAggregates.get(key)[1]+popAggregates.get(key)[0]));
        }
        System.out.println("D = 0.8");
        res = agg.aggregatePop(0.8);
        cop = new ArrayList<Integer>(res.keySet());
        Collections.sort(cop);
        for (Integer key : cop) {
            System.out.println("    Population size: " + key + " Number of Evals: " + popAggregates.get(key)[2] + " Number of solutions: " + popAggregates.get(key)[1] + " Number of instances: " + (popAggregates.get(key)[1]+popAggregates.get(key)[0]));
        }

        // START - Results for different variation types

        System.out.println("RESULTS FOR DIFFERENT VARIATION TYPES");
        System.out.println("    UNIFORM");

        // START - Results for different M values
        resMu = agg.aggregateM("Uniform", 0);
        copM = new ArrayList<Integer>(resMu.keySet());
        Collections.sort(copM);
        for (Integer key : copM) {
            System.out.println("        M value: " + key + " Avg. Number of Evals: " + mAggregates.get(key)[2] + " Number of solutions found: " + mAggregates.get(key)[1] + " Number of instances: " + (mAggregates.get(key)[1]+mAggregates.get(key)[0]));
        }

        System.out.println("    ONEPOINT");
        Map<Integer,List<Integer>> resMop = agg.aggregateM("OnePoint", 0);
        List<Integer> copMop = new ArrayList<Integer>(resMop.keySet());
        Collections.sort(copMop);
        for (Integer key : copMop) {
            System.out.println("        M value: " + key + " Avg. Number of Evals: " + mAggregates.get(key)[2] + " Number of solutions found: " + mAggregates.get(key)[1] + " Number of instances: " + (mAggregates.get(key)[1]+mAggregates.get(key)[0]));
        }
        System.out.println("\n");

        // END ---------------------------------------------------


        // START - Results for different variation types
        
        Map<String,List<Integer>> resType = agg.aggregateType();
        List<String> copT = new ArrayList<String>(resType.keySet());
        for (String key : copT) {
            // System.out.println("Population size: " + key + " Avg. Number of Evals: " + typeAggregates.get(key)[2] + " Number of solutions found: " + typeAggregates.get(key)[1] + " Number of instances: " + (typeAggregates.get(key)[1]+typeAggregates.get(key)[0]));
        }
        
        // END ---------------------------------------------------


    }
    
}
