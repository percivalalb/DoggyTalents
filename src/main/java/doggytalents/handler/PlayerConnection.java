package doggytalents.handler;

import doggytalents.ModItems;
import doggytalents.lib.ConfigValues;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;

public class PlayerConnection {

    public static void playerLoggedIn(final PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        
        CompoundNBT tag = player.getPersistantData();

        if(!tag.contains(PlayerEntity.PERSISTED_NBT_TAG))
            tag.put(PlayerEntity.PERSISTED_NBT_TAG, new CompoundNBT());
        
        CompoundNBT persistTag = tag.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
        
        if(ConfigValues.STARTING_ITEMS && !persistTag.getBoolean("gotDTStartingItems")) {
            persistTag.putBoolean("gotDTStartingItems", true);

            player.inventory.addItemStackToInventory(new ItemStack(ModItems.DOGGY_CHARM));
            player.inventory.addItemStackToInventory(new ItemStack(ModItems.WHISTLE));
        }
    }
}
