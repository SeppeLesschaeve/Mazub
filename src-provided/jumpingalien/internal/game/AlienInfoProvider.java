package jumpingalien.internal.game;

import java.util.Optional;

import jumpingalien.util.Sprite;

public interface AlienInfoProvider<T> {

	public T getAlien();
	
	public Optional<int[]> getAlienXYPixel();

	public Optional<int[]> getAlienSize();
	
	public Optional<double[]> getAlienXYPrecise();

	public Optional<Sprite> getPlayerSprite();

	public Optional<double[]> getAlienVelocity();

	public Optional<double[]> getAlienAcceleration();

	public Optional<Integer> getAlienHealth();

}
