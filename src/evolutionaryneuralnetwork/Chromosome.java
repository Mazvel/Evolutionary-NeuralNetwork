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
    private Double fitnessValue;

    public Chromosome(NetworkGene networkGene, ArrayList<LinkGene> linkGenes){
        this.networkGene = networkGene;
        this.linkGenes = linkGenes;
        fitnessValue = 0.0;
    }

    public Chromosome(JSONObject chromosome)
    {
        this.linkGenes = new ArrayList<LinkGene>();
        this.fitnessValue = (Double)chromosome.get("fitness");

        JSONObject network = (JSONObject)chromosome.get("network");
        JSONArray hidden = (JSONArray)network.get("hidden");
        int[] hiddenNodes = new int[hidden.size()];
        for(int i = 0; i < hidden.size(); i++)
        {
            hiddenNodes[i] = JSONHandler.jsonNumberToInt(hidden.get(i));
        }

        this.networkGene = new NetworkGene(JSONHandler.jsonNumberToInt(network.get("input")),hiddenNodes,JSONHandler.jsonNumberToInt(network.get("output")));

        JSONArray links = (JSONArray)chromosome.get("links");
        Iterator<JSONObject> linkIter = links.iterator();
        while (linkIter.hasNext())
        {
            JSONObject obj = linkIter.next();
            double w = (Double)obj.get("w");
            boolean active = (Boolean)obj.get("isActivated");
            int startLayer = JSONHandler.jsonNumberToInt(obj.get("startLayer"));
            int startNode = JSONHandler.jsonNumberToInt(obj.get("startNode"));
            int endNode = JSONHandler.jsonNumberToInt(obj.get("endNode"));
            this.linkGenes.add(new LinkGene(startLayer,startNode,endNode,w,active));
        }
    }

    public void setFitnessValue(double fitnessValue){this.fitnessValue = fitnessValue;}

    public double getFitnessValue(){return fitnessValue;}

    public NetworkGene getNetworkGene(){return networkGene;}

    public ArrayList<LinkGene> getLinkGenes(){return linkGenes;}

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

        return chromosome.toJSONString();
    }
}
