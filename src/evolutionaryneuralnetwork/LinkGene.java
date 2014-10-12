package evolutionaryneuralnetwork;

/**
 * Created by MagnusSnorri on 30/05/2014.
 * This object contains information about each link which is stored in the chromosome as gene. Each gene has information
 * about the starting layer, the start node and end node, the weight and activation. It is possible to reconstruct the
 * link in the NeuralNetwork class based on this information.
 */
public class LinkGene {
    private int startLayer;
    private int startNode;
    private int endNode;
    private double weight;
    private boolean activated;

    public LinkGene(int startLayer, int startNode, int endNode, double weight, boolean activated){
        this.startLayer = startLayer;
        this.startNode = startNode;
        this.endNode = endNode;
        this.weight = weight;
        this.activated = activated;
    }

    public int getStartLayer(){return startLayer;}

    public int getStartNode(){return startNode;}

    public int getEndNode(){return endNode;}

    public double getWeight(){return weight;}

    public boolean isActivated(){return activated;}

    public void setWeight(double weight){this.weight = weight;}

    public void setActivation(boolean activated){this.activated = activated;}

}
