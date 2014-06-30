package evolutionaryneuralnetwork;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by MagnusSnorri on 30/05/2014.
 */
public class NeuralNetwork {
    private ArrayList<Layer> networkLayers;
    private double initialWeightInterval = 3;
    private int numberOfInputs;
    private int[] numberOfHiddenNodes;
    private int numberOfOutputs;
    ArrayList<LinkGene> linkGenes;
    ArrayList<NodeGene> nodeGenes;

    /**
     *
     * @param numberOfInputs number of Nodes in the input layer
     * @param numberOfHiddenNodes integer array representing the number of hidden layers and number
     *                            of nodes in each hidden layer.
     * @param numberOfOutputs number of Nodes in the output layer.
     */
    public NeuralNetwork(int numberOfInputs, int[] numberOfHiddenNodes, int numberOfOutputs){
        this.numberOfInputs = numberOfInputs;
        this.numberOfHiddenNodes = numberOfHiddenNodes;
        this.numberOfOutputs = numberOfOutputs;
        linkGenes = new ArrayList<LinkGene>();
        nodeGenes = new ArrayList<NodeGene>();
        networkLayers = createNodes(numberOfInputs, numberOfHiddenNodes, numberOfOutputs);
        createLinks(networkLayers);
    }
    public NeuralNetwork(Chromosome chromosome){
        this.numberOfInputs = chromosome.getNetworkGene().getNumberOfInputNodes();
        this.numberOfHiddenNodes = chromosome.getNetworkGene().getNumberOfHiddenNodes();
        this.numberOfOutputs = chromosome.getNetworkGene().getNumberOfOutputNodes();
        nodeGenes = chromosome.getNodeGenes();
        networkLayers = createNodes(numberOfInputs, numberOfHiddenNodes, numberOfOutputs, nodeGenes);
        linkGenes = chromosome.getLinkGenes();
        createLinks(networkLayers,linkGenes);


    }

    /**
     * Initializes the Nodes in the Neural Network based on how many Nodes should be in each layer.
     * @param numberOfInputs number of Nodes in the input layer
     * @param numberOfHiddenNodes integer array representing the number of hidden layers and number
     *                            of nodes in each hidden layer.
     * @param numberOfOutputs number of Nodes in the output layer.
     * @return ArrayList of evolutionaryneuralnetwork.Layer objects where each evolutionaryneuralnetwork.Layer object includes the evolutionaryneuralnetwork.Node objects in that
     *          layer.
     */
    private ArrayList<Layer> createNodes(int numberOfInputs, int[] numberOfHiddenNodes, int numberOfOutputs){
        ArrayList<Layer> layers = new ArrayList<Layer>();
        Layer inputLayer = new Layer();
        for(int i = 0; i<numberOfInputs;i++){
            Node tmpNode = new Node();
            int functionType = randInt(1,tmpNode.getNumberOfFunctions());
            tmpNode.setFunctionType(functionType);
            inputLayer.addNode(tmpNode);
            nodeGenes.add(new NodeGene(0, i, functionType));

        }
        layers.add(inputLayer);
        Layer hiddenLayer;
        for(int i = 0; i< numberOfHiddenNodes.length; i++){
            int numberOfNodes = numberOfHiddenNodes[i];
            hiddenLayer = new Layer();
            for(int j = 0; j<numberOfNodes; j++){
                Node tmpNode = new Node();
                int functionType = randInt(1,tmpNode.getNumberOfFunctions());
                tmpNode.setFunctionType(functionType);
                hiddenLayer.addNode(tmpNode);
                nodeGenes.add(new NodeGene(i + 1, j, functionType));

            }
            layers.add(hiddenLayer);
        }
        Layer outputLayer = new Layer();
        for(int i = 0; i<numberOfOutputs;i++){
            Node tmpNode = new Node();
            tmpNode.setFunctionType(0);
            outputLayer.addNode(tmpNode);
            nodeGenes.add(new NodeGene(numberOfHiddenNodes.length + 1, i, 0));
        }
        layers.add(outputLayer);
        return layers;
    }

    private ArrayList<Layer> createNodes(int numberOfInputs, int[] numberOfHiddenNodes, int numberOfOutputs, ArrayList<NodeGene> nodeGenes){
        ArrayList<Layer> layers = new ArrayList<Layer>();
        Layer inputLayer = new Layer();
        int nodeCounter = 0;
        for(int i = 0; i<numberOfInputs;i++){
            NodeGene tmpNodeGene = nodeGenes.get(nodeCounter);
            Node tmpNode = new Node();
            tmpNode.setFunctionType(tmpNodeGene.getTransferFunction());
            inputLayer.addNode(tmpNode);
            nodeCounter++;
        }
        layers.add(inputLayer);
        Layer hiddenLayer;
        for(int i = 0; i< numberOfHiddenNodes.length; i++){
            int numberOfNodes = numberOfHiddenNodes[i];
            hiddenLayer = new Layer();
            for(int j = 0; j<numberOfNodes; j++){
                NodeGene tmpNodeGene = nodeGenes.get(nodeCounter);
                Node tmpNode = new Node();
                tmpNode.setFunctionType(tmpNodeGene.getTransferFunction());
                hiddenLayer.addNode(tmpNode);
                nodeCounter++;
            }
            layers.add(hiddenLayer);
        }
        Layer outputLayer = new Layer();
        for(int i = 0; i<numberOfOutputs;i++){
            NodeGene tmpNodeGene = nodeGenes.get(nodeCounter);
            Node tmpNode = new Node();
            tmpNode.setFunctionType(tmpNodeGene.getTransferFunction());
            outputLayer.addNode(tmpNode);
            nodeCounter++;
        }
        layers.add(outputLayer);
        return layers;
    }

    /**
     * Initializes links between every pair of Nodes in two adjacent layers.
     * @param networkLayers Input is ArrayList of evolutionaryneuralnetwork.Layer objects where each evolutionaryneuralnetwork.Layer object includes
     *                      evolutionaryneuralnetwork.Node objects for that evolutionaryneuralnetwork.Layer in the Neural Network.
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
                    boolean activated = r.nextBoolean();
                    currentLayerNode.addInputNode(new Link(previousLayerNode, weight, activated));
                    linkGenes.add(new LinkGene(i-1,previousLayer.indexOf(previousLayerNode),
                            currentLayer.indexOf(currentLayerNode),weight,activated ));

                }
            }
        }
    }

    private void createLinks(ArrayList<Layer> networkLayers, ArrayList<LinkGene> linkGenes){
        for(LinkGene linkGene: linkGenes){

            Node startNode = networkLayers.get(linkGene.getStartLayer()).getNodes().get(linkGene.getStartNode());
            Node endNode = networkLayers.get(linkGene.getStartLayer()+1).getNodes().get(linkGene.getEndNode());
            Link link = new Link(startNode, linkGene.getWeight(), linkGene.isActivated());
            endNode.addInputNode(link);
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

    public Chromosome generateChromosome(){
        NetworkGene networkGene = new NetworkGene(numberOfInputs,numberOfHiddenNodes,numberOfOutputs);
        return new Chromosome(networkGene,linkGenes, nodeGenes);

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
}
