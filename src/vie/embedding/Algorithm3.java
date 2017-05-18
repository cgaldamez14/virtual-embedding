package vie.embedding;

import java.util.ArrayList;
import java.util.List;

import vie.models.Path;
import vie.models.PathNode;
import vie.models.PhysicalLink;
import vie.models.PhysicalNode;
import vie.models.Topology;
import vie.models.VirtualLink;
import vie.models.VirtualNode;
import vie.models.VirtualRequest;
import vie.utilities.DijkstraShortestPath;

public class Algorithm3 extends NodeMapper{
	
	/**
	 * Constructor calls the constructor of the super class.
	 * @param topology - topology being used for mapping.
	 * @param virtualRequest - virtual request that will be mapped.
	 */
	public Algorithm3(Topology topology , VirtualRequest virtualRequest){
		super(topology,virtualRequest);
	}
	
	@Override
	protected void attemptNodeMapping(){
		List<List<PhysicalNode>> subsets = topology.getFunctionSubsets();
		
		topology.getNodes().get(virtualRequest.getVirtualNodes().get(0).getMapID()).disable();															// disable physical node mapped to origin 
		topology.getNodes().get(virtualRequest.getVirtualNodes().get(virtualRequest.getVirtualNodes().size() - 1).getMapID()).disable();				// disable physical node mapped to destination
		
		List<VirtualNode> virtualNodes = virtualRequest.getVirtualNodes();
		for(int i = 1; i < virtualRequest.getVirtualNodes().size() - 1;i++){
			int shortest = Integer.MAX_VALUE;
			PhysicalNode best = null;
			
			VirtualNode currentVN = virtualNodes.get(i);
			int computationalDemand = currentVN.getComputationalDemand();
			int requestedFunction = currentVN.getRequestedFunctionID();
			
			for(PhysicalNode n: subsets.get(requestedFunction - 1)){
				if(n.isDisabled() || n.getComputationalAvailability() < computationalDemand )continue;

				int distance =  topology.getPathDictionary().get(virtualNodes.get(i-1).getMapID()).get(n.getID()).getPathDistance();
				if(distance < shortest){
					shortest = distance;
					best = n;
				}
			}
			
			if(best != null){
				reserveNodeResource(computationalDemand, best);
				currentVN.setMap(best.getID());
				best.disable();
			}
			else{
				releaseNodeResource();
				virtualRequest.block();
				break;
			}
		}
		
		
		enableAll();

	}
	
	protected void attemptLinkMapping(){
		for(VirtualLink link : virtualRequest.getVirtualLinks()){
			List<PhysicalLink> links = new ArrayList<>();
			
			int bandwidthDemand = link.getbandwidthDemand(); 


			boolean validPath = true;
			Path path = topology.getPathDictionary()
					.get(virtualRequest.getVirtualNodes().get(link.getOriginID()).getMapID())
					.get(virtualRequest.getVirtualNodes().get(link.getDestinationID()).getMapID());
			
			PathNode current = path.getStart();
			PathNode next = current.next();
			
			
			while (next.hasNext()){
				PhysicalLink l = topology.getLink(current.getNodeID(), next.getNodeID());
				
				if(l.getbandwidthAvailability() < bandwidthDemand){
					validPath = false;
					break;
				}
				
				links.add(l);
				current = next;
				next= current.next();
			}
			
			if (validPath) {
				link.setLinkMapping(path);
				reserveLinkResource(bandwidthDemand, links);
			}
			else{
				releaseLinkResource();
				virtualRequest.block();
				break;
			}
			
		}
	}
	
	@Override
	public boolean attemptRequestMapping(){	
		setDictionary();
		return super.attemptRequestMapping();
	}
	
	private void setDictionary(){
		
		List<VirtualNode> virtualNodes = virtualRequest.getVirtualNodes();
		
		if(virtualNodes.size() == 2){
			Path path = new DijkstraShortestPath(topology, virtualNodes.get(0).getMapID(), virtualNodes.get(1).getMapID()).getShortestPath();
			topology.getPathDictionary().get(virtualNodes.get(0).getMapID()).put( virtualNodes.get(1).getMapID(), path);
			return;
		}
		
		
		List<List<PhysicalNode>> subsets = topology.getFunctionSubsets();
		
		List<PhysicalNode> origins = new ArrayList<>();
		origins.add(topology.getNodes().get(virtualNodes.get(0).getMapID()));
		int functionOfInterest = virtualNodes.get(1).getRequestedFunctionID();
		generateList(origins,subsets.get(functionOfInterest-1),1);

	}
	
	private void generateList(List<PhysicalNode> origins,  List<PhysicalNode> destinations, int next){
		if (next == -1) return;
		
		for(PhysicalNode pno: origins){
			for(PhysicalNode pnd: destinations){
				if(pno.getID() != pnd.getID()){
					Path path = new DijkstraShortestPath(topology, pno.getID(), pnd.getID()).getShortestPath();
					topology.getPathDictionary().get(pno.getID()).put(pnd.getID(), path);
				}
			}
		}
		
		if(next+1 == virtualRequest.getVirtualNodes().size() - 1){
			List<PhysicalNode> last = new ArrayList<>();
			last.add(topology.getNodes().get(virtualRequest.getVirtualNodes().get(next+1).getMapID()));
			generateList(destinations, last,next + 1);
		}
		else if (next == virtualRequest.getVirtualNodes().size() - 1){
			generateList(null, null,-1);
		}
		else{
		int functionOfInterest = virtualRequest.getVirtualNodes().get(next+1).getRequestedFunctionID();
		generateList(destinations, topology.getFunctionSubsets().get(functionOfInterest-1),next + 1);
		}
	}

}
