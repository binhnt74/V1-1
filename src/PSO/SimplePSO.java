package PSO;

public class SimplePSO {
    //Sphere function
    public static double fitnessFunction1 (double [] series){
        double result = 0;
        for (double x: series){
            result += x*x;
        }
        return result;
    }

    public static void main(String[] args){
//        double[] s = {1, 2, 3, 4, 5};
//        System.out.println("F1 = "+fitnessFunction1(s));
        Swarm swarm = new Swarm();
//        Particle[] p = new Particle[50];
//        swarm.setParticles(p);
        int dimension = 5;
        int N = 100;
        Particle[] p = new Portion[N];
        for (int i = 0; i < N; i++) {
            p[i] = new Portion(dimension);

        }

        swarm.setParticles(p);

        swarm.InitSwampOfParticles(dimension,-0.1,0.1);
        swarm.printPosition();

        LoadAllocation[] loadAllocation = new LoadAllocation[N];
        for (int i = 0; i < N; i++) {
            loadAllocation[i] = new LoadAllocation();
            loadAllocation[i].initRandom();
        }

        FitnessFunctions.setLoadAllocation(loadAllocation);

        //swarm.pso(100, FitnessFunctions.sphereFF);
        PSOGeneral.pso(50, swarm, FitnessFunctions.minMax);
    }

}
