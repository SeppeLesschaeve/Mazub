package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Raw;

public interface Run {

	@Raw
	public void run(double deltaT);
	
	@Raw
	public void endRun();

}
