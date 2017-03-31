package vie.test;

import java.io.IOException;

import vie.models.Pair;
import vie.models.Topology;
import vie.simulation.Simulator;
import vie.utilities.NetworkTopology;

public class TrasponderTest {
	public static void main(String args[]) throws IOException{
		Topology.COMPUTATIONAL_AVAILABILITY = 500000;
		Topology.BANDWIDTH_AVAILABILITY = 1000000;
		
		System.out.println("-------------------------- RANDOM ---------------------------");

		for(int i = 10; i <= 200; i+=10){
			Simulator simulator = new Simulator(NetworkTopology.NSFNET);
			simulator.setNumberOfRequest(500);
			simulator.generateRequests();
		
			
			Pair<Integer,Integer> results = simulator.getNumberOfTransponders(100,i, "random");
			
			System.out.println(i + "\t" + results.first() + "\t" + results.second());
		}
		
		System.out.println("-------------------------- UNIFORM ---------------------------");

		for(int i = 10; i <= 200; i+=10){
			Simulator simulator = new Simulator(NetworkTopology.NSFNET);
			simulator.setNumberOfRequest(500);
			simulator.generateRequests();
			
			Pair<Integer,Integer> results = simulator.getNumberOfTransponders(100,i, "uniform");
			
			System.out.println(i + "\t" + results.first() + "\t" + results.second());
		}
		
		System.out.println("-------------------------- GAUSSIAN ---------------------------");

		for(int i = 10; i <= 200; i+=10){
			Simulator simulator = new Simulator(NetworkTopology.NSFNET);
			simulator.setNumberOfRequest(500);
			simulator.generateRequests();
			
			Pair<Integer,Integer> results = simulator.getNumberOfTransponders(100,i, "gaussian");
			
			System.out.println(i + "\t" + results.first() + "\t" + results.second());
		}

	}
}
