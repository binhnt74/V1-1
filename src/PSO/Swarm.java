package PSO;

import java.util.Random;
import java.util.function.Function;

public class Swarm {
//    int number;  //number of particles
    Particle[] particles;
    // hyper parameters

    public Particle[] getParticles() {
        return particles;
    }

    public void setParticles(Particle[] particles) {
        this.particles = particles;
    }
//    static double w = 0.729;    //inertia
//    static double c1 = 1.49445; //cognitive (particle)
//    static double c2 = 1.49445; //social (swarm)

    public Swarm(){
//        this.number = 0;
        //particles = new Particle[number];
    }

    //Sphere function
    public static double fitnessFunction (double [] series){
        double result = 0;
        for (double x: series){
            result += x*x;
        }
        return result;
    }


    public void InitSwampOfParticles(int dimension, double minVelo, double maxVelo){
//        this.number = number;
        //particles = new Particle[number];
//        for (int i = 0; i < particles.length; i++) {
//            particles[i] = new Particle(dimension);
//        }

        for (Particle p: particles){

            p.setMinVelocity(minVelo);
            p.setMaxVelocity(maxVelo);
            p.setDimension(dimension);
        }
        //init velocity & position of particles
        for (Particle p: particles){
            p.init();
        }

    }

    public void printPosition(){
        for (Particle p:particles){
            p.printPosition();
        }
    }
}
