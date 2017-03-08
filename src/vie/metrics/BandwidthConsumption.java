package vie.metrics;

import java.io.IOException;

import vie.simulation.Simulator;
import vie.utilities.NetworkTopology;

public class BandwidthConsumption{

	public static int averageBandwidthConsumption(int iterations, int numberOfRequests, int algorithm) throws IOException{


		int sum = 0;
		for(int i = 0; i < iterations; i++){
			Simulator simulator = new Simulator(NetworkTopology.US_MESH);
			simulator.setNumberOfRequest(numberOfRequests);
			simulator.start(algorithm);
			sum += simulator.getTopology().getTotalBandwidthConsumption();
			//System.out.println("done with " + i);
		}
		
		System.out.println(sum);
		return sum / iterations;

	}

}
