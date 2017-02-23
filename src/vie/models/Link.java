package vie.models;

public class Link {

	public static int generateRandomBandwidth() {
		
		int bandwidth = (int)(Math.random() * 4);      // Random number between 0-4
		
		switch(bandwidth){
		case 0: return 10;
		case 1: return 40;
		case 2: return 100;
		case 3: return 200;
		//case 4: return 400;
		//case 5: return 1000;
		default: return -1;
		}
	}

}
