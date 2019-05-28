package doggytalents.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import doggytalents.ModBlocks;
import doggytalents.ModEntities;
import doggytalents.ModItems;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.IDataTracker;
import doggytalents.api.inferface.IDogInteractItem;
import doggytalents.configuration.ConfigHandler;
import doggytalents.entity.ai.DogDataTracker;
import doggytalents.entity.ai.DogLocationManager;
import doggytalents.entity.ai.EntityAIDogBeg;
import doggytalents.entity.ai.EntityAIDogFeed;
import doggytalents.entity.ai.EntityAIDogWander;
import doggytalents.entity.ai.EntityAIFetch;
import doggytalents.entity.ai.EntityAIFollowOwner;
import doggytalents.entity.ai.EntityAIModeAttackTarget;
import doggytalents.entity.ai.EntityAIOwnerHurtByTarget;
import doggytalents.entity.ai.EntityAIOwnerHurtTarget;
import doggytalents.entity.ai.EntityAIShepherdDog;
import doggytalents.entity.features.CoordFeature;
import doggytalents.entity.features.DogFeature;
import doggytalents.entity.features.DogGenderFeature;
import doggytalents.entity.features.LevelFeature;
import doggytalents.entity.features.ModeFeature;
import doggytalents.entity.features.ModeFeature.EnumMode;
import doggytalents.entity.features.TalentFeature;
import doggytalents.helper.DogUtil;
import doggytalents.helper.TalentHelper;
import doggytalents.inventory.InventoryTreatBag;
import doggytalents.item.ItemChewStick;
import doggytalents.item.ItemFancyCollar;
import doggytalents.lib.Constants;
import doggytalents.lib.GuiNames;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityTurtle;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityDog extends EntityAbstractDog implements IInteractionObject {

	public IDataTracker dataTracker;
	public DogLocationManager locationManager;
	public EntityAIFetch aiFetchBone;
	
	public TalentFeature TALENTS;
	public LevelFeature LEVELS;
	public ModeFeature MODE;
	public CoordFeature COORDS;
	public DogGenderFeature GENDER;
	private List<DogFeature> FEATURES;
	
	public Map<String, Object> objects;
	
	//Timers
    private float timeWolfIsHappy;
    private float prevTimeWolfIsHappy;
    private boolean isWolfHappy;
    public boolean hiyaMaster;
    
    private int hungerTick;
    private int prevHungerTick;
    private int healingTick;
    private int prevHealingTick;
    private int regenerationTick;
    private int prevRegenerationTick;
    private int foodBowlCheck;
    private int reversionTime;
    
    public int prevSize;
	
	public EntityDog(World worldIn) {
		super(ModEntities.DOG, worldIn);
		this.TALENTS = new TalentFeature(this);
		this.LEVELS = new LevelFeature(this);
		this.MODE = new ModeFeature(this);
		this.COORDS = new CoordFeature(this);
		this.GENDER = new DogGenderFeature(this);
		this.FEATURES = Arrays.asList(TALENTS, LEVELS, MODE, COORDS);
		this.objects = new HashMap<String, Object>();
		
		this.locationManager = DogLocationManager.getHandler(this.getEntityWorld());
		
		TalentHelper.onClassCreation(this);
		
		if(this.getGender().isEmpty()) {
            if (rand.nextInt(2) == 0) {
                this.setGender("male");
            } else {
                this.setGender("female");
            }
        }
	}
	
	@Override
	protected void initEntityAI() {
		this.aiSit = new EntityAISit(this);
        this.aiFetchBone = new EntityAIFetch(this, 1.0D, 20.0F);

		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, this.aiSit);
		this.tasks.addTask(3, new EntityAbstractDog.AIAvoidEntity(this, EntityLlama.class, 24.0F, 1.5D, 1.5D));
		this.tasks.addTask(4, new EntityAILeapAtTarget(this, 0.4F));
		 //TODO this.tasks.addTask(4, new EntityAIPatrolArea(this));
		this.tasks.addTask(5, this.aiFetchBone);
		this.tasks.addTask(6, new EntityAIAttackMelee(this, 1.0D, true));
		this.tasks.addTask(7, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
		this.tasks.addTask(8, new EntityAIMate(this, 1.0D));
		this.tasks.addTask(9, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(10, new EntityAIDogWander(this, 1.0D));
		this.tasks.addTask(11, new EntityAIDogBeg(this, 8.0F));
		this.tasks.addTask(12, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(13, new EntityAILookIdle(this));
        this.tasks.addTask(14, new EntityAIDogFeed(this, 20.0F));
		this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
		this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
		this.targetTasks.addTask(3, new EntityAIModeAttackTarget(this));
		this.targetTasks.addTask(4, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(5, new EntityAITargetNonTamed<>(this, EntityAnimal.class, false, (entity) -> {
			return entity instanceof EntitySheep || entity instanceof EntityRabbit;
		}));
		this.targetTasks.addTask(5, new EntityAITargetNonTamed<>(this, EntityTurtle.class, false, EntityTurtle.TARGET_DRY_BABY));
		this.targetTasks.addTask(6, new EntityAINearestAttackableTarget<>(this, AbstractSkeleton.class, false));
		this.targetTasks.addTask(7, new EntityAIShepherdDog(this, 0, false));
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		this.dataTracker = new DogDataTracker(this);
		this.dataTracker.entityInit();
	}
	
	@Override
    protected SoundEvent getAmbientSound() {
        if (this.getDogHunger() <= Constants.LOW_HUNGER && ConfigHandler.COMMON.whineWhenHungerLow()) {
            return SoundEvents.ENTITY_WOLF_WHINE;
        }
        if (this.rand.nextInt(3) == 0) {
            return this.isTamed() && this.getHealth() < this.getMaxHealth() / 2 ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT;
        } else {
            return SoundEvents.ENTITY_WOLF_AMBIENT;
        }

        //return this.rand.nextInt(3) == 0 ? (this.getDogHunger() <= Constants.lowHunger ? SoundEvents.ENTITY_WOLF_WHINE : this.isTamed() && this.getHealth() < this.getMaxHealth() / 2 ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT) : SoundEvents.ENTITY_WOLF_AMBIENT;
    }
	
	@Override
    protected ResourceLocation getLootTable() {
        return null; //TODO DOG Loot
    }
	
	@Override
	public void writeAdditional(NBTTagCompound compound) {
		super.writeAdditional(compound);
		this.FEATURES.stream().forEach(f -> f.writeAdditional(compound));

        compound.putInt("doggyTex", this.getTameSkin());
        compound.putInt("collarColour", this.getCollarData());
        compound.putInt("dogHunger", this.getDogHunger());
        compound.putBoolean("willObey", this.willObeyOthers());
        compound.putBoolean("friendlyFire", this.canFriendlyFire());
        compound.putBoolean("radioCollar", this.hasRadarCollar());
        compound.putBoolean("sunglasses", this.hasSunglasses());
        compound.putInt("capeData", this.getCapeData());
        compound.putInt("dogSize", this.getDogSize());
        compound.putString("dogGender", this.getGender());
        
        TalentHelper.writeToNBT(this, compound);
	}

	@Override
	public void readAdditional(NBTTagCompound compound) {
		super.readAdditional(compound);
		this.FEATURES.stream().forEach(f -> f.readAdditional(compound));
		
        this.setTameSkin(compound.getInt("doggyTex"));
        if (compound.contains("collarColour", 99)) this.setCollarData(compound.getInt("collarColour"));
        this.setDogHunger(compound.getInt("dogHunger"));
        this.setWillObeyOthers(compound.getBoolean("willObey"));
        this.setFriendlyFire(compound.getBoolean("friendlyFire"));
        this.hasRadarCollar(compound.getBoolean("radioCollar"));
        this.setHasSunglasses(compound.getBoolean("sunglasses"));
        if(compound.contains("capeData", 99)) this.setCapeData(compound.getInt("capeData"));
        if(compound.contains("dogSize", 99)) this.setDogSize(compound.getInt("dogSize"));
        this.setGender(compound.getString("dogGender"));
        
        TalentHelper.readFromNBT(this, compound);
        
        //Backwards Compatibility
        if (compound.contains("dogName"))
            this.setCustomName(new TextComponentString(compound.getString("dogName")));
	}
	
	@Override
    public void livingTick() {
		// 
		//EntityMoveHelper old = this.getMoveHelper();
		//Path oldPath = this.getNavigator().getPath();
		//double oldSpeed = 1.5D;
        super.livingTick();
        //this.getNavigator().setSpeed(1.5D);
        //this.getNavigator().setPath(oldPath, oldSpeed);
        //this.getMoveHelper().read(old);
        
        if (ConfigHandler.COMMON.hungerOn()) {
            this.prevHungerTick = this.hungerTick;

            if (!this.isBeingRidden() && !this.isSitting() /** && !this.mode.isMode(EnumMode.WANDERING) && !this.level.isDireDog() || worldObj.getWorldInfo().getWorldTime() % 2L == 0L **/)
                this.hungerTick += 1;

            this.hungerTick += TalentHelper.onHungerTick(this, this.hungerTick - this.prevHungerTick);

            if (this.hungerTick > 400) {
                this.setDogHunger(this.getDogHunger() - 1);
                this.hungerTick -= 400;
            }
        }

        if (ConfigHandler.COMMON.dogsImmortal()) {
            this.prevRegenerationTick = this.regenerationTick;

            if (this.isSitting()) {
                this.regenerationTick += 1;
                this.regenerationTick += TalentHelper.onRegenerationTick(this, this.regenerationTick - this.prevRegenerationTick);
            } else if (!this.isSitting())
                this.regenerationTick = 0;

            if (this.regenerationTick >= 2400 && this.isIncapacicated()) {
                this.setHealth(2);
                this.setDogHunger(1);
            } else if (this.regenerationTick >= 2400 && !this.isIncapacicated()) {
                if (this.regenerationTick >= 4400 && this.getDogHunger() < 60) {
                    this.setDogHunger(this.getDogHunger() + 1);
                    this.world.setEntityState(this, (byte) 7);
                    this.regenerationTick = 2400;
                }
            }
        }

        if (this.getHealth() != Constants.LOW_HEATH_LEVEL) {
            this.prevHealingTick = this.healingTick;
            this.healingTick += this.nourishment();

            if (this.healingTick >= 6000) {
                if (this.getHealth() < this.getMaxHealth())
                    this.setHealth(this.getHealth() + 1);

                this.healingTick = 0;
            }
        }

        if (this.getHealth() <= 0 && this.isImmortal()) {
            this.deathTime = 0;
            this.setHealth(1);
        }

        if (this.getDogHunger() <= 0 && this.world.getWorldInfo().getDayTime() % 100L == 0L && this.getHealth() > Constants.LOW_HEATH_LEVEL) {
            this.attackEntityFrom(DamageSource.GENERIC, 1);
            //this.fleeingTick = 0;
        }

        if (this.world.isRemote && this.LEVELS.isDireDog() && ConfigHandler.CLIENT.direParticles())
            for (int i = 0; i < 2; i++)
                this.world.addParticle(Particles.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, (this.posY + rand.nextDouble() * (double) height) - 0.25D, posZ + (rand.nextDouble() - 0.5D) * (double) this.width, (this.rand.nextDouble() - 0.5D) * 2D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2D);

        if (this.reversionTime > 0)
            this.reversionTime -= 1;

        //Remove dog from players head if sneaking
        Entity entityRidden = this.getRidingEntity();

        if (entityRidden instanceof EntityPlayer)
            if (entityRidden.isSneaking())
                this.stopRiding();

        //Check if dog bowl still exists every 50t/2.5s, if not remove
        if (this.foodBowlCheck++ > 50 && this.COORDS.hasBowlPos()) {
            if (this.world.isBlockLoaded(new BlockPos(this.COORDS.getBowlX(), this.COORDS.getBowlY(), this.COORDS.getBowlZ())))
                if (this.world.getBlockState(new BlockPos(this.COORDS.getBowlX(), this.COORDS.getBowlY(), this.COORDS.getBowlZ())).getBlock() != ModBlocks.FOOD_BOWL)
                    this.COORDS.resetBowlPosition();

            this.foodBowlCheck = 0;
        }

        this.updateBoundingBox();

        if(this.isAlive()) { //Prevent the data from being added when the entity dies
            if(ConfigHandler.COMMON.debugMode()) System.out.println("Update/Add Request From Living");
            this.locationManager.addOrUpdateLocation(this);
        }else{
            if(ConfigHandler.COMMON.debugMode()) System.out.println("Remove Request From Living");
            this.locationManager.removeDog(this);
        }

        TalentHelper.onLivingUpdate(this);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.rand.nextInt(200) == 0)
            this.hiyaMaster = true;

        if (((this.isBegging()) || (this.hiyaMaster)) && (!this.isWolfHappy)) {
            this.isWolfHappy = true;
            this.timeWolfIsHappy = 0.0F;
            this.prevTimeWolfIsHappy = 0.0F;
        } else
            this.hiyaMaster = false;

        if (this.isWolfHappy) {
            if (this.timeWolfIsHappy % 1.0F == 0.0F)
                this.playSound(SoundEvents.ENTITY_WOLF_PANT, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.prevTimeWolfIsHappy = this.timeWolfIsHappy;
            this.timeWolfIsHappy += 0.05F;
            if (this.prevTimeWolfIsHappy >= 8.0F) {
                this.isWolfHappy = false;
                this.prevTimeWolfIsHappy = 0.0F;
                this.timeWolfIsHappy = 0.0F;
            }
        }

        if (this.isTamed()) {
            EntityPlayer player = (EntityPlayer) this.getOwner();

            if (player != null) {
                float distanceToOwner = player.getDistance(this);

                if (distanceToOwner <= 2F && this.hasBone()) {
                    if (isServer()) {
                        ItemStack fetchItem = ItemStack.EMPTY;

                        switch (this.getBoneVariant()) {
                            case 0:
                                fetchItem = new ItemStack(ModItems.THROW_BONE_WET);
                                break;
                            case 1:
                                fetchItem = new ItemStack(ModItems.THROW_STICK_WET);
                                break;
                        }

                        this.entityDropItem(fetchItem, 0.0F);
                    }

                    this.setNoFetchItem();
                }
            }
        }

        TalentHelper.onUpdate(this);
    }
	
	@Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if(TalentHelper.interactWithPlayer(this, player, stack)) {
            return true;
        }

        if (this.isTamed()) {
            if (!stack.isEmpty()) {
                int foodValue = this.foodValue(stack);

                if (foodValue != 0 && this.getDogHunger() < Constants.HUNGER_POINTS && this.canInteract(player) && !this.isIncapacicated()) {
                    if (!player.abilities.isCreativeMode)
                        stack.shrink(1);

                    this.setDogHunger(this.getDogHunger() + foodValue);
                    if (stack.getItem() == ModItems.CHEW_STICK)
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
                else if (stack.getItem() == Items.STICK && this.canInteract(player) && !this.isIncapacicated()) {
                	
                	if(player instanceof EntityPlayerMP && !(player instanceof FakePlayer)) {
        	        	EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;
        	
        	        	NetworkHooks.openGui(entityPlayerMP, this, buf -> buf.writeInt(this.getEntityId()));
        	        }
                	
                    return true;
                } else if (stack.getItem() == ModItems.RADIO_COLLAR && this.canInteract(player) && !this.hasRadarCollar() && !this.isIncapacicated()) {
                    this.hasRadarCollar(true);

                    if (!player.abilities.isCreativeMode)
                        stack.shrink(1);
                    return true;
                } else if (stack.getItem() == ModItems.WOOL_COLLAR && this.canInteract(player) && !this.hasCollar() && !this.isIncapacicated()) {
                    int colour = -1;

                    if (stack.hasTag() && stack.getTag().contains("collar_colour"))
                        colour = stack.getTag().getInt("collar_colour");

                    this.setCollarData(colour);

                    if (!player.abilities.isCreativeMode)
                        stack.shrink(1);
                    return true;
                } else if (stack.getItem() instanceof ItemFancyCollar && this.canInteract(player) && !this.hasCollar() && !this.isIncapacicated()) {
                    this.setCollarData(-3 - ((ItemFancyCollar)stack.getItem()).type.ordinal());

                    if (!player.abilities.isCreativeMode)
                        stack.shrink(1);
                    return true;
                } else if (stack.getItem() == ModItems.CAPE && this.canInteract(player) && !this.hasCape() && !this.isIncapacicated()) {
                    this.setFancyCape();
                    if (!player.abilities.isCreativeMode)
                        stack.shrink(1);
                    return true;
                } else if (stack.getItem() == ModItems.LEATHER_JACKET && this.canInteract(player) && !this.hasCape() && !this.isIncapacicated()) {
                    this.setLeatherJacket();
                    if (!player.abilities.isCreativeMode)
                        stack.shrink(1);
                    return true;
                } else if (stack.getItem() == ModItems.CAPE_COLOURED && this.canInteract(player) && !this.hasCape() && !this.isIncapacicated()) {
                    int colour = -1;

                    if (stack.hasTag() && stack.getTag().contains("cape_colour"))
                        colour = stack.getTag().getInt("cape_colour");

                    this.setCapeData(colour);

                    if (!player.abilities.isCreativeMode)
                        stack.shrink(1);
                    return true;
                } else if (stack.getItem() == ModItems.SUNGLASSES && this.canInteract(player) && !this.hasSunglasses() && !this.isIncapacicated()) {
                    this.setHasSunglasses(true);
                    if (!player.abilities.isCreativeMode)
                        stack.shrink(1);
                    return true;
                } else if (stack.getItem() instanceof IDogInteractItem && this.canInteract(player) && !this.isIncapacicated()) {
                	IDogInteractItem treat = (IDogInteractItem) stack.getItem();
                	ActionResult<ItemStack> result = treat.onItemRightClick(stack, this, this.world, player);
                  
                	if(result.getType().equals(EnumActionResult.SUCCESS))
                		return true;
                	else if(result.getType().equals(EnumActionResult.FAIL))
                		return false;
                	
                } else if (stack.getItem() == ModItems.COLLAR_SHEARS && this.canInteract(player)) {
                    if (this.isServer()) {
                        if (this.hasCollar() || this.hasSunglasses() || this.hasCape()) {
                            this.reversionTime = 40;
                            if (this.hasCollarColoured()) {
                                ItemStack collarDrop = new ItemStack(ModItems.WOOL_COLLAR, 1);
                                if (this.isCollarColoured()) {
                                    collarDrop.setTag(new NBTTagCompound());
                                    collarDrop.getTag().putInt("collar_colour", this.getCollarData());
                                }
                                this.entityDropItem(collarDrop, 1);
                                this.setNoCollar();
                            }

                            if (this.hasFancyCollar()) {
                            	Item drop = ModItems.MULTICOLOURED_COLLAR;
                            	if(this.getCollarData() == -3)
                            		drop = ModItems.CREATIVE_COLLAR;
                            	else if(this.getCollarData() == -4)
                            		drop = ModItems.SPOTTED_COLLAR;
                            	
                                this.entityDropItem(drop, 1);
                                this.setNoCollar();
                            }

                            if (this.hasFancyCape()) {
                                this.entityDropItem(new ItemStack(ModItems.CAPE, 1), 1);
                                this.setNoCape();
                            }

                            if (this.hasCapeColoured()) {
                                ItemStack capeDrop = new ItemStack(ModItems.CAPE_COLOURED, 1);
                                if (this.isCapeColoured()) {
                                    capeDrop.setTag(new NBTTagCompound());
                                    capeDrop.getTag().putInt("cape_colour", this.getCapeData());
                                }
                                this.entityDropItem(capeDrop, 1);
                                this.setNoCape();
                            }

                            if (this.hasLeatherJacket()) {
                                this.entityDropItem(new ItemStack(ModItems.LEATHER_JACKET, 1), 1);
                                this.setNoCape();
                            }

                            if (this.hasSunglasses()) {
                                this.entityDropItem(new ItemStack(ModItems.SUNGLASSES, 1), 1);
                                this.setHasSunglasses(false);
                            }
                        } else if (this.reversionTime < 1) {
                            this.setTamed(false);
                            this.navigator.clearPath();
                            this.aiSit.setSitting(false);
                            this.setHealth(8);
                            this.TALENTS.resetTalents();
                            this.setOwnerId(UUID.randomUUID());
                            this.setWillObeyOthers(false);
                            this.MODE.setMode(EnumMode.DOCILE);
                            if (this.hasRadarCollar())
                                this.entityDropItem(ModItems.RADIO_COLLAR);
                            this.hasRadarCollar(false);
                            this.reversionTime = 40;
                        }
                    }

                    return true;
                } else if (stack.getItem().equals(Item.getItemFromBlock(Blocks.CAKE)) && this.canInteract(player) && this.isIncapacicated()) {
                    if (!player.abilities.isCreativeMode)
                        stack.shrink(1);

                    if(this.isServer()) {
                        this.aiSit.setSitting(true);
                        this.setHealth(this.getMaxHealth());
                        this.setDogHunger(Constants.HUNGER_POINTS);
                        this.regenerationTick = 0;
                        this.setAttackTarget((EntityLivingBase) null);
                        this.playTameEffect(true);
                        this.world.setEntityState(this, (byte) 7);
                    }

                    return true;
                } else if (stack.getItem().isIn(net.minecraftforge.common.Tags.Items.DYES) && this.canInteract(player)) { //TODO Add Plants compatibility
                    if (!this.hasCollarColoured())
                        return true;

                    if (!this.isCollarColoured()) {
                        int colour = EnumDyeColor.getColor(stack).func_196057_c();

                        this.setCollarData(colour);
                    } else {
                        int[] aint = new int[3];
                        int i = 0;
                        int count = 2; //The number of different sources of colour

                        int l = this.getCollarData();
                        float f = (float) (l >> 16 & 255) / 255.0F;
                        float f1 = (float) (l >> 8 & 255) / 255.0F;
                        float f2 = (float) (l & 255) / 255.0F;
                        i = (int) ((float) i + Math.max(f, Math.max(f1, f2)) * 255.0F);
                        aint[0] = (int) ((float) aint[0] + f * 255.0F);
                        aint[1] = (int) ((float) aint[1] + f1 * 255.0F);
                        aint[2] = (int) ((float) aint[2] + f2 * 255.0F);

                        float[] afloat = EnumDyeColor.getColor(stack).getColorComponentValues();
                        int l1 = (int) (afloat[0] * 255.0F);
                        int i2 = (int) (afloat[1] * 255.0F);
                        int j2 = (int) (afloat[2] * 255.0F);
                        i += Math.max(l1, Math.max(i2, j2));
                        aint[0] += l1;
                        aint[1] += i2;
                        aint[2] += j2;

                        int i1 = aint[0] / count;
                        int j1 = aint[1] / count;
                        int k1 = aint[2] / count;
                        float f3 = (float) i / (float) count;
                        float f4 = (float) Math.max(i1, Math.max(j1, k1));
                        i1 = (int) ((float) i1 * f3 / f4);
                        j1 = (int) ((float) j1 * f3 / f4);
                        k1 = (int) ((float) k1 * f3 / f4);
                        int k2 = (i1 << 8) + j1;
                        k2 = (k2 << 8) + k1;
                        this.setCollarData(k2);
                    }
                    return true;
                } else if (stack.getItem() == ModItems.TREAT_BAG && this.getDogHunger() < Constants.HUNGER_POINTS && this.canInteract(player)) {

                    InventoryTreatBag treatBag = new InventoryTreatBag(player, player.inventory.currentItem, stack);
                    treatBag.openInventory(player);

                    int slotIndex = DogUtil.getFirstSlotWithFood(this, treatBag);
                    if (slotIndex >= 0)
                        DogUtil.feedDog(this, treatBag, slotIndex);

                    treatBag.closeInventory(player);
                    return true;
                }
            }

            if (!this.isBreedingItem(stack) && this.canInteract(player)) {
                if (this.isServer()) {
                    this.aiSit.setSitting(!this.isSitting());
                    this.isJumping = false;
                    this.navigator.clearPath();
                    this.setAttackTarget((EntityLivingBase) null);
                }
                return true;
            }
        } else if (stack.getItem() == ModItems.COLLAR_SHEARS && this.reversionTime < 1) {
            if (this.isServer()) {
                this.locationManager.removeDog(this);
                this.remove();
                EntityWolf wolf = new EntityWolf(this.world);
                wolf.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
                this.world.spawnEntity(wolf);
            }
            return true;
        } else if (stack.getItem() == Items.BONE) {
            if (!player.abilities.isCreativeMode)
                stack.shrink(1);

            if (isServer()) {
                if (this.rand.nextInt(3) == 0) {
                    this.setTamed(true);
                    this.navigator.clearPath();
                    this.setAttackTarget((EntityLivingBase) null);
                    this.aiSit.setSitting(true);
                    this.setHealth(20.0F);
                    this.setOwnerId(player.getUniqueID());
                    this.playTameEffect(true);
                    this.world.setEntityState(this, (byte) 7);
                } else {
                    this.playTameEffect(false);
                    this.world.setEntityState(this, (byte) 6);
                }
            }

            return true;
        }

        return super.processInteract(player, hand);
    }
	
	@Override
    public EntityDog createChild(EntityAgeable entityAgeable) {
        EntityDog entitydog = new EntityDog(this.world);
        UUID uuid = this.getOwnerId();

        if (uuid != null) {
            entitydog.setOwnerId(uuid);
            entitydog.setTamed(true);
        }

        entitydog.setGrowingAge(-24000 * (ConfigHandler.COMMON.tenDayPups() ? 10 : 1));

        if(ConfigHandler.COMMON.pupsGetParentLevels() && entityAgeable instanceof EntityDog) {
            int combinedLevel = this.LEVELS.getLevel() + ((EntityDog)entityAgeable).LEVELS.getLevel();
            combinedLevel /= 2;
            combinedLevel = Math.min(combinedLevel, 20);
            entitydog.LEVELS.setLevel(combinedLevel);
        }
        
        return entitydog;
    }
	
	@Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);
    }
	
	
	@Override
	public boolean canPassengerSteer() {
		return super.canPassengerSteer();
	}
	
	@Override
	public boolean canBeSteered() {
		return true;
	}
	
	@Override
    public double getYOffset() {
        return this.getRidingEntity() instanceof EntityPlayer ? 0.5D : 0.0D;
    }
	
	@Override
    public void travel(float strafe, float vertical, float forward) {
        if (this.isBeingRidden() && this.canBeSteered()) {
            EntityLivingBase entitylivingbase = (EntityLivingBase) this.getControllingPassenger();
            this.rotationYaw = entitylivingbase.rotationYaw;
            this.prevRotationYaw = this.rotationYaw;
            this.rotationPitch = entitylivingbase.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.renderYawOffset;
            strafe = entitylivingbase.moveStrafing * 0.75F;
            forward = entitylivingbase.moveForward;

            if (forward <= 0.0F) {
                forward *= 0.5F;
            }

            if (this.onGround) {
                if (forward > 0.0F) {
                    float f2 = MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F);
                    float f3 = MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F);
                    this.motionX += (double) (-0.4F * f2 * 0.05F); // May change
                    this.motionZ += (double) (0.4F * f3 * 0.05F);
                }
            }

            this.stepHeight = 1.0F;
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.5F;

            if (this.canPassengerSteer()) {
                float f = (float) this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue() * 0.5F;

                this.setAIMoveSpeed(f);
                super.travel(strafe, vertical, forward);
            } else if (entitylivingbase instanceof EntityPlayer) {
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
            }

            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d1 = this.posX - this.prevPosX;
            double d0 = this.posZ - this.prevPosZ;
            float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;

            if (f2 > 1.0F) {
                f2 = 1.0F;
            }

            this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
        } else {
            this.stepHeight = 0.5F;
            this.jumpMovementFactor = 0.02F;
            super.travel(strafe, vertical, forward);
        }
    }
	
	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return !stack.isEmpty() && DoggyTalentsAPI.BREED_WHITELIST.containsItem(stack);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
    public boolean getAlwaysRenderNameTagForRender() {
        return this.hasCustomName();
    }
	
	// Changes visibility to public
	@Override
	public void playTameEffect(boolean successful) {
		super.playTameEffect(successful);
	}
	
	// Talent Hooks
	@Override
    public void fall(float distance, float damageMultiplier) {
        if (!TalentHelper.isImmuneToFalls(this))
            super.fall(distance - TalentHelper.fallProtection(this), damageMultiplier);
    }
	
	@Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if (this.isInvulnerableTo(damageSource))
            return false;
        else {
            Entity entity = damageSource.getTrueSource();
            //Friendly fire
            if (!this.canFriendlyFire() && entity instanceof EntityPlayer && (this.willObeyOthers() || this.isOwner((EntityPlayer) entity)))
                return false;

            if (!TalentHelper.attackEntityFrom(this, damageSource, damage))
                return false;

            if (this.aiSit != null)
                this.aiSit.setSitting(false);

            if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
                damage = (damage + 1.0F) / 2.0F;

            return super.attackEntityFrom(damageSource, damage);
        }
    }
	
	@Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (!TalentHelper.shouldDamageMob(this, entityIn))
            return false;

        int damage = 4 + (MathHelper.floor(this.effectiveLevel()) + 1) / 2;
        damage = TalentHelper.attackEntityAsMob(this, entityIn, damage);

        if (entityIn instanceof EntityZombie)
            ((EntityZombie)entityIn).setAttackTarget(this);

        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)damage);//(float)((int)this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue()));
   		if (flag) {
   			this.applyEnchantments(this, entityIn);
   		}

   		return flag;
    }
	
	@Override
	public void onDeath(DamageSource cause) {
        if (this.isServer() && this.world.getGameRules().getBoolean("showDeathMessages") && !this.isImmortal() && this.getOwner() instanceof EntityPlayerMP) {
            System.out.println("From onDeath");
            this.locationManager.removeDog(this); //TODO can't figure out why on death the dog won't be removed
            this.getOwner().sendMessage(this.getCombatTracker().getDeathMessage());
        }
    }
	
	@Override
    public boolean isPotionApplicable(PotionEffect potionEffect) {
        if (this.isIncapacicated())
            return false;

        if (!TalentHelper.isPostionApplicable(this, potionEffect))
            return false;

        return true;
    }
	
	@Override
    public void setFire(int amount) {
        if (TalentHelper.setFire(this, amount))
            super.setFire(amount);
    }
	
	@Override
    public boolean canBreatheUnderwater() {
        return TalentHelper.canBreatheUnderwater(this);
    }

    @Override
    protected boolean canTriggerWalking() {
        return TalentHelper.canTriggerWalking(this);
    }
    
    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        if (!TalentHelper.shouldDismountInWater(this, rider))
            return true;

        return false;
    }
    
    @Override
    public boolean canRiderInteract() {
        return true;
    }
    
    @Override
    public boolean shouldAttackEntity(EntityLivingBase target, EntityLivingBase owner) {
        if(TalentHelper.canAttackEntity(this, target))
            return true;

        if (!(target instanceof EntityCreeper) && !(target instanceof EntityGhast)) {
            if (target instanceof EntityDog) {
                EntityDog entitydog = (EntityDog) target;

                if (entitydog.isTamed() && entitydog.getOwner() == owner)
                    return false;
            } else if (target instanceof EntityWolf) {
                EntityWolf entitywolf = (EntityWolf) target;

                if (entitywolf.isTamed() && entitywolf.getOwner() == owner)
                    return false;
            }

            if (target instanceof EntityPlayer && owner instanceof EntityPlayer && !((EntityPlayer) owner).canAttackPlayer((EntityPlayer) target))
                return false;
            else if (target == owner)
                return false;
            else
                return !(target instanceof AbstractHorse) || !((AbstractHorse) target).isTame();
        }

        return false;
    }

    @Override
    public boolean canAttackClass(Class<? extends EntityLivingBase> cls) {
        if(TalentHelper.canAttackClass(this, cls))
            return true;

        return super.canAttackClass(cls);
    }
    
	public void tryToJump() {
        if(this.onGround)
        	this.jump();
        else if(this.isInWater() && this.TALENTS.getLevel("swimmerdog") > 0)
        	this.motionY = 0.2F;
	}
	
    @Override
    protected float getJumpUpwardsMotion() {
    	float verticalVelocity = 0.42F + 0.06F * this.TALENTS.getLevel("wolfmount");
		if(this.TALENTS.getLevel("wolfmount") == 5) verticalVelocity += 0.04F;
        return verticalVelocity;
    }
    
    @Override
    public boolean canDespawn() {
    	return false;
    }
    
    @Override
    protected void onFinishShaking() {
        if(this.isServer()) {
            int lvlFisherDog = this.TALENTS.getLevel("fisherdog");
            int lvlHellHound = this.TALENTS.getLevel("hellhound");

            if (this.rand.nextInt(15) < lvlFisherDog * 2)
                this.entityDropItem(this.rand.nextInt(15) < lvlHellHound * 2 ? Items.COOKED_COD : Items.COD);
        }
    }
	
	public float getWagAngle(float partialTickTime, float offset) {
        float f = (this.prevTimeWolfIsHappy + (this.timeWolfIsHappy - this.prevTimeWolfIsHappy) * partialTickTime + offset) / 2.0F;
        if (f < 0.0F) f = 0.0F;
        else if (f > 2.0F) f %= 2.0F;
        return MathHelper.sin(f * (float) Math.PI * 11.0F) * 0.3F * (float) Math.PI;
    }
	
	public boolean isImmortal() {
		return this.isTamed() && ConfigHandler.COMMON.dogsImmortal() || this.LEVELS.isDireDog();
	}
	
	public boolean isIncapacicated() {
		return this.isImmortal() && this.getHealth() <= Constants.LOW_HEATH_LEVEL;
	}
	
	public double effectiveLevel() {
		return (this.LEVELS.getLevel() + this.LEVELS.getDireLevel()) / 10.0D;
	}
	
	public double getHealthRelative() {
        return getHealth() / (double) getMaxHealth();
    }
	
	public boolean canInteract(EntityPlayer player) {
        return this.isOwner(player) || this.willObeyOthers();
    }
	
	public int foodValue(ItemStack stack) {
        if (stack.isEmpty())
            return 0;

        int foodValue = 0;

        Item item = stack.getItem();

        if (stack.getItem() != Items.ROTTEN_FLESH && item instanceof ItemFood) {
            ItemFood itemfood = (ItemFood) item;

            if (itemfood.isMeat())
                foodValue = 40;
        } else if (stack.getItem() == ModItems.CHEW_STICK) {
            return 10;
        }

        foodValue = TalentHelper.changeFoodValue(this, stack, foodValue);

        return foodValue;
    }
	
	public int nourishment() {
        int amount = 0;

        if (this.getDogHunger() > 0) {
            amount = 40 + 4 * (MathHelper.floor(this.effectiveLevel()) + 1);

            if (isSitting() && this.TALENTS.getLevel("quickhealer") == 5) {
                amount += 20 + 2 * (MathHelper.floor(this.effectiveLevel()) + 1);
            }

            if (!this.isSitting()) {
                amount *= 5 + this.TALENTS.getLevel("quickhealer");
                amount /= 10;
            }
        }

        return amount;
    }
	
	public int masterOrder() {
        int order = 0;

        EntityPlayer player = (EntityPlayer) this.getOwner();

        if (player != null) {
            float distanceAway = player.getDistance(this);
            Item mainhand = player.getHeldItemOffhand().getItem();
            Item offhand = player.getHeldItemMainhand().getItem();

            if ((mainhand != null || offhand != null) && (mainhand instanceof ItemTool || offhand instanceof ItemTool) && distanceAway <= 20F)
                order = 1;

            if ((mainhand != null || offhand != null) && (mainhand instanceof ItemTool || offhand instanceof ItemSword) || (mainhand != null || offhand != null) && (mainhand instanceof ItemBow || offhand instanceof ItemBow))
                order = 2;

            if ((mainhand != null || offhand != null) && (mainhand == Items.WHEAT || offhand == Items.WHEAT)) //Round up Talent
                order = 3;

            if ((mainhand != null || offhand != null) && (mainhand == Items.BONE || offhand == Items.BONE)) //Roar Talent
                order = 4;
        }

        return order;
    }
	
	public void updateBoundingBox() {
        if (this.prevSize == this.getDogSize()) return;

        switch (this.getDogSize()) {
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
	
	public void mountTo(EntityLivingBase entityLiving) {
        entityLiving.rotationYaw = this.rotationYaw;
        entityLiving.rotationPitch = this.rotationPitch;

        if (isServer())
            entityLiving.startRiding(this);
    }
	
	public boolean isBegging() {
	    return this.dataTracker.isBegging();
	}
	    
	public void setBegging(boolean flag) {
	    this.dataTracker.setBegging(flag);
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
        return this.isCreativeCollar() ? 1000 : this.LEVELS.getLevel() + this.LEVELS.getDireLevel() + (this.LEVELS.isDireDog() ? 15 : 0) + (this.getGrowingAge() < 0 ? 0 : 15);
    }

    public int spendablePoints() {
        return this.points() - this.usedPoints();
    }

    public int usedPoints() {
        return TalentHelper.getUsedPoints(this);
    }

    public int deductive(int id) {
    	if(id >= 1 && id <= 5)
    		return new int[] {1,3,6,10,15}[id - 1];
    	
    	return 0;
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

        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = (argb >> 0) & 0xFF;

        return new float[]{(float) r / 255F, (float) g / 255F, (float) b / 255F};
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

        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = (argb >> 0) & 0xFF;

        return new float[]{(float) r / 255F, (float) g / 255F, (float) b / 255F};
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
    
    public boolean isServer() {
        return !this.world.isRemote;
    }
    
    public boolean isClient() {
        return this.world.isRemote;
    }

	@Override
	public Container createContainer(InventoryPlayer inventory, EntityPlayer player) {
		return new Container() {
			@Override
			public boolean canInteractWith(EntityPlayer playerIn) {
				return true;
			}
		};
	}

	@Override
	public String getGuiID() {
		return GuiNames.DOG_INFO;
	}
}
