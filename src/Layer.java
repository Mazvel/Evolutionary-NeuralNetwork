import java.util.ArrayList;

/**
 * Created by MagnusSnorri on 30/05/2014.
 * The Layer object is an ArrayList of nodes. Used to distinguish nodes into different layers of the
 * Neural Network.
 */
public class Layer {
    ArrayList<Node> nodes;

    public Layer(){
        nodes = new ArrayList<Node>();
    }

    /**
     *
     * @param node The node that should be added to this Layer.
     */
    public void addNode(Node node){nodes.add(node);}

    /**
     *
     * @return Get the Nodes in this Layer.
     */
    public ArrayList<Node> getNodes(){return nodes;}
}
