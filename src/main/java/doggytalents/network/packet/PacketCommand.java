package doggytalents.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import doggytalents.ModItems;
import doggytalents.core.helper.ChatHelper;
import doggytalents.entity.EntityDTDoggy;
import doggytalents.entity.data.EnumMode;
import doggytalents.network.IPacket;

/**
 * @author ProPercivalalb
 */
public class PacketCommand extends IPacket {

	public int commandId;
	
	public PacketCommand() {}
	public PacketCommand(int commandId) {
		this();
		this.commandId = commandId;
	}

	@Override
	public void read(DataInputStream data) throws IOException {
		this.commandId = data.readInt();
	}

	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeInt(commandId);
	}

	@Override
	public void execute(EntityPlayer player) {
		World world = player.worldObj;
		ItemStack stack = player.getCurrentEquippedItem();
		if(stack == null)
			return;
		
		if(stack.getItem() == ModItems.commandEmblem) {

			if(commandId == 1)
			{
				world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
				boolean isDog = false;
			    List nearEnts = world.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.expand(20D, 20D, 20D));
			    for (Object o : nearEnts)
			        {
			            if (o instanceof EntityDTDoggy)
			            {
			            	EntityDTDoggy dog = (EntityDTDoggy)o;
			            	if(dog.canInteract(player))
			            	{
			            		dog.getSitAI().setSitting(false);
			            		dog.setPathToEntity((PathEntity)null);
			            	    dog.setTarget((Entity)null);
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
            			player.addChatMessage(ChatHelper.getChatComponentTranslation("dogCommand.come"));
            		}
				}
				else if(commandId == 2)
				{
					world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
					boolean isDog = false;
			        List nearEnts = world.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.expand(20D, 20D, 20D));
			        for (Object o : nearEnts)
			        {
			            if (o instanceof EntityDTDoggy)
			            {
			            	EntityDTDoggy dog = (EntityDTDoggy)o;
			            	if(dog.canInteract(player))
			            	{
			            		dog.getSitAI().setSitting(true);
			            		dog.setPathToEntity((PathEntity)null);
			            	    dog.setTarget((Entity)null);
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
			        	player.addChatMessage(ChatHelper.getChatComponentTranslation("dogCommand.stay"));
			        }
				}
				else if(commandId == 3)
				{
					world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
					boolean isDog = false;
			        List nearEnts = world.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.expand(20D, 20D, 20D));
			        for (Object o : nearEnts)
			        {
			            if (o instanceof EntityDTDoggy)
			            {
			            	EntityDTDoggy dog = (EntityDTDoggy)o;
			            	if(dog.canInteract(player))
			            	{
			            		if(dog.getMaxHealth() / 2 >= dog.getHealth())
			            		{
			            			dog.getSitAI().setSitting(true);
			            			dog.setPathToEntity((PathEntity)null);
				            	    dog.setTarget((Entity)null);
				            	    dog.setAttackTarget((EntityLivingBase)null);
			            		}
			            		else
			            		{
			            			dog.getSitAI().setSitting(false);
			            	        dog.setPathToEntity((PathEntity)null);
			            	        dog.setTarget((Entity)null);
			            	        dog.setAttackTarget((EntityLivingBase)null);
			            		}
			            		isDog = true;
			            	}
			            }
			        }
			        if(isDog)
			        {
			        	player.addChatMessage(ChatHelper.getChatComponentTranslation("dogCommand.ok"));
			        }
				}
				else if(commandId == 4)
				{
					world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
					boolean isDog = false;
			        List nearEnts = world.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.expand(20D, 20D, 20D));
			        for (Object o : nearEnts)
			        {
			            if (o instanceof EntityDTDoggy)
			            {
			            	EntityDTDoggy dog = (EntityDTDoggy)o;
			            	if(dog.canInteract(player) && !dog.isSitting() && !dog.mode.isMode(EnumMode.WANDERING))
			            	{
			            		 int i = MathHelper.floor_double(player.posX) - 2;
			                     int j = MathHelper.floor_double(player.posZ) - 2;
			                     int k = MathHelper.floor_double(player.boundingBox.minY);
			                     for (int l = 0; l <= 4; l++)
			                     {
			                         for (int i1 = 0; i1 <= 4; i1++)
			                         {
			                             if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && World.doesBlockHaveSolidTopSurface(world, i + l, k - 1, j + i1) && !world.getBlock(i + l, k, j + i1).isNormalCube() && !world.getBlock(i + l, k + 1, j + i1).isNormalCube() && world.getBlock(i + l, k + 1, j + i1) != Blocks.flowing_lava && world.getBlock(i + l, k + 1, j + i1) != Blocks.lava)
			                             {
			                                 dog.setLocationAndAngles((float)(i + l) + 0.5F, k, (float)(j + i1) + 0.5F, dog.rotationYaw, dog.rotationPitch);
			                             }
			                         }
			                     }
			                    isDog = true;
			            	}
			            }
			        }
			        
			        if(isDog)
			        {
			        	player.addChatMessage(ChatHelper.getChatComponentTranslation("dogCommand.heel"));
			        }
				}
					
			}
	}

}
