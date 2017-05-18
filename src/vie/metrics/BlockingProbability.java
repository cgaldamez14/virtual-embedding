package vie.metrics;

import java.io.IOException;

import vie.simulation.Simulator;
import vie.utilities.NetworkTopology;

public class BlockingProbability{

	public static final int COMPUTATIONAL_AVAILABILITY = 8000;
	public static final int BANDWIDTH_AVAILABILITY = 3000;
	
	public static double[] averageBlockingProbability(int iterations, int numberOfRequests) throws IOException{
		double sum1 = 0;
		double sum2 = 0;
		double sum3 = 0;
		for(int i = 0; i < iterations; i++){
			Simulator simulator = new Simulator(NetworkTopology.NSFNET,COMPUTATIONAL_AVAILABILITY, BANDWIDTH_AVAILABILITY);
			simulator.setNumberOfRequest(numberOfRequests);
			simulator.generateRequests();
			simulator.start(1);
			sum1 += simulator.getTopology().getRequestsMapped() / (double)numberOfRequests;
			
			
			simulator = new Simulator(NetworkTopology.NSFNET,COMPUTATIONAL_AVAILABILITY, BANDWIDTH_AVAILABILITY);
			simulator.setNumberOfRequest(numberOfRequests);
			simulator.generateRequests();
			simulator.start(2);
			sum2 += simulator.getTopology().getRequestsMapped() / (double)numberOfRequests;

			
			simulator = new Simulator(NetworkTopology.NSFNET,COMPUTATIONAL_AVAILABILITY, BANDWIDTH_AVAILABILITY);
			simulator.setNumberOfRequest(numberOfRequests);
			simulator.generateRequests();
			simulator.start(3);
			sum3 += simulator.getTopology().getRequestsMapped() / (double)numberOfRequests;
		}

		return new double[]{100 - (100 * (sum1/iterations)),100 - (100 * (sum2/iterations)),100 - (100 * (sum3/iterations))};

	}
	
	
	public static double[] averageBlockingVSRequest(int iterations, int num) throws IOException{
		
		double sum1 = 0;
		double sum2 = 0;
		double sum3 = 0;
		for(int i = 0; i < iterations; i++){
			Simulator simulator = new Simulator(NetworkTopology.NSFNET,COMPUTATIONAL_AVAILABILITY, BANDWIDTH_AVAILABILITY);
			simulator.setNumberOfRequest(300);
			simulator.setMaxNodes(num);
			simulator.generateRequests();
			
			simulator.start(1);
			sum1 += simulator.getTopology().getRequestsMapped() / 300.0;
			simulator.resetAllResources();
			
			
			simulator = new Simulator(NetworkTopology.NSFNET,COMPUTATIONAL_AVAILABILITY, BANDWIDTH_AVAILABILITY);
			simulator.setNumberOfRequest(300);
			simulator.setMaxNodes(num);

			simulator.generateRequests();
			simulator.start(2);
			sum2 += simulator.getTopology().getRequestsMapped() / 300.0;
			simulator.resetAllResources();

			
			simulator = new Simulator(NetworkTopology.NSFNET,COMPUTATIONAL_AVAILABILITY, BANDWIDTH_AVAILABILITY);
			simulator.setNumberOfRequest(300);
			simulator.setMaxNodes(num);

			simulator.generateRequests();
			simulator.start(3);
			sum3 += simulator.getTopology().getRequestsMapped() / 300.0;
			simulator.resetAllResources();
		}

		return new double[]{100 - (100 * (sum1/iterations)),100 - (100 * (sum2/iterations)),100 - (100 * (sum3/iterations))};

	}
	

}
