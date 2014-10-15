Evolutionary-NeuralNetwork
==========================

The file Neuroevolution for Car Racing (pdf) which can be found in this repository gives information about the implementaiton and what is was used for. 

Java implementation of evolutionary neural network which optimizes based on user-defined fitness value. This implementation was created for the final project in a Neural Network course at TUDelft 2014. The network was used to optimize race care agent which got information about it's position relative to the race track from it's sensors which was fed into the network which then outputed appropriate action. We started with 100 randomly initialized networks based on pre-defined architecture and then based on the fitness function of distance raced we chose the fittest individuals in each generation as candidates for reproduction for the next generation. After about 40 generations the agent was able to drive through the track without colliding at a decent speed.

This is a general solution and it should be possible to use this in any optimization problem which makes use of evolutionary neural networks. The test.java shows how the initial generation is created and then evolved until the maximum number of generations has been reached. It is also possible to use the JSONHandler to save the genes (chromosome) from any individual so it is possible to reconstruct that network later.

Beware:This implementation has not been thoroughly tested.
