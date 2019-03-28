package doggytalents.talent;

import java.util.List;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;

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
		tagCompound.putInt("charmercharge", charmerCharge);
	}
	
	@Override
	public void readFromNBT(EntityDog dog, NBTTagCompound tagCompound) {
		dog.objects.put("charmercharge", tagCompound.getInt("charmercharge"));
	}
	
	@Override
	public void onLivingUpdate(EntityDog dog) {
		if(!dog.isTamed()) return;
		
		int charmerCharge = (Integer)dog.objects.get("charmercharge");
		if(charmerCharge > 0) {
			charmerCharge -= 1;
			dog.objects.put("charmercharge", charmerCharge);
        }
		
		int level = dog.TALENTS.getLevel(this);
		EntityPlayer player = (EntityPlayer)dog.getOwner();
		
		if(dog.isServer() && dog.TALENTS.getLevel(this) != 0 && charmerCharge == 0) {
			EntityLiving entityliving = this.charmVillagers(dog, 5D);

			if(entityliving != null && player != null) {
				int j1 = dog.getRNG().nextInt(level) + (level != 5 ? 0 : 1);

	            if(j1 == 0)
	            {
	            	player.sendMessage(new TextComponentTranslation("dogtalent.puppyeyes.1.part1", dog.GENDER.getGenderPronoun()));
	            	player.sendMessage(new TextComponentTranslation("dogtalent.puppyeyes.1.part2", dog.GENDER.getGenderSubject()));
	                entityliving.entityDropItem(Items.PORKCHOP, 2);
	            }

	            if(j1 == 1)
	            {
	              	player.sendMessage(new TextComponentTranslation("dogtalent.puppyeyes.2.part1", dog.GENDER.getGenderTitle()));
	               	player.sendMessage(new TextComponentTranslation("dogtalent.puppyeyes.2.part2", dog.GENDER.getGenderTitle()));
	               	player.sendMessage(new TextComponentTranslation("dogtalent.puppyeyes.2.part3", dog.GENDER.getGenderTitle()));
	                entityliving.entityDropItem(Items.PORKCHOP, 5);
	            }

	            if(j1 == 2)
	            {
	            	player.sendMessage(new TextComponentTranslation("dogtalent.puppyeyes.3.part1"));
	                player.sendMessage(new TextComponentTranslation("dogtalent.puppyeyes.3.part2"));
	                player.sendMessage(new TextComponentTranslation("dogtalent.puppyeyes.3.part3"));
	                entityliving.entityDropItem(Items.IRON_INGOT, 3);
	            }

	            if(j1 == 3)
	            {
	             	player.sendMessage(new TextComponentTranslation("dogtalent.puppyeyes.4.part1"));
	               	player.sendMessage(new TextComponentTranslation("dogtalent.puppyeyes.4.part2"));
	               	player.sendMessage(new TextComponentTranslation("dogtalent.puppyeyes.4.part3"));
	                entityliving.entityDropItem(Items.GOLD_INGOT, 2);
	            }

	            if(j1 == 4)
	            {
	             	player.sendMessage(new TextComponentTranslation("dogtalent.puppyeyes.5.part1"));
	               	player.sendMessage(new TextComponentTranslation("dogtalent.puppyeyes.5.part2"));
	               	player.sendMessage(new TextComponentTranslation("dogtalent.puppyeyes.5.part3"));
	                entityliving.entityDropItem(Items.DIAMOND, 1);
	            }

	            if(j1 == 5)
	            {
	               	player.sendMessage(new TextComponentTranslation("dogtalent.puppyeyes.6.part1"));
	               	player.sendMessage(new TextComponentTranslation("dogtalent.puppyeyes.6.part2"));
	              	player.sendMessage(new TextComponentTranslation("dogtalent.puppyeyes.6.part3"));
	                entityliving.entityDropItem(Items.APPLE, 1);
	                entityliving.entityDropItem(Blocks.CAKE, 1);
	                entityliving.entityDropItem(Items.SLIME_BALL, 3);
	                entityliving.entityDropItem(Items.PORKCHOP, 5);
	            }

	            dog.objects.put("charmercharge", level != 5 ? 48000 : 24000);
			}
		}
	}
	
	public EntityLiving charmVillagers(EntityDog dogIn, double radiusIn) {
		double distance = -1D;
		EntityVillager charmVillager = null;
	    List<EntityVillager> list = dogIn.world.getEntitiesWithinAABB(EntityVillager.class, dogIn.getBoundingBox().grow(radiusIn, radiusIn, radiusIn));

	    for (EntityVillager entityVillager : list) {
	    	double d2 = entityVillager.getDistanceSq(dogIn.posX, dogIn.posY, dogIn.posZ);

	    	if ((radiusIn < 0.0D || d2 < radiusIn * radiusIn) && (distance == -1D || d2 < distance) && entityVillager.canEntityBeSeen(dogIn)) {
	    		distance = d2;
	    		charmVillager = entityVillager;
	    	}
	    }

	    return charmVillager;
	}
	
	@Override
	public String getKey() {
		return "puppyeyes";
	}

}
