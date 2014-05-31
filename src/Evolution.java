import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by MagnusSnorri on 31/05/2014.
 */
public class Evolution {
    private double cloningProbability;
    private double mutationProbability;
    private int generationSize;
    private double percentForReproduction;
    private int numberOfIndividualsForReproduction;
    private boolean minimize;

    public Evolution(double cloningProbability, double mutationProbability, int generationSize, double percentForReproduction, boolean minimize){
        this.cloningProbability = cloningProbability;
        this.mutationProbability = mutationProbability;
        this.generationSize = generationSize;
        this.percentForReproduction = percentForReproduction;
        this.minimize = minimize;
        numberOfIndividualsForReproduction = (int)(generationSize*percentForReproduction);
    }

    public ArrayList<Chromosome> generateNewPopulation(ArrayList<Chromosome> currentPopulation){
        ArrayList<Chromosome> newPopulation = new ArrayList<Chromosome>();
        if(minimize){
            Collections.sort(currentPopulation);
        }else {
            Collections.sort(currentPopulation, Collections.reverseOrder());
        }
        System.out.println("Best fitness value: " + currentPopulation.get(0).getFitnessValue());
        List<Chromosome> fittestIndividuals = currentPopulation.subList(0,numberOfIndividualsForReproduction);
        double[] cumulativeWeights = getCumulativeWeights(fittestIndividuals);
        Chromosome individualA;
        Chromosome individualB;
        for(Chromosome c: currentPopulation){
            individualA = fittestIndividuals.get(indexOfSelectedIndividual(cumulativeWeights, Math.random()));
            individualB = fittestIndividuals.get(indexOfSelectedIndividual(cumulativeWeights, Math.random()));
            int counter = 0;
            while(individualA.equals(individualB)){
                individualB = fittestIndividuals.get(indexOfSelectedIndividual(cumulativeWeights, Math.random()));
                counter++;
                if(counter > 100){
                    counter = counter +1;
                }
            }
            Chromosome beforeMutation = crossoverChromosomes(individualA,individualB);
            Chromosome afterMutation = mutate(beforeMutation);
            newPopulation.add(afterMutation);
        }
        return newPopulation;
    }

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
    private int indexOfSelectedIndividual(double[] cumulativeWeights, double r){
        int positionOfFirstIndividual = 0;
        for(int i = 1; i<numberOfIndividualsForReproduction;i++){
            if(cumulativeWeights[i-1] < r && r<cumulativeWeights[i]){
                positionOfFirstIndividual = i;
            }
        }
        return positionOfFirstIndividual;
    }
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
    private LinkGene crossoverLinkGenes(LinkGene a, LinkGene b){
        Random r = new Random();
        double weightA = a.getWeight();
        double weightB = b.getWeight();
        double interval = Math.abs(weightA-weightB);
        double newWeight = (weightA+weightB)*0.5 + r.nextGaussian()*interval*2;
      //  String binaryA = "0b" + Long.toBinaryString(Double.doubleToRawLongBits(a.getWeight()));
      //  String binaryB = "0b" + Long.toBinaryString(Double.doubleToRawLongBits(b.getWeight()));
     //   int crossOverPoint = randInt(0, binaryA.length());
     //   String newBinary = binaryA.substring(0,crossOverPoint) + binaryB.substring(crossOverPoint+1,binaryB.length());
    //    double newWeight = Double.longBitsToDouble(new BigInteger(newBinary, 2).longValue());
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

    private Chromosome mutate(Chromosome individual){
        ArrayList<LinkGene> linkGenes = individual.getLinkGenes();
        ArrayList<LinkGene> newLinkGenes = new ArrayList<LinkGene>();
        for(LinkGene l: linkGenes) {

            /* String binaryWeight = "0b" + Long.toBinaryString(Double.doubleToRawLongBits(l.getWeight()));
            char[] charArray = binaryWeight.toCharArray();
            for (char c : charArray) {
                if (Math.random() < mutationProbability) {
                    c = flipBit(c);
                }
            }
            l.setWeight(Double.longBitsToDouble(new BigInteger(charArray.toString(), 2).longValue()));
            */
            double newWeight = l.getWeight();
            if(Math.random() < mutationProbability) {
                newWeight = l.getWeight() * Math.random() * 2;
                if(Math.random() < 0.2){
                    newWeight = -newWeight;

                }
                l.setWeight(newWeight);
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

    private char flipBit(char c){
        if(c == 1){
            c = 0;
        }else {
            c = 1;
        }
        return c;
    }

}
