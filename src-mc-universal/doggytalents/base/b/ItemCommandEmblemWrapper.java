package doggytalents.base.b;

import doggytalents.base.ObjectLib;
import doggytalents.item.ItemCommandEmblem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCommandEmblemWrapper extends ItemCommandEmblem {

	@Override
	public ItemStack onItemRightClick(ItemStack stackIn, World worldIn, EntityPlayer playerIn) {
        worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if(!worldIn.isRemote) {
            worldIn.spawnEntity(ObjectLib.createDoggyBeam(worldIn, playerIn));
        }
        
        return stackIn;
    }
}
