package vie.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PhysicalNode {

	public static int nodeCount = 0;
	
	private int id;
	private int computationalAvailability;
	
	private boolean disabled = false;
	
	private List<Integer> networkFunctions;
	private Map<Integer, Integer> adjacentNodes;
	
	private Map<Integer, Integer> removedAdjacentNodes;
	
	
	public PhysicalNode(int computationalAvailability) {
		this.computationalAvailability = computationalAvailability;
		this.networkFunctions = new ArrayList<>();
		this.adjacentNodes = new HashMap<>();
		this.removedAdjacentNodes = new HashMap<>();
		this.id = nodeCount++;
	}
	
	public void removeAdjacentNode(int id){
		removedAdjacentNodes.put(id, adjacentNodes.get(id));
		adjacentNodes.remove(id);
	}
	
	public void saveRemovedAdjacentNode(int id){
		removedAdjacentNodes.put(id, adjacentNodes.get(id));
	}
	
	public void resetAdjacentNodes(){
		adjacentNodes.putAll(removedAdjacentNodes);
	}

	
	//------------------------------ MUTATORS ------------------------------//
	
	public int addNetworkFunction(int functionID) {
		networkFunctions.add(functionID);
		return functionID;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public void setComputationAvailability(int computationalAvailability){
		this.computationalAvailability = computationalAvailability;
	}

	//------------------------------ ACCESSORS -----------------------------//
	
	public int getID() {
		return id;
	}

	public int getComputationalAvailability() {
		return computationalAvailability;
	}

	public Map<Integer, Integer> getAdjacentNodes() {
		return adjacentNodes;
	}
	
	public int getNetworkFunction(int i){
		return networkFunctions.get(i);
	}
	
	// ---------------------------- BOOLEAN ---------------------------------//
	
	public void disable(){
		disabled = true;
	}
	
	public void enable(){
		disabled = false;
	}
	
	public boolean isDisabled(){
		return disabled;
	}
	
	public String toString(){
		return "PHYSICAL NODE: " + id;
	}
}
