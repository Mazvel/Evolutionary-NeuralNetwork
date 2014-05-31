package evolutionaryneuralnetwork;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

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
