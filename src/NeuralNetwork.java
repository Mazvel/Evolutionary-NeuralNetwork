import java.util.ArrayList;
import java.util.Random;

/**
 * Created by MagnusSnorri on 30/05/2014.
 */
public class NeuralNetwork {
    private ArrayList<Layer> networkLayers;
    private double initialWeightInterval = 3;

    /**
     *
     * @param numberOfInputs number of Nodes in the input layer
     * @param numberOfHiddenNodes integer array representing the number of hidden layers and number
     *                            of nodes in each hidden layer.
     * @param numberOfOutputs number of Nodes in the output layer.
     */
    public NeuralNetwork(int numberOfInputs, int[] numberOfHiddenNodes, int numberOfOutputs){
        networkLayers = createNodes(numberOfInputs, numberOfHiddenNodes, numberOfOutputs);
        createLinks(networkLayers);
    }

    /**
     * Initializes the Nodes in the Neural Network based on how many Nodes should be in each layer.
     * @param numberOfInputs number of Nodes in the input layer
     * @param numberOfHiddenNodes integer array representing the number of hidden layers and number
     *                            of nodes in each hidden layer.
     * @param numberOfOutputs number of Nodes in the output layer.
     * @return ArrayList of Layer objects where each Layer object includes the Node objects in that
     *          layer.
     */
    private ArrayList<Layer> createNodes(int numberOfInputs, int[] numberOfHiddenNodes, int numberOfOutputs){
        ArrayList<Layer> layers = new ArrayList<Layer>();
        Layer inputLayer = new Layer();
        for(int i = 0; i<numberOfInputs;i++){
            inputLayer.addNode(new Node());
        }
        layers.add(inputLayer);
        Layer hiddenLayer;
        for(int i: numberOfHiddenNodes){
            hiddenLayer = new Layer();
            for(int j = 0; j<i; j++){
                hiddenLayer.addNode(new Node());
            }
            layers.add(hiddenLayer);
        }
        Layer outputLayer = new Layer();
        for(int i = 0; i<numberOfOutputs;i++){
            Node tmpNode = new Node();
            tmpNode.setFunctionType(0);
            outputLayer.addNode(tmpNode);
        }
        layers.add(outputLayer);
        return layers;
    }

    /**
     * Initializes links between every pair of Nodes in two adjacent layers.
     * @param networkLayers Input is ArrayList of Layer objects where each Layer object includes
     *                      Node objects for that Layer in the Neural Network.
     */
    private void createLinks(ArrayList<Layer> networkLayers){
        ArrayList<Node> previousLayer;
        ArrayList<Node> currentLayer;
        Random r = new Random();
        for(int i = 1; i < networkLayers.size();i++){
            previousLayer = networkLayers.get(i-1).getNodes();
            currentLayer = networkLayers.get(i).getNodes();
            for(Node currentLayerNode: currentLayer){
                for(Node previousLayerNode: previousLayer){
                    double weight = Math.random()*initialWeightInterval -0.5*initialWeightInterval;
                    currentLayerNode.addInputNode(new Link(previousLayerNode, weight, r.nextBoolean()));
                }
            }
        }
    }

    /**
     * Evaluates the Neural Network based on the provided inputVector.
     * @param inputVector array of double values that are fed to the input layer of the
     *                    Neural Network
     * @return output vector from the values of the output layer after evaluating each node in the
     *          Neural Network.
     */
    public double[] evaluateNeuralNetwork(double[] inputVector){
        ArrayList<Node> inputLayer = networkLayers.get(0).getNodes();
        ArrayList<Node> outputLayer = networkLayers.get(networkLayers.size()-1).getNodes();
        //If the input vector is not of the same size as the input layer return null.
        if(inputVector.length != inputLayer.size()){
            return null;
        }
        for(int i = 0; i < inputVector.length;i++){
            inputLayer.get(i).setValue(inputVector[i]);
        }
        ArrayList<Node> currentLayer;
        for(int i = 1; i < networkLayers.size();i++){
            currentLayer = networkLayers.get(i).getNodes();
            for(Node n: currentLayer){
                n.evaluateNode();
            }
        }
        double[] outputVector = new double[outputLayer.size()];
        for(int i = 0; i < outputLayer.size();i++){
            outputVector[i] = outputLayer.get(i).getValue();
        }
        return outputVector;
    }
}
