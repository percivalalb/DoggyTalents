package doggytalents.api;

import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

/**
 * @author ProPercivalalb
 */
public class DefaultBedMaterial implements IDogBedMaterial {

	private Block block;
	private int metadata;
	
	public DefaultBedMaterial(Block block, int meta) {
		this.block = block;
		this.metadata = meta;
	}
	
	@Override
	public IIcon getMaterialIcon(int side) {
		return block.getIcon(side, metadata);
	}

}
