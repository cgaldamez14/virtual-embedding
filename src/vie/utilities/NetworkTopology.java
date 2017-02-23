package vie.utilities;

import java.io.IOException;

public enum NetworkTopology {

	NSFNET(14,21,"Network-NSF.txt"),
	US_MESH(24,23,"USmesh.txt");
	
	private int numberOfPhysicalNodes;
	private int numberOfPhysicalLinks;
	private String fileName;
	
	NetworkTopology(int numberOfPhysicalNodes, int numberOfPhysicalLinks, String filename){
		this.numberOfPhysicalNodes = numberOfPhysicalNodes;
		this.numberOfPhysicalLinks = numberOfPhysicalLinks;
		this.fileName = filename;
	}
	
	public int getNumberOfPhysicalNodes(){ return numberOfPhysicalNodes; }
	
	public int getNumberOfPhysicalLinks(){ return numberOfPhysicalLinks; }
	
	public String getFilePath() throws IOException{ return new java.io.File(".").getCanonicalPath() + "/src/vie/assets/" + fileName;  }

	
}
