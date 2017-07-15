package doggytalents.talent;

import java.util.List;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import doggytalents.helper.ChatUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ProPercivalalb
 */
public class PuppyEyes extends ITalent {
	
	@Override
	public void onClassCreation(EntityDog dog) {
		dog.objects.put("charmercharge", 0);
	}
	
	@Override
	public void writeToNBT(EntityDog dog, NBTTagCompound tagCompound) {
		int charmerCharge = (Integer)dog.objects.get("charmercharge");
		tagCompound.setInteger("charmercharge", charmerCharge);
	}
	
	@Override
	public void readFromNBT(EntityDog dog, NBTTagCompound tagCompound) {
		dog.objects.put("charmercharge", tagCompound.getInteger("charmercharge"));
	}
	
	@Override
	public void onLivingUpdate(EntityDog dog) {
		
		int charmerCharge = (Integer)dog.objects.get("charmercharge");
		if(charmerCharge > 0 && dog.isTamed()) {
			charmerCharge -= 1;
			dog.objects.put("charmercharge", charmerCharge);
        }
		
		int level = dog.talents.getLevel(this);
		EntityPlayer player = (EntityPlayer)dog.getOwner();
		
		if(!dog.world.isRemote && dog.talents.getLevel(this) != 0 && charmerCharge == 0) {
			EntityLiving entityliving = this.charmVillagers(dog, 5D);

			if(entityliving != null && player != null) {
				int j1 = dog.getRNG().nextInt(level) + (level != 5 ? 0 : 1);

	            if(j1 == 0)
	            {
	            	player.sendMessage(ChatUtil.getChatComponentTranslation("dogtalent.puppyeyes.1.part1"));
	            	player.sendMessage(ChatUtil.getChatComponentTranslation("dogtalent.puppyeyes.1.part2"));
	                entityliving.dropItem(Items.PORKCHOP, 2);
	            }

	            if(j1 == 1)
	            {
	              	player.sendMessage(ChatUtil.getChatComponentTranslation("dogtalent.puppyeyes.2.part1"));
	               	player.sendMessage(ChatUtil.getChatComponentTranslation("dogtalent.puppyeyes.2.part2"));
	               	player.sendMessage(ChatUtil.getChatComponentTranslation("dogtalent.puppyeyes.2.part3"));
	                entityliving.dropItem(Items.PORKCHOP, 5);
	            }

	            if(j1 == 2)
	            {
	            	player.sendMessage(ChatUtil.getChatComponentTranslation("dogtalent.puppyeyes.3.part1"));
	                player.sendMessage(ChatUtil.getChatComponentTranslation("dogtalent.puppyeyes.3.part2"));
	                player.sendMessage(ChatUtil.getChatComponentTranslation("dogtalent.puppyeyes.3.part3"));
	                entityliving.dropItem(Items.IRON_INGOT, 3);
	            }

	            if(j1 == 3)
	            {
	             	player.sendMessage(ChatUtil.getChatComponentTranslation("dogtalent.puppyeyes.4.part1"));
	               	player.sendMessage(ChatUtil.getChatComponentTranslation("dogtalent.puppyeyes.4.part2"));
	               	player.sendMessage(ChatUtil.getChatComponentTranslation("dogtalent.puppyeyes.4.part3"));
	                entityliving.dropItem(Items.GOLD_INGOT, 2);
	            }

	            if(j1 == 4)
	            {
	             	player.sendMessage(ChatUtil.getChatComponentTranslation("dogtalent.puppyeyes.5.part1"));
	               	player.sendMessage(ChatUtil.getChatComponentTranslation("dogtalent.puppyeyes.5.part2"));
	               	player.sendMessage(ChatUtil.getChatComponentTranslation("dogtalent.puppyeyes.5.part3"));
	                entityliving.dropItem(Items.DIAMOND, 1);
	            }

	            if(j1 == 5)
	            {
	               	player.sendMessage(ChatUtil.getChatComponentTranslation("dogtalent.puppyeyes.6.part1"));
	               	player.sendMessage(ChatUtil.getChatComponentTranslation("dogtalent.puppyeyes.6.part2"));
	              	player.sendMessage(ChatUtil.getChatComponentTranslation("dogtalent.puppyeyes.6.part3"));
	                entityliving.dropItem(Items.APPLE, 1);
	                entityliving.dropItem(Items.CAKE, 1);
	                entityliving.dropItem(Items.SLIME_BALL, 3);
	                entityliving.dropItem(Items.PORKCHOP, 5);
	            }

	            dog.objects.put("charmercharge", level != 5 ? 48000 : 24000);
			}
		}
	}
	
	public EntityLiving charmVillagers(EntityDog dog, double d) {
		double d1 = -1D;
		EntityPlayer player = (EntityPlayer)dog.getOwner();
	    EntityLiving entityliving = null;
	    List list = dog.world.getEntitiesWithinAABBExcludingEntity(dog, dog.getEntityBoundingBox().grow(d, d, d));

	    for (int i = 0; i < list.size(); i++) {
	    	Entity entity1 = (Entity)list.get(i);

	            if (!(entity1 instanceof EntityVillager))
	                continue;

	            double d2 = entity1.getDistanceSq(dog.posX, dog.posY, dog.posZ);

	            if ((d < 0.0D || d2 < d * d) && (d1 == -1D || d2 < d1) && ((EntityLiving)entity1).canEntityBeSeen(dog)) {
	                d1 = d2;
	                entityliving = (EntityLiving)entity1;
	            }
	        }

	        return entityliving;
	    }
	
	@Override
	public String getKey() {
		return "puppyeyes";
	}

}
