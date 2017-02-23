package vie.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhysicalNode {

	private static int nodeCount = 0;
	
	private int id;
	private int computationalAvailability;
	
	private boolean disabled = false;
	
	private List<Integer> networkFunctions;
	private Map<Integer, Integer> adjacentNodes;
	
	
	public PhysicalNode(int computationalAvailability) {
		this.computationalAvailability = computationalAvailability;
		this.networkFunctions = new ArrayList<>();
		this.adjacentNodes = new HashMap<>();
		this.id = nodeCount++;
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
