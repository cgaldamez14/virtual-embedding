package vie.models;

public class Pair<T1,T2> {

	private T1 object1;
	private T2 object2;
	
	public Pair(T1 object1, T2 object2){
		this.object1 = object1;
		this.object2 = object2;
	}
	
	public T1 first(){
		return object1;
	}
	
	public T2 second(){
		return object2;
	}
	
	public String toString(){
		return "( " + first() + " , " + second() + " )";
	}
	
}
