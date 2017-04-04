package doggytalents.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doggytalents.DoggyTalentsMod;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.entity.EntityDoggyBeam;

/**
 * @author ProPercivalalb
 **/
public class ItemCommandEmblem extends ItemDT {
	
	public ItemCommandEmblem() {
		super("command_emblem");
		this.setCreativeTab(DoggyTalentsAPI.CREATIVE_TAB);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if(!world.isRemote) {
            world.spawnEntityInWorld(new EntityDoggyBeam(world, player));
        }

        return stack;
    }
}
