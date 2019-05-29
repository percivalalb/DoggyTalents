package doggytalents.item;

import java.util.List;

import doggytalents.ModSounds;
import doggytalents.entity.EntityDog;
import doggytalents.entity.features.ModeFeature.EnumMode;
import doggytalents.helper.DogUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemWhistle extends Item {
	
	public ItemWhistle(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if(world.isRemote) {
			//world.playSound(player, player.getPosition(), player.isSneaking() ? SWSound.WHISTLE_LONG : SWSound.WHISTLE_SHORT, SoundCategory.PLAYERS, 1, 1);
		} else {
			ItemStack stack = player.getHeldItem(hand);
			
			if(player.isSneaking()) {
				if(!stack.hasTag()) {
					stack.setTag(new NBTTagCompound());
					stack.getTag().putByte("mode", (byte)0);
				}
				
				int mode = stack.getTag().getInt("mode");
				stack.getTag().putInt("mode", (mode + 1) % 5);
				
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
			else {
				byte mode = 0;
				
				if(stack.hasTag() && stack.getTag().contains("mode", 99)) {
					mode = stack.getTag().getByte("mode");
				}
				
				List<EntityDog> dogsList = world.getEntitiesWithinAABB(EntityDog.class, player.getBoundingBox().grow(100D, 50D, 100D), dog -> dog.isOwner(player));
				boolean successful = false;
				
				if(mode == 0) { // Come
    			    for(EntityDog dog : dogsList) {
    			    	dog.getAISit().setSitting(false);
    			    	dog.getNavigator().clearPath();
    			    	dog.setAttackTarget((EntityLivingBase)null);
    			    	if(dog.MODE.isMode(EnumMode.WANDERING))
    			    		dog.MODE.setMode(EnumMode.DOCILE);
    			    	successful = true;
    			    }
    			
    			    world.playSound(null, player.getPosition(), ModSounds.WHISTLE_LONG, SoundCategory.PLAYERS, 3F, 0.8F + world.rand.nextFloat() * 0.2F);
    			    
    			    if(successful)
    			    	player.sendMessage(new TextComponentTranslation("dogcommand.come"));
    			}
				else if(mode == 1) { // Heel
    				for(EntityDog dog : dogsList) {
    					if(!dog.isSitting() && !dog.MODE.isMode(EnumMode.WANDERING)) {
    						DogUtil.teleportDogToOwner(player, dog, world, dog.getNavigator());
    						successful = true;
    					}  
    				}
    			    
    				world.playSound(null, player.getPosition(), ModSounds.WHISTLE_LONG, SoundCategory.PLAYERS, 3F, 0.8F + world.rand.nextFloat() * 0.2F);
    				
    				if(successful)
    					player.sendMessage(new TextComponentTranslation("dogcommand.heel"));
    			}
    			else if(mode == 2) { // Stay
    				for(EntityDog dog : dogsList) {
    					dog.getAISit().setSitting(true);
    					dog.getNavigator().clearPath();
    					dog.setAttackTarget((EntityLivingBase)null);
    					if(dog.MODE.isMode(EnumMode.WANDERING))
    						dog.MODE.setMode(EnumMode.DOCILE);
    					successful = true;
    				}
    				
    				world.playSound(null, player.getPosition(), ModSounds.WHISTLE_SHORT, SoundCategory.PLAYERS, 3F, 0.8F + world.rand.nextFloat() * 0.2F);
    				
    		        if(successful)
    		        	player.sendMessage(new TextComponentTranslation("dogcommand.stay"));
    			}
    			else if(mode == 3) { // Ok
    				for(EntityDog dog : dogsList) {
    					if(dog.getMaxHealth() / 2 >= dog.getHealth()) {
    						dog.getAISit().setSitting(true);
    						dog.getNavigator().clearPath();
    						dog.setAttackTarget((EntityLivingBase)null);
    					}
    					else {
    						dog.getAISit().setSitting(false);
    						dog.getNavigator().clearPath();
    						dog.setAttackTarget((EntityLivingBase)null);
    					}
    					successful = true;      
    				}
    				
    				world.playSound(null, player.getPosition(), ModSounds.WHISTLE_LONG, SoundCategory.PLAYERS, 3F, 0.4F + world.rand.nextFloat() * 0.2F);
    				
    				if(successful)
    					player.sendMessage(new TextComponentTranslation("dogcommand.ok"));
    			}
    			else {
    				world.playSound(null, player.getPosition(), ModSounds.WHISTLE_SHORT, SoundCategory.PLAYERS, 3F, 0.8F + world.rand.nextFloat() * 0.2F);
    				//player.sendMessage(new TextComponentTranslation("dogcommand.shepherd"));
    			}
    			
    			
    			//world.playSound(null, player.getPosition(), ModSounds.WHISTLE_LONG, SoundCategory.PLAYERS, 0.8F, 0.8F + world.rand.nextFloat() * 0.2F);
    			//world.playSound(null, player.getPosition(), ModSounds.WHISTLE_SHORT, SoundCategory.PLAYERS, 0.8F, 0.6F + world.rand.nextFloat() * 0.2F);

			}
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
	
	@Override
	public String getTranslationKey(ItemStack stack) {
		byte mode = 0;
		
		if(stack.hasTag() && stack.getTag().contains("mode", 99)) {
			mode = stack.getTag().getByte("mode");
		}
		return this.getTranslationKey() + "." + mode;

	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }
}