package doggytalents.handler;

import doggytalents.DoggyTalentsMod;
import doggytalents.client.gui.GuiDogInfo;
import doggytalents.client.gui.GuiFoodBowl;
import doggytalents.client.gui.GuiPackPuppy;
import doggytalents.client.gui.GuiTreatBag;
import doggytalents.entity.EntityDog;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.FMLPlayMessages;

public class GuiHandler {
	
    public static GuiScreen openGui(FMLPlayMessages.OpenContainer openContainer) {
    	String guiId = openContainer.getId().toString();
    	
    	if(guiId.equals("doggytalents:doginfo")) {
    		int entityId = openContainer.getAdditionalData().readInt();
        	EntityPlayer clientPlayer = DoggyTalentsMod.PROXY.getPlayerEntity();
    		
    		Entity target = clientPlayer.world.getEntityByID(entityId);
            if(!(target instanceof EntityDog))
            	return null;
			EntityDog dog = (EntityDog)target;
			return new GuiDogInfo(dog, clientPlayer);
    	}
    	else if(guiId.equals("doggytalents:packpuppy")) {
    		int entityId = openContainer.getAdditionalData().readInt();
        	EntityPlayer clientPlayer = DoggyTalentsMod.PROXY.getPlayerEntity();
    		
    		Entity target = clientPlayer.world.getEntityByID(entityId);
            if(!(target instanceof EntityDog))
            	return null;
			EntityDog dog = (EntityDog)target;
			return new GuiPackPuppy(clientPlayer, dog);
    	}
    	else if(guiId.equals("doggytalents:treatbag")) {
    		int slotId = openContainer.getAdditionalData().readInt();
        	EntityPlayer clientPlayer = DoggyTalentsMod.PROXY.getPlayerEntity();
    		
    		return new GuiTreatBag(clientPlayer, slotId, clientPlayer.inventory.getStackInSlot(slotId));
    	}
    	
    	BlockPos pos = openContainer.getAdditionalData().readBlockPos();
    	EntityPlayer clientPlayer = DoggyTalentsMod.PROXY.getPlayerEntity();
    	
    	TileEntity tileentity = clientPlayer.world.getTileEntity(pos);

        if (tileentity instanceof TileEntityFoodBowl) {
             return new GuiFoodBowl(clientPlayer.inventory, (TileEntityFoodBowl)tileentity);
        }
        
    	return null;
    }
}