package vie.simulation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vie.embedding.Algorithm1;
import vie.embedding.Algorithm2;
import vie.embedding.Algorithm3;
import vie.embedding.Mapper;
import vie.models.Link;
import vie.models.Node;
import vie.models.Pair;
import vie.models.PhysicalLink;
import vie.models.PhysicalNode;
import vie.models.Topology;
import vie.models.VirtualLink;
import vie.models.VirtualNode;
import vie.models.VirtualRequest;
import vie.utilities.NetworkTopology;
import vie.utilities.TopologyUtil;

@SuppressWarnings("unused")
public class Simulator {

	private int NUMBER_OF_REQUESTS = 100;
	
	private List<VirtualRequest> requests;
	private Topology topology;
	
	public static int MAX_NODES = 4;
	
	public Simulator(NetworkTopology type) throws IOException{
		
		this.requests = new ArrayList<>();
		this.topology = TopologyUtil.readAdjacencyMatrix(type);
		topology.setSubsets();
	}
	
	public Topology getTopology(){
		return topology;
	}
	
	public void setNumberOfRequest(int numberOfRequests){
		NUMBER_OF_REQUESTS= numberOfRequests;
	}
	
	public void resetAllResources(){
		topology.requestsMapped = 0;
		for(VirtualRequest vr: requests){
			if(vr.isBlocked()){
				vr.unblock();
			}
			for(int i = 1; i < vr.getVirtualNodes().size() - 1; i++){
				vr.getVirtualNodes().get(i).setMap(-1);
			}
			for(VirtualLink vl: vr.getVirtualLinks()){
				vl.setLinkMapping(null);
			}
		}
		
		for(Map.Entry<Integer, PhysicalNode> entry: topology.getNodes().entrySet()){
			entry.getValue().setComputationAvailability(Topology.COMPUTATIONAL_AVAILABILITY);
		}
		
		for(PhysicalLink pl: topology.getLinks()){
			pl.setBandwidthAvailability(Topology.BANDWIDTH_AVAILABILITY);
		}
	}
	
	public void start(int algorithm){		
		int success = 0;
		int request = 0;
		for(VirtualRequest vr: requests){
			
			Mapper mapper = null;
			switch(algorithm){
			case 1:
				mapper = new Algorithm1(topology, vr);
				break;
			case 2:
				mapper = new Algorithm2(topology, vr);
				break;
			case 3:
				mapper = new Algorithm3(topology, vr);
				break;
			}


			if (mapper.attemptRequestMapping()){
				//System.out.println("\n****************************** REQUEST " + request + " ********************************\n");
				//System.out.println(vr);
				//System.out.println("************************** END OF REQUEST " + request + " *****************************\n");

				success++;
				topology.requestsMapped++;
			}
			request++;
		}
		
		//System.out.print("\r" + success + " / " + requests.size() + " were successfully mapped.");
		//System.out.print("TOTAL BANDWIDTH CONSUMPTION: " + topology.getTotalBandwidthConsumption());
		//System.out.flush();
	}
	
	public void generateRequests(){
		for (int requestNumber = 0; requestNumber < NUMBER_OF_REQUESTS; requestNumber++)
			generateRequest(requestNumber);
	}
	
	private void generateRequest(int number){
		
		int numberOfVirtualNodes = 2 + (int)(Math.random() * MAX_NODES);     // Max number of nodes is 6 and minimum is 2
		int numberOfVirtualLinks = numberOfVirtualNodes - 1;		 // Linear array topology has 1 less link than the total number of nodes
		
		VirtualRequest request = new VirtualRequest(numberOfVirtualNodes, numberOfVirtualLinks);
		requests.add(request);
		
		// Assumption is that origin and destination are already mapped. We are given mappings for theses
		// but for testing I am randomly generating the mapping of these.\
		int originMapID = (int)(Math.random() * topology.getType().getNumberOfPhysicalNodes());
		int destinationMapID;
		do{
			destinationMapID = (int)(Math.random() * topology.getType().getNumberOfPhysicalNodes());
		}while(destinationMapID == originMapID);
		
		PhysicalNode originMap = topology.getNodes().get(originMapID);
		PhysicalNode destinationMap = topology.getNodes().get(destinationMapID);
		
		int speed;
		
		// Generate speed for origin virtual node
		//do{
		//	speed = Node.generateRandomComputationalSpeed();
		//}while(speed > originMap.getComputationalAvailability());
		
		VirtualNode originVirtualNode = new VirtualNode(0, Node.generateRandomComputationalSpeed());
		//originMap.setComputationAvailability(originMap.getComputationalAvailability() - speed); // Update computational availability
		originVirtualNode.setMap(originMapID);
		request.getVirtualNodes().add(originVirtualNode);
		
		for (int vnID = 1; vnID < numberOfVirtualNodes - 1; vnID ++){
			VirtualNode virtualNode = new VirtualNode(vnID, Node.generateRandomComputationalSpeed());
			virtualNode.setRequestedFunction(1 + (int)(Math.random() * 3));
			request.getVirtualNodes().add(virtualNode);
		}
		
		// Generate speed for destination virtual node
		//do{
		//	speed = Node.generateRandomComputationalSpeed();
		//}while(speed > destinationMap.getComputationalAvailability());
		
		VirtualNode destinationVirtualNode = new VirtualNode(numberOfVirtualNodes - 1, Node.generateRandomComputationalSpeed());
		//destinationMap.setComputationAvailability(destinationMap.getComputationalAvailability() - speed); // Update computational availability
		destinationVirtualNode.setMap(destinationMapID);
		request.getVirtualNodes().add(destinationVirtualNode);
		
		List<VirtualNode> nodes = request.getVirtualNodes();
		int currentVirtualNode;
		int nextVirtualNode;
		
		for (int virtualNode = 0; virtualNode < numberOfVirtualLinks; virtualNode++){
			currentVirtualNode = nodes.get(virtualNode).getID();
			nextVirtualNode = nodes.get(virtualNode + 1).getID();
			request.getVirtualLinks().add(new VirtualLink(new Pair<Integer, Integer>(currentVirtualNode, nextVirtualNode), Link.generateRandomBandwidth()));
		}
		
		
	}
}
