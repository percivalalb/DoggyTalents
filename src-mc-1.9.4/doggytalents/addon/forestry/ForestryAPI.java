package doggytalents.addon.forestry;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Loader;

/**
 * @author ProPercivalalb
 */
public class ForestryAPI {
	
	public ForestryAPI(String modId) {
		if(!Loader.isModLoaded(modId))
			return;
		
	}
	
	public Block getBlockFromName(String name) {
		return Block.getBlockFromName(name);
	}
}
