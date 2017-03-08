package vie.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import vie.utilities.NetworkTopology;

public class Topology {

	public static final int COMPUTATIONAL_AVAILABILITY = 500000;
	public static final int BANDWIDTH_AVAILABILITY = 1000000;
	
	//public static final int COMPUTATIONAL_AVAILABILITY = 1500;
	//public static final int BANDWIDTH_AVAILABILITY = 7500;
	
	private Map<Integer, PhysicalNode> nodes;	
	private List<PhysicalLink> links;
	
	private List<List<PhysicalNode>> subsets;
	
	private Map<Integer,Map<Integer,Path>> pathDictionary;

	private NetworkTopology type;
	public int requestsMapped = 0;
	
	public Topology(NetworkTopology type){
		this.type = type;
		subsets = new ArrayList<>();
		
		pathDictionary = new HashMap<>();
		for(int i = 0; i < type.getNumberOfPhysicalNodes();i++){
			pathDictionary.put(i,new HashMap<>());
		}
	}
	
	
	public Map<Integer, PhysicalNode> getNodes() {
		return nodes;
	}

	public List<PhysicalLink> getLinks(){
		return links;
	}
	
	public PhysicalLink getLink(int origin, int destination){
		for(PhysicalLink link: links){
			Pair<Integer,Integer> endNodes = link.getEndNodes();
			if((endNodes.first() == origin && endNodes.second() == destination) || (endNodes.second() == origin && endNodes.first() == destination))
				return link;
		}
		return null;
	}
	

	public NetworkTopology getType() {
		return type;
	}


	public void setNodes(Map<Integer, PhysicalNode> nodes) {
		this.nodes = nodes;
	}
	
	public void setLinks(List<PhysicalLink> links) {
		this.links = links;
	}


	public Map<Integer,Map<Integer,Path>> getPathDictionary() {
		return pathDictionary;
	}


	public void setPathDictionary(Map<Integer,Map<Integer,Path>> pathDictionary) {
		this.pathDictionary = pathDictionary;
	}
	
	public void setSubsets(){
		
		subsets.add(new ArrayList<>());
		subsets.add(new ArrayList<>());
		subsets.add(new ArrayList<>());
		
		for(Entry<Integer, PhysicalNode> entry : nodes.entrySet()){
			subsets.get(entry.getValue().getNetworkFunction(0) - 1).add(entry.getValue());
			subsets.get(entry.getValue().getNetworkFunction(1) - 1).add(entry.getValue());
		}
	}
	
	public List<List<PhysicalNode>> getSubsets(){
		return subsets;
	}
	
	public int getTotalBandwidthConsumption(){
		int sum = 0;
		
		for(PhysicalLink link: links){
			sum += (BANDWIDTH_AVAILABILITY - link.getbandwidthAvailability());
		}
		
		return sum;
	}

}
