package vie.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vie.models.Pair;
import vie.models.PhysicalLink;
import vie.models.PhysicalNode;
import vie.models.Topology;

public class TopologyUtil {

	private final static int SPACE = 32;
	private final static int NEW_LINE = 10;
	private final static int TAB = 9;
	
	public static Topology readAdjacencyMatrix(NetworkTopology type) throws IOException{
		
		Topology topology = new Topology(type);
		//System.out.println(topology);
		
		int numberOfNodes = type.getNumberOfPhysicalNodes();
		
		topology.setNodes(createPhysicalNodes(numberOfNodes, Topology.COMPUTATIONAL_AVAILABILITY));
		topology.setLinks(createPhysicalLinks(topology));
		
		topology.setSubsets();

		return topology;
		
	}
	
	private static Map<Integer, PhysicalNode> createPhysicalNodes(int numberOfNodes, int compute){
		
		Map<Integer,PhysicalNode> nodes = new HashMap<>();
		PhysicalNode node = null;
		
		for ( int i = 0; i < numberOfNodes; i++){
			node = new PhysicalNode(Topology.COMPUTATIONAL_AVAILABILITY);
			int fstNetworkFunction = node.addNetworkFunction(1 + (int)(Math.random() * 3));
			int sndNetworkFunction;
			do{
				sndNetworkFunction = node.addNetworkFunction(1 + (int)(Math.random() * 3));
			}while(fstNetworkFunction == sndNetworkFunction);
			node.addNetworkFunction(sndNetworkFunction);
			nodes.put(node.getID(), node);
		}
		
		node.nodeCount = 0;
		
		//System.out.println(nodes);
		return nodes;
	}
	
	private static List<PhysicalLink> createPhysicalLinks(Topology topology) throws IOException{
		
		List<PhysicalLink> links = new ArrayList<>();
		FileInputStream input = new FileInputStream(new java.io.File(topology.getType().getFilePath()));
		Map<Integer, PhysicalNode> nodes = topology.getNodes();
		
		int linkDistanceInt;
		String linkDistanceStr = "";
		int currentNode = 0, adjacentNode = 0;
		int character;
		
		while (input.available() > 0){
			character = input.read();
			if(character == SPACE || character == TAB || character == NEW_LINE){
				if (linkDistanceStr != ""){
					linkDistanceInt = Integer.parseInt(linkDistanceStr);
					if(linkDistanceInt > 0){
						nodes.get(currentNode).getAdjacentNodes().put(adjacentNode, linkDistanceInt);
						if(adjacentNode > currentNode){
							links.add(new PhysicalLink (linkDistanceInt, Topology.BANDWIDTH_AVAILABILITY, new Pair<>(currentNode, adjacentNode)));
						}
					}
					adjacentNode++;
				}
				if (character == NEW_LINE){
					currentNode++;
					adjacentNode = 0;
				}
				linkDistanceStr = "";
				continue;
			}
			linkDistanceStr += (character - 48);
		}
		
		input.close();
		
		return links;
	}
}
