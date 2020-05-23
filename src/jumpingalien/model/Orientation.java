package jumpingalien.model;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Value;

@Value
public class Orientation {

	public static final Orientation POSITIVE = new Orientation(1);
	public static final Orientation NEUTRAL = new Orientation(0);
	public static final Orientation NEGATIVE = new Orientation(-1);

	private int value;

	private Orientation(int value){
		this.value = value;
	}

	@Basic
	public int getValue(){
		return this.value;
	}

	@Override
	public int hashCode(){
		return getValue();
	}

	@Override
	public boolean equals(Object other){
		if(!(other instanceof  Orientation)) return false;
		else return this.getValue() == ((Orientation) other).getValue();
	}
	
	@Override
	public String toString() {
		return "[Orientation:" + getValue() + "]";
	}
	
}
