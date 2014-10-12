package evolutionaryneuralnetwork;

import java.util.ArrayList;

/**
 * Created by MagnusSnorri on 30/05/2014.
 */
class Test {

    private static int generationSize = 200;

    public static void main(String[] args) {
        //Basic test that creates network with 5 inputs, 8 nodes in the hidden layer and 3 outputs.
        //The network is then fed random numbers on some interval and the goal is to minimize the total distance
        //summed over all the outputs and their target number.
        //In the first generation the best fitness value is about 4 and in the last generation the best fitness value
        //is 0.1 so this shows that the evolutionary neural network converges on better solution over different
        //generations.
        Evolution evolution = new Evolution(0.2, 0.001, generationSize, 0.1, true);
        int numberOfGenerations = 100;
        int numberOfActions = 10;
        ArrayList<Chromosome> population = initializePopulation();
        for(int i = 0; i<numberOfGenerations;i++){
            for(int j = 0; j<generationSize;j++) {
                NeuralNetwork n = new NeuralNetwork(population.get(j));
                double fitnessSum = 0;
                for (int k = 0; k < numberOfActions; k++) {
                    fitnessSum += fitnessFunction(n.evaluateNeuralNetwork(generateInputs()));
                }
                double averageFitness = fitnessSum/numberOfActions;
                population.get(j).setFitnessValue(averageFitness);
            }
            population = evolution.generateNewPopulation(population);
        }
        System.out.println("Done");
    }

    public static ArrayList<Chromosome> initializePopulation(){
        ArrayList<Chromosome> population = new ArrayList<Chromosome>();
        for(int i = 0; i < generationSize; i++){
            NeuralNetwork neuralNetwork = new NeuralNetwork(5, new int[]{8}, 3);
            population.add(neuralNetwork.generateChromosome());
        }
        return population;
    }

    public static double fitnessFunction(double[] outputs){
        double[] target = new double[]{-4.5, 3.2,0.2};
        double distance = 0;
        for(int i = 0; i<outputs.length;i++) {
            distance += Math.abs(outputs[i] - target[i]);
        }
        return distance;
    }
    public static double[] generateInputs(){
        double[] inputs = new double[5];
        inputs[0] = Math.random()*5;
        inputs[1] = Math.random()*2;
        inputs[2] = Math.random()*4 - 3;
        inputs[3] = Math.random() - 1;
        inputs[4] = Math.random()*3 - 1;
        return inputs;
    }

}