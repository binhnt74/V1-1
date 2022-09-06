package PSO;

import java.util.function.Function;

public class FitnessFunctions {
    public static LoadAllocation[] loadAllocation;

    static Function<double[], Double> sphereFF = series -> {
        double result = 0;
        for (double x: series){
            result += x*x;
        }
        return result;
    };

    static Function<double[], Double> minMax = loads ->{
        double result = 0;
        if (loadAllocation == null){
            loadAllocation = new LoadAllocation[loads.length];
            for (int i = 0; i < loads.length; i++) {
                loadAllocation[i] = new LoadAllocation();
            }
        }
        for (int i = 0; i < loads.length; i++) {
            loadAllocation[i].portion = loads[i];
        }
        for (int i = 0; i < loadAllocation.length; i++) {
            //find max load
            if ((long)(loadAllocation[i].portion*loadAllocation[i].workload)*(1D/loadAllocation[i].bandwidth + 1D/loadAllocation[i].speed) > result)
                result = (long)(loadAllocation[i].portion*loadAllocation[i].workload)*(1D/loadAllocation[i].bandwidth + 1D/loadAllocation[i].speed);
        }

        return result;
    };

    public static LoadAllocation[] getLoadAllocation() {
        return loadAllocation;
    }

    public static void setLoadAllocation(LoadAllocation[] loadAllocation) {
        FitnessFunctions.loadAllocation = loadAllocation;
    }
}
