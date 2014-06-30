package evolutionaryneuralnetwork;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by MagnusSnorri on 30/05/2014.
 */
public class Chromosome implements Comparable<Chromosome> {
    private NetworkGene networkGene;
    private ArrayList<LinkGene> linkGenes;
    private ArrayList<NodeGene> nodeGenes;
    private Double fitnessValue;

    public Chromosome(NetworkGene networkGene, ArrayList<LinkGene> linkGenes, ArrayList<NodeGene> nodeGenes){
        this.networkGene = networkGene;
        this.linkGenes = linkGenes;
        this.nodeGenes = nodeGenes;
        fitnessValue = 0.0;
    }

    public Chromosome(JSONObject chromosome)
    {
        this.linkGenes = new ArrayList<LinkGene>();
        this.nodeGenes = new ArrayList<NodeGene>();
        this.fitnessValue = (Double)chromosome.get("fitness");

        JSONObject network = (JSONObject)chromosome.get("network");
        JSONArray hidden = (JSONArray)network.get("hidden");
        int[] hiddenNodes = new int[hidden.size()];
        for(int i = 0; i < hidden.size(); i++)
        {
            hiddenNodes[i] = objToInteger(hidden.get(i));
        }

        this.networkGene = new NetworkGene(objToInteger(network.get("input")),hiddenNodes, objToInteger(network.get("output")));
        JSONArray links = (JSONArray)chromosome.get("links");
        Iterator<JSONObject> linkIter = links.iterator();
        while (linkIter.hasNext())
        {
            JSONObject obj = linkIter.next();
            double w = (Double)obj.get("w");
            boolean active = (Boolean)obj.get("isActivated");
            int startLayer = objToInteger(obj.get("startLayer"));
            int startNode = objToInteger(obj.get("startNode"));
            int endNode = objToInteger(obj.get("endNode"));
            this.linkGenes.add(new LinkGene(startLayer,startNode,endNode,w,active));
        }
        JSONArray nodes = (JSONArray)chromosome.get("nodes");
        Iterator<JSONObject> nodeIter = nodes.iterator();
        while (nodeIter.hasNext())
        {
            JSONObject obj = linkIter.next();
            int layer = objToInteger(obj.get("layer"));
            int index = objToInteger(obj.get("index"));
            int transferFunction = objToInteger(obj.get("transferFunction"));
            this.nodeGenes.add(new NodeGene(layer, index, transferFunction));
        }
    }

    public void setFitnessValue(double fitnessValue){this.fitnessValue = fitnessValue;}

    public double getFitnessValue(){return fitnessValue;}

    public NetworkGene getNetworkGene(){return networkGene;}

    public ArrayList<LinkGene> getLinkGenes(){return linkGenes;}

    public ArrayList<NodeGene> getNodeGenes(){return nodeGenes;}

    public void setLinkGenes(ArrayList<LinkGene> linkGenes){this.linkGenes = linkGenes;}

    public int compareTo(Chromosome chromosome)
    {
        return this.fitnessValue.compareTo(chromosome.getFitnessValue());
    }

    public String toJSON(int generation)
    {
        JSONObject chromosome = new JSONObject();
        JSONObject network = new JSONObject();
        JSONArray links = new JSONArray();
        JSONArray hidden = new JSONArray();
        JSONArray nodes = new JSONArray();

        for(LinkGene linkGene : linkGenes)
        {
            JSONObject obj = new JSONObject();
            obj.put("w", linkGene.getWeight());
            obj.put("startNode", linkGene.getStartNode());
            obj.put("endNode", linkGene.getEndNode());
            obj.put("startLayer", linkGene.getStartLayer());
            obj.put("isActivated", linkGene.isActivated());

            links.add(obj);
        }
        for(NodeGene nodeGene : nodeGenes)
        {
            JSONObject obj = new JSONObject();
            obj.put("layer", nodeGene.getLayer());
            obj.put("index", nodeGene.getIndex());
            obj.put("transferFunction", nodeGene.getTransferFunction());

            nodes.add(obj);
        }
        network.put("input",networkGene.getNumberOfInputNodes());
        network.put("output",networkGene.getNumberOfOutputNodes());
        for(int i : networkGene.getNumberOfHiddenNodes())
        {
            hidden.add(i);
        }
        network.put("hidden", hidden);
        chromosome.put("fitness", fitnessValue);
        chromosome.put("generation", generation);
        chromosome.put("network", network);
        chromosome.put("links", links);
        chromosome.put("nodes", nodes);

        return chromosome.toJSONString();
    }


    // Function to cast Long values from a JSONARRAY to Integer.
    private Integer objToInteger(Object obj)
    {
        Long l = (Long)obj;
        return Integer.valueOf(l.intValue());
    }

    public void printNetworkStructure(){
        int numberOfLayers = networkGene.getNumberOfHiddenNodes().length + 2;
        System.out.println("Number of layers: " + numberOfLayers);
        System.out.println("--------------------------");
        System.out.println("Number of nodes: " + networkGene.getNumberOfInputNodes());
        for(int i: networkGene.getNumberOfHiddenNodes()){
            System.out.println("Number of nodes: " + i);
        }
        System.out.println("Number of nodes: " + networkGene.getNumberOfOutputNodes());
        System.out.println("--------------------------");
    }

}
