package jumpingalien.internal.game;

import java.util.Collection;
import java.util.Optional;

import jumpingalien.internal.gui.sprites.ImageSprite;
import jumpingalien.model.School;
import jumpingalien.model.Shark;
import jumpingalien.model.Skullcab;
import jumpingalien.model.Slime;
import jumpingalien.model.Sneezewort;

public interface ObjectInfoProvider {

	public Collection<Slime> getSlimes();

	public Collection<Shark> getSharks();

	public Collection<Sneezewort> getSneezeworts();
	
	public Collection<Skullcab> getSkullcabs();

	public Optional<int[]> getLocation(Sneezewort plant);
	
	public Optional<int[]> getLocation(Skullcab plant);

	public Optional<int[]> getLocation(Shark shark);

	public Optional<int[]> getLocation(Slime slime);

	public Optional<ImageSprite> getCurrentSprite(Sneezewort plant);
	
	public Optional<ImageSprite> getCurrentSprite(Skullcab plant);

	public Optional<ImageSprite> getCurrentSprite(Shark shark);

	public Optional<ImageSprite> getCurrentSprite(Slime slime);

	public Optional<School> getSchool(Slime slime);

}
