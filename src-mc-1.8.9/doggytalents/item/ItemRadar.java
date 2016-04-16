package doggytalents.item;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.entity.EntityDog;

/**
 * @author ProPercivalalb
 **/
public class ItemRadar extends ItemMap {
	
	public ItemRadar() {
		this.setCreativeTab(DoggyTalentsAPI.CREATIVE_TAB);
		this.setMaxStackSize(1);
	}

	/**
	@Override
	public Packet createMapDataPacket(ItemStack stack, World worldIn, EntityPlayer player) {
        return this.getMapData(player, stack, worldIn).getMapPacket(stack, worldIn, player);
    }
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(!worldIn.isRemote) {
			MapData mapdata = this.getMapData(entityIn, stack, worldIn);
		
			if(isSelected)
				this.updateMapData(worldIn, entityIn, mapdata);
	    }
	}
	
	public MapData getMapData(Entity entityIn, ItemStack stack, World worldIn) {
		MapData mapdata = new MapData("Radar");
	    mapdata.dimension = worldIn.provider.getDimensionId();
	    mapdata.calculateMapCenter((double)worldIn.getWorldInfo().getSpawnX(), (double)worldIn.getWorldInfo().getSpawnZ(), mapdata.scale);
		//mapdata.calculateMapCenter(entityIn.posX, entityIn.posZ, 3);
		mapdata.markDirty();
		if(entityIn instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer)entityIn;
			mapdata.updateVisiblePlayers(entityplayer, stack);
        }
		
		return mapdata;
	}**/
	
	@Override
	public Packet createMapDataPacket(ItemStack stack, World worldIn, EntityPlayer player) {
		return null;
	}
	@Override
	 public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	    {
		 
	    }
	@Override
	public MapData getMapData(ItemStack stack, World worldIn) {
		return null;
		
		/**String s = "map_" + stack.getMetadata();
        MapData mapdata = (MapData)worldIn.loadItemData(MapData.class, s);

        if (mapdata == null && !worldIn.isRemote)
        {
            stack.setItemDamage(worldIn.getUniqueDataId("map"));
            s = "map_" + stack.getMetadata();
            mapdata = new MapData(s);
            mapdata.scale = 4;
            mapdata.calculateMapCenter((double)worldIn.getWorldInfo().getSpawnX(), (double)worldIn.getWorldInfo().getSpawnZ(), mapdata.scale);
            mapdata.dimension = worldIn.provider.getDimensionId();
            mapdata.markDirty();
            worldIn.setItemData(s, mapdata);
        }
        return mapdata;**/
    }
}
