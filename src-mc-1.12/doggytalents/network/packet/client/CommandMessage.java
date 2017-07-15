package doggytalents.network.packet.client;

import java.util.List;

import doggytalents.ModItems;
import doggytalents.entity.EntityDog;
import doggytalents.entity.ModeUtil.EnumMode;
import doggytalents.helper.ChatUtil;
import doggytalents.helper.DogUtil;
import doggytalents.network.AbstractMessage.AbstractServerMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
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

		if((player.getHeldItemMainhand().getItem() == ModItems.COMMAND_EMBLEM || player.getHeldItemOffhand().getItem() == ModItems.COMMAND_EMBLEM)) {

			if(this.commandId == 1)
			{
				world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
				boolean isDog = false;
			    List nearEnts = world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().grow(20D, 20D, 20D));
			    for (Object o : nearEnts)
			        {
			            if (o instanceof EntityDog)
			            {
			            	EntityDog dog = (EntityDog)o;
			            	if(dog.canInteract(player))
			            	{
			            		dog.getSitAI().setSitting(false);
			            		dog.getNavigator().clearPathEntity();
			            	    dog.setAttackTarget((EntityLivingBase)null);
			            	    if(dog.mode.isMode(EnumMode.WANDERING)) {
			            	    	dog.mode.setMode(EnumMode.DOCILE);
			            	    }
			                    isDog = true;
			            	}
			            }
			        }
            		if(isDog)
            		{
            			player.sendMessage(ChatUtil.getChatComponentTranslation("dogcommand.come"));
            		}
				}
				else if(this.commandId == 2)
				{
					world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
					boolean isDog = false;
			        List nearEnts = world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().grow(20D, 20D, 20D));
			        for (Object o : nearEnts)
			        {
			            if (o instanceof EntityDog)
			            {
			            	EntityDog dog = (EntityDog)o;
			            	if(dog.canInteract(player))
			            	{
			            		dog.getSitAI().setSitting(true);
			            		  dog.getNavigator().clearPathEntity();
			            	    dog.setAttackTarget((EntityLivingBase)null);
			            	    if(dog.mode.isMode(EnumMode.WANDERING)) {
			            	    	dog.mode.setMode(EnumMode.DOCILE);
			            	    }
			                    isDog = true;
			            	}
			            }
			        }
			        if(isDog)
			        {
			        	player.sendMessage(ChatUtil.getChatComponentTranslation("dogcommand.stay"));
			        }
				}
				else if(this.commandId == 3)
				{
					world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
					boolean isDog = false;
			        List nearEnts = world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().grow(20D, 20D, 20D));
			        for (Object o : nearEnts)
			        {
			            if (o instanceof EntityDog)
			            {
			            	EntityDog dog = (EntityDog)o;
			            	if(dog.canInteract(player))
			            	{
			            		if(dog.getMaxHealth() / 2 >= dog.getHealth())
			            		{
			            			dog.getSitAI().setSitting(true);
			            			  dog.getNavigator().clearPathEntity();
				            	    dog.setAttackTarget((EntityLivingBase)null);
			            		}
			            		else
			            		{
			            			dog.getSitAI().setSitting(false);
			            	        dog.getNavigator().clearPathEntity();
			            	        dog.setAttackTarget((EntityLivingBase)null);
			            		}
			            		isDog = true;
			            	}
			            }
			        }
			        if(isDog)
			        {
			        	player.sendMessage(ChatUtil.getChatComponentTranslation("dogcommand.ok"));
			        }
				}
				else if(this.commandId == 4)
				{
					world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
					boolean isDog = false;
			        List<Entity> nearEnts = world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().grow(20D, 20D, 20D));
			        for (Object o : nearEnts) {
			            if(o instanceof EntityDog)
			            {
			            	EntityDog dog = (EntityDog)o;
			            	if(dog.canInteract(player) && !dog.isSitting() && !dog.mode.isMode(EnumMode.WANDERING)) {
			            		DogUtil.teleportDogToOwner(player, dog, world, dog.getNavigator());
			            		
			                    isDog = true;
			            	}
			            }
			        }
			        
			        if(isDog)
			        {
			        	player.sendMessage(ChatUtil.getChatComponentTranslation("dogcommand.heel"));
			        }
				}
					
			}
		
	}
}
	