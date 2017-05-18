package vie.embedding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import vie.models.Path;
import vie.models.PathNode;
import vie.models.PhysicalLink;
import vie.models.PhysicalNode;
import vie.models.Topology;
import vie.models.VirtualLink;
import vie.models.VirtualNode;
import vie.models.VirtualRequest;
import vie.utilities.DijkstraShortestPath;

/**
 * The Algorithm 2 class is a implementation of node mapping and link mapping of virtual requests.
 * Algorithm two finds the possible candidate physical nodes containing the requested network function and 
 * computation requirement for each of the intermediate virtual nodes. It is assumed that the source node
 * and destination node are already mapped. From the potential candidate nodes, algorithm one will map the virtual
 * node to the closest candidate node to the <strong>destination node</strong>.
 * @author Carlos M. Galdamez
 *
 */
public class Algorithm2 extends NodeMapper{
	
	/**
	 * Constructor calls the constructor of the super class.
	 * @param topology - topology being used for mapping.
	 * @param virtualRequest - virtual request that will be mapped.
	 */
	public Algorithm2(Topology topology , VirtualRequest virtualRequest){
		super(topology,virtualRequest);
	}
	
	/**
	 * Attempts to map each intermediate node in the virtual request to the
	 * given physical topology. It mapping is not possible request will be blocked.
	 */
	@Override
	protected void attemptNodeMapping(){
		List<VirtualNode> vns = virtualRequest.getVirtualNodes();
		Map<Integer,PhysicalNode> pns = topology.getNodes();
		
		pns.get(vns.get(0).getMapID()).disable();							// disable physical node mapped to origin 
		pns.get(vns.get(vns.size() - 1).getMapID()).disable();				// disable physical node mapped to destination
		
		for (int i = 1 ; i < vns.size() - 1; i++){							// no mapping needed for origin and destination
			
			// Demands for current virtual node
			VirtualNode currentVN = vns.get(i);
			int computeDemand = currentVN.getComputationalDemand();
			int requestedFunction = currentVN.getRequestedFunctionID();
			
			Integer closestNodeID = null;
			int shortestDistance = Integer.MAX_VALUE;
			
			// Iterate through all physical nodes to see if they are possible candidates
			for(Entry<Integer, PhysicalNode> pn : pns.entrySet()){
				
				// Current physical node availability
				PhysicalNode currentPN = pn.getValue();
				int computeAvailability = currentPN.getComputationalAvailability();
				
				/* FIRST CHECK: Makes sure if any prior virtual nodes have been mapped to the current physical node */
				if (currentPN.isDisabled()) continue;
				
				/* SECOND CHECK: Makes sure that the current node has the requests network function */
				if (currentPN.getNetworkFunction(0) == requestedFunction || currentPN.getNetworkFunction(1) == requestedFunction){
					Path path = new DijkstraShortestPath(topology, vns.get(vns.size() - 1).getMapID(), currentPN.getID()).getShortestPath();
					int distance = path.getPathDistance();
					
					/* THIRD CHECK: Makes sure that the candidate node meets the computational demand and
					 *              is the closest node to the mapping of the destination virtual node. */
					if(distance < shortestDistance && computeAvailability >= computeDemand){
						shortestDistance = distance;
						closestNodeID = currentPN.getID();
					}
				}
				
			}
			
			/* If we find a candidate node then we reserve the resources and disable the node
			 * so no other node in the same request get mapped to the same node. If there
			 * is no candidate node then all reserved resources will be released and request will be 
			 * blocked. */
			if (closestNodeID != null){
				currentVN.setMap(closestNodeID);
				pns.get(closestNodeID).disable();
				reserveNodeResource(computeDemand, pns.get(closestNodeID));
			}
			else{
				releaseNodeResource();
				virtualRequest.block();
				break;
			}
		}
		
		// Enable all nodes so other mapper classed can use them.
		enableAll();
	}
	
	/**
	 * Attempts to map all virtual links in the virtual request to links in the 
	 * given physical topology. If whole path cannot be successfully mapped
	 * request will be blocked.<br>
	 * 
	 * <strong>NOTE:</strong> Node mapping must be completed before this step.
	 */
	@Override
	protected void attemptLinkMapping(){
		List<VirtualLink> vls = virtualRequest.getVirtualLinks();
		
		for(int i = 0; i < vls.size(); i++){
			
			// Virtual link demands
			VirtualLink currentVL = vls.get(i);
			int bandwidthDemand = currentVL.getbandwidthDemand();
			int originMapID = virtualRequest.getVirtualNodes().get(currentVL.getOriginID()).getMapID();
			int destinationMapID = virtualRequest.getVirtualNodes().get(currentVL.getDestinationID()).getMapID();
			
			// All paths are considered valid in the beginning, then they are checked.
			boolean validPath = true;
			
			// Find the shortest path from the source mapping to the destination mapping
			Path path = new DijkstraShortestPath(topology, originMapID, destinationMapID).getShortestPath();
			
			PathNode current = path.getStart();
			PathNode next = current.next();
			
			/* Links list will be used to keep track of links
			 * which can be successfully be mapped. */
			List<PhysicalLink> links = new ArrayList<>();
			
			while (next.hasNext()){
				// Current link in the shortest path
				PhysicalLink link = topology.getLink(current.getNodeID(), next.getNodeID());
				
				// If link does not meet bandwidth demand then break loop.
				if(link.getbandwidthAvailability() < bandwidthDemand){
					validPath = false;
					break;
				}
				
				// Add link to link list if bandwidth demands are met.
				links.add(link);
				current = next;
				next= current.next();
			}
			
			/* If all the links in the shortest path are valid then the resources will 
			 * reserved and virtual link will be mapped to a path in the physical topology.
			 * Otherwise all link resources will be released and request will be blocked.
			 */
			if (validPath) {
				currentVL.setLinkMapping(path);
				reserveLinkResource(bandwidthDemand, links);
			}
			else{
				releaseLinkResource();
				virtualRequest.block();
				break;
			}
		}
	}
}
