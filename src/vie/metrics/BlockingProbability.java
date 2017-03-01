package vie.metrics;

import java.io.IOException;

import vie.simulation.Simulator;
import vie.utilities.NetworkTopology;

public class BlockingProbability{

	public static double averageBlockingProbability(int iterations, int numberOfRequests, int algorithm) throws IOException{


		double sum = 0;
		for(int i = 0; i < iterations; i++){
			Simulator simulator = new Simulator(NetworkTopology.NSFNET);
			simulator.setNumberOfRequest(numberOfRequests);
			simulator.start(algorithm);
			sum += simulator.getTopology().requestsMapped / (double)numberOfRequests;
		}
		//System.out.println(sum);
		//System.out.println(100 - (sum / (double)numberOfRequests));
		return 100 - (100 * (sum/iterations));

	}

}
