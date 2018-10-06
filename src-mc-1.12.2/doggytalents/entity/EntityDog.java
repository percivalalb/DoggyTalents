package doggytalents.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import doggytalents.DoggyTalents;
import doggytalents.ModBlocks;
import doggytalents.ModItems;
import doggytalents.api.IDogTreat;
import doggytalents.api.IDogTreat.EnumFeedBack;
import doggytalents.entity.ModeUtil.EnumMode;
import doggytalents.entity.ai.EntityAIDogBeg;
import doggytalents.entity.ai.EntityAIDogFeed;
import doggytalents.entity.ai.EntityAIDogWander;
import doggytalents.entity.ai.EntityAIFetch;
import doggytalents.entity.ai.EntityAIFollowOwner;
import doggytalents.entity.ai.EntityAIModeAttackTarget;
import doggytalents.entity.ai.EntityAIOwnerHurtByTarget;
import doggytalents.entity.ai.EntityAIOwnerHurtTarget;
import doggytalents.entity.ai.EntityAIShepherdDog;
import doggytalents.helper.DogUtil;
import doggytalents.inventory.InventoryTreatBag;
import doggytalents.item.ItemChewStick;
import doggytalents.lib.Constants;
import doggytalents.lib.Reference;
import doggytalents.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
public class EntityDog extends EntityAbstractDog /*implements IRangedAttackMob*/ { //TODO RangedAttacker
	
    private float timeWolfIsHappy;
    private float prevTimeWolfIsHappy;
    private boolean isWolfHappy;
    public boolean hiyaMaster;
    private int reversionTime;
    private boolean hasBone;
    public EntityAIFetch aiFetchBone;
    public TalentUtil talents;
    public LevelUtil levels;
    public ModeUtil mode;
    public CoordUtil coords;
    public Map<String, Object> objects;
    
	public int prevSize;
    
    //Timers
    private int hungerTick;
   	private int prevHungerTick;
    private int healingTick;
    private int prevHealingTick;
    private int regenerationTick;
    private int prevRegenerationTick;
    private int foodBowlCheck;
    
    //TODO public List<BlockPos> patrolOutline;
    
    public EntityDog(World world) {
        super(world);
        this.setTamed(false);

        //TODO this.patrolOutline = new ArrayList<BlockPos>();
        this.objects = new HashMap<String, Object>();
        TalentHelper.onClassCreation(this);
    }
    
    @Override
    protected void initEntityAI() {
       	this.aiSit = new EntityAISit(this);
        this.aiFetchBone = new EntityAIFetch(this, 1.0D, 20.0F);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, true));
        //TODO this.tasks.addTask(4, new EntityAIPatrolArea(this));
        this.tasks.addTask(6, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F)); //Uses EntityDogClass
        this.tasks.addTask(5, this.aiFetchBone);
        this.tasks.addTask(7, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(8, new EntityAIDogWander(this, 1.0D));
        this.tasks.addTask(9, new EntityAIDogBeg(this, 8.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(10, new EntityAILookIdle(this));
        this.tasks.addTask(11, new EntityAIDogFeed(this, 20.0F));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIModeAttackTarget(this));
        this.targetTasks.addTask(4, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(5, new EntityAITargetNonTamed(this, EntityAnimal.class, false, entity -> (entity instanceof EntitySheep || entity instanceof EntityRabbit)));
        this.targetTasks.addTask(6, new EntityAIShepherdDog(this, EntityAnimal.class, 0, false));
    }
	
    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : (Entity)this.getPassengers().get(0);
    }
    
	@Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
    }
	
    @Override
    protected SoundEvent getAmbientSound() {
    	if(this.getDogHunger() <= Constants.LOW_HUNGER) {
    		return SoundEvents.ENTITY_WOLF_WHINE;
    	}
    	if (this.rand.nextInt(3) == 0)
        {
            return this.isTamed() && this.getHealth() < this.getMaxHealth() / 2 ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT;
        }
        else
        {
            return SoundEvents.ENTITY_WOLF_AMBIENT;
        }
    	
        //return this.rand.nextInt(3) == 0 ? (this.getDogHunger() <= Constants.lowHunger ? SoundEvents.ENTITY_WOLF_WHINE : this.isTamed() && this.getHealth() < this.getMaxHealth() / 2 ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT) : SoundEvents.ENTITY_WOLF_AMBIENT;
    }
    
	
    @Override
	protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_WOLF_HURT;
    }

	@Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WOLF_DEATH;
    }
    
    @Override
    protected ResourceLocation getLootTable() {
        return null; //TODO DOG Loot
    }
    
	@Override
    public void updatePassenger(Entity passenger){
        super.updatePassenger(passenger);

        if(passenger instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving)passenger;
            this.renderYawOffset = entityliving.renderYawOffset;
        }
    }
	
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.isTamed() ? 20.0D : 8.0D);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean getAlwaysRenderNameTagForRender() {
        return this.hasCustomName();
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        //Allow for the global searching of dogs with the whistle item
        //this.enablePersistence();
        
        // This method is called from the constructor early
        this.talents = new TalentUtil(this);
        this.levels = new LevelUtil(this);
        this.mode = new ModeUtil(this);
        this.coords = new CoordUtil(this);
        
        this.dataTracker.entityInit();
        
        if (this.getGender().isEmpty()) {
        	if (rand.nextInt(2) == 0) {
        		this.setGender("male");
        	}
        	else {
        		this.setGender("female");
        	}
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setString("version", Reference.MOD_VERSION);
        
        tagCompound.setInteger("doggyTex", this.getTameSkin());
        tagCompound.setInteger("collarColour", this.getCollarData());
        tagCompound.setInteger("dogHunger", this.getDogHunger());
        tagCompound.setBoolean("willObey", this.willObeyOthers());
        tagCompound.setBoolean("friendlyFire", this.canFriendlyFire());
        tagCompound.setBoolean("radioCollar", this.hasRadarCollar());
        tagCompound.setBoolean("sunglasses", this.hasSunglasses());
        tagCompound.setInteger("capeData", this.getCapeData());
        tagCompound.setInteger("dogSize", this.getDogSize());
        tagCompound.setString("dogGender", this.getGender());
        
        this.talents.writeTalentsToNBT(tagCompound);
        this.levels.writeTalentsToNBT(tagCompound);
        this.mode.writeToNBT(tagCompound);
        this.coords.writeToNBT(tagCompound);
        TalentHelper.writeToNBT(this, tagCompound);
        
        //this.locationManager.addOrUpdateLocation(this);
        
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompound) {
        super.readEntityFromNBT(tagCompound);

        String lastVersion = tagCompound.getString("version");
        this.setTameSkin(tagCompound.getInteger("doggyTex"));
        if(tagCompound.hasKey("collarColour", 99)) this.setCollarData(tagCompound.getInteger("collarColour"));
        this.setDogHunger(tagCompound.getInteger("dogHunger"));
        this.setWillObeyOthers(tagCompound.getBoolean("willObey"));
        this.setFriendlyFire(tagCompound.getBoolean("friendlyFire"));
        this.hasRadarCollar(tagCompound.getBoolean("radioCollar"));
        this.setHasSunglasses(tagCompound.getBoolean("sunglasses"));
        if(tagCompound.hasKey("capeData", 99)) this.setCapeData(tagCompound.getInteger("capeData"));
        if(tagCompound.hasKey("dogSize", 99)) this.setDogSize(tagCompound.getInteger("dogSize"));
        this.setGender(tagCompound.getString("dogGender"));
        
        this.talents.readTalentsFromNBT(tagCompound);
        this.levels.readTalentsFromNBT(tagCompound);
        this.mode.readFromNBT(tagCompound);
        this.coords.readFromNBT(tagCompound);
        TalentHelper.readFromNBT(this, tagCompound);
        
        //this.locationManager.updateEntityId(this);
        
        //Backwards Compatibility
        if(tagCompound.hasKey("dogName"))
        	this.setCustomNameTag(tagCompound.getString("dogName"));
    }
    
    public EntityAISit getSitAI() {
    	return this.aiSit;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        
        if(Constants.IS_HUNGER_ON) {
        	this.prevHungerTick = this.hungerTick;
        	
	        if(!this.isBeingRidden() && !this.isSitting() /** && !this.mode.isMode(EnumMode.WANDERING) && !this.level.isDireDog() || worldObj.getWorldInfo().getWorldTime() % 2L == 0L **/)
	        	this.hungerTick += 1;
	        
	        this.hungerTick += TalentHelper.onHungerTick(this, this.hungerTick - this.prevHungerTick);
	        
	        if (this.hungerTick > 400) {
	            this.setDogHunger(this.getDogHunger() - 1);
	            this.hungerTick -= 400;
	        }
        }
        
        if(Constants.DOGS_IMMORTAL) {
        	this.prevRegenerationTick = this.regenerationTick;
        	
	        if(this.isSitting()) {
	        	this.regenerationTick += 1;
	        	this.regenerationTick += TalentHelper.onRegenerationTick(this, this.regenerationTick - this.prevRegenerationTick);
	        }
	        else if(!this.isSitting())
	        	this.regenerationTick = 0;
	        
	        if(this.regenerationTick >= 2400 && this.isIncapacicated()) {
	            this.setHealth(2);
	            this.setDogHunger(1);
	        }
	        else if(this.regenerationTick >= 2400 && !this.isIncapacicated()) {
		        if(this.regenerationTick >= 4400 && this.getDogHunger() < 60) {
		        	this.setDogHunger(this.getDogHunger() + 1);
		            this.world.setEntityState(this, (byte)7);
		            this.regenerationTick = 2400;
		        }
	        }
    	}
        
        if(this.getHealth() != Constants.LOW_HEATH_LEVEL) {
	        this.prevHealingTick = this.healingTick;
	        this.healingTick += this.nourishment();
	        
	        if (this.healingTick >= 6000) {
	            if (this.getHealth() < this.getMaxHealth())
	            	this.setHealth(this.getHealth() + 1);
	            
	            this.healingTick = 0;
	        }
        }
        
        if(this.getHealth() <= 0 && this.isImmortal()) {
            this.deathTime = 0;
            this.setHealth(1);
        }
        
        if(this.getDogHunger() <= 0 && this.world.getWorldInfo().getWorldTime() % 100L == 0L && this.getHealth() > Constants.LOW_HEATH_LEVEL) {
            this.attackEntityFrom(DamageSource.GENERIC, 1);
            //this.fleeingTick = 0;
        }
        
        if(this.levels.isDireDog() && Constants.DIRE_PARTICLES)
            for(int i = 0; i < 2; i++)
                this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, (this.posY + rand.nextDouble() * (double)height) - 0.25D, posZ + (rand.nextDouble() - 0.5D) * (double)this.width, (this.rand.nextDouble() - 0.5D) * 2D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2D);
        
        if(this.reversionTime > 0)
        	this.reversionTime -= 1;
        
        //Remove dog from players head if sneaking
        Entity entityRidden = this.getRidingEntity();
        
        if(entityRidden instanceof EntityPlayer)
        	if(entityRidden.isSneaking())
        		this.dismountRidingEntity();
        
        //Check if dog bowl still exists every 50t/2.5s, if not remove
        if(this.foodBowlCheck++ > 50 && this.coords.hasBowlPos()) {
        	if(this.world.isBlockLoaded(new BlockPos(this.coords.getBowlX(), this.coords.getBowlY(), this.coords.getBowlZ())))
        		if(this.world.getBlockState(new BlockPos(this.coords.getBowlX(), this.coords.getBowlY(), this.coords.getBowlZ())).getBlock() != ModBlocks.FOOD_BOWL)
        			this.coords.resetBowlPosition();
        	
        	this.foodBowlCheck = 0;
        }
        
        this.updateBoundingBox();

        //this.locationManager.addOrUpdateLocation(this);
        
        TalentHelper.onLivingUpdate(this);
    }

	@Override
    public void onUpdate() {
        super.onUpdate();
        
        if(this.rand.nextInt(200) == 0)
        	this.hiyaMaster = true;
        
        if(((this.isBegging()) || (this.hiyaMaster)) && (!this.isWolfHappy)) {
        	this.isWolfHappy = true;
          	this.timeWolfIsHappy = 0.0F;
          	this.prevTimeWolfIsHappy = 0.0F;
        }
        else
        	this.hiyaMaster = false;
        
        if(this.isWolfHappy) {
        	if(this.timeWolfIsHappy % 1.0F == 0.0F)
        		this.playSound(SoundEvents.ENTITY_WOLF_PANT, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        	this.prevTimeWolfIsHappy = this.timeWolfIsHappy;
        	this.timeWolfIsHappy += 0.05F;
        	if (this.prevTimeWolfIsHappy >= 8.0F) {
        		this.isWolfHappy = false;
        		this.prevTimeWolfIsHappy = 0.0F;
        		this.timeWolfIsHappy = 0.0F;
        	}
        }
        
        if(this.isTamed()) {
    		EntityPlayer player = (EntityPlayer)this.getOwner();
    		
    		if(player != null) {
    			float distanceToOwner = player.getDistance(this);

                if(distanceToOwner <= 2F && this.hasBone()) {
                	if(isServer()) {
                		ItemStack fetchItem = ItemStack.EMPTY;
                		
                		switch(this.getBoneVariant()) {
                		case 0: fetchItem = new ItemStack(ModItems.THROW_BONE, 1, 1); break;
                		case 1: fetchItem = new ItemStack(ModItems.THROW_BONE, 1, 3); break;
                		}
                		
                		this.entityDropItem(fetchItem, 0.0F);
                	}
                	
                    this.setNoFetchItem();
                }
    		}
    	}
        
        TalentHelper.onUpdate(this);
    }
    
    public boolean isControllingPassengerPlayer() {
        return this.getControllingPassenger() instanceof EntityPlayer;
    }
    
    public boolean isImmortal() {
        return this.isTamed() && Constants.DOGS_IMMORTAL || this.levels.isDireDog();
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    	if(!TalentHelper.isImmuneToFalls(this))
    		super.fall(distance - TalentHelper.fallProtection(this), damageMultiplier);
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if(this.isEntityInvulnerable(damageSource))
            return false;
        else {
            Entity entity = damageSource.getTrueSource();
            //Friendly fire
            if(!this.canFriendlyFire() && entity instanceof EntityPlayer && (this.willObeyOthers() || this.isOwner((EntityPlayer)entity)))
            	return false;
            
        	if(!TalentHelper.attackEntityFrom(this, damageSource, damage))
        		return false;
        	
            if(this.aiSit != null)
            	this.aiSit.setSitting(false);

            if(entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
                damage = (damage + 1.0F) / 2.0F;

            return super.attackEntityFrom(damageSource, damage);
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
    	if(!TalentHelper.shouldDamageMob(this, entity))
    		return false;
    	
    	int damage = 4 + (MathHelper.floor(this.effectiveLevel()) + 1) / 2;
        damage = TalentHelper.attackEntityAsMob(this, entity, damage);
        
        if(entity instanceof EntityZombie)
            ((EntityZombie)entity).setAttackTarget(this);
        
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float)damage);
    }

    @Override
    public void setTamed(boolean flag) {
        super.setTamed(flag);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(flag ? 20.0D : 8.0D);
    }

    public void mountTo(EntityLivingBase entityLiving) {
        entityLiving.rotationYaw = this.rotationYaw;
        entityLiving.rotationPitch = this.rotationPitch;

        if(isServer())
            entityLiving.startRiding(this);
    }
    
    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
    	
    	ItemStack stack = player.getHeldItem(hand);
    	
        if(TalentHelper.interactWithPlayer(this, player, stack)) {
        	return true;
        }
        
        if(this.isTamed()) {
            if(!stack.isEmpty()) {
            	int foodValue = this.foodValue(stack);
            	
            	if(foodValue != 0 && this.getDogHunger() < Constants.HUNGER_POINTS && this.canInteract(player) && !this.isIncapacicated()) {
            		if(!player.capabilities.isCreativeMode)
            			stack.shrink(1);
            		
                    this.setDogHunger(this.getDogHunger() + foodValue);
                    if(stack.getItem() == ModItems.CHEW_STICK)
                    	((ItemChewStick)ModItems.CHEW_STICK).addChewStickEffects(this);
                    
                    return true;
                }
            	/*else if(stack.getItem() == Items.BONE && this.canInteract(player)) {
            		this.startRiding(player);
            		
            		if(this.aiSit != null)
            			this.aiSit.setSitting(true);
            		
                    return true;
                }*/
            	//TODO else if(stack.getItem() == Items.BIRCH_DOOR && this.canInteract(player)) {
            	//	this.patrolOutline.add(this.getPosition());
            	//}
            	//else if(stack.getItem() == Items.OAK_DOOR && this.canInteract(player)) {
            	//	this.patrolOutline.clear();
            	//}
            	else if(stack.getItem() == Items.STICK && this.canInteract(player) && !this.isIncapacicated()) {
            		player.openGui(DoggyTalents.INSTANCE, CommonProxy.GUI_ID_DOGGY, this.world, this.getEntityId(), MathHelper.floor(this.posY), MathHelper.floor(this.posZ));
                 	return true;
                }
                else if(stack.getItem() == ModItems.RADIO_COLLAR && this.canInteract(player) && !this.hasRadarCollar() && !this.isIncapacicated()) {
                 	this.hasRadarCollar(true);
                 	
                	if(!player.capabilities.isCreativeMode)
                		stack.shrink(1);
                 	return true;
                }
                else if(stack.getItem() == ModItems.WOOL_COLLAR && this.canInteract(player) && !this.hasCollar() && !this.isIncapacicated()) {
                	int colour = -1;
                	
                	if(stack.hasTagCompound() && stack.getTagCompound().hasKey("collar_colour"))
                		colour = stack.getTagCompound().getInteger("collar_colour");
                	
                 	this.setCollarData(colour);
                 	
                   	if(!player.capabilities.isCreativeMode)
                   		stack.shrink(1);
                 	return true;
                }
                else if(stack.getItem() == ModItems.FANCY_COLLAR && this.canInteract(player) && !this.hasCollar() && !this.isIncapacicated()) {
                	this.setCollarData(-3 - stack.getItemDamage());
                 	
                   	if(!player.capabilities.isCreativeMode)
                   		stack.shrink(1);
                   	return true;
                }
                else if(stack.getItem() == ModItems.CAPE && this.canInteract(player) && !this.hasCape() && !this.isIncapacicated()) { 
                	this.setFancyCape();
                	if(!player.capabilities.isCreativeMode)
                		stack.shrink(1);
                 	return true;
                }
                else if(stack.getItem() == ModItems.LEATHER_JACKET && this.canInteract(player) && !this.hasCape() && !this.isIncapacicated()) { 
                	this.setLeatherJacket();
                	if(!player.capabilities.isCreativeMode)
                		stack.shrink(1);
                 	return true;
                }
                else if(stack.getItem() == ModItems.CAPE_COLOURED && this.canInteract(player) && !this.hasCape() && !this.isIncapacicated()) { 
                	int colour = -1;
                	
                	if(stack.hasTagCompound() && stack.getTagCompound().hasKey("cape_colour"))
                		colour = stack.getTagCompound().getInteger("cape_colour");
                	
                 	this.setCapeData(colour);
                 	
                   	if(!player.capabilities.isCreativeMode)
                   		stack.shrink(1);
                 	return true;
                }
                else if(stack.getItem() == ModItems.SUNGLASSES && this.canInteract(player) && !this.hasSunglasses() && !this.isIncapacicated()) { 
                	this.setHasSunglasses(true);
                	if(!player.capabilities.isCreativeMode)
                		stack.shrink(1);
                 	return true;
                }
                else if(stack.getItem() instanceof IDogTreat && this.canInteract(player) && !this.isIncapacicated()) {
                 	IDogTreat treat = (IDogTreat)stack.getItem();
                 	EnumFeedBack type = treat.canGiveToDog(player, this, this.levels.getLevel(), this.levels.getDireLevel());
                 	treat.giveTreat(type, player, stack, this);
                 	return true;
                }
                else if(stack.getItem() == ModItems.COLLAR_SHEARS && this.canInteract(player)) {
                	if(this.isServer()) {
                		if(this.hasCollar() || this.hasSunglasses() || this.hasCape()) {
                			this.reversionTime = 40;
                			if(this.hasCollarColoured()) {
	                			ItemStack collarDrop = new ItemStack(ModItems.WOOL_COLLAR, 1, 0);
	                			if(this.isCollarColoured()) {
		                			collarDrop.setTagCompound(new NBTTagCompound());
		                			collarDrop.getTagCompound().setInteger("collar_colour", this.getCollarData());
	                			}
	                	     	this.entityDropItem(collarDrop, 1);
	                	     	this.setNoCollar();
                			}
                			
                			if(this.hasFancyCollar()) {
                				this.entityDropItem(new ItemStack(ModItems.FANCY_COLLAR, 1, this.getFancyCollarIndex()), 1);
                				this.setNoCollar();
                			}
                			
                			if(this.hasFancyCape()) {
	                	     	this.entityDropItem(new ItemStack(ModItems.CAPE, 1, 0), 1);
	                	     	this.setNoCape();
                			}
                			
                			if(this.hasCapeColoured()) {
                				ItemStack capeDrop = new ItemStack(ModItems.CAPE_COLOURED, 1, 0);
                				if(this.isCapeColoured()) {
		                			capeDrop.setTagCompound(new NBTTagCompound());
		                			capeDrop.getTagCompound().setInteger("cape_colour", this.getCapeData());
                				}
	                	     	this.entityDropItem(capeDrop, 1);
	                	     	this.setNoCape();
                			}
                			
                			if(this.hasLeatherJacket()) {
	                	     	this.entityDropItem(new ItemStack(ModItems.LEATHER_JACKET, 1, 0), 1);
	                	     	this.setNoCape();
                			}
                			
                			if(this.hasSunglasses()) {
	                	     	this.entityDropItem(new ItemStack(ModItems.SUNGLASSES, 1, 0), 1);
	                	     	this.setHasSunglasses(false);
                			}
                		}
                		else if(this.reversionTime < 1) {
                			this.setTamed(false);
	                	    this.navigator.clearPath();
	                        this.setSitting(false);
	                        this.setHealth(8);
	                        this.talents.resetTalents();
	                        this.setOwnerId(UUID.randomUUID());
	                        this.setWillObeyOthers(false);
	                        this.mode.setMode(EnumMode.DOCILE);
	                        if(this.hasRadarCollar())
	                        	this.dropItem(ModItems.RADIO_COLLAR, 1);
	                        this.hasRadarCollar(false);
	                        this.reversionTime = 40;
                		}
                    }

                	return true;
                }
                else if(stack.getItem() == Items.CAKE && this.canInteract(player) && this.isIncapacicated()) {
                	if(!player.capabilities.isCreativeMode)
                		stack.shrink(1);
                	
                    if(isServer()) {
                        this.aiSit.setSitting(true);
                        this.setHealth(this.getMaxHealth());
                        this.setDogHunger(Constants.HUNGER_POINTS);
                        this.regenerationTick = 0;
                        this.setAttackTarget((EntityLivingBase)null);
                        this.playTameEffect(true);
                        this.world.setEntityState(this, (byte)7);
                    }

                    return true;
                }
                else if(stack.getItem() == Items.DYE && this.canInteract(player)) { //TODO Add Plants compatibility
                    if(!this.hasCollarColoured())
                    	return true;
                    
                    if(!this.isCollarColoured()) {
                        int colour = EnumDyeColor.byDyeDamage(stack.getMetadata()).getColorValue();
                    	
                    	this.setCollarData(colour);
                    }
                    else {
                        int[] aint = new int[3];
                        int i = 0;
                        int count = 2; //The number of different sources of colour
                        
                        int l = this.getCollarData();
                        float f = (float)(l >> 16 & 255) / 255.0F;
                        float f1 = (float)(l >> 8 & 255) / 255.0F;
                        float f2 = (float)(l & 255) / 255.0F;
                        i = (int)((float)i + Math.max(f, Math.max(f1, f2)) * 255.0F);
                        aint[0] = (int)((float)aint[0] + f * 255.0F);
                        aint[1] = (int)((float)aint[1] + f1 * 255.0F);
                        aint[2] = (int)((float)aint[2] + f2 * 255.0F);

                        float[] afloat = EnumDyeColor.byDyeDamage(stack.getMetadata()).getColorComponentValues();
                        int l1 = (int)(afloat[0] * 255.0F);
                        int i2 = (int)(afloat[1] * 255.0F);
                        int j2 = (int)(afloat[2] * 255.0F);
                        i += Math.max(l1, Math.max(i2, j2));
                        aint[0] += l1;
                        aint[1] += i2;
                        aint[2] += j2;

                        int i1 = aint[0] / count;
                     	int j1 = aint[1] / count;
                    	int k1 = aint[2] / count;
                     	float f3 = (float)i / (float)count;
                     	float f4 = (float)Math.max(i1, Math.max(j1, k1));
                     	i1 = (int)((float)i1 * f3 / f4);
                     	j1 = (int)((float)j1 * f3 / f4);
                     	k1 = (int)((float)k1 * f3 / f4);
                     	int k2 = (i1 << 8) + j1;
                     	k2 = (k2 << 8) + k1;
                     	this.setCollarData(k2);
                    }
                    return true;
                }
                else if(stack.getItem() == ModItems.TREAT_BAG && this.getDogHunger() < Constants.HUNGER_POINTS && this.canInteract(player)) {
                	
                	InventoryTreatBag treatBag = new InventoryTreatBag(player, player.inventory.currentItem, stack);
            		treatBag.openInventory(player);
                	
                	int slotIndex = DogUtil.getFirstSlotWithFood(this, treatBag);
                 	if(slotIndex >= 0)
                 		DogUtil.feedDog(this, treatBag, slotIndex);
                 	
            		treatBag.closeInventory(player);
                 	return true;
                }
            }

            if(!this.isBreedingItem(stack) && this.canInteract(player)) {
            	if(this.isServer()) {
	                this.aiSit.setSitting(!this.isSitting());
	                this.isJumping = false;
	                this.navigator.clearPath();
	                this.setAttackTarget((EntityLivingBase)null);
            	}
                return true;
            }
        }
        else if(stack.getItem() == ModItems.COLLAR_SHEARS && this.reversionTime < 1) {
        	if(this.isServer()) {
	            this.setDead();
	            EntityWolf wolf = new EntityWolf(this.world);
	            wolf.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
	            this.world.spawnEntity(wolf);
        	}
            return true;
        }
        else if(stack.getItem() == Items.BONE) {
        	if(!player.capabilities.isCreativeMode)
        		stack.shrink(1);

            if(isServer()) {
                if(this.rand.nextInt(3) == 0) {
                    this.setTamed(true);
                    this.navigator.clearPath();
                    this.setAttackTarget((EntityLivingBase)null);
                    this.aiSit.setSitting(true);
                    this.setHealth(20.0F);
                    this.setOwnerId(player.getUniqueID());
                    this.playTameEffect(true);
                    this.world.setEntityState(this, (byte)7);
                }
                else {
                    this.playTameEffect(false);
                    this.world.setEntityState(this, (byte)6);
                }
            }

            return true;
        }

        return super.processInteract(player, hand);
    }
    
    @Override
    public void travel(float strafe, float vertical, float forward) {
    	 if (this.isBeingRidden() && this.canBeSteered()) {
			EntityLivingBase entitylivingbase = (EntityLivingBase)this.getControllingPassenger();
            this.rotationYaw = entitylivingbase.rotationYaw;
            this.prevRotationYaw = this.rotationYaw;
            this.rotationPitch = entitylivingbase.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.renderYawOffset;
            strafe = entitylivingbase.moveStrafing * 0.75F;
            forward = entitylivingbase.moveForward;

            if (forward <= 0.0F)
            {
                forward *= 0.5F;
            }

            if (this.onGround){
                if(forward > 0.0F) {
                    float f2 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
                    float f3 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
                    this.motionX += (double)(-0.4F * f2 * 0.05F); // May change
                    this.motionZ += (double)(0.4F * f3 * 0.05F);
                }
            }
            
            this.stepHeight = 1.0F;
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.5F;

            if (this.canPassengerSteer())
            {
            	float f = (float)this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 0.5F;

          	   this.setAIMoveSpeed(f);
          	   super.travel(strafe, vertical, forward);
            }
            else if (entitylivingbase instanceof EntityPlayer)
            {
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
            }

            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d1 = this.posX - this.prevPosX;
            double d0 = this.posZ - this.prevPosZ;
            float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
            
            if (f2 > 1.0F)
            {
                f2 = 1.0F;
            }

            this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
        }
        else
        {
        	this.stepHeight = 0.5F;
            this.jumpMovementFactor = 0.02F;
            super.travel(strafe, vertical, forward);
        }
    }
    
    @Override
    protected boolean isMovementBlocked() {
        return this.isPlayerSleeping() || super.isMovementBlocked(); //this.getRidingEntity() != null || this.riddenByEntity instanceof EntityPlayer || super.isMovementBlocked();
    }
    
    @Override
    public boolean isPotionApplicable(PotionEffect potionEffect) {
        if(this.isIncapacicated())
            return false;

        if(!TalentHelper.isPostionApplicable(this, potionEffect))
        	return false;

        return true;
    }
    
    @Override
    public void setFire(int amount) {
    	if(TalentHelper.setFire(this, amount))
    		super.setFire(amount);
    }
    
    public int foodValue(ItemStack stack) {
    	if(stack.isEmpty())
    		return 0;
    	
    	int foodValue = 0;
    	
    	Item item = stack.getItem();
    	
        if(stack.getItem() != Items.ROTTEN_FLESH && item instanceof ItemFood) {
            ItemFood itemfood = (ItemFood)item;

            if (itemfood.isWolfsFavoriteMeat())
            	foodValue = 40;
        }
        else if(stack.getItem() == ModItems.CHEW_STICK) {
        	return 10;
        }
        
        foodValue = TalentHelper.changeFoodValue(this, stack, foodValue);

        return foodValue;
    }
    
    public int masterOrder() {
    	int order = 0;
    	
        EntityPlayer player = (EntityPlayer)this.getOwner();
    	
        if(player != null) {
            float distanceAway = player.getDistance(this);
            Item mainhand = player.getHeldItemOffhand().getItem();
            Item offhand = player.getHeldItemMainhand().getItem();
            
            if((mainhand != null || offhand != null) && (mainhand instanceof ItemTool || offhand instanceof ItemTool) && distanceAway <= 20F)
                order = 1;

            if((mainhand != null || offhand != null) && (mainhand instanceof ItemTool || offhand instanceof ItemSword) || (mainhand != null || offhand != null) && (mainhand instanceof ItemBow || offhand instanceof ItemBow))
                order = 2;

            if((mainhand != null || offhand != null) && (mainhand == Items.WHEAT || offhand == Items.WHEAT)) //Round up Talent
                order = 3;
            
            if((mainhand != null || offhand != null) && (mainhand == Items.BONE || offhand == Items.BONE)) //Roar Talent
            	order = 4;
        }

        return order;
    }
    
    public float getWagAngle(float partialTickTime, float offset) {
        float f = (this.prevTimeWolfIsHappy + (this.timeWolfIsHappy - this.prevTimeWolfIsHappy) * partialTickTime + offset) / 2.0F;
        if(f < 0.0F) f = 0.0F;
        else if(f > 2.0F) f %= 2.0F;
        return MathHelper.sin(f * (float)Math.PI * 11.0F) * 0.3F * (float)Math.PI;
    }

    @Override
    public boolean isPlayerSleeping() {
        return false;
    }
    
    @Override
    public boolean canBreatheUnderwater() {
        return TalentHelper.canBreatheUnderwater(this);
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return TalentHelper.canTriggerWalking(this);
    }
    
    public boolean canInteract(EntityPlayer player) {
    	return this.isOwner(player) || this.willObeyOthers();
    }
    
    public int nourishment() {
        int amount = 0;

        if (this.getDogHunger() > 0) {
            amount = 40 + 4 * (MathHelper.floor(this.effectiveLevel()) + 1);

            if (isSitting() && this.talents.getLevel("quickhealer") == 5) {
                amount += 20 + 2 * (MathHelper.floor(this.effectiveLevel()) + 1);
            }

            if (!this.isSitting()) {
                amount *= 5 + this.talents.getLevel("quickhealer");
                amount /= 10;
            }
        }

        return amount;
    }
    
    @Override
    public void playTameEffect(boolean successful) {
       super.playTameEffect(successful);
    }
    
    public double effectiveLevel() {
        return (this.levels.getLevel() + this.levels.getDireLevel()) / 10.0D;
    }
    
    public int getTameSkin() {
   	 	return this.dataTracker.getTameSkin();
    }

    public void setTameSkin(int index) {
   		this.dataTracker.setTameSkin(index);
    }
    
    public void setWillObeyOthers(boolean flag) {
    	this.dataTracker.setWillObeyOthers(flag);
    }
    
    public boolean willObeyOthers() {
    	return this.dataTracker.willObeyOthers();
    }
    
    public void setFriendlyFire(boolean flag) {
    	this.dataTracker.setFriendlyFire(flag);
    }
    
    public boolean canFriendlyFire() {
    	return this.dataTracker.canFriendlyFire();
    }
    
    public int points() {
        return this.isCreativeCollar() ? 1000 : this.levels.getLevel() + this.levels.getDireLevel() + (this.levels.isDireDog() ? 15 : 0) + (this.getGrowingAge() < 0 ? 0 : 15);
    }

    public int spendablePoints() {
        return this.points() - this.usedPoints();
    }
    
    public int usedPoints() {
		return TalentHelper.getUsedPoints(this);
    }
    
    public int deductive(int par1) {
        byte byte0 = 0;
        switch(par1) {
        case 1: return 1;
		case 2: return 3;
        case 3: return 6;
        case 4: return 10;
        case 5: return 15;
        default: return 0;
        }
    }
    
    @Override
    public EntityDog createChild(EntityAgeable entityAgeable) {
    	EntityDog entitydog = new EntityDog(this.world);
        UUID uuid = this.getOwnerId();

        if(uuid != null) {
            entitydog.setOwnerId(uuid);
            entitydog.setTamed(true);
        }
         
        entitydog.setGrowingAge(-24000 * (Constants.TEN_DAY_PUPS ? 10 : 1));

        return entitydog;
    }
    
    public int getDogHunger() {
		return this.dataTracker.getDogHunger();
	}
    
    public void setDogHunger(int value) {
    	this.dataTracker.setDogHunger(value);
    }
    
    public void hasRadarCollar(boolean flag) {
    	this.dataTracker.hasRadarCollar(flag);
    }
    
    public boolean hasRadarCollar() {
    	return this.dataTracker.hasRadarCollar();
    }
    
    public void setNoFetchItem() {
    	this.dataTracker.setNoFetchItem();
    }
    
    public int getBoneVariant() {
    	return this.dataTracker.getBoneVariant();
    }
    
	public void setBoneVariant(int value) {
		this.dataTracker.setBoneVariant(value);
	}
    
    public boolean hasBone() {
    	return this.dataTracker.hasBone();
    }
    
    public void setHasSunglasses(boolean flag) {
    	this.dataTracker.setHasSunglasses(flag);
    }
    
    public boolean hasSunglasses() {
    	return this.dataTracker.hasSunglasses();
    }
    
    @Override
	public boolean shouldAttackEntity(EntityLivingBase target, EntityLivingBase owner) {
        if(TalentHelper.canAttackEntity(this, target))
    		return true;
    	
        if(!(target instanceof EntityCreeper) && !(target instanceof EntityGhast)) {
            if(target instanceof EntityDog) {
            	EntityDog entitydog = (EntityDog)target;

                if(entitydog.isTamed() && entitydog.getOwner() == owner)
                    return false;
            }
            else if(target instanceof EntityWolf) {
            	EntityWolf entitywolf = (EntityWolf)target;

                if(entitywolf.isTamed() && entitywolf.getOwner() == owner)
                    return false;
            }

            if(target instanceof EntityPlayer && owner instanceof EntityPlayer && !((EntityPlayer)owner).canAttackPlayer((EntityPlayer)target))
                return false;
            else if(target == owner)
            	return false;
            else
                return !(target instanceof AbstractHorse) || !((AbstractHorse)target).isTame();
        }
       
        return false;
    }
    
    @Override
    public boolean canAttackClass(Class<? extends EntityLivingBase> cls) {
    	if(TalentHelper.canAttackClass(this, cls))
    		return true;
    	
        return super.canAttackClass(cls);
    }
    
    public boolean isIncapacicated() {
    	return this.isImmortal() && this.getHealth() <= Constants.LOW_HEATH_LEVEL;
    }
    
    @Override
    public boolean shouldDismountInWater(Entity rider) {
    	if(!TalentHelper.shouldDismountInWater(this, rider))
    		return false;
    		
		return super.shouldDismountInWater(rider);
	}
    
    //Collar related functions
    public int getCollarData() {
    	return this.dataTracker.getCollarColour();
    }
    
    public void setCollarData(int value) {
    	this.dataTracker.setCollarColour(value);
    }
    
    public void setNoCollar() {
    	this.setCollarData(-2);
    }
    
    public boolean hasCollar() {
		return this.getCollarData() != -2;
	}
    
    public boolean hasCollarColoured() {
		return this.getCollarData() >= -1;
	}
    
    public boolean isCollarColoured() {
		return this.getCollarData() > -1;
	}
	
	public void setHasCollar() {
		this.setCollarData(-1);
	}
	
	public boolean hasFancyCollar() {
		return this.getCollarData() < -2;
	}
	
	public int getFancyCollarIndex() {
		return -3 - this.getCollarData();
	}
	
	public boolean isCreativeCollar() {
		return this.getCollarData() == -3;
	}
	
	public float[] getCollar() {
		int argb = this.getCollarData();
		
		int r = (argb >> 16) &0xFF;
		int g = (argb >> 8) &0xFF;
		int b = (argb >> 0) &0xFF;
		
		return new float[] {(float)r / 255F, (float)g / 255F, (float)b / 255F};
	}
	
	//Cape related functions
    public int getCapeData() {
    	return this.dataTracker.getCapeData();
    }
    
    public void setCapeData(int value) {
    	this.dataTracker.setCapeData(value);
    }
    
    public boolean hasCape() {
		return this.getCapeData() != -2;
	}
    
	public boolean hasCapeColoured() {
		return this.getCapeData() >= -1;
	}
	
	public boolean hasFancyCape() {
		return this.getCapeData() == -3;
	}
	
	public boolean hasLeatherJacket() {
		return this.getCapeData() == -4;
	}
	
	public boolean isCapeColoured() {
		return this.getCapeData() > -1;
	}
	
	public void setFancyCape() {
		this.setCapeData(-3);
	}
	
	public void setLeatherJacket() {
		this.setCapeData(-4);
	}
	
	public void setCapeColoured() {
		this.setCapeData(-1);
	}
	
    public void setNoCape() {
    	this.setCapeData(-2);
	}
	
	public float[] getCapeColour() {
		int argb = this.getCapeData();
		
		int r = (argb >> 16) &0xFF;
		int g = (argb >> 8) &0xFF;
		int b = (argb >> 0) &0xFF;
		
		return new float[] {(float)r / 255F, (float)g / 255F, (float)b / 255F};
	}
	
	//Gender related functions
	public String getGender() {
		return this.dataTracker.getGender();
	}
	
	public void setGender(String value) {
		this.dataTracker.setGender(value);
	}
	
	//Dog Size related functions
	public int getDogSize() {
		return this.dataTracker.getDogSize();
	}
   
	public void setDogSize(int value) {
		this.dataTracker.setDogSize(value);
	}

	public void updateBoundingBox() {
		if(this.prevSize == this.getDogSize()) return;
		
		
		
		switch(this.getDogSize()) {
		case 1:
			this.setScale(0.5F);
			break;
		case 2:
			this.setScale(0.7F);
			break;
		case 3:
			this.setScale(1.0F);
			break;
		case 4:
			this.setScale(1.3F);
			break;
		case 5:
			this.setScale(1.6F);
			break;
		}
		
		this.prevSize = this.getDogSize();
	}
	
	@Override
	protected void onFinishShaking() {
		if(isServer()) {
			int lvlFisherDog = this.talents.getLevel("fisherdog");
			int lvlHellHound = this.talents.getLevel("hellhound");
			
			if(this.rand.nextInt(15) < lvlFisherDog * 2)
				this.dropItem(this.rand.nextInt(15) < lvlHellHound * 2 ? Items.COOKED_FISH : Items.FISH, 1);
		}
	}
	
	//TODO RangedAttacker
		/*@Override
		public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
			
		}

		@Override
		public void setSwingingArms(boolean swingingArms) {
			
		}*/
	
	/**
	 * Returns the entity's health relative to the maximum health.
	 *
	 * @return health normalized between 0 and 1
	 */
	public double getHealthRelative() {
		return getHealth() / (double) getMaxHealth();
	}
	
	/**
	 * Checks if this entity is running on a client.
	 *
	 * Required since MCP's isClientWorld returns the exact opposite...
	 *
	 * @return true if the entity runs on a client or false if it runs on a
	 *         server
	 */
	@Override
	public boolean isClient() {
		return this.world.isRemote;
	}

	/**
	 * Checks if this entity is running on a server.
	 *
	 * @return true if the entity runs on a server or false if it runs on a
	 *         client
	 */
	@Override
	public boolean isServer() {
		return !this.world.isRemote;
	}

}