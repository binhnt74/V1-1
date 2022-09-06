package PSO;

import java.util.function.Function;

public class PSOGeneral {
    //Particle[] particles;
    // hyper parameters
    static double w = 0.729;    //inertia
    static double c1 = 1.49445; //cognitive (particle)
    static double c2 = 1.49445; //social (swarm)
    public static void arrayCopy(double [] source, double [] dest){
        for (int i = 0; i < source.length; i++) {
            dest[i] = source[i];
        }
    }

    static public double[]  pso(int numberOfIteration, Swarm swarm, Function<double[], Double> fitnessFunc){
        if (swarm.particles.length == 0) return null;
        double[] best_position = new double[swarm.particles[0].getDimension()];
        arrayCopy(swarm.particles[0].position, best_position);
        double bestValue = swarm.particles[0].getBestParticleFitnessValue();
        for (int i = 1; i < swarm.particles.length; i++) {
            if (swarm.particles[i].getBestParticleFitnessValue()<bestValue){
                //bestValue = swarm.particles[i].getBestParticleFitnessValue();
                arrayCopy(swarm.particles[i].position, best_position);
                bestValue = fitnessFunc.apply(best_position);
//                for (int j = 0; j < swarm.particles[i].getDimension(); j++) {
//                    best_position[j] = swarm.particles[i].getPosition()[j];
//                }
            }

        }
        System.out.println("Init best position:");
        Particle.printArray(best_position);
        System.out.println("Init best value: "+fitnessFunc.apply(best_position));

        for (int i = 0; i < numberOfIteration; i++) {
            int dim = swarm.particles[0].getDimension();
            //Particle p;
            for (Particle p:swarm.particles) {
                //p = particles[j];
                double posSum = 0;
                for (int k = 0; k < dim-1; k++) {
                    double velo;
                    double v = p.velocity[k];
                    double pos = p.position[k];

                    velo = w * p.velocity[k] +
                            c1 * Particle.rand.nextDouble()*(p.getBestParticlePosition()[k]-p.position[k]) +
                            c2 * Particle.rand.nextDouble()*(best_position[k]-p.position[k]);
                    if (velo<p.getMinVelocity()) velo = p.getMinVelocity();
                    if (velo>p.getMaxVelocity()) velo = p.getMaxVelocity();
                    p.setVelocity(k,velo);

                    //System.out.println("   Old position: ");
//                    Particle.printArray(p.position);
                    p.position[k] += p.velocity[k];
                    posSum += p.position[k];
                    //System.out.println("   New position: ");
//                    Particle.printArray(p.position);


                }
                p.position[dim-1] = 1- posSum;
                //update local best
                //if (Swarm.fitnessFunction(particles[j].getPosition()) < particles[j].getBestParticleFitnessValue()){
                if (fitnessFunc.apply(p.getPosition()) < p.getBestParticleFitnessValue()){
                    p.setBestParticleFitnessValue(Swarm.fitnessFunction(p.getPosition()));
                    p.copyBestPosition(p.getPosition());

                }
                if (fitnessFunc.apply(p.getPosition()) < bestValue ){
                    bestValue = fitnessFunc.apply(p.getPosition());
                    for (int k = 0; k < dim; k++) {
                        best_position[k] = p.getPosition()[k];
                    }
                }

            }
            System.out.println("Best value so far : "+fitnessFunc.apply(best_position));
            System.out.print("   best_position = ");
            Particle.printArray(best_position);
//
//            for (int j = 0; j < dim-1; j++) {
//                System.out.print(best_position[j] + ", ");
//            }
//            System.out.println(best_position[dim-1]+ ")");

        }
        return best_position;
    }
}
