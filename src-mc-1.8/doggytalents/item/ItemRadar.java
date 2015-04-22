package doggytalents.item;

import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import doggytalents.api.DoggyTalentsAPI;

/**
 * @author ProPercivalalb
 **/
public class ItemRadar extends ItemMap {
	
	public ItemRadar() {
		this.setCreativeTab(DoggyTalentsAPI.CREATIVE_TAB);
		this.setMaxStackSize(1);
	}

	@Override
	public MapData getMapData(ItemStack p_77873_1_, World p_77873_2_) {
        MapData mapdata = new MapData("radar");
        return mapdata;
    }
}
