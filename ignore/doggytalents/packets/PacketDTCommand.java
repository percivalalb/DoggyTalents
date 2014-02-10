package doggytalents.packets;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.List;

import doggytalents.DoggyTalentsMod;
import doggytalents.entity.EntityDTDoggy;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class PacketDTCommand implements IPacket {

	@Override
	public void handle(INetworkManager manager, Packet250CustomPayload packet, EntityPlayer player) {
		World world = player.worldObj;
		try {
			if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().itemID == DoggyTalentsMod.commandEmblem.itemID) {
				DataInputStream var1 = new DataInputStream(new ByteArrayInputStream(packet.data));
				int var2 = var1.readInt();
            
				if(var2 == 1)
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
			            		dog.setSitting(false);
			                    dog.setPathToEntity((PathEntity)null);
			                    dog.setAttackTarget((EntityLiving)null);
			                    isDog = true;
			            	}
			            }
			        }
            		if(isDog)
            		{
            			player.addChatMessage("Stand!");
            		}
				}
				else if(var2 == 2)
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
			            		dog.setSitting(true);
			                    dog.setPathToEntity((PathEntity)null);
			                    dog.setAttackTarget((EntityLiving)null);
			                    isDog = true;
			            	}
			            }
			        }
			        if(isDog)
			        {
			        	player.addChatMessage("Sit!");
			        }
				}
				else if(var2 == 1)
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
			            		dog.setSitting(false);
			                    dog.setPathToEntity((PathEntity)null);
			                    dog.setAttackTarget((EntityLiving)null);
			                  	
			                    isDog = true;
			            	}
			            }
			        }
			        if(isDog)
			        {
			        	player.addChatMessage("Stand!");
			        }
				}
				else if(var2 == 3)
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
			            			dog.setSitting(true);
			                        dog.setPathToEntity((PathEntity)null);
			                        dog.setAttackTarget((EntityLiving)null);
			            		}
			            		else
			            		{
			                		dog.setSitting(false);
			                        dog.setPathToEntity((PathEntity)null);
			                        dog.setAttackTarget((EntityLiving)null);
			            		}
			            		isDog = true;
			            	}
			            }
			        }
			        if(isDog)
			        {
			        	player.addChatMessage("Okay!");
			        }
				}
				else if(false)
				{
					world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
					boolean isDog = false;
			        List nearEnts = world.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.expand(20D, 20D, 20D));
			        for (Object o : nearEnts)
			        {
			            if (o instanceof EntityDTDoggy)
			            {
			            	EntityDTDoggy dog = (EntityDTDoggy)o;
			            	if(dog.getOwner().equalsIgnoreCase(player.username))
			            	{
			            		 int i = MathHelper.floor_double(player.posX) - 2;
			                     int j = MathHelper.floor_double(player.posZ) - 2;
			                     int k = MathHelper.floor_double(player.boundingBox.minY);
			                     for (int l = 0; l <= 4; l++)
			                     {
			                         for (int i1 = 0; i1 <= 4; i1++)
			                         {
			                             if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && player.worldObj.isBlockNormalCube(i + l, k - 1, j + i1) && !player.worldObj.isBlockNormalCube(i + l, k, j + i1) && !player.worldObj.isBlockNormalCube(i + l, k + 1, j + i1)  && player.worldObj.getBlockId(i + l, k + 1, j + i1) != Block.lavaMoving.blockID && player.worldObj.getBlockId(i + l, k + 1, j + i1) != Block.lavaStill.blockID)
			                             {
			                                 dog.setLocationAndAngles((float)(i + l) + 0.5F, k, (float)(j + i1) + 0.5F, dog.rotationYaw, dog.rotationPitch);
			                             }
			                         }
			                     }
			                    isDog = true;
			            	}
			            }
			        }
				}
					
			}
        }
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
