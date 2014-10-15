package doggytalents.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doggytalents.DoggyTalentsMod;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

/**
 * @author ProPercivalalb
 **/
public class ItemRadar extends ItemMap {
	
	public ItemRadar() {
		this.setCreativeTab(DoggyTalentsMod.creativeTab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon("doggytalents:radar");
	}
	
	@Override
	public MapData getMapData(ItemStack p_77873_1_, World p_77873_2_) {
        MapData mapdata = new MapData("radar");

        return mapdata;
    }
	
	@Override
	public void onUpdate(ItemStack p_77663_1_, World p_77663_2_, Entity p_77663_3_, int p_77663_4_, boolean p_77663_5_)
    {
        if (!p_77663_2_.isRemote)
        {
            MapData mapdata = this.getMapData(p_77663_1_, p_77663_2_);

            if (p_77663_3_ instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)p_77663_3_;
                mapdata.updateVisiblePlayers(entityplayer, p_77663_1_);
            }

            if (p_77663_5_)
            {
                this.updateMapData(p_77663_2_, p_77663_3_, mapdata);
            }
        }
    }
}
