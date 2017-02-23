package vie.embedding;

import vie.models.PhysicalNode;
import vie.models.Topology;
import vie.models.VirtualNode;
import vie.models.VirtualRequest;

public abstract class NodeMapper extends LinkMapper {	
	
	public NodeMapper(Topology topology, VirtualRequest virtualRequest) {
		super(topology, virtualRequest);
	}

	@Override
	protected void releaseNodeResource() {
		for (VirtualNode vn: virtualRequest.getVirtualNodes()){
			if(vn.getMapID() == -1) continue;
				
			PhysicalNode currentMap = topology.getNodes().get(vn.getMapID());
			currentMap.setComputationAvailability(currentMap.getComputationalAvailability() + vn.getComputationalDemand());
		}
		
	}

	@Override
	protected void reserveNodeResource(int computeDemand, PhysicalNode physicalNode) {
		physicalNode.setComputationAvailability(physicalNode.getComputationalAvailability() - computeDemand);
	}
}
