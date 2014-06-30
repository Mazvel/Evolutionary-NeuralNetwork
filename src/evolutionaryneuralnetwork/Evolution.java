package evolutionaryneuralnetwork;

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
    private double networkArchitectureMutationProbability;
    private boolean minimize;

    public Evolution(double cloningProbability, double mutationProbability, int generationSize, double percentForReproduction, boolean minimize, double networkArchitectureMutationProbability){
        this.cloningProbability = cloningProbability;
        this.mutationProbability = mutationProbability;
        this.generationSize = generationSize;
        this.percentForReproduction = percentForReproduction;
        this.minimize = minimize;
        this.networkArchitectureMutationProbability = networkArchitectureMutationProbability;
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
        currentPopulation.get(0).printNetworkStructure();
        List<Chromosome> fittestIndividuals = new ArrayList<Chromosome>();
        int uniqueIndividualsFound = 0;
        double lastFitnessValue = 0.0;
        while(uniqueIndividualsFound<numberOfIndividualsForReproduction){
            //System.out.println("Stuck in first while...");
            if(lastFitnessValue != currentPopulation.get(0).getFitnessValue()){
            fittestIndividuals.add(currentPopulation.get(0));
            lastFitnessValue = currentPopulation.get(0).getFitnessValue();
            uniqueIndividualsFound++;
            }
            currentPopulation.remove(0);
        }
      //  System.out.println("Fitness of best individuals!");
     //   System.out.println("----------------------------");
        for(Chromosome c: fittestIndividuals){
       //     System.out.println("Fitness: " + c.getFitnessValue());
        }
    //    System.out.println("----------------------------");
        double[] cumulativeWeights = getCumulativeWeights(fittestIndividuals);
        Chromosome individualA;
        Chromosome individualB;
        for(int i = 0; i<generationSize;i++){
            individualA = fittestIndividuals.get(indexOfSelectedIndividual(cumulativeWeights, Math.random()));
            individualB = fittestIndividuals.get(indexOfSelectedIndividual(cumulativeWeights, Math.random()));
            while(individualA.getFitnessValue() == individualB.getFitnessValue()){
                individualB = fittestIndividuals.get(indexOfSelectedIndividual(cumulativeWeights, Math.random()));
                //System.out.println("Stuck in while");
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
        //With some probability we clone the individual. We always clone the better individual.
        if(Math.random() < cloningProbability){
            if(individualA.getFitnessValue() > individualB.getFitnessValue()){
                return individualA;
            }else {
                return individualB;
            }
        }
        ArrayList<LinkGene> newIndividualLinks = new ArrayList<LinkGene>();
        int numberOfInputs = individualA.getNetworkGene().getNumberOfInputNodes();
        int numberOfOutputs = individualA.getNetworkGene().getNumberOfOutputNodes();
        int[] individualAHiddenLayers = individualA.getNetworkGene().getNumberOfHiddenNodes();
        int[] individualBHiddenLayers = individualB.getNetworkGene().getNumberOfHiddenNodes();
        int[] newIndividualHiddenLayers = newIndividualArchitecture(individualAHiddenLayers, individualBHiddenLayers);
        ArrayList<LinkGene> individualALinks = individualA.getLinkGenes();
        ArrayList<LinkGene> individualBLinks = individualB.getLinkGenes();
        int numberOfALinks = individualALinks.size();
        int numberOfBLinks = individualBLinks.size();
        int counterA = 0;
        int counterB = 0;
        int newIndividualNumberOfLinks = 0;
        for(int i = 0; i < newIndividualHiddenLayers.length; i++){
            if(i == 0) {
                newIndividualNumberOfLinks += numberOfInputs*newIndividualHiddenLayers[i];
            }
            else{
                newIndividualNumberOfLinks += newIndividualHiddenLayers[i-1]*newIndividualHiddenLayers[i];
            }
        }
        newIndividualNumberOfLinks += numberOfOutputs*newIndividualHiddenLayers[newIndividualHiddenLayers.length - 1];
        int layer = 0;
        int startNode = 0;
        int endNode = 0;
        int index = 0;
        boolean addA;
        boolean addB;
        Random r = new Random();

        int [] newIndividualAllLayers = new int[newIndividualHiddenLayers.length+2];
        newIndividualAllLayers[0] = numberOfInputs;
        newIndividualAllLayers[newIndividualAllLayers.length-1] = numberOfOutputs;
        for(int i = 1; i < newIndividualAllLayers.length - 1; i++){
            newIndividualAllLayers[i] = newIndividualHiddenLayers[i-1];
        }
        for(int i = 0; i < newIndividualNumberOfLinks; i++){
            if(i >= index + newIndividualAllLayers[layer]*newIndividualAllLayers[layer+1]){
                index += newIndividualAllLayers[layer]*newIndividualAllLayers[layer+1];
                layer++;
                startNode = 0;
                endNode = 0;
            }
            if(counterA >= numberOfALinks){
                addA = false;
            }else {
                if (individualALinks.get(counterA).getStartLayer() == layer
                        && individualALinks.get(counterA).getStartNode() == startNode
                        && individualALinks.get(counterA).getEndNode() == endNode) {
                    addA = true;
                    counterA++;
                } else {
                    addA = false;
                }
            }
            if(counterB >= numberOfBLinks) {
                addB = false;
            }else{
                if (individualBLinks.get(counterB).getStartLayer() == layer
                        && individualBLinks.get(counterB).getStartNode() == startNode
                        && individualBLinks.get(counterB).getEndNode() == endNode) {
                    addB = true;
                    counterB++;
                } else {
                    addB = false;
                }
            }
            if(addA && addB){
                newIndividualLinks.add(crossoverLinkGenes(individualALinks.get(counterA-1), individualBLinks.get(counterB-1)));
            }else if(addA){
                newIndividualLinks.add(individualALinks.get(counterA-1));
            }else if(addB){
                newIndividualLinks.add(individualBLinks.get(counterB-1));
            }else {
                double weight = Math.random()*3 -0.5*3;
                boolean activated = r.nextBoolean();
                newIndividualLinks.add(new LinkGene(layer,startNode,endNode,weight,activated));
            }
            endNode++;
            if(endNode >= newIndividualAllLayers[layer + 1]){
                endNode = 0;
                startNode++;
            }
        }
        ArrayList<NodeGene> newIndividualNodeGenes = new ArrayList<NodeGene>();
        int nodeCounterA = 0;
        int nodeCounterB = 0;
        int numberOfNodesA = 0;
        int numberOfNodesB = 0;
        for(int i: individualAHiddenLayers){
            numberOfNodesA += i;
        }
        numberOfNodesA += numberOfInputs + numberOfOutputs;
        for(int i: individualBHiddenLayers){
            numberOfNodesB += i;
        }
        numberOfNodesB += numberOfInputs + numberOfOutputs;
        ArrayList<NodeGene> individualANodeGenes = individualA.getNodeGenes();
        ArrayList<NodeGene> individualBNodeGenes = individualB.getNodeGenes();
        for(int i = 0; i < newIndividualAllLayers.length;i++){
            for(int j = 0; j < newIndividualAllLayers[i]; j++){
                if(nodeCounterA >= numberOfNodesA){
                    addA = false;
                }else {
                    if (individualANodeGenes.get(nodeCounterA).getLayer() == i &&
                            individualANodeGenes.get(nodeCounterA).getIndex() == j) {
                        addA = true;
                        nodeCounterA++;
                    } else {
                        addA = false;
                    }
                }
                if(nodeCounterB >= numberOfNodesB){
                    addB = false;
                }else {
                    if (individualBNodeGenes.get(nodeCounterB).getLayer() == i &&
                            individualBNodeGenes.get(nodeCounterB).getIndex() == j) {
                        addB = true;
                        nodeCounterB++;
                    } else {
                        addB = false;
                    }
                }
                if(addA && addB){
                    newIndividualNodeGenes.add(crossoverNodeGenes(individualANodeGenes.get(nodeCounterA-1), individualBNodeGenes.get(nodeCounterB-1)));
                }else if(addA){
                    newIndividualNodeGenes.add(individualANodeGenes.get(nodeCounterA-1));
                }else if(addB){
                    newIndividualNodeGenes.add(individualBNodeGenes.get(nodeCounterB-1));
                }else {
                    int transferFunction = randInt(1,new Node().getNumberOfFunctions());
                    newIndividualNodeGenes.add(new NodeGene(i,j,transferFunction));
                }
            }
        }


        return new Chromosome(new NetworkGene(numberOfInputs,newIndividualHiddenLayers, numberOfOutputs),newIndividualLinks, newIndividualNodeGenes);

    }
    private int[] newIndividualArchitecture(int[] individualA, int[] individualB){
        int[] newIndividual;
        //First determine the number of hidden layers in the new individual
        //With some probability we mutate number of hidden layers.
        if(Math.random() < networkArchitectureMutationProbability){
          //  System.out.println("Mutating number of layers!");
            //If either of the individuals has only one hidden layer then we must add hidden layer
            newIndividual = new int[addOrRemove(individualA.length,individualB.length, 1)];
        }
        else{
            //Now we will randomize the number of hidden layers based on the interval [hiddenLayersA;hiddenLayersB]
            if(individualA.length > individualB.length) {
                newIndividual = new int[randInt(individualB.length, individualA.length)];
            }else {
                newIndividual = new int[randInt(individualA.length, individualB.length)];
            }

        }
        //Now we know the number of hidden layers our new individual will have.
        for(int i = 0; i < newIndividual.length; i++){
            if(Math.random() < networkArchitectureMutationProbability){
            //    System.out.println("Mutating number of nodes!");
                //First we check if this layer exists in both of the parents
                if(i+1 > individualA.length || i+1 > individualB.length){
                    //One of the individuals does not have this layer
                    //Now there are three possibilities.
                    if(i+1 > individualA.length && i+1 > individualB.length){
                        //Neither has this layer. Let's RANDOM!
                        newIndividual[i] = randInt(2,6);
                    }else if(i+1 > individualA.length){
                        //Individual A does not have this.
                        if(individualB[i] == 2){
                            newIndividual[i] = individualB[i] +1;
                        }else {
                            if (Math.random() < 0.5) {
                                newIndividual[i] = individualB[i] - 1;
                            } else {
                                newIndividual[i] = individualB[i] + 1;
                            }
                        }
                    }else if(i+1 > individualB.length){
                        //Individual B does not have this.
                        if(individualA[i] == 2){
                            newIndividual[i] = individualA[i] + 1;
                        } else {
                            if (Math.random() < 0.5) {
                                newIndividual[i] = individualA[i] - 1;
                            } else {
                                newIndividual[i] = individualA[i] + 1;
                            }
                        }
                    }
                }else{
                    //Both of the individuals have this layer
                    newIndividual[i] = addOrRemove(individualA[i], individualB[i], 2);
                }

            }
            if(i+1 > individualA.length || i+1 > individualB.length){
                //One of the individuals does not have this layer
                //Now there are three possibilities.
                if(i+1 > individualA.length && i+1 > individualB.length){
                    //Neither has this layer. Let's RANDOM!
                    newIndividual[i] = randInt(4,30);
                }else if(i+1 > individualA.length){
                    //Individual A does not have this.
                    if(Math.random() < 0.5){
                        newIndividual[i] = individualB[i] - 1;
                    }else {
                        newIndividual[i] = individualB[i] +1;
                    }
                }else if(i+1 > individualB.length){
                    //Individual A does not have this.
                    if(Math.random() < 0.5){
                        newIndividual[i] = individualA[i] - 1;
                    }else {
                        newIndividual[i] = individualA[i] +1;
                    }
                }
            }else{
                //Both of the individuals have this layer
                if(individualA[i] > individualB[i]){
                    newIndividual[i] = randInt(individualB[i], individualB[i]);
                }else {
                    newIndividual[i] = randInt(individualA[i], individualB[i]);
                }

            }
        }
        return newIndividual;
    }
    private int addOrRemove(int a, int b, int minimum){
        if(a == minimum || b == minimum){
            //We know that at least one individual is equal to one so we are guaranteed to increment by one if we sum them together.
            if(a > b){
                return a + 1;
            }else {
                return b + 1;
            }
        }
        else{
            //Both of the individuals have at least two hidden layers/nodes.
            if(Math.random() < 0.5){
                //We are going to remove
                if(a > b){
                    return b - 1;
                }
                else {
                    return a - 1;
                }
            }
            else {
                //We are going to add
                if(a > b){
                    return a + 1;
                }else{
                    return b + 1;
                }
            }
        }
    }

    private LinkGene crossoverLinkGenes(LinkGene a, LinkGene b){
        Random r = new Random();

        double weightA = a.getWeight();
        double weightB = b.getWeight();
        //weightA = (double)Math.round(weightA * 100000) / 100000;
        //weightB = (double)Math.round(weightB * 100000) / 100000;
        //double interval = Math.abs(weightA-weightB);
        //double newWeight = (weightA+weightB)*0.5 + r.nextGaussian()*interval*2;
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

       /* String binaryA = Long.toBinaryString(Double.doubleToRawLongBits(weightA));
        String binaryB = Long.toBinaryString(Double.doubleToRawLongBits(weightB));
        int crossOverPoint = randInt(0, binaryA.length());
        String newBinary = binaryA.substring(0,crossOverPoint) + binaryB.substring(crossOverPoint+1,binaryB.length());
        double newWeight = Double.longBitsToDouble(new BigInteger(newBinary, 2).longValue());
        int tmp = (int)newWeight; */
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

    private NodeGene crossoverNodeGenes(NodeGene a, NodeGene b){
        Node tmpNode = new Node();
        if(!(a.getLayer() == b.getLayer() && a.getIndex() == b.getIndex())){
            System.out.println("Something went TERRIBLY wrong");
        }
        int layer = a.getLayer();
        int index = a.getIndex();
        int transferFunction;
        if(Math.random() < mutationProbability){
            transferFunction = randInt(1,tmpNode.getNumberOfFunctions());
        }
        else {
            if(Math.random() < 0.5){
                transferFunction = a.getTransferFunction();
            }else {
                transferFunction = b.getTransferFunction();
            }
        }
        return new NodeGene(layer, index, transferFunction);
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
                newWeight = l.getWeight() * Math.random() * 4 - 2 * l.getWeight();
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
        return new Chromosome(individual.getNetworkGene(),newLinkGenes, individual.getNodeGenes());
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
