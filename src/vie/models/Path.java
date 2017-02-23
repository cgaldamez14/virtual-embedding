package vie.models;

public class Path {

	private int pathDistance;
	private int throughput;
	
	private PathNode start;
	
	public Path(int originNodeID){
		start = new PathNode(originNodeID);
	}
	
	public int getThroughput(){
		return throughput;
	}
	
	public void setThroughput( int throughput){
		this.throughput = throughput;
	}
	
	public int getPathDistance(){
		return pathDistance;
	}
	
	public void setPathDistance(int pathDistance){
		this.pathDistance = pathDistance;
	}
	
	public PathNode getStart(){
		return start;
	}
	
	public void append(Path path){
		PathNode current = start;
		
		while(current.hasNext())
			current = current.next();
		
		if(current.getNodeID() == path.getStart().getNodeID())
			current.setNext(path.getStart().next());
		else
			current.setNext(path.getStart());
		
		pathDistance += path.getPathDistance();
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder("PATH: ");
		
		PathNode current = start;
		
		do{
			sb.append(current.getNodeID() + ((current.hasNext())? " --> " : ""));
			current = current.next();
		}while (current != null);
		
		sb.append("\nPath Distance: " + pathDistance);
		sb.append("\nThroughput: " + throughput + "\n");
		
		return sb.toString();
	}
	
	
}
