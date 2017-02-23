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

public class Algorithm1 extends NodeMapper{

	public Algorithm1(Topology topology , VirtualRequest virtualRequest){
		super(topology,virtualRequest);
	}
	
	@Override
	protected void attemptNodeMapping(){
		List<VirtualNode> vns = virtualRequest.getVirtualNodes();
		Map<Integer,PhysicalNode> pns = topology.getNodes();
		
		pns.get(vns.get(0).getMapID()).disable();							// disable physical node mapped to origin 
		pns.get(vns.get(vns.size() - 1).getMapID()).disable();				// disable physical node mapped to destination
		
		for (int i = 1 ; i < vns.size() - 1; i++){							// no mapping needed for origin and destination
			
			VirtualNode currentVN = vns.get(i);
			int computeDemand = currentVN.getComputationalDemand();
			int requestedFunction = currentVN.getRequestedFunctionID();
			
			Integer closestNodeID = null;
			int shortestDistance = Integer.MAX_VALUE;
			
			for(Entry<Integer, PhysicalNode> pn : pns.entrySet()){
				
				PhysicalNode currentPN = pn.getValue();
				int computeAvailability = currentPN.getComputationalAvailability();
				
				/* First check makes sure if any prior virtual nodes have been mapped to the current physical node */
				if (currentPN.isDisabled()) continue;
				
				if (currentPN.getNetworkFunction(0) == requestedFunction || currentPN.getNetworkFunction(1) == requestedFunction){
					Path path = new DijkstraShortestPath(topology, vns.get(i - 1).getMapID(), currentPN.getID()).getShortestPath();
					int distance = path.getPathDistance();
					
					if(distance < shortestDistance && computeAvailability >= computeDemand){
						shortestDistance = distance;
						closestNodeID = currentPN.getID();
					}
				}
				
			}
			
			if (closestNodeID != null){
				currentVN.setMap(closestNodeID);
				pns.get(closestNodeID).disable();
				reserveNodeResource(computeDemand, pns.get(closestNodeID));
			}else{
				releaseNodeResource();
				virtualRequest.block();
				break;
			}
		}
		
		enableAll();
	}
	
	@Override
	protected void attemptLinkMapping(){
		List<VirtualLink> vls = virtualRequest.getVirtualLinks();
		
		for(int i = 0; i < vls.size(); i++){
			VirtualLink currentVL = vls.get(i);
			int bandwidthDemand = currentVL.getbandwidthDemand();
			int originMapID = virtualRequest.getVirtualNodes().get(currentVL.getOriginID()).getMapID();
			int destinationMapID = virtualRequest.getVirtualNodes().get(currentVL.getDestinationID()).getMapID();
			
			
			boolean validPath = true;
			Path path = new DijkstraShortestPath(topology, originMapID, destinationMapID).getShortestPath();
			
			PathNode current = path.getStart();
			PathNode next = current.next();
			
			List<PhysicalLink> links = new ArrayList<>();
			
			while (next.hasNext()){
				PhysicalLink link = topology.getLink(current.getNodeID(), next.getNodeID());
				if(link.getbandwidthAvailability() < bandwidthDemand){
					validPath = false;
					break;
				}
				
				links.add(link);
				current = next;
				next= current.next();
			}
			
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
