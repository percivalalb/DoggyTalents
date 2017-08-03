package doggytalents.item;

import doggytalents.entity.EntityDog;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public class ItemDoggyCharm extends ItemDT {
	
    public ItemDoggyCharm() {
        super("doggy_charm");
        this.setMaxStackSize(1);
    }
    
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        if (world.isRemote) {
            return true;
        }
        else {
        	Block block = world.getBlock(x, y, z);
            x += Facing.offsetsXForSide[side];
            y += Facing.offsetsYForSide[side];
            z += Facing.offsetsZForSide[side];
            double yOffset = 0.0D;

            if (side == 1 && block.getRenderType() == 11)
            	yOffset = 0.5D;

            if (spawnCreature(world, (double)x + 0.5D, (double)y + yOffset, (double)z + 0.5D, player) != null && !player.capabilities.isCreativeMode)
                --stack.stackSize;

            return true;
        }
    }
    
    public Entity spawnCreature(World worldIn, double x, double y, double z, EntityPlayer playerIn) {

        EntityDog dog = new EntityDog(worldIn);

        dog.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(worldIn.rand.nextFloat() * 360.0F), 0.0F);
        dog.rotationYawHead = dog.rotationYaw;
        dog.renderYawOffset = dog.rotationYaw;
        dog.setTamed(true);
        dog.updateEntityAttributes();
        dog.func_152115_b(playerIn.getUniqueID().toString());
        worldIn.spawnEntityInWorld(dog);
        dog.playLivingSound();

        return dog;
    }
        
}
