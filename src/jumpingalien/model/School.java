package jumpingalien.model;

import java.util.*;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * This class holds a TreeMap of slimes as values and their id as key where slimes can be added and removed
 * and can be linked to a world
 * 
 * @invar ...
 * 		| hasProperSlimes()
 * @invar ...
 * 		| hasProperWorld()
 * 
 * @version 4.0
 * @author Seppe Lesschaeve (Informatica)
 *
 */
public class School{

	private TreeMap<Long, Slime> slimes = new TreeMap<>();
	private World world;
	
	/**
	 * This constructor will set the world
	 * 
	 * @param world
	 * 			This parameter is used as world
	 * 
	 * @throws IllegalStateException
	 * 		...
	 * 		|world != null && !world.canHaveAsSchool(this)
	 * 
	 * @post ...
	 * 		|this.setWorld(world)
	 *		|if(this.getWorld() != null) then this.getWorld().setSchool(this)
	 *
	 */
	public School(World world) throws IllegalStateException{
		if(world != null && !world.canHaveAsSchool(this)) throw new IllegalStateException();
		this.setWorld(world);
		if(this.getWorld() != null) {this.getWorld().setSchool(this);}
	}

	/**
	 * This method returns the school
	 * 
	 * @return ...
	 * 		| Set<Slime> school = new HashSet<>()
	 *		|this.slimes.values().stream().forEach(slime -> school.add(slime))
	 *		|return school
	 *
	 */
	@Basic
	public Set<Slime> getSlimes(){
		Set<Slime> school = new HashSet<>();
		this.slimes.values().stream().forEach(slime -> school.add((Slime) slime));
		return school;
	}
	

	/**
	 * This method returns the slime with lowest Id
	 * 
	 * @return ...
	 * 		|result == slimes.firstEntry().getValue()
	 * 
	 */
	@Basic
	public Slime getLowest() {
		return slimes.firstEntry().getValue();
	}
	
	/**
	 * This method returns the slime with highest Id
	 * 
	 * @return ...
	 * 		|result == slimes.lastEntry().getValue()
	 * 
	 */
	@Basic
	public Slime getHighest() {
		return slimes.lastEntry().getValue();
	}

	/**
	 * This method is used to indicate whether this school has a proper World
	 * 
	 * @return ...
	 * 		| if(!canHaveAsWorld(getWorld())) then result == false
	 * 		| if(getWorld() != null) then result == getWorld().hasProperSchool(this)
	 */
	public boolean hasProperWorld() {
		if(!canHaveAsWorld(getWorld())) return false;
		if(getWorld() != null) return getWorld().hasProperSchool(this);
		return false;
	}
	
	/**
	 * This method is used to indicate whether a world can be used as world
	 * 
	 * @param world
	 * 			This parameter is the world
	 * @return ...
	 * 		| result == ( world == null || !world.isTerminated() )
	 */
	public boolean canHaveAsWorld(World world) {
		return world == null || !world.isTerminated();
	}
	
	/**
	 * This method returns the World
	 * 
	 * @return ...
	 * 		|result == world
	 */
	@Basic
	public World getWorld() {
		return this.world;
	}
	
	/**
	 * This method is used to set the World
	 * 
	 * @param world
	 * 			This parameter is used as the world
	 * 
	 * @throws IllegalArgumentException
	 * 			...
	 * 		| !canHaveAsWorld(world)
	 * @post ...
	 * 		| (new this).world = world
	 */
	protected void setWorld(World world) {
		if(!canHaveAsWorld(world)) throw new IllegalArgumentException();
		this.world = world;
	}
	
	/**
	 * This method is used to unset the world
	 * 
	 * @effect ...
	 * 		| this.setWorld(null)
	 * 
	 */
	private void unsetWorld() {
		this.setWorld(null);
	}
	
	/**
	 * This method returns if this school is terminated
	 * 
	 * @return ...
	 * 		| result == (this.getWorld() == null && this.slimes == null)
	 * 
	 */
	@Basic
	public boolean isTerminated() {
		return this.getWorld() == null && this.slimes == null;
	}
	
	/**
	 * This method returns whether a slime can be used or not
	 * 
	 * @param slime
	 * 			This parameter is used as slime
	 *  
	 * @return ...
	 * 		| result == !(slime.isTerminated() || (slime.getSchool() != null && slime.getSchool().getSlimes() != null &&
	 *		|	slime.getSchool().getSlimes().contains(slime)))
	 */
	public boolean canHaveAsSlime(Slime slime) {
		return !(slime.isTerminated() || (slime.getSchool() != null && slime.getSchool().getSlimes() != null &&
				slime.getSchool().getSlimes().contains(slime)));
	}

	/**
	 * This method is used to check whether a slime is attached to this school
	 * 
	 * @param slime
	 * 			This parameter is used as slime
	 * @return ...
	 * 		| canHaveAsSlime(slime) && getSlimes().contains(slime) && slime.getSchool() == this
	 */
	public boolean hasProperSlime(Slime slime) {
		return canHaveAsSlime(slime) && getSlimes().contains(slime) && slime.getSchool() == this;
	}
	
	/**
	 * This method returns whether this School has proper slimes attached to it.
	 * 
	 * @return ...
	 * 		| result == for each slime in slimes.values():
	 * 		|		hasProperSlime(slime)
	 */
	public boolean hasProperSlimes() {
		for(Slime slime : getSlimes()) {
			if(!hasProperSlime(slime)) return false;
		}
		return true;
	}
	/**
	 * This method is used to add a slime to this school
	 *
	 * @param slime
	 * 			This parameter is used as slime 
	 * 
	 * @throws IllegalArgumentException
	 * 		...
	 * 		|!canHaveAsSlime(slime) || slimes == null
	 * @post ...
	 * 		| slime.setSchool(this) && slimes.put(slime.getId(), slime)
	 * 
	 */
	@Raw
	public void addSlime(@Raw Slime slime) throws IllegalArgumentException{
		if(!canHaveAsSlime(slime) || slimes == null) throw new IllegalArgumentException();
		slime.setSchool(this);
		slimes.put(slime.getId(), slime);
	}

	/**
	 * This method is used to remove a slime from this school
	 * 
	 * @param slime
	 * 			This parameter is used as slime 
	 * @throws IllegalArgumentException
	 * 		...
	 * 		|!getSlimes().contains(slime)
	 * 
	 * @post ...
	 * 		| slime.setSchool(null) && this.slimes.remove(slime.getId())
	 *
	 */
	@Raw
	public void removeSlime(@Raw Slime slime) throws IllegalArgumentException{
		if(!getSlimes().contains(slime)) throw new IllegalArgumentException();
		slime.setSchool(null);
		this.slimes.remove(slime.getId());
	}
	
	/**
	 * This method is used to terminate this school
	 * 
	 * @post ...
	 * 		|slimes == null
	 * @effect ...
	 * 		| unsetWorld()
	 */
	public void terminate() {
		slimes = null;
		unsetWorld();
	}
}