package vie.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import vie.models.Path;
import vie.models.PathNode;
import vie.models.PhysicalLink;
import vie.models.PhysicalNode;
import vie.models.Topology;

public class KShortestPaths {
	
	int K;
	
	Topology topology;
	
	int source;
	int destination;
	
	List<Path> shortestPaths;
	List<Path> potentialPaths;
	
	public KShortestPaths(int K, int source, int destination, Topology topology){
		this.K = K - 1;
		
		this.source = source;
		this.destination = destination;
		
		shortestPaths = new ArrayList<>();
		potentialPaths = new ArrayList<>();
		
		this.topology = topology;
	}
	
	public List<Path> getShortestPaths(){
		
				
		shortestPaths.add(new DijkstraShortestPath(topology, source, destination).getShortestPath());
		System.out.println("SHORTEST PATH:" + shortestPaths.get(0));

		
		for(int k = 1; k <= K; k++){
			for(int i = 0; i < shortestPaths.get(k-1).getNumberOfHops(); i++){
				System.out.println("------------------------------------------------");
				List<PhysicalLink> removedLinks = new ArrayList<>();
				
				int spurNode = shortestPaths.get(k-1).getNode(i).getNodeID();
				System.out.println("SPUR: " + spurNode);

				Path rootPath = shortestPaths.get(k-1).getNodes(i);
				System.out.println("ROOT PATH: " + rootPath);
				
				
				int distance = new DijkstraShortestPath(topology, rootPath.getStart().getNodeID(), spurNode).getShortestPath().getPathDistance();
				
				for (Path p : shortestPaths){
					if(rootPath.equals(p.getNodes(i))){
							System.out.println(p.getNode(i).getNodeID() + " " + p.getNode(i).next().getNodeID());
							PhysicalLink rLink = topology.getLink(p.getNode(i).getNodeID(), p.getNode(i).next().getNodeID());
							System.out.println("REMOVING: " + rLink);
							removedLinks.add(rLink);
							topology.getLinks().remove(rLink);
							
							topology.getNodes().get(p.getNode(i).getNodeID()).removeAdjacentNode(p.getNode(i).next().getNodeID());
							topology.getNodes().get(p.getNode(i).next().getNodeID()).removeAdjacentNode(p.getNode(i).getNodeID());

					}
				}
				
				
				PathNode current = rootPath.getStart();
				while(current.hasNext()){
					if(!current.hasNext()) break;
					System.out.println("DISABLING NODE " + current.getNodeID());
					for (Iterator<Map.Entry<Integer,Integer>> it = topology.getNodes().get(current.getNodeID()).getAdjacentNodes().entrySet().iterator();it.hasNext();) {
						Entry<Integer, Integer> curr = it.next();
						topology.getNodes().get(current.getNodeID()).saveRemovedAdjacentNode(curr.getKey());
						
						it.remove();
						topology.getNodes().get(curr.getKey()).removeAdjacentNode(current.getNodeID());
					}
					

					current = current.next();
				}
				
				Path spurPath = new DijkstraShortestPath(topology, spurNode, destination).getShortestPath();
				
				current = rootPath.getStart();
				while(current.hasNext()){
					topology.getNodes().get(current.getNodeID()).enable();
					current = current.next();
				}
				
				for(Entry<Integer, PhysicalNode> entry : topology.getNodes().entrySet()){
					entry.getValue().resetAdjacentNodes();
				}
				
				topology.getLinks().addAll(removedLinks);
				

				rootPath.append(spurPath);
				rootPath.setPathDistance(rootPath.getPathDistance() + distance);
				
				potentialPaths.add(rootPath);
				System.out.println(rootPath);

				System.out.println("------------------------------------------------");

				
			}
			if(potentialPaths.size() == 0) break;
			
			Collections.sort(potentialPaths, (n1, n2) -> new Integer(n1.getPathDistance()).compareTo(new Integer(n2.getPathDistance())));
			shortestPaths.add(potentialPaths.get(0));
			
			potentialPaths.remove(0);
		}
		
		System.out.println(shortestPaths);
		return shortestPaths;
	}
}
