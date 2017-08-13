package doggytalents.client.model;

import doggytalents.base.ObjectLibClient;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
@SideOnly(Side.CLIENT)
public class ModelHelper {
	
	@SideOnly(Side.CLIENT)
    public static void setModel(Item item, int meta, String modelName) {
		ObjectLibClient.METHODS.setModel(item, meta, modelName);
    }
	
	@SideOnly(Side.CLIENT)
	public static void setModel(Block block, int meta, String modelName) {
		setModel(Item.getItemFromBlock(block), meta, modelName);
	}

    @SideOnly(Side.CLIENT)
    public static void setDefaultModel(Item item) {
        setModel(item, 0, item.getRegistryName().toString());
    }

    @SideOnly(Side.CLIENT)
    public static void setDefaultModel(Block block) {
        setDefaultModel(Item.getItemFromBlock(block));
    }

}
