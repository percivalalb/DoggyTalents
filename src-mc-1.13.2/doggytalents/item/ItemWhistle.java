package doggytalents.item;

import java.util.List;
import java.util.stream.Collectors;

import doggytalents.ModSounds;
import doggytalents.ModTalents;
import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import doggytalents.entity.features.ModeFeature.EnumMode;
import doggytalents.helper.DogUtil;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
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
				stack.getTag().putInt("mode", (mode + 1) % 7);
				
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
			else {
				byte mode = 0;
				
				if(stack.hasTag() && stack.getTag().contains("mode", 99)) {
					mode = stack.getTag().getByte("mode");
				}
				
				List<EntityDog> dogsList = world.getEntitiesWithinAABB(EntityDog.class, player.getBoundingBox().grow(100D, 50D, 100D), dog -> dog.isOwner(player));
				boolean successful = false;
				
				if(mode == 0) { // Stand
    			    for(EntityDog dog : dogsList) {
    			    	dog.getAISit().setSitting(false);
    			    	dog.getNavigator().clearPath();
    			    	dog.setAttackTarget((EntityLivingBase)null);
    			    	if(dog.MODE.isMode(EnumMode.WANDERING))
    			    		dog.MODE.setMode(EnumMode.DOCILE);
    			    	successful = true;
    			    }
    			
    			    world.playSound(null, player.getPosition(), ModSounds.WHISTLE_LONG, SoundCategory.PLAYERS, 0.6F + world.rand.nextFloat() * 0.1F, 0.8F + world.rand.nextFloat() * 0.2F);
    			    
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
    			    
    				world.playSound(null, player.getPosition(), ModSounds.WHISTLE_LONG, SoundCategory.PLAYERS, 0.6F + world.rand.nextFloat() * 0.1F, 0.8F + world.rand.nextFloat() * 0.2F);
    				
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
    				
    				world.playSound(null, player.getPosition(), ModSounds.WHISTLE_SHORT, SoundCategory.PLAYERS, 0.6F + world.rand.nextFloat() * 0.1F, 0.8F + world.rand.nextFloat() * 0.2F);
    				
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
    				
    				world.playSound(null, player.getPosition(), ModSounds.WHISTLE_LONG, SoundCategory.PLAYERS, 0.6F + world.rand.nextFloat() * 0.1F, 0.4F + world.rand.nextFloat() * 0.2F);
    				
    				if(successful)
    					player.sendMessage(new TextComponentTranslation("dogcommand.ok"));
    			}
    			else if(mode == 4) {
    				for(EntityDog dog : dogsList) {
    					dog.getAISit().setSitting(true);
    					dog.getNavigator().clearPath();
    					dog.setAttackTarget((EntityLivingBase)null);
    					dog.MODE.setMode(EnumMode.DOCILE);
    					successful = true;
    				}
    				
    				world.playSound(null, player.getPosition(), ModSounds.WHISTLE_SHORT, SoundCategory.PLAYERS, 0.6F + world.rand.nextFloat() * 0.1F, 0.8F + world.rand.nextFloat() * 0.2F);
    				//player.sendMessage(new TextComponentTranslation("dogcommand.shepherd"));
    			} else if(mode == 5) {
    				world.playSound((EntityPlayer)null, player.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
    				if(!world.isRemote) {
    					EntityDoggyBeam doggyBeam = new EntityDoggyBeam(world, player);
    		            doggyBeam.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.0F, 1.0F);
    		            world.spawnEntity(doggyBeam);
    				}
    			} else if(mode == 6) {
    				List<EntityDog> roarDogs = dogsList.stream().filter(dog -> dog.TALENTS.getLevel(ModTalents.ROARING_GALE) > 0).collect(Collectors.toList());
    				if(roarDogs.isEmpty()) {
    					player.sendStatusMessage(new TextComponentTranslation("talent.doggytalents.roaring_gale.level"), true);
    				} else {
    					List<EntityDog> cdDogs = roarDogs.stream().filter(dog -> (int)dog.objects.get("roarcooldown") == 0).collect(Collectors.toList());
    					if(cdDogs.isEmpty()) {
    						player.sendStatusMessage(new TextComponentTranslation("talent.doggytalents.roaring_gale.cooldown"), true);
    					} else {
    						for(EntityDog dog : dogsList) {
    	    					int level = dog.TALENTS.getLevel(ModTalents.ROARING_GALE);
    	    					int roarCooldown = (Integer)dog.objects.get("roarcooldown");

    	    					roarCooldown = level == 5 ? 60 : 100;
    	    						
    	    					byte damage = (byte)(level > 4 ? level * 2 : level);
    	    						
    	    					/**
    	    					 * If level = 1, set duration to  20 ticks (1 second); level = 2, set duration to 24 ticks (1.2 seconds)
    	    					 * If level = 3, set duration to 36 ticks (1.8 seconds); If level = 4, set duration to 48 ticks (2.4 seconds)
    	    					 * If level = max (5), set duration to 70 ticks (3.5 seconds); 
    	    					 * */
    	    					byte effectDuration = (byte)(level > 4 ? level * 14 : level * (level == 1 ? 20 : 12));
    	    					byte knockback = (byte)level;
    	    						
    	    					boolean hit = false;
    	    					List<EntityLivingBase> list = dog.world.<EntityLivingBase>getEntitiesWithinAABB(EntityLiving.class, dog.getBoundingBox().grow(level * 4, 4D, level * 4).grow(0.0D, (double) dog.world.getHeight(), 0.0D));
    	    					for(EntityLivingBase mob : list) {
    	    						if(mob instanceof IMob) {
    	    							hit = true;
    	    							mob.attackEntityFrom(DamageSource.GENERIC, damage);
    	    							mob.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, effectDuration, 127, false, false));
    	    							mob.addPotionEffect(new PotionEffect(MobEffects.GLOWING, effectDuration, 1, false, false));
    	    							mob.addVelocity(MathHelper.sin(mob.rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F, 0.1D, -MathHelper.cos(mob.rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F);
    	    						}
    	    					}
    	    						
    	    					if(hit) {
    	    						dog.playSound(SoundEvents.ENTITY_WOLF_GROWL, 0.7F, 1.0F);
    	    					} else {
    	    						dog.playSound(SoundEvents.ENTITY_WOLF_AMBIENT, 1F, 1.2F);
    	    						roarCooldown /= 2;
    	    					}
    	    						
    	    					dog.objects.put("roarcooldown", roarCooldown);
    						}
    					}
    				}
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