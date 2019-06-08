package doggytalents.handler;

import doggytalents.ModItems;
import doggytalents.configuration.ConfigHandler;
import doggytalents.lib.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class PlayerConnection {

	@SubscribeEvent
	public void playerLoggedIn(final PlayerLoggedInEvent event) {
		PlayerEntity player = event.getPlayer();
		
		CompoundNBT tag = player.getEntityData();

        if(!tag.contains(PlayerEntity.PERSISTED_NBT_TAG))
        	tag.put(PlayerEntity.PERSISTED_NBT_TAG, new CompoundNBT());
        
        CompoundNBT persistTag = tag.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
        
		if(Constants.STARTING_ITEMS && !persistTag.getBoolean("gotDTStartingItems")) {
			persistTag.putBoolean("gotDTStartingItems", true);

            player.inventory.addItemStackToInventory(new ItemStack(ModItems.DOGGY_CHARM));
            player.inventory.addItemStackToInventory(new ItemStack(ModItems.WHISTLE));
        }
	}
}
