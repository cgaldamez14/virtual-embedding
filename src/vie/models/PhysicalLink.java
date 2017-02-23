package vie.models;

public class PhysicalLink {

	private int linkDistance;
	private int bandwidthAvailability;
	private Pair<Integer,Integer> endNodes;

	
	public PhysicalLink(int linkDistance, int bandwidthAvailability, Pair<Integer,Integer> endNodes) {
		
		this.linkDistance = linkDistance;
		this.bandwidthAvailability = bandwidthAvailability;
		this.endNodes = endNodes;
	}

	public int getLinkDistance(){
		return linkDistance;
	}
	
	public int getbandwidthAvailability(){
		return bandwidthAvailability;
	}
	
	public Pair<Integer, Integer> getEndNodes(){
		return endNodes;
	}
	
	public void setBandwidthAvailability(int bandwidthAvailability){
		this.bandwidthAvailability = bandwidthAvailability;
	}
	
	public String toString(){
		return "PHYSICAL LINK: " + endNodes.first() + " <---> " + endNodes.second() + " ; BANDWIDTH: " + bandwidthAvailability;
	}
}
