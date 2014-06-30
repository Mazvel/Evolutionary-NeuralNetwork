package evolutionaryneuralnetwork;

import java.util.ArrayList;

/**
 * Created by MagnusSnorri on 30/05/2014.
 * evolutionaryneuralnetwork.Node is an object in a Neural Network that is connected with Links. Each node can access the Nodes connected to
 * it through ArrayList of links and using the weight of each link and the value of each node it is possible to
 * evaluate this node. Since this is a feed-forward network the node does not have information about it's
 * successors.
 */
public class Node {
    private int functionType;
    private ArrayList<Link> inputNodes;
    private double value;
    private int numberOfFunctions;

    /**

     */
    public Node(){
        inputNodes = new ArrayList<Link>();
        value = 0;
        functionType = 1;
        numberOfFunctions = 3;
    }

    /**
     * Add new link to the ArrayList of inputNodes.
     * @param link evolutionaryneuralnetwork.Link connecting this node to previous layer.
     */
    public void addInputNode(Link link){inputNodes.add(link);}

    /**
     * Set the function type used when evaluating this node.
     * @param type positive integer value
     */
    public void setFunctionType(int type){this.functionType = type;}

    /**
     * Function to set the value of the node, usually straight from the sensor data.
     * @param value double value representing he value of this node.
     */
    public void setValue(double value){this.value = value;}

    /**
     * Function to get the value of the node.
     * @return the value of this node.
     */
    public double getValue(){return value;}

    /**
     * Function to get number of different transfer function types.
     * @return
     */
    public int getNumberOfFunctions(){return numberOfFunctions;}

    /**
     * This function evaluates the value for this node in the neural network based on it's inputs.
     * After calculating the sum of weight*value for every input node it calls the appropriate value
     * function based on the functionType variable.
     */
    public void evaluateNode(){
        double sum = 0;
        for(Link l:inputNodes){
            if(l.isActive()) {
                sum += l.getWeight() * l.getStartNode().getValue();
            }
        }
        switch(functionType){
            case 0: {
                value = sum;
                break;
            }
            case 1: {
                value = sigmoid(sum);
                break;
            }
            case 2: {
                double tmp = sigmoid(sum);
                if (tmp > 0.8){
                    value = 1;
                }else {
                    value = 0;
                }

            }

            case 3: {
                double tmp = sigmoid(sum);
                if (tmp > 0.2){
                    value = 1;
                }else {
                    value = 0;
                }
            }
            default: {
                break;
            }
        }
    }

    /**
     * Sigmoid function
     * @param value double value representing the sum of weight*value for every input node
     * @return the return value is double bounded between zero and one.
     */
    private double sigmoid(double value){
        return (1/( 1 + Math.pow(Math.E,(-1*value))));
    }
}
