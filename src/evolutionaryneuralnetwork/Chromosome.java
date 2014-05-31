package evolutionaryneuralnetwork;

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
}
