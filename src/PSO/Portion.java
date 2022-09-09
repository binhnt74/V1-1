package PSO;

public class Portion extends Particle {
    public Portion(int dim) {
        super(dim);
        minVelocity = -1;
        maxVelocity = 1;
    }
    @Override
    public void initPosition(){
        //Random rand = new Random(System.currentTimeMillis());
        //rand.setSeed(System.currentTimeMillis());
        //rand.setSeed(System.currentTimeMillis());
        double sum = 0;
        for (int i = 0; i < dimension-1; i++) {
            double p = 0;
            do {
                p = rand.nextDouble();

            } while (p+sum>=1D);
            sum += p/dimension;
            position[i] = p/dimension;
            //bestParticlePosition[i] = position[i];
        }
        position[dimension-1] = 1D-sum;
        for (int i = 0; i < dimension; i++) {
            bestParticlePosition[i] = position[i];
        }

    }
    @Override
    public void initVelocity(){
        //Random rand = new Random(System.currentTimeMillis());
        //rand.setSeed(System.currentTimeMillis());
        for (int i = 0; i < dimension; i++) {
            velocity[i] = rand.nextDouble();
        }
    }

    @Override
    public void init(){
        setFitness(FitnessFunctions.minMax);
        initVelocity();
        initPosition();
        bestParticleFitnessValue = fitness.apply(bestParticlePosition);
        //super.init();
    }
}
