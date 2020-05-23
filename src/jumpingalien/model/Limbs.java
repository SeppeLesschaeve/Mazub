package jumpingalien.model;

/**
 * This class is able to hold and to change the number of limbs of an animal with limbs
 * 
 * @invar ...
 * 		| getLegs() >= 0
 * @invar ...
 * 		| getArms() >= 0
 * 
 * @author Seppe Lesschaeve (Informatica)
 * @version 2.0
 *
 */
public class Limbs {

	private int arms;
	private int legs;
	
	/**
	 * This constructor sets limbs
	 * 
	 * @param legs
	 * 			This parameter is used as the number of legs
	 * 
	 * @pre ...
	 * 		| legs >= 2
	 * @effect ...
	 * 		| setArms(arms)  && setLegs(legs)
	 */
	public Limbs(int arms, int legs) {
		setArms(arms);
		setLegs(legs);
	}
	
	/**
	 * This method returns the number of arms
	 * 
	 * @return ...
	 * 		| result == Integer.valueof(this.arms)
	 */
	public int getArms() {
		return Integer.valueOf(arms);
	}
	
	/**
	 * This method changes the number of arms
	 * 
	 * @param arms
	 * 			the number of arms
	 * 
	 * @pre  ...
	 * 		|arms >= 0
	 * @post ...
	 * 		| new.getArms() == arms
	 */
	public void setArms(int arms) {
		this.arms = arms;
	}
	
	/**
	 * This method returns the number of legs
	 * 
	 * @return ...
	 * 		| result == Integer.valueof(this.legs)
	 */
	public int getLegs() {
		return Integer.valueOf(legs);
	}
	
	/**
	 * This method changes the number of legs
	 * 
	 * @param legs
	 * 			the number of legs
	 * 
	 * @pre  ...
	 * 		|legs >= 0
	 * @post ...
	 * 		| new.getLegs() == legs
	 */
	public void setLegs(int legs) {
		this.legs = legs;
	}
	
}
