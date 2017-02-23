package vie.main;

import java.io.IOException;

import vie.simulation.Simulator;
import vie.utilities.NetworkTopology;

public class VirtualInfrastructureEmbedding {
	public static void main(String args[]) throws IOException{
		//Simulator simulator = new Simulator(NetworkTopology.US_MESH);
		Simulator simulator = new Simulator(NetworkTopology.NSFNET);
		simulator.start();
		
	}
}
