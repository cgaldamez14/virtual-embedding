package vie.metrics;

import java.io.IOException;

import vie.models.Topology;
import vie.simulation.Simulator;
import vie.utilities.NetworkTopology;

public class BlockingProbability{

	public static double[] averageBlockingProbability(int iterations, int numberOfRequests) throws IOException{

		Topology.BANDWIDTH_AVAILABILITY = 8000;
		Topology.COMPUTATIONAL_AVAILABILITY = 3000;

		double sum1 = 0;
		double sum2 = 0;
		double sum3 = 0;
		for(int i = 0; i < iterations; i++){
			Simulator simulator = new Simulator(NetworkTopology.NSFNET);
			simulator.setNumberOfRequest(numberOfRequests);
			simulator.generateRequests();
			
			simulator.start(1);
			sum1 += simulator.getTopology().requestsMapped / (double)numberOfRequests;
			simulator.resetAllResources();
			
			
			simulator = new Simulator(NetworkTopology.NSFNET);
			simulator.setNumberOfRequest(numberOfRequests);
			simulator.generateRequests();
			simulator.start(2);
			sum2 += simulator.getTopology().requestsMapped / (double)numberOfRequests;
			simulator.resetAllResources();

			
			simulator = new Simulator(NetworkTopology.NSFNET);
			simulator.setNumberOfRequest(numberOfRequests);
			simulator.generateRequests();
			simulator.start(3);
			sum3 += simulator.getTopology().requestsMapped / (double)numberOfRequests;
			simulator.resetAllResources();
		}
		//System.out.println(sum);
		//System.out.println(100 - (sum / (double)numberOfRequests));
		//System.out.println((sum1 / iterations) + " " + (sum2/iterations) + " " + (sum3/ iterations));
		return new double[]{100 - (100 * (sum1/iterations)),100 - (100 * (sum2/iterations)),100 - (100 * (sum3/iterations))};

	}
	
	
	public static double[] averageBlockingVSRequest(int iterations, int num) throws IOException{

		Topology.BANDWIDTH_AVAILABILITY = 8000;
		Topology.COMPUTATIONAL_AVAILABILITY = 3000;
		
		double sum1 = 0;
		double sum2 = 0;
		double sum3 = 0;
		for(int i = 0; i < iterations; i++){
			Simulator simulator = new Simulator(NetworkTopology.NSFNET);
			simulator.setNumberOfRequest(300);
			simulator.setMaxNodes(num);
			simulator.generateRequests();
			
			simulator.start(1);
			sum1 += simulator.getTopology().requestsMapped / 300.0;
			simulator.resetAllResources();
			
			
			simulator = new Simulator(NetworkTopology.NSFNET);
			simulator.setNumberOfRequest(300);
			simulator.setMaxNodes(num);

			simulator.generateRequests();
			simulator.start(2);
			sum2 += simulator.getTopology().requestsMapped / 300.0;
			simulator.resetAllResources();

			
			simulator = new Simulator(NetworkTopology.NSFNET);
			simulator.setNumberOfRequest(300);
			simulator.setMaxNodes(num);

			simulator.generateRequests();
			simulator.start(3);
			sum3 += simulator.getTopology().requestsMapped / 300.0;
			simulator.resetAllResources();
		}
		//System.out.println(sum);
		//System.out.println(100 - (sum / (double)numberOfRequests));
		//System.out.println((sum1 / iterations) + " " + (sum2/iterations) + " " + (sum3/ iterations));
		return new double[]{100 - (100 * (sum1/iterations)),100 - (100 * (sum2/iterations)),100 - (100 * (sum3/iterations))};

	}
	

}
