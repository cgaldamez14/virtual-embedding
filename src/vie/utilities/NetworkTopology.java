package vie.utilities;

import java.io.IOException;

/**
 * Network Topology enum is used to acquire information regarding the available adjacency matrices.
 * Each enum provides the number of physical nodes , number of physical links and the file path of the
 * containing the adjacency matrix for the available topologies. 
 * @author Carlos M. Galdamez
 *
 */
public enum NetworkTopology {

	SIMPLE(6,9,"Simple.txt"),
	NSFNET(14,21,"Network-NSF.txt"),
	US_MESH(24,23,"USmesh.txt");
	
	private int numberOfPhysicalNodes;
	private int numberOfPhysicalLinks;
	private String fileName;
	
	/**
	 * Constructor for enum.
	 * @param numberOfPhysicalNodes
	 * @param numberOfPhysicalLinks
	 * @param filename
	 */
	NetworkTopology(int numberOfPhysicalNodes, int numberOfPhysicalLinks, String filename){
		this.numberOfPhysicalNodes = numberOfPhysicalNodes;
		this.numberOfPhysicalLinks = numberOfPhysicalLinks;
		this.fileName = filename;
	}
	
	/**
	 * Gets the total number of physical nodes in the topology.
	 * @return integer representing number of physical nodes in the topology.
	 */
	public int getNumberOfPhysicalNodes(){ return numberOfPhysicalNodes; }
	
	/**
	 * Gets the total number of physical links in the topology.
	 * @return integer representing number of physical links in the topology.
	 */
	public int getNumberOfPhysicalLinks(){ return numberOfPhysicalLinks; }
	
	/**
	 * Gets the file path of the adjacency matrix.
	 * @return String representation of the file path of the adjacency matrix of the topology.
	 * @throws IOException
	 */
	public String getFilePath() throws IOException{ return new java.io.File(".").getCanonicalPath() + "/src/vie/assets/" + fileName;  }

	
}
