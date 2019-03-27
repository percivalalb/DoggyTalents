package doggytalents.network.client;

import java.util.List;
import java.util.function.Supplier;

import doggytalents.ModItems;
import doggytalents.entity.EntityDog;
import doggytalents.entity.ai.ModeFeature.EnumMode;
import doggytalents.helper.DogUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketCommand {
	
	public int commandId;
	
	public PacketCommand(int commandId) {
		this.commandId = commandId;
	}
    
	public static void encode(PacketCommand msg, PacketBuffer buf) {
		buf.writeInt(msg.commandId);
	}
	
	public static PacketCommand decode(PacketBuffer buf) {
		int commandId = buf.readInt();
		return new PacketCommand(commandId);
	}
	
	
	public static class Handler {
        public static void handle(final PacketCommand message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
            	EntityPlayerMP player = ctx.get().getSender();
            	World world = player.world;

        		Iterable<ItemStack> heldStacks = player.getHeldEquipment();
        		
        		for(ItemStack heldStack : heldStacks) {
        			if(heldStack.isEmpty() || heldStack.getItem() != ModItems.COMMAND_EMBLEM) continue;
        			
        			List<EntityDog> nearEnts = world.getEntitiesWithinAABB(EntityDog.class, player.getBoundingBox().grow(20D, 20D, 20D));
        			player.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
        			
        			boolean sucessful = false;
        			
        			if(message.commandId == 1) {
        			    for(EntityDog dog : nearEnts) {
        			    	if(dog.canInteract(player)) {
        			    		dog.setSitting(false);
        			    		dog.getNavigator().clearPath();
        			    		dog.setAttackTarget((EntityLivingBase)null);
        			    		if(dog.MODE.isMode(EnumMode.WANDERING))
        			    			dog.MODE.setMode(EnumMode.DOCILE);
        			    		sucessful = true;
        			    	}
        			    }
        			
        			    if(sucessful)
        			    	player.sendMessage(new TextComponentTranslation("dogcommand.come"));
        			}
        			else if(message.commandId == 2) {
        				for(EntityDog dog : nearEnts) {
        		
        					if(dog.canInteract(player)) {
        						dog.setSitting(true);
        						dog.getNavigator().clearPath();
        						dog.setAttackTarget((EntityLivingBase)null);
        						if(dog.MODE.isMode(EnumMode.WANDERING))
        							dog.MODE.setMode(EnumMode.DOCILE);
        						sucessful = true;
        					}
        				}
        				
        		        if(sucessful)
        		        	player.sendMessage(new TextComponentTranslation("dogcommand.stay"));
        			}
        			else if(message.commandId == 3) {
        				for(EntityDog dog : nearEnts) {
        					if(dog.canInteract(player)) {
        						if(dog.getMaxHealth() / 2 >= dog.getHealth()) {
        							dog.setSitting(true);
        							dog.getNavigator().clearPath();
        							dog.setAttackTarget((EntityLivingBase)null);
        						}
        						else {
        							dog.setSitting(false);
        							dog.getNavigator().clearPath();
        							dog.setAttackTarget((EntityLivingBase)null);
        						}
        						sucessful = true;
        					}
        			            
        				}
        				
        				if(sucessful)
        					player.sendMessage(new TextComponentTranslation("dogcommand.ok"));
        			}
        			else if(message.commandId == 4) {
        				for(EntityDog dog : nearEnts) {
        					if(dog.canInteract(player) && !dog.isSitting() && !dog.MODE.isMode(EnumMode.WANDERING)) {
        						DogUtil.teleportDogToOwner(player, dog, world, dog.getNavigator());
        			            		
        						sucessful = true;
        					}
        			            
        				}
        			        
        				if(sucessful)
        					player.sendMessage(new TextComponentTranslation("dogcommand.heel"));
        			}
        			
        			break;
        		}
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
