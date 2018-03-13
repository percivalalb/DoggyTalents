package doggytalents.network.packet.client;

import java.util.List;

import doggytalents.ModItems;
import doggytalents.entity.EntityDog;
import doggytalents.entity.ModeUtil.EnumMode;
import doggytalents.helper.DogUtil;
import doggytalents.network.AbstractMessage.AbstractServerMessage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TextComponentTranslation;
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

		Iterable<ItemStack> heldStacks = player.getHeldEquipment();
		
		for(ItemStack heldStack : heldStacks) {
			if(heldStack.isEmpty() || heldStack.getItem() != ModItems.COMMAND_EMBLEM) continue;
			
			List<EntityDog> nearEnts = world.getEntitiesWithinAABB(EntityDog.class, player.getEntityBoundingBox().grow(20D, 20D, 20D));
			player.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
			
			boolean sucessful = false;
			
			if(this.commandId == 1) {
			    for(EntityDog dog : nearEnts) {
			    	if(dog.canInteract(player)) {
			    		dog.getSitAI().setSitting(false);
			    		dog.getNavigator().clearPath();
			    		dog.setAttackTarget((EntityLivingBase)null);
			    		if(dog.mode.isMode(EnumMode.WANDERING))
			    			dog.mode.setMode(EnumMode.DOCILE);
			    		sucessful = true;
			    	}
			    }
			
			    if(sucessful)
			    	player.sendMessage(new TextComponentTranslation("dogcommand.come"));
			}
			else if(this.commandId == 2) {
				for(EntityDog dog : nearEnts) {
		
					if(dog.canInteract(player)) {
						dog.getSitAI().setSitting(true);
						dog.getNavigator().clearPath();
						dog.setAttackTarget((EntityLivingBase)null);
						if(dog.mode.isMode(EnumMode.WANDERING))
							dog.mode.setMode(EnumMode.DOCILE);
						sucessful = true;
					}
				}
				
		        if(sucessful)
		        	player.sendMessage(new TextComponentTranslation("dogcommand.stay"));
			}
			else if(this.commandId == 3) {
				for(EntityDog dog : nearEnts) {
					if(dog.canInteract(player)) {
						if(dog.getMaxHealth() / 2 >= dog.getHealth()) {
							dog.getSitAI().setSitting(true);
							dog.getNavigator().clearPath();
							dog.setAttackTarget((EntityLivingBase)null);
						}
						else {
							dog.getSitAI().setSitting(false);
							dog.getNavigator().clearPath();
							dog.setAttackTarget((EntityLivingBase)null);
						}
						sucessful = true;
					}
			            
				}
				
				if(sucessful)
					player.sendMessage(new TextComponentTranslation("dogcommand.ok"));
			}
			else if(this.commandId == 4) {
				for(EntityDog dog : nearEnts) {
					if(dog.canInteract(player) && !dog.isSitting() && !dog.mode.isMode(EnumMode.WANDERING)) {
						DogUtil.teleportDogToOwner(player, dog, world, dog.getNavigator());
			            		
						sucessful = true;
					}
			            
				}
			        
				if(sucessful)
					player.sendMessage(new TextComponentTranslation("dogcommand.heel"));
			}
			
			break;
		}
	}
}
	