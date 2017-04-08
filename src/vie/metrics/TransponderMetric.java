package vie.metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import vie.models.Pair;
import vie.models.Topology;
import vie.simulation.Simulator;
import vie.utilities.NetworkTopology;

public class TransponderMetric{
	
	private static enum BANDWIDTH_DISTRIBUTION { RANDOM, GAUSSIAN, UNIFORM };
	private static enum EMBEDDING_METHOD { BACKUP, WO_BACKUP }; 
	
	private final String PROJECT_DIRECTORY =  new File(".").getCanonicalPath();
	private final String RESULTS_DIRECTORY = "/src/vie/results/";
	
	public TransponderMetric()throws IOException{}
	
	public void start() throws IOException{
		getResults(BANDWIDTH_DISTRIBUTION.RANDOM,EMBEDDING_METHOD.WO_BACKUP);
		getResults(BANDWIDTH_DISTRIBUTION.UNIFORM,EMBEDDING_METHOD.WO_BACKUP);
		getResults(BANDWIDTH_DISTRIBUTION.GAUSSIAN,EMBEDDING_METHOD.WO_BACKUP);
		
		getResults(BANDWIDTH_DISTRIBUTION.RANDOM,EMBEDDING_METHOD.BACKUP);
		getResults(BANDWIDTH_DISTRIBUTION.UNIFORM,EMBEDDING_METHOD.BACKUP);
		getResults(BANDWIDTH_DISTRIBUTION.GAUSSIAN,EMBEDDING_METHOD.BACKUP);

	}
	
	public void getResults(BANDWIDTH_DISTRIBUTION distributionType, EMBEDDING_METHOD method) throws IOException{
		File file = new File(PROJECT_DIRECTORY + RESULTS_DIRECTORY + "transponder_"+ distributionType.name().toLowerCase() + "_" + method.name().toLowerCase() + ".csv");
		PrintWriter pw = new PrintWriter(file);
		
		pw.println("Max_Bandwidth,ODU,OTN");

		for(int i = 10; i <= 200; i+=10){
			//Simulator simulator = new Simulator(NetworkTopology.NSFNET);
			//simulator.setNumberOfRequest(500);
			//simulator.generateRequests();
		
			int sum1 = 0;
			int sum2 = 0;
			for(int j = 0; j < 20; j++){
				Simulator simulator = new Simulator(NetworkTopology.NSFNET);
				simulator.setNumberOfRequest(500);
				simulator.generateRequests();
				sum1 += simulator.getTranspondersODU(100,i, distributionType.name().toLowerCase(), (method.equals(EMBEDDING_METHOD.WO_BACKUP))?false:true);
				sum2 += simulator.getTransponderOTN(100,i, distributionType.name().toLowerCase(), (method.equals(EMBEDDING_METHOD.WO_BACKUP))?false:true);
			}
			Pair<Integer,Integer> results = new Pair<Integer, Integer>(sum1/20,sum2/20);			
			pw.println(i + "," + results.first() + "," + results.second());
		}
		
		pw.close();
	}
	
	public static void main(String args[]) throws IOException{
		new TransponderMetric().start();
	}
}
