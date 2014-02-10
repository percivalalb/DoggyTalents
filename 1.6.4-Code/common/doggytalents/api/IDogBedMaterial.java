package doggytalents.api;

import net.minecraft.util.Icon;

/**
 * @author ProPercivalalb
 */
public interface IDogBedMaterial {

	/**
	 * Gets the sprite/icon that should be rendered 
	 * @param side The side can be used to determine the icon, the bedding will always have a side value of 1
	 * @return The icon that will be rendered
	 */
	public Icon getMaterialIcon(int side);
}
