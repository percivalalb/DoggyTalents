package doggytalents.talent;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @author ProPercivalalb
 */
public class PuppyEyesTalent extends Talent {
	
	@Override
	public void onClassCreation(EntityDog dog) {
		dog.objects.put("charmercharge", 0);
		dog.objects.put("villagersorter", new EntityAINearestAttackableTarget.Sorter(dog));
	}
	
	@Override
	public void writeAdditional(EntityDog dog, NBTTagCompound tagCompound) {
		int charmerCharge = (Integer)dog.objects.get("charmercharge");
		tagCompound.setInteger("charmercharge", charmerCharge);
	}
	
	@Override
	public void readAdditional(EntityDog dog, NBTTagCompound tagCompound) {
		dog.objects.put("charmercharge", tagCompound.getInteger("charmercharge"));
	}
	
	@Override
	public void livingTick(EntityDog dog) {
		if(!dog.isTamed()) return;
		
		int charmerCharge = (Integer)dog.objects.get("charmercharge");
		if(charmerCharge > 0) {
			charmerCharge -= 1;
			dog.objects.put("charmercharge", charmerCharge);
        }
		
		int level = dog.TALENTS.getLevel(this);
		EntityPlayer player = (EntityPlayer)dog.getOwner();
		
		if(!dog.world.isRemote && dog.TALENTS.getLevel(this) != 0 && charmerCharge == 0) {
			EntityLiving entityliving = this.charmVillagers(dog, 5D);

			if(entityliving != null && player != null) {
				int j1 = dog.getRNG().nextInt(level) + (level != 5 ? 0 : 1);
				
	            if(j1 == 0) {
	            	player.sendMessage(new TextComponentTranslation("talent.doggytalents.puppy_eyes.msg.1.line.1", dog.GENDER.getGenderPronoun()));
	            	player.sendMessage(new TextComponentTranslation("talent.doggytalents.puppy_eyes.msg.1.line.2", dog.GENDER.getGenderSubject()));
	                entityliving.dropItem(Items.PORKCHOP, 2);
	            } else if(j1 == 1) {
	              	player.sendMessage(new TextComponentTranslation("talent.doggytalents.puppy_eyes.msg.2.line.1", dog.GENDER.getGenderTitle()));
	               	player.sendMessage(new TextComponentTranslation("talent.doggytalents.puppy_eyes.msg.2.line.2", dog.GENDER.getGenderTitle()));
	               	player.sendMessage(new TextComponentTranslation("talent.doggytalents.puppy_eyes.msg.2.line.3", dog.GENDER.getGenderTitle()));
	                entityliving.dropItem(Items.PORKCHOP, 5);
	            } else if(j1 == 2) {
	            	player.sendMessage(new TextComponentTranslation("talent.doggytalents.puppy_eyes.msg.3.line.1"));
	                player.sendMessage(new TextComponentTranslation("talent.doggytalents.puppy_eyes.msg.3.line.2"));
	                player.sendMessage(new TextComponentTranslation("talent.doggytalents.puppy_eyes.msg.3.line.3"));
	                entityliving.dropItem(Items.IRON_INGOT, 3);
	            } else if(j1 == 3) {
	             	player.sendMessage(new TextComponentTranslation("talent.doggytalents.puppy_eyes.msg.4.line.1"));
	               	player.sendMessage(new TextComponentTranslation("talent.doggytalents.puppy_eyes.msg.4.line.2"));
	               	player.sendMessage(new TextComponentTranslation("talent.doggytalents.puppy_eyes.msg.4.line.3"));
	                entityliving.dropItem(Items.GOLD_INGOT, 2);
	            } else if(j1 == 4) {
	             	player.sendMessage(new TextComponentTranslation("talent.doggytalents.puppy_eyes.msg.5.line.1"));
	               	player.sendMessage(new TextComponentTranslation("talent.doggytalents.puppy_eyes.msg.5.line.2"));
	               	player.sendMessage(new TextComponentTranslation("talent.doggytalents.puppy_eyes.msg.5.line.3"));
	                entityliving.dropItem(Items.DIAMOND, 1);
	            } else if(j1 == 5) {
	               	player.sendMessage(new TextComponentTranslation("talent.doggytalents.puppy_eyes.msg.6.line.1"));
	               	player.sendMessage(new TextComponentTranslation("talent.doggytalents.puppy_eyes.msg.6.line.2"));
	              	player.sendMessage(new TextComponentTranslation("talent.doggytalents.puppy_eyes.msg.6.line.3"));
	                entityliving.dropItem(Items.APPLE, 1);
	                entityliving.dropItem(Items.CAKE, 1);
	                entityliving.dropItem(Items.SLIME_BALL, 3);
	                entityliving.dropItem(Items.PORKCHOP, 5);
	            }

	            dog.objects.put("charmercharge", level != 5 ? 48000 : 24000);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public EntityLiving charmVillagers(EntityDog dogIn, double radiusIn) {
	    List<EntityVillager> list = dogIn.world.getEntitiesWithinAABB(EntityVillager.class, dogIn.getEntityBoundingBox().grow(radiusIn, radiusIn, radiusIn), village -> village.canEntityBeSeen(dogIn));
	    Collections.sort(list, (Comparator<Entity>)dogIn.objects.get("villagersorter"));
	    
	    if(list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}
}
