/**
 * Created by MagnusSnorri on 30/05/2014.
 * Link is an object connecting two Nodes. Each Link has pointer to it's start Node and every Node has List of it's
 * input Links.
 */
public class Link {
    private Node startNode;
    private double weight;
    private boolean active;

    /**
     * @param startNode The node that the link connects to.
     * @param weight The weight of this link.
     * @param active Boolean value representing if this link should be used when evaluating the neural network or not.
     */
    public Link(Node startNode, double weight, boolean active){
        this.startNode = startNode;
        this.weight = weight;
        this.active = active;
    }

    /**
     * Get the weight of this Link
     * @return double value representing the weight of this link
     */
    public double getWeight(){return weight;}

    /**
     * Get the Node this link connects to.
     * @return The start Node of this link
     */
    public Node getStartNode(){return startNode;}

    /**
     * Function to determine if this Node should be used in the evaluation of the Neural Network or not.
     * @return boolean value; true if this link should be used
     */
    public boolean isActive(){return active;}
}
