package jumpingalien.util;

import annotate.Basic;
import annotate.Immutable;
import annotate.Raw;

/**
 * A class of sprites involving a name, a width and a height.
 * @invar  Each sprite can have its name as name .
 *       | canHaveAsName(this.getName())
 * @invar  Each sprite can have its width as width .
 *       | canHaveAsWidth(this.getWidth())
 * @invar  Each sprite can have its height as height .
 *       | canHaveAsHeight(this.getHeight())
 */
public class Sprite {

	/**
	 * Initialize this new sprite with given name.
	 * 
	 * @param  name
	 *         The name for this new sprite.
	 * @param  width
	 *         The width for this new sprite.
	 * @param  height
	 *         The height for this new sprite.
	 * @post   If the given name is a valid name for any sprite,
	 *         the name of this new sprite is equal to the given
	 *         name. Otherwise, the name of this new sprite is equal
	 *         to "Nameless".
	 *       | if (isValidName(name))
	 *       |   then new.getName() == name
	 *       |   else new.getName() == "Nameless"
	 * @post   If the given width is a valid width for any sprite,
	 *         the width of this new sprite is equal to the given
	 *         width. Otherwise, the width of this new sprite is equal
	 *         to 100.
	 *       | if (isValidWidth(width))
	 *       |   then new.getWidth() == width
	 *       |   else new.getWidth() == 100
	 * @post   If the given height is a valid height for any sprite,
	 *         the height of this new sprite is equal to the given
	 *         height. Otherwise, the height of this new sprite is equal
	 *         to 50.
	 *       | if (isValidHeight(height))
	 *       |   then new.getHeight() == height
	 *       |   else new.getHeight() == 50
	 */
	public Sprite(String name, int width, int height) {
		if (!canHaveAsName(name))
			name = "Nameless";
		this.name = name;
		if (!canHaveAsWidth(width))
			width = 100;
		this.width = width;
		if (!canHaveAsHeight(height))
			height = 50;
		this.height = height;
	}
	
	/**
	 * Return the name of this sprite.
	 */
	@Basic
	@Raw
	@Immutable
	public String getName() {
		return this.name;
	}

	/**
	 * Check whether this sprite can have the given name as its name.
	 *  
	 * @param  name
	 *         The name to check.
	 * @return 
	 *       | result == (name != null) && (name.length() > 0)
	*/
	@Raw
	public boolean canHaveAsName(String name) {
		return (name != null) && (name.length() > 0);
	}

	/**
	 * Variable registering the name of this sprite.
	 */
	private final String name;

	/**
	 * Return the width of this sprite.
	 */
	@Basic
	@Raw
	@Immutable
	public int getWidth() {
		return this.width;
	}

	/**
	 * Check whether this sprite can have the given width as its width.
	 *  
	 * @param  width
	 *         The width to check.
	 * @return 
	 *       | result == width > 0
	*/
	@Raw
	public boolean canHaveAsWidth(int width) {
		return width > 0;
	}

	/**
	 * Variable registering the width of this sprite.
	 */
	private final int width;

	/**
	 * Return the height of this sprite.
	 */
	@Basic
	@Raw
	@Immutable
	public int getHeight() {
		return this.height;
	}

	/**
	 * Check whether this sprite can have the given height as its height.
	 *  
	 * @param  height
	 *         The height to check.
	 * @return 
	 *       | result == height > 0
	*/
	@Raw
	public boolean canHaveAsHeight(int height) {
		return height > 0;
	}

	/**
	 * Variable registering the height of this sprite.
	 */
	private final int height;

	/**
	 * Return a textual representation of this sprite.
	 */
	@Override
	public String toString() {
		return String.format("Sprite '%s' (width %d x height %d)", name, width, height);
	}

}
