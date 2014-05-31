package evolutionaryneuralnetwork;

/**
 * Created by MagnusSnorri on 30/05/2014.
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
