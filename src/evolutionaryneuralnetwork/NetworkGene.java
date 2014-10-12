package evolutionaryneuralnetwork;

/**
 * Created by MagnusSnorri on 30/05/2014.
 * This object contains information about the neural network to be stored as a gene in the chromosome. The gene has
 * information about the number of input nodes, number ouf output nodes and number of nodes in each layer of the
 * neural network. Based on this gene it is possible to reconstruct the architecture of the network in the NeuralNetwork
 * class
 */
public class NetworkGene {
    private int numberOfInputNodes;
    private int[] numberOfHiddenNodes;
    private int numberOfOutputNodes;

    public NetworkGene(int numberOfInputNOdes, int[] numberOfHiddenNodes, int numberOfOutputNodes){
        this.numberOfInputNodes = numberOfInputNOdes;
        this.numberOfHiddenNodes = numberOfHiddenNodes;
        this.numberOfOutputNodes = numberOfOutputNodes;
    }

    public int getNumberOfInputNodes(){return numberOfInputNodes;}

    public int[] getNumberOfHiddenNodes(){return numberOfHiddenNodes;}

    public int getNumberOfOutputNodes(){return numberOfOutputNodes;}
}
