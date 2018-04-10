package doggytalents.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.stream.Collectors;

import doggytalents.entity.EntityDog;
import doggytalents.entity.ai.EntityAIFollowOwner;
import doggytalents.helper.DogUtil;

/**
 * Credit to the author of Sophisticated Wolves, NightKosh for onItemRightClick code
 *
 */
public class ItemWhistle extends ItemDT {

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (world.isRemote) {
			world.playSound(player, player.getPosition(), player.isSneaking() ? SoundEvents.BLOCK_ANVIL_PLACE : SoundEvents.BLOCK_ANVIL_HIT /*SWSound.WHISTLE_LONG : SWSound.WHISTLE_SHORT*/, SoundCategory.PLAYERS, 1, 1);
		} else {
			List<EntityDog> dogsList = world.getLoadedEntityList().stream().filter(dog -> dog instanceof EntityDog).map(dog -> (EntityDog) dog).collect(Collectors.toList());
			System.out.println(dogsList);
			for(EntityDog dog : dogsList) {
				
				int xPos = MathHelper.floor(player.posX);
				int zPos = MathHelper.floor(player.posZ);
				int yPos = MathHelper.floor(player.getEntityBoundingBox().minY);
				
				if (dog.isTamed() && dog.isOwner(player) && (!dog.isSitting() || player.isSneaking())) {
					for (int x = -2; x <= 2; x++) {
						for (int z = -2; z <= 2; z++) {
							if(DogUtil.isTeleportFriendlyBlock(dog, world, xPos, zPos, yPos, x, z)) {
								dog.setSitting(false);
								dog.getAISit().setSitting(false);
								dog.setLocationAndAngles(xPos + x + 0.5, yPos, zPos + z + 0.5, dog.rotationYaw, dog.rotationPitch);
								dog.getNavigator().clearPath();
								dog.setAttackTarget(null);
							}
						}
					}
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}