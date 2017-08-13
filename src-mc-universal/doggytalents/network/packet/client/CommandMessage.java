package doggytalents.network.packet.client;

import java.util.List;

import doggytalents.ModItems;
import doggytalents.base.ObjectLib;
import doggytalents.entity.EntityDog;
import doggytalents.entity.ModeUtil.EnumMode;
import doggytalents.helper.DogUtil;
import doggytalents.network.AbstractMessage.AbstractServerMessage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

public class CommandMessage extends AbstractServerMessage {
	
	public int commandId;
	
	public CommandMessage() {}
    public CommandMessage(int commandId) {
        this.commandId = commandId;
    }

	@Override
	protected void read(PacketBuffer buffer) {
		this.commandId = buffer.readInt();
	}
	
	@Override
	protected void write(PacketBuffer buffer) {
		buffer.writeInt(this.commandId);
		
	}
	
	@Override
	public void process(EntityPlayer player, Side side) {
		World world = player.world;

		List<ItemStack> heldStacks = ObjectLib.BRIDGE.getHeldItems(player);
		
		for(ItemStack heldStack : heldStacks) {
			if(heldStack.getItem() != ModItems.COMMAND_EMBLEM) continue;
			
			List<EntityDog> nearEnts = world.getEntitiesWithinAABB(ObjectLib.ENTITY_DOG_CLASS, player.getEntityBoundingBox().grow(20D, 20D, 20D));
			ObjectLib.BRIDGE.playSound(player, "random.bow", 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
			
			boolean sucessful = false;
			
			if(this.commandId == 1) {
			    for(EntityDog dog : nearEnts) {
			    	if(dog.canInteract(player)) {
			    		dog.getSitAI().setSitting(false);
			    		dog.getNavigator().clearPathEntity();
			    		dog.setAttackTarget((EntityLivingBase)null);
			    		if(dog.mode.isMode(EnumMode.WANDERING))
			    			dog.mode.setMode(EnumMode.DOCILE);
			    		sucessful = true;
			    	}
			    }
			
			    if(sucessful)
			    	ObjectLib.BRIDGE.addTranslatedMessage(player, "dogcommand.come");
			}
			else if(this.commandId == 2) {
				for(EntityDog dog : nearEnts) {
		
					if(dog.canInteract(player)) {
						dog.getSitAI().setSitting(true);
						dog.getNavigator().clearPathEntity();
						dog.setAttackTarget((EntityLivingBase)null);
						if(dog.mode.isMode(EnumMode.WANDERING))
							dog.mode.setMode(EnumMode.DOCILE);
						sucessful = true;
					}
				}
				
		        if(sucessful)
		        	ObjectLib.BRIDGE.addTranslatedMessage(player, "dogcommand.stay");
			}
			else if(this.commandId == 3) {
				for(EntityDog dog : nearEnts) {
					if(dog.canInteract(player)) {
						if(dog.getMaxHealth() / 2 >= dog.getHealth()) {
							dog.getSitAI().setSitting(true);
							dog.getNavigator().clearPathEntity();
							dog.setAttackTarget((EntityLivingBase)null);
						}
						else {
							dog.getSitAI().setSitting(false);
							dog.getNavigator().clearPathEntity();
							dog.setAttackTarget((EntityLivingBase)null);
						}
						sucessful = true;
					}
			            
				}
				
				if(sucessful)
					ObjectLib.BRIDGE.addTranslatedMessage(player, "dogcommand.ok");
			}
			else if(this.commandId == 4) {
				for(EntityDog dog : nearEnts) {
					if(dog.canInteract(player) && !dog.isSitting() && !dog.mode.isMode(EnumMode.WANDERING)) {
						DogUtil.teleportDogToOwner(player, dog, world, dog.getNavigator());
			            		
						sucessful = true;
					}
			            
				}
			        
				if(sucessful)
					ObjectLib.BRIDGE.addTranslatedMessage(player, "dogcommand.heel");
			}
			
			break;
		}
	}
}
	