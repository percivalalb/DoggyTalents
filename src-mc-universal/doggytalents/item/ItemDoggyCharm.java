package doggytalents.item;

import doggytalents.base.ObjectLib;
import doggytalents.entity.EntityDog;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public class ItemDoggyCharm extends ItemDT {
	
    public ItemDoggyCharm() {
        super();
        this.setMaxStackSize(1);
    }
    
    public Entity spawnCreature(World worldIn, double x, double y, double z, EntityPlayer playerIn) {

        EntityDog dog = ObjectLib.createDog(worldIn);

        dog.setLocationAndAngles(x, y, z, ObjectLib.BRIDGE.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
        dog.rotationYawHead = dog.rotationYaw;
        dog.renderYawOffset = dog.rotationYaw;
        dog.setTamed(true);
        dog.updateEntityAttributes();
        dog.setOwnerUUID(playerIn.getUniqueID());
        worldIn.spawnEntity(dog);
        dog.playLivingSound();

        return dog;
    }
        
}
