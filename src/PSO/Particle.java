package PSO;

import MyUtils.Logging;

import java.sql.Time;
import java.util.Random;
import java.util.Timer;
import java.util.function.Function;
import java.util.logging.Level;

public class Particle {
    public static Random rand = new Random();
    int dimension;
    double[] velocity;
    double[] position;
    double[] bestParticlePosition;
    double bestParticleFitnessValue;
    double minVelocity;
    double maxVelocity;
    static Function<double[], Double> fitness = FitnessFunctions.sphereFF;

    public static Function<double[], Double> getFitness() {
        return fitness;
    }

    public static void  setFitness(Function<double[], Double> fit) {
        fitness = fit;
    }

    public Particle(int dim){
        dimension = dim;
        velocity = new double[dimension];
        position = new double[dimension];
        bestParticlePosition = new double[dimension];
        minVelocity = -100;
        maxVelocity = 100;
        bestParticleFitnessValue = 10;
    }

    public static void printArray(double[] arr){
//        Logging.logger.log(Level.INFO, "(");
        StringBuilder st = new StringBuilder("(");
        //System.out.print("(");
        for (int i = 0; i < arr.length-1; i++) {
            //Logging.logger.log(Level.INFO, arr[i]+",");
            st.append(arr[i]).append(",");
            //System.out.print(arr[i]+",");
        }
        st.append(arr[arr.length - 1]).append(")");
//        Logging.logger.log(Level.INFO, st);
        System.out.println(st);
    }

    public double[] getVelocity() {
        return velocity;
    }

    public void setVelocity(double[] velocity) {
        this.velocity = velocity;
    }

    public double[] getBestParticlePosition() {
        return bestParticlePosition;
    }

    public void setBestParticlePosition(double[] bestParticlePosition) {
        this.bestParticlePosition = bestParticlePosition;
    }

    public void setVelocity(int ind, double velo) {
        velocity[ind] = velo;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public void setPosition(int ind, double pos) {
        this.position[ind] = pos;
    }

    public void copyBestPosition(double[] newBestPosition){
        for (int i = 0; i < dimension; i++) {
            bestParticlePosition[i] = newBestPosition[i];

        }
    }

    public double getBestParticleFitnessValue() {
        return bestParticleFitnessValue;
    }

    public void setBestParticleFitnessValue(double bestParticleFitnessValue) {
        this.bestParticleFitnessValue = bestParticleFitnessValue;
    }

    public double getMinVelocity() {
        return minVelocity;
    }

    public void setMinVelocity(double minVelocity) {
        this.minVelocity = minVelocity;
    }

    public double getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(double maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public void initVelocity(){
        //Random rand = new Random(System.currentTimeMillis());
        //rand.setSeed(System.currentTimeMillis());
        for (int i = 0; i < dimension; i++) {
            velocity[i] = minVelocity + rand.nextDouble()*(maxVelocity-minVelocity);
        }
    }

    public void initPosition(){
        //Random rand = new Random(System.currentTimeMillis());
        //rand.setSeed(System.currentTimeMillis());
        //rand.setSeed(System.currentTimeMillis());
        for (int i = 0; i < dimension; i++) {
            position[i] = minVelocity + rand.nextDouble()*(maxVelocity-minVelocity);
            bestParticlePosition[i] = position[i];
        }

    }

    public void init(){
        initVelocity();
        initPosition();
        bestParticleFitnessValue = fitness.apply(bestParticlePosition);
    }

    public void printPosition(){
        System.out.println("Position = (");
//        for (int i = 0; i < dimension-1; i++) {
//            System.out.print(position[i]+",");
//        }
//        System.out.println(position[dimension-1] + ")");
        printArray(position);
    }
}
