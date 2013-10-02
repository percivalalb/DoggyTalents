package doggytalents.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import doggytalents.ModItems;
import doggytalents.core.helper.LogHelper;
import doggytalents.entity.EntityDTDoggy;
import doggytalents.entity.data.EnumMode;
import doggytalents.entity.data.EnumTalents;
import doggytalents.network.PacketTypeHandler;

/**
 * @author ProPercivalalb
 */
public class PacketCommand extends DTPacket {

	public int commandId;
	
	public PacketCommand() {
		super(PacketTypeHandler.DOG_COMMAND, false);
	}
	
	public PacketCommand(int commandId) {
		this();
		this.commandId = commandId;
	}

	@Override
	public void readData(DataInputStream data) throws IOException {
		this.commandId = data.readInt();
	}

	@Override
	public void writeData(DataOutputStream dos) throws IOException {
		dos.writeInt(commandId);
	}

	@Override
	public void execute(INetworkManager network, EntityPlayer player) {
		World world = player.worldObj;
		ItemStack stack = player.getCurrentEquippedItem();
		if(stack == null)
			return;
		
		if(stack.itemID == ModItems.commandEmblem.itemID) {

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
            			player.addChatMessage("Stand!");
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
			        	player.addChatMessage("Stay!");
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
			            		if(dog.getMaxHealth() / 2 >= dog.getMaxHealth())
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
			        	player.addChatMessage("Okay!");
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

}
