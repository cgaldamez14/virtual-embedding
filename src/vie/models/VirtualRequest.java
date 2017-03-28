package vie.models;

import java.util.ArrayList;
import java.util.List;

public class VirtualRequest {

	private List<VirtualNode> virtualNodes;
	private List<VirtualLink> virtualLinks;

	private boolean blocked = false;
	
	public VirtualRequest(int numberOfVirtualNodes, int numberOfVirtualLinks) {
		virtualNodes = new ArrayList<>();
		virtualLinks = new ArrayList<>();
	}

	public List<VirtualNode> getVirtualNodes() {
		return virtualNodes;
	}

	public List<VirtualLink> getVirtualLinks() {
		return virtualLinks;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("Virtual Node Request Information: \n");
		sb.append("--------------------------------- \n");
		
		for (VirtualNode vn: virtualNodes)
			sb.append(vn + "\n");
		
		sb.append("\nVirtual Link Request Information: \n");
		sb.append("--------------------------------- \n");

		
		Path p = new Path(virtualNodes.get(0).getMapID());
		for (VirtualLink vl: virtualLinks){
			p.append(vl.getLinkMapping());
			sb.append(vl + "\n");
		}
		
		sb.append("\n" + p);
		
		return sb.toString();
	}

	public void block() {
		blocked = true;
		
	}
	
	public void unblock(){
		blocked = false;
	}

	public boolean isBlocked() {
		return blocked;
	}

}
