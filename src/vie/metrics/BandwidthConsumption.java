package vie.metrics;

import java.io.IOException;

import vie.simulation.Simulator;
import vie.utilities.NetworkTopology;

public class BandwidthConsumption{
	
	public static final int COMPUTATIONAL_AVAILABILITY = 500000;
	public static final int BANDWIDTH_AVAILABILITY = 1000000;
	

	public static double[] averageBandwidthConsumption(int iterations, int numberOfRequests) throws IOException{

		int sum1 = 0;
		int sum2 = 0;
		int sum3 = 0;
		
		for(int i = 0; i < iterations; i++){
			Simulator simulator = new Simulator(NetworkTopology.NSFNET, COMPUTATIONAL_AVAILABILITY, BANDWIDTH_AVAILABILITY);
			simulator.setNumberOfRequest(numberOfRequests);
			simulator.generateRequests();
			simulator.start(1);
			sum1 += simulator.getTopology().getTotalBandwidthConsumption();
			simulator.resetAllResources();
			
			simulator = new Simulator(NetworkTopology.NSFNET,COMPUTATIONAL_AVAILABILITY, BANDWIDTH_AVAILABILITY);
			simulator.setNumberOfRequest(numberOfRequests);
			simulator.generateRequests();
			simulator.start(2);
			sum2 += simulator.getTopology().getTotalBandwidthConsumption();
			simulator.resetAllResources();
			
			simulator = new Simulator(NetworkTopology.NSFNET,COMPUTATIONAL_AVAILABILITY, BANDWIDTH_AVAILABILITY);
			simulator.setNumberOfRequest(numberOfRequests);
			simulator.generateRequests();
			simulator.start(3);
			sum3 += simulator.getTopology().getTotalBandwidthConsumption();
			simulator.resetAllResources();
			//System.out.println("done with " + i);
		}
		
		//System.out.println(sum);
		return new double[]{sum1 /iterations,sum2 /iterations,sum3 / iterations};

	}

}
