package doggytalents.api.inferface;

import net.minecraft.block.Block;

/**
 * @author ProPercivalalb
 */
public class DefaultDogBedIcon implements IDogBedIcon {

	private Block block;
	private int metadata;
	
	public DefaultDogBedIcon(Block block) {
		this(block, 0);
	}
	
	public DefaultDogBedIcon(Block block, int meta) {
		this.block = block;
		this.metadata = meta;
	}
}
