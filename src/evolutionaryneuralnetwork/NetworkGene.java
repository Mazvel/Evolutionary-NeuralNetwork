package evolutionaryneuralnetwork;

/**
 * Created by MagnusSnorri on 30/05/2014.
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
