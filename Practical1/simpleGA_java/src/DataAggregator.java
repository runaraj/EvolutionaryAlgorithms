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

    
    public Map<Integer, List<Integer>> aggregateM() {
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

                    if (m == mVal && d == 0.2) {
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
            // Get data from gotStuck
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
            allEvals.put(m, new ArrayList<>(evaluations));
            evaluations.clear();
            generations.clear();
        }

        mAggregates = counters;

        System.out.println("Stuck count, Global count, eval avg, gen. avg");
        for (Integer key : counters.keySet()) {
            System.out.println("M value: " + key);
            for (int i = 0; i < 4; i++) {
                System.out.print(counters.get(key)[i] + " ");
            }
            System.out.println();
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

        Map<Double, List<Integer>> allEvals = new HashMap<>();
        allEvals.put(0.8, evaluationsList08);
        allEvals.put(0.2, evaluationsList02);

        // return counters;
        return allEvals;
    }

    
    public Map<Integer, List<Integer>> aggregatePop() {
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
            allEvals.put(p, new ArrayList<>(evaluations));
            evaluations.clear();
            generations.clear();
        }

        System.out.println("Stuck count, Global count, eval avg, gen. avg");
        for (Integer key : counters.keySet()) {
            System.out.println("P value: " + key);
            for (int i = 0; i < 4; i++) {
                System.out.print(counters.get(key)[i] + " ");
            }
            System.out.println();
        }

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

        System.out.println("Stuck count, Global count, eval avg, gen. avg");
        for (String key : counters.keySet()) {
            System.out.println("Type value: " + key);
            for (int i = 0; i < 4; i++) {
                System.out.print(counters.get(key)[i] + " ");
            }
            System.out.println();
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

    private double calcZvalue(int x, double mean, double std){
        double out = (x-mean)/std;
        return out;

    }

    private double calcMean(List<Integer> data) {
        int sum = 0;
        for (Integer in: data) {
            sum+=in;
        }
        double mean = sum/data.size();
        return mean;
    }
    private double calcMeanDouble(List<Double> data) {
        double sum = 0.0;
        for (Double in: data) {
            sum+=in;
        }
        double mean = sum/data.size();
        return mean;
    }

    // takes in an array of values and returns an array with their Z values
    public List<Double> getZvalues(List<Integer> data) {
        List<Double> out = new ArrayList<>();
        double std = getStandardDeviation(data);
        double mean = calcMean(data);

        for (Integer x : data) {
            double z = calcZvalue(x, mean, std);
            out.add(z);
        }
        return out;
        
    }




    public static void main(String[] args) throws IOException {

        DataAggregator agg = new DataAggregator();

        // The following prints show that:
        // The average number of evals increase with m
        // The std also increases at the same time
        // HOWEVER, for each value of m, the std of the resulting
        //          number of evaluations is relatively equal
        //          i.e. the Z-value has approx the same std
        // agg.aggregateM();
        //Map<Integer, List<Integer>> resPop = agg.aggregatePop();
        Map<Double, List<Integer>>  resD =  agg.aggregateD();
        for(Double key: resD.keySet()){
                System.out.println("D-value is equal to: " + key);
                 for(int i = 0; i<4; i++){
                     System.out.print(resD.get(key).get(i)+" ");
                }
                System.out.println();
        }

        Map<Integer,List<Integer>> resM = agg.aggregateM();


        System.out.println("Stuck count, Global count, eval avg, gen. avg");

        for (Integer key : resM.keySet()) {

            System.out.println(resM.get(key).get(3) + " ");
        }
            //System.out.println("M value: " + key);
            /*
            for (int i = 0; i < 4; i++) {
                System.out.print(resM.get(key).get(i) + " ");
            }
            System.out.println();

            /*
        }

        // System.out.println(agg.getZvalues(res.get(4)));
        /*
        int set1 = 1;
        int set2 = 4;
        int set3 = 8;
        int set4 = ;

        System.out.println("Z-value std:");
        System.out.println(agg.getStandardDeviationDouble(agg.getZvalues(res.get(set1))));
        System.out.println(agg.getStandardDeviationDouble(agg.getZvalues(res.get(set2))));
        System.out.println(agg.getStandardDeviationDouble(agg.getZvalues(res.get(set3))));
        System.out.println(agg.getStandardDeviationDouble(agg.getZvalues(res.get(set4))));

        System.out.println("Z-value mean:");
        System.out.println(agg.calcMeanDouble(agg.getZvalues(res.get(set1))));
        System.out.println(agg.calcMeanDouble(agg.getZvalues(res.get(set2))));
        System.out.println(agg.calcMeanDouble(agg.getZvalues(res.get(set3))));
        System.out.println(agg.calcMeanDouble(agg.getZvalues(res.get(set4))));

        System.out.println("Standard Deviations:");
        System.out.println(agg.getStandardDeviation(res.get(set1)));
        System.out.println(agg.getStandardDeviation(res.get(set2)));    
        System.out.println(agg.getStandardDeviation(res.get(set3)));
        System.out.println(agg.getStandardDeviation(res.get(set4)));

        System.out.println("MEANS:");
        System.out.println(agg.calcMean(res.get(set1)));
        System.out.println(agg.calcMean(res.get(set2)));
        System.out.println(agg.calcMean(res.get(set3)));
        System.out.println(agg.calcMean(res.get(set4)));

        */
        // System.out.println(res.get(16));
        // System.out.println(agg.getStandardDeviation(res.get(16)));
        //System.out.println(mAggregates.get(8)[0]);
        // Map<Integer, List<Integer>> resPop = agg.aggregatePop();
        // System.out.println(resPop.get(100).size());
        // Map<Double, List<Integer>> resD = agg.aggregateD();
        // System.out.println(resD.get(0.2).size());
        // Map<String, List<Integer>> resT = agg.aggregateType();
        // System.out.println(resT.get("Uniform").size());
        // System.out.println(agg.getStandardDeviation(resT.get("Uniform")));
        // Map<Double,int[]> dMap = agg.aggregateD();
        // System.out.println(dMap);
        // for(Double key: dMap.keySet()){
        //     System.out.println("D-value is equal to: " + key);
        //     for(int i = 0; i<4; i++){
        //         System.out.print(dMap.get(key)[i]+" ");
        //     }
        //     System.out.println();
        // }
        
    }
    
}
