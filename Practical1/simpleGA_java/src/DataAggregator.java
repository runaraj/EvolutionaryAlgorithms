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

    public static void main(String[] args) throws IOException {

        File directory = new File("experiments");

        //init new file names
        String gotStuck = "experiments/gotStuck.txt";
        String foundGlobalOptimum = "experiments/foundGlobalOptimum.txt";

        List<String> files = new ArrayList<>(Arrays.asList(gotStuck, foundGlobalOptimum));

        //int[] = [#gotStuck, #foundGlobal]
        Map<String, int[]> solutionCounts = new HashMap<>();


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

                    if (f.equals(gotStuck)) {
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

    }
    
}
