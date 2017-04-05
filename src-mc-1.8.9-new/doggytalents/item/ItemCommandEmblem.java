package doggytalents.item;

import doggytalents.entity.EntityDoggyBeam;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public class ItemCommandEmblem extends ItemDT {
	
	public ItemCommandEmblem() {
		super();
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if(!world.isRemote)
            world.spawnEntityInWorld(new EntityDoggyBeam(world, player));

        return stack;
	}
}
