package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Raw;

public interface Jump {
	
	@Raw
	public void jump(double deltaT);
	
	@Raw
	public void endJump(double deltaT);

}
