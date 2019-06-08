package doggytalents.client.gui;

import doggytalents.DoggyTalentsMod;
import doggytalents.entity.EntityDog;
import doggytalents.lib.GuiNames;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;

@OnlyIn(Dist.CLIENT)
public class GuiHandler {
	
    public static Screen openGui(FMLPlayMessages.OpenContainer openContainer) {
    	ContainerType<?> guiId = openContainer.getType();
    	PlayerEntity clientPlayer = DoggyTalentsMod.PROXY.getPlayerEntity();
    	int windowId = openContainer.getWindowId();
    	
    	if(guiId.equals(GuiNames.DOG_INFO)) {
    		int entityId = openContainer.getAdditionalData().readInt();
    		
    		Entity target = clientPlayer.world.getEntityByID(entityId);
            if(!(target instanceof EntityDog))
            	return null;
			EntityDog dog = (EntityDog)target;
			
			return new GuiDogInfo(dog, clientPlayer);
    	}
    	else if(guiId.equals(GuiNames.PACK_PUPPY)) {
    		int entityId = openContainer.getAdditionalData().readInt();
    		
    		Entity target = clientPlayer.world.getEntityByID(entityId);
            if(!(target instanceof EntityDog))
            	return null;
			EntityDog dog = (EntityDog)target;
			
			return new GuiPackPuppy(windowId, clientPlayer, dog);
    	}
    	else if(guiId.equals(GuiNames.TREAT_BAG)) {
    		int slotId = openContainer.getAdditionalData().readInt();
    		
    		return new GuiTreatBag(windowId, clientPlayer, slotId, clientPlayer.inventory.getStackInSlot(slotId));
    	}
    	else if(guiId.equals(GuiNames.FOOD_BOWL)) {
	    	BlockPos pos = openContainer.getAdditionalData().readBlockPos();
	    	
	    	TileEntity tileentity = clientPlayer.world.getTileEntity(pos);
	
	        if(!(tileentity instanceof TileEntityFoodBowl))
	        	return null;
	    
	        return new GuiFoodBowl(windowId, clientPlayer.inventory, (TileEntityFoodBowl)tileentity);
    	}
        
    	return null;
    }
}