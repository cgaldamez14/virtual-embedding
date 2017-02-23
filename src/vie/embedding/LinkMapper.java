package vie.embedding;

import java.util.List;

import vie.models.Path;
import vie.models.PathNode;
import vie.models.PhysicalLink;
import vie.models.Topology;
import vie.models.VirtualLink;
import vie.models.VirtualRequest;

public abstract class LinkMapper extends Mapper {

	public LinkMapper(Topology topology, VirtualRequest virtualRequest) {
		super(topology, virtualRequest);
	}

	
	@Override
	protected void releaseLinkResource() {
		for(VirtualLink link: virtualRequest.getVirtualLinks()){
			if(link.getLinkMapping() == null) continue;
			Path path = link.getLinkMapping();
			
			PathNode current = path.getStart();
			PathNode next = current.next();
				
			while (next.hasNext()){
				PhysicalLink l = topology.getLink(current.getNodeID(), next.getNodeID());
				l.setBandwidthAvailability(l.getbandwidthAvailability() + link.getbandwidthDemand());
			
				current = next;
				next= current.next();
			}
		}
		
	}

	@Override
	protected void reserveLinkResource(int bandwidthDemand, List<PhysicalLink> links) {
		for(PhysicalLink link: links){
			link.setBandwidthAvailability(link.getbandwidthAvailability() - bandwidthDemand);
		}
	}
}
