package vie.test;

import java.io.IOException;

import vie.simulation.Simulator;
import vie.utilities.KShortestPaths;
import vie.utilities.NetworkTopology;

public class KShortestTest {
	public static void main(String args[]) throws IOException{
		Simulator simulator = new Simulator(NetworkTopology.NSFNET,0, 0);
		KShortestPaths ksp = new KShortestPaths(3,0,11,simulator.getTopology());
		ksp.getShortestPaths();
	}
}
