package doggytalents.client.gui;

import doggytalents.DoggyTalentsMod;
import doggytalents.entity.EntityDog;
import doggytalents.lib.GuiNames;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;

@OnlyIn(Dist.CLIENT)
public class GuiHandler {
	
    public static GuiScreen openGui(FMLPlayMessages.OpenContainer openContainer) {
    	String guiId = openContainer.getId().toString();
    	EntityPlayer clientPlayer = DoggyTalentsMod.PROXY.getPlayerEntity();
    	
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
			
			return new GuiPackPuppy(clientPlayer, dog);
    	}
    	else if(guiId.equals(GuiNames.TREAT_BAG)) {
    		int slotId = openContainer.getAdditionalData().readInt();
    		
    		return new GuiTreatBag(clientPlayer, slotId, clientPlayer.inventory.getStackInSlot(slotId));
    	}
    	else if(guiId.equals(GuiNames.FOOD_BOWL)) {
	    	BlockPos pos = openContainer.getAdditionalData().readBlockPos();
	    	
	    	TileEntity tileentity = clientPlayer.world.getTileEntity(pos);
	
	        if(!(tileentity instanceof TileEntityFoodBowl))
	        	return null;
	    
	        return new GuiFoodBowl(clientPlayer.inventory, (TileEntityFoodBowl)tileentity);
    	}
        
    	return null;
    }
}