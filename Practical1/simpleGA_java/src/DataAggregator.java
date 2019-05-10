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
import java.util.HashMap;
import java.util.Map;


public class DataAggregator {

    File directory = new File("experiments");

    //init new file names
    String gotStuck = "experiments/gotStuck.txt";
    String foundGlobalOptimum = "experiments/foundGlobalOptimum.txt";
    List<String> files = new ArrayList<>(Arrays.asList(gotStuck, foundGlobalOptimum));




    /*
    public void aggregateM() {
        int stuckCount = 0;
        int foundGlobalCount = 0;
        int evaluationsTotal = 0;
        int generationsTotal = 0;
        
        
        //int[] = [stuckCount, foundGlobalCount, evalTotal, genTotal]
        Map<Integer, int[]> counters = new HashMap<>();


        BufferedReader reader;
        for (String f : files) {
            try {
                reader = new BufferedReader(new FileReader(f));
                String line = reader.readLine();
                while (line != null) {
                    String[] variableConfig = line.split("_");
                    String pop = variableConfig[1];
                    String m = variableConfig[2];
                    String k = variableConfig[3];
                    String d = variableConfig[4];
                    String type = variableConfig[5];

                    int[] count = {0,0};

                    if (f.equals(this.gotStuck)) {
                        count[0] = 1;
                    } else {
                        count[1] = 1;
                    }
                    
                    String solutionsCountKey = pop+m+k+d+type;
                    if (solutionCounts.containsKey(solutionsCountKey)){
                        solutionCounts.get(solutionsCountKey)[0] += count[0];
                        solutionCounts.get(solutionsCountKey)[1] += count[1];
                        // int[] res = add(solutionCounts.get(solutionsCountKey), count);
                        // solutionCounts.replace(solutionsCountKey, res);
                    } else {
                        solutionCounts.put(solutionsCountKey, count);
                    }
    
                    line = reader.readLine();
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (String key : solutionCounts.keySet()) {
            System.out.println("Key: " + key);
            System.out.println(solutionCounts.get(key)[0] + " " + solutionCounts.get(key)[1]);
            System.out.println();
        }


    }*/

    public Map<Double,int[]> aggregateD() {
        int stuckCount = 0;
        int foundGlobalCount = 0;
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
        return counters;
    }

    
    public void aggregatePop() {
        
    }
    
    public void aggregateType() {
        
    }

    public void readGotStuck() {

    }

    public void readFoundGlobalOptimum() {
        
    }
    public static void main(String[] args) throws IOException {
        DataAggregator agg = new DataAggregator();
        Map<Double,int[]> dMap = agg.aggregateD();
        System.out.println(dMap);
        for(Double key: dMap.keySet()){
            System.out.println("D-value is equal to: " + key);
            for(int i = 0; i<4; i++){
                System.out.print(dMap.get(key)[i]+" ");
            }
            System.out.println();
        }
        
    }
    
}
