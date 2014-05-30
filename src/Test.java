/**
 * Created by MagnusSnorri on 30/05/2014.
 */
class Test {

    public static void main(String[] args) {

        NeuralNetwork neuralNetwork = new NeuralNetwork(5, new int[]{10}, 6);
        double[] output = new double[2];
        output = neuralNetwork.evaluateNeuralNetwork(new double[]{0.4, -2.3, 3.4,-2.4,0.1});
        for(double d: output){
            System.out.println("Output: " + d);
        }
    }
}
