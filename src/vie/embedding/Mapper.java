package vie.embedding;

import java.util.List;

import vie.models.PhysicalLink;
import vie.models.PhysicalNode;
import vie.models.Topology;
import vie.models.VirtualNode;
import vie.models.VirtualRequest;

public abstract class Mapper {

	protected VirtualRequest virtualRequest;
	protected Topology topology;
	
	public Mapper(Topology topology , VirtualRequest virtualRequest){
		this.virtualRequest = virtualRequest;
		this.topology = topology;
	}
	
	protected abstract void releaseLinkResource();
	protected abstract void reserveLinkResource(int bandwidthDemand, List<PhysicalLink> links);
	protected abstract void releaseNodeResource();
	protected abstract void reserveNodeResource(int computeDemand, PhysicalNode physicalNode);
	
	protected abstract void attemptLinkMapping();
	protected abstract void attemptNodeMapping();
	
	public void enableAll(){
		for (VirtualNode vn: virtualRequest.getVirtualNodes()){
			if(vn.getMapID() != -1)
				topology.getNodes().get(vn.getMapID()).enable();
		}
	}
	
	public boolean attemptRequestMapping(){
		attemptNodeMapping();
		if(!virtualRequest.isBlocked())
			attemptLinkMapping();
		
		return !virtualRequest.isBlocked();}
}

