package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Value;

@Value
public class Constant {
	
	public static final Constant MAZUB_END_MOVE_TIME = new Constant(1.0);
	public static final Constant MAZUB_FEATURE_TIME = new Constant(0.2);
	public static final Constant MAZUB_SHARK = new Constant(-50);
	public static final Constant MAZUB_SLIME = new Constant(-20);
	public static final Constant MAZUB_SPIDER = new Constant(-15);
	public static final Constant MAZUB_LIVING_PLANT = new Constant(50);
	public static final Constant MAZUB_DEAD_PLANT = new Constant(-20);
	public static final Constant MAZUB_MAGMA = new Constant(-50);
	public static final Constant MAZUB_GAS = new Constant(-4);
	public static final Constant MAZUB_WATER = new Constant(-2); 
	public static final Constant PLANT_SWITCH_TIME = new Constant(0.5);
	public static final Constant SLIME_MAZUB = new Constant(-30); 
	public static final Constant SLIME_WATER = new Constant(-4); 
	public static final Constant SLIME_GAS = new Constant(2);
	public static final Constant SLIME_WATER_TIME = new Constant(0.4);
	public static final Constant SLIME_GAS_TIME = new Constant(0.3); 
	public static final Constant SHARK_REST_TIME = new Constant(1.0); 
	public static final Constant SHARK_SWITCH_TIME = new Constant(0.5); 
	public static final Constant SHARK_MAZUB = new Constant(-50); 
	public static final Constant SHARK_SLIME = new Constant(10); 
	public static final Constant SHARK_OUT_WATER = new Constant(-6); 
	public static final Constant SHARK_OUT_WATER_TIME = new Constant(0.2);
	public static final Constant BLOCK = new Constant(0.6); 
	
	private double value;
	
	private Constant(double value) {
		this.value = value;
	}
	
	@Basic
	public double getValue() {
		return Double.valueOf(value);
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Constant)) return false;
		else return this.getValue() == ((Constant) other).getValue();
	}
	
	@Override 
	public int hashCode() {
		return Integer.valueOf(this.hashCode());
	}
	
	@Override
	public String toString() {
		return "[ Constant: " + getValue() + " ]";
	}
}
