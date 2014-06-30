package evolutionaryneuralnetwork;

/**
 * Created by MagnusSnorri on 29/06/2014.
 */
public class NodeGene {
    private int layer;
    private int index;
    private int transferFunction;

    public NodeGene(int layer, int index, int transferFunction){
        this.layer = layer;
        this.index = index;
        this.transferFunction = transferFunction;
    }

    public int getLayer(){return layer;}
    public int getIndex(){return index;}
    public int getTransferFunction(){return transferFunction;}

}
