package evolutionaryneuralnetwork;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by MagnusSnorri on 31/05/2014.
 * This class is responsible for the evolution of the network over different generations where new generation is based
 * on the fittest individuals from the previous generation.
 */
public class Evolution {
    private double cloningProbability;
    private double mutationProbability;
    private int generationSize;
    private int numberOfIndividualsForReproduction;
    private boolean minimize;

    /**
     * The constructor for the evolution class
     * @param cloningProbability the probability of individual being cloned
     * @param mutationProbability the probability of individual genes or individual being mutated
     * @param generationSize the number of individuals in each generation
     * @param percentForReproduction the percent of individuals chosen for the fittest individuals
     * @param minimize true if the network is minimizing fitness value, false if maximizing
     */
    public Evolution(double cloningProbability, double mutationProbability, int generationSize, double percentForReproduction, boolean minimize){
        this.cloningProbability = cloningProbability;
        this.mutationProbability = mutationProbability;
        this.generationSize = generationSize;
        this.minimize = minimize;
        numberOfIndividualsForReproduction = (int)(generationSize*percentForReproduction);
    }

    /**
     * Takes the list of individuals in the current population, selects the fittest individuals for reproduction and
     * produces new generation of new individuals
     * @param currentPopulation list of all the individuals in current population
     * @return list of all the individuals in the new population after reproduction
     */
    public ArrayList<Chromosome> generateNewPopulation(ArrayList<Chromosome> currentPopulation){
        ArrayList<Chromosome> newPopulation = new ArrayList<Chromosome>();
        if(minimize){
            Collections.sort(currentPopulation);
        }else {
            Collections.sort(currentPopulation, Collections.reverseOrder());
        }
        System.out.println("Best fitness value: " + currentPopulation.get(0).getFitnessValue());
        List<Chromosome> fittestIndividuals = new ArrayList<Chromosome>();
        int uniqueIndividualsFound = 0;
        double lastFitnessValue = 0.0;
        while(uniqueIndividualsFound<numberOfIndividualsForReproduction){
            if(lastFitnessValue != currentPopulation.get(0).getFitnessValue()){
                fittestIndividuals.add(currentPopulation.get(0));
                lastFitnessValue = currentPopulation.get(0).getFitnessValue();
                uniqueIndividualsFound++;
            }
            currentPopulation.remove(0);
        }
        double[] cumulativeWeights = getCumulativeWeights(fittestIndividuals);
        Chromosome individualA;
        Chromosome individualB;
        for(int i = 0; i<generationSize;i++){
            individualA = fittestIndividuals.get(indexOfSelectedIndividual(cumulativeWeights, Math.random()));
            individualB = fittestIndividuals.get(indexOfSelectedIndividual(cumulativeWeights, Math.random()));
            while(individualA.equals(individualB)){
                individualB = fittestIndividuals.get(indexOfSelectedIndividual(cumulativeWeights, Math.random()));
            }
            Chromosome beforeMutation = crossoverChromosomes(individualA,individualB);
            Chromosome afterMutation = mutate(beforeMutation);
            newPopulation.add(afterMutation);
        }
        return newPopulation;
    }

    /**
     * This function constructs an array of weight percentage interval of the fittest individuals starting in 0 and
     * ending in 100.
     * @param fittestIndividuals list of the fittest individuals chosen for reproduction
     * @return the cumulativeWeight array
     */
    private double[] getCumulativeWeights(List<Chromosome> fittestIndividuals){
        double weightSum = 0;
        for(Chromosome c: fittestIndividuals){
            if(minimize){
                weightSum += 1/c.getFitnessValue();
            }else {
                weightSum += c.getFitnessValue();
            }
        }
        double[] normalisedWeights = new double[numberOfIndividualsForReproduction];
        for(int i = 0; i<numberOfIndividualsForReproduction;i++){
            if(minimize) {
                normalisedWeights[i] =  (1.0/fittestIndividuals.get(i).getFitnessValue())/weightSum;
            }else {
                normalisedWeights[i] = fittestIndividuals.get(i).getFitnessValue() / weightSum;
            }
        }
        double[] cumulativeWeights = new double[numberOfIndividualsForReproduction];
        cumulativeWeights[0] = normalisedWeights[0];
        for(int i = 1; i<numberOfIndividualsForReproduction;i++){
            cumulativeWeights[i] = cumulativeWeights[i-1] + normalisedWeights[i];
        }
        return cumulativeWeights;
    }

    /**
     * This function finds the index of the selected individual based on the cumulative weight array and a random number
     * @param cumulativeWeights array of double numbers representing the weight interval percentage of each individual
     *                          starting at 0 and ending in 100 across the whole array
     * @param r random number from 0 to 1
     * @return the index of the interval containing r in the cumulativeWeights array
     */
    private int indexOfSelectedIndividual(double[] cumulativeWeights, double r){
        int positionOfFirstIndividual = 0;
        for(int i = 1; i<numberOfIndividualsForReproduction;i++){
            if(cumulativeWeights[i-1] < r && r<cumulativeWeights[i]){
                positionOfFirstIndividual = i;
            }
        }
        return positionOfFirstIndividual;
    }

    /**
     * This function takes in two chromosomes and creates a new chromosome based on the crossover from the two inputs
     * @param individualA Chromosome of individual a
     * @param individualB Chromosome of individual b
     * @return New chromosome which is based on crossover of all the genes of individuals a and b
     */
    private Chromosome crossoverChromosomes(Chromosome individualA, Chromosome individualB){
        if(Math.random() < cloningProbability){
            if(individualA.getFitnessValue() > individualB.getFitnessValue()){
                return individualA;
            }else {
                return individualB;
            }
        }
        ArrayList<LinkGene> individualALinks = individualA.getLinkGenes();
        ArrayList<LinkGene> individualBLinks = individualB.getLinkGenes();
        ArrayList<LinkGene> newIndividualLinks = new ArrayList<LinkGene>();
        for(int i = 0; i< individualALinks.size();i++){
            newIndividualLinks.add(crossoverLinkGenes(individualALinks.get(i), individualBLinks.get(i)));
        }
        return new Chromosome(individualA.getNetworkGene(),newIndividualLinks);

    }

    /**
     * This function takes in two link genes and returns a new linkGene based on their crossover
     * @param a linkGene from individual a
     * @param b linkGene from individual b
     * @return new LinkGene which is based on crossover from genes a and b
     */
    private LinkGene crossoverLinkGenes(LinkGene a, LinkGene b){
        double weightA = a.getWeight();
        double weightB = b.getWeight();

        long weightALong = Double.doubleToRawLongBits(weightA);
        long weightBLong = Double.doubleToRawLongBits(weightB);
        long mask = -1L; // all bits set to 1
        double newWeight;
        do{
            int crossOverPoint = randInt(0, Long.SIZE);
            long combined;
            // treat special cases because of modulo Long.SIZE of second parameter of shifting operations
            if (crossOverPoint == 0) {
                combined = weightBLong;
            } else if (crossOverPoint == Long.SIZE) {
                combined = weightALong;
            } else {
                combined = (weightALong & (mask << (Long.SIZE - crossOverPoint))) |
                        (weightBLong & (mask >>> crossOverPoint));
            }

            newWeight = Double.longBitsToDouble(combined);
        } while(newWeight == Double.NaN || newWeight == Double.POSITIVE_INFINITY || newWeight == Double.NEGATIVE_INFINITY);
        boolean newActivation;
        if(a.isActivated() == b.isActivated()){
            newActivation = a.isActivated();
        }else {
            if(Math.random() > 0.5){
                newActivation = a.isActivated();
            }
            else {
                newActivation = b.isActivated();
            }
        }
        return new LinkGene(a.getStartLayer(),a.getStartNode(),a.getEndNode(),newWeight, newActivation);
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt(int min, int max) {

        // Usually this should be a field rather than a method variable so
        // that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    /**
     * This function mutates the genes in a individual
     * @param individual chromosome of all the genes in the individual
     * @return chromosome of the new mutated individual
     */
    private Chromosome mutate(Chromosome individual){
        ArrayList<LinkGene> linkGenes = individual.getLinkGenes();
        ArrayList<LinkGene> newLinkGenes = new ArrayList<LinkGene>();
        for(LinkGene l: linkGenes) {
            double newWeight = l.getWeight();
            if(Math.random() < mutationProbability) {
                newWeight = l.getWeight() * Math.random() * 2;
                if(Math.random() < 0.2){
                    newWeight = -newWeight;
                }
            }
            boolean newActivation = l.isActivated();
            if (Math.random() < mutationProbability) {
                newActivation = !l.isActivated();
            }
            LinkGene newGene = new LinkGene(l.getStartLayer(),l.getStartNode(),l.getEndNode(),newWeight,newActivation);
            newLinkGenes.add(newGene);
        }
        return new Chromosome(individual.getNetworkGene(),newLinkGenes);
    }
}