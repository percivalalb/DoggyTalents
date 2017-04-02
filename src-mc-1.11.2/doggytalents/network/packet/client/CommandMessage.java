package doggytalents.network.packet.client;

import java.util.List;

import doggytalents.ModItems;
import doggytalents.entity.EntityDog;
import doggytalents.entity.ModeUtil.EnumMode;
import doggytalents.helper.ChatHelper;
import doggytalents.network.AbstractMessage.AbstractServerMessage;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
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

		if((player.getHeldItemMainhand().getItem() == ModItems.commandEmblem || player.getHeldItemOffhand().getItem() == ModItems.commandEmblem)) {

			FMLLog.info(this.commandId + " id");
			if(this.commandId == 1)
			{
				world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
				boolean isDog = false;
			    List nearEnts = world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().expand(20D, 20D, 20D));
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
            			player.sendMessage(ChatHelper.getChatComponentTranslation("dogcommand.come"));
            		}
				}
				else if(this.commandId == 2)
				{
					world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
					boolean isDog = false;
			        List nearEnts = world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().expand(20D, 20D, 20D));
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
			        	player.sendMessage(ChatHelper.getChatComponentTranslation("dogcommand.stay"));
			        }
				}
				else if(this.commandId == 3)
				{
					world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
					boolean isDog = false;
			        List nearEnts = world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().expand(20D, 20D, 20D));
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
			        	player.sendMessage(ChatHelper.getChatComponentTranslation("dogcommand.ok"));
			        }
				}
				else if(this.commandId == 4)
				{
					world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
					boolean isDog = false;
			        List nearEnts = world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().expand(20D, 20D, 20D));
			        for (Object o : nearEnts)
			        {
			            if(o instanceof EntityDog)
			            {
			            	EntityDog dog = (EntityDog)o;
			            	if(dog.canInteract(player) && !dog.isSitting() && !dog.mode.isMode(EnumMode.WANDERING))
			            	{
			            		 int i = MathHelper.floor(player.posX) - 2;
			                     int j = MathHelper.floor(player.posZ) - 2;
			                     int k = MathHelper.floor(player.getEntityBoundingBox().minY);
			                     for (int l = 0; l <= 4; l++)
			                     {
			                         for (int i1 = 0; i1 <= 4; i1++)
			                         {
			                             if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && world.getBlockState(new BlockPos(i + l, k - 1, j + i1)).isFullyOpaque() && this.isEmptyBlock(world, new BlockPos(i + l, k, j + i1)) && this.isEmptyBlock(world, new BlockPos(i + l, k + 1, j + i1)))
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
			        	player.sendMessage(ChatHelper.getChatComponentTranslation("dogcommand.heel"));
			        }
				}
					
			}
		
	}
	
	private boolean isEmptyBlock(World world, BlockPos pos) {
        IBlockState iblockstate = world.getBlockState(pos);
        return iblockstate.getMaterial() == Material.AIR ? true : !iblockstate.isFullCube();
    }
}
	