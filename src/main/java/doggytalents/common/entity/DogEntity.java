package doggytalents.common.entity;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import doggytalents.DoggyAccessories;
import doggytalents.DoggyItems;
import doggytalents.DoggySerializers;
import doggytalents.DoggyTags;
import doggytalents.DoggyTalents2;
import doggytalents.api.feature.EnumGender;
import doggytalents.api.feature.EnumMode;
import doggytalents.api.feature.FoodHandler;
import doggytalents.api.inferface.Accessory;
import doggytalents.api.inferface.AccessoryInstance;
import doggytalents.api.inferface.AccessoryType;
import doggytalents.api.inferface.IDogAlteration;
import doggytalents.api.inferface.IDogFoodHandler;
import doggytalents.api.inferface.IDogItem;
import doggytalents.api.inferface.Talent;
import doggytalents.client.screen.DogInfoScreen;
import doggytalents.common.config.ConfigValues;
import doggytalents.common.entity.DogLevel.Type;
import doggytalents.common.storage.DogLocationStorage;
import doggytalents.common.storage.DogRespawnStorage;
import doggytalents.common.util.BackwardsComp;
import doggytalents.common.util.Cache;
import doggytalents.common.util.NBTUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.EntityDataManager.DataEntry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.DistExecutor;

public class DogEntity extends WolfEntity {

    // Use Cache.make to ensure static fields are not initialised too early (before Serializers have been registered)
    private static final Cache<DataParameter<List<AccessoryInstance>>> ACCESSORIES =  Cache.make(() -> (DataParameter<List<AccessoryInstance>>) EntityDataManager.createKey(DogEntity.class, DoggySerializers.ACCESSORY_SERIALIZER.get().getSerializer()));
    private static final Cache<DataParameter<Map<Talent, Integer>>> TALENTS =  Cache.make(() -> (DataParameter<Map<Talent, Integer>>) EntityDataManager.createKey(DogEntity.class, DoggySerializers.TALENT_LEVEL_SERIALIZER.get().getSerializer()));
    private static final DataParameter<Optional<ITextComponent>> LAST_KNOWN_NAME = EntityDataManager.createKey(DogEntity.class, DataSerializers.OPTIONAL_TEXT_COMPONENT);
    private static final DataParameter<Byte> DOG_FLAGS = EntityDataManager.createKey(DogEntity.class, DataSerializers.BYTE);
    private static final Cache<DataParameter<EnumGender>> GENDER = Cache.make(() -> (DataParameter<EnumGender>) EntityDataManager.createKey(DogEntity.class,  DoggySerializers.GENDER_SERIALIZER.get().getSerializer()));
    private static final Cache<DataParameter<EnumMode>> MODE = Cache.make(() -> (DataParameter<EnumMode>) EntityDataManager.createKey(DogEntity.class, DoggySerializers.MODE_SERIALIZER.get().getSerializer()));
    private static final DataParameter<Optional<BlockPos>> BOWL_POS = EntityDataManager.createKey(DogEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);
    private static final DataParameter<Float> HUNGER_INT = EntityDataManager.createKey(DogEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<String> CUSTOM_SKIN = EntityDataManager.createKey(DogEntity.class, DataSerializers.STRING);
    private static final Cache<DataParameter<DogLevel>> DOG_LEVEL = Cache.make(() -> (DataParameter<DogLevel>) EntityDataManager.createKey(DogEntity.class, DoggySerializers.DOG_LEVEL_SERIALIZER.get().getSerializer()));
    private static final DataParameter<Byte> SIZE = EntityDataManager.createKey(DogEntity.class, DataSerializers.BYTE);

    public final Map<Integer, Object> objects = Maps.newHashMap();
    private final List<IDogAlteration> alterations = Lists.newArrayList();

    public DogEntity(EntityType<? extends DogEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isAlive()) {

            // On server side
            if (!this.world.isRemote) {

                // Every 2 seconds
                if(this.ticksExisted % 40 == 0) {
                    DogLocationStorage.get(this.world).getOrCreateData(this).update(this);

                    if(this.getOwner() != null) {
                        this.setOwnersName(this.getOwner().getName());
                    }
                }
            }
        }

        this.alterations.forEach((alter) -> alter.tick(this));
    }

    @Override
    public void livingTick() {
        super.livingTick();

        this.alterations.forEach((alter) -> alter.livingTick(this));
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(ACCESSORIES.get(), Lists.newArrayList());
        this.dataManager.register(TALENTS.get(), Maps.newHashMap());
        this.dataManager.register(LAST_KNOWN_NAME, Optional.empty());
        this.dataManager.register(DOG_FLAGS, (byte) 0);
        this.dataManager.register(GENDER.get(), EnumGender.UNISEX);
        this.dataManager.register(MODE.get(), EnumMode.DOCILE);
        this.dataManager.register(BOWL_POS, Optional.empty());
        this.dataManager.register(HUNGER_INT, 60F);
        this.dataManager.register(CUSTOM_SKIN, "");
        this.dataManager.register(DOG_LEVEL.get(), new DogLevel(0, 0));
        this.dataManager.register(SIZE, (byte) 3);
    }

    private static final Item[] HELMETS = new Item[] {Items.IRON_HELMET, Items.DIAMOND_HELMET, Items.GOLDEN_HELMET, Items.LEATHER_HELMET, Items.CHAINMAIL_HELMET, Items.TURTLE_HELMET};
    private static final List<Supplier<Accessory>> HELMET_ACCESSORIES = Lists.newArrayList(() -> DoggyAccessories.IRON_HELMET, () -> DoggyAccessories.DIAMOND_HELMET, () -> DoggyAccessories.GOLDEN_HELMET, () -> DoggyAccessories.LEATHER_HELMET, () -> DoggyAccessories.CHAINMAIL_HELMET, () -> DoggyAccessories.TURTLE_HELMET);

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {

        ItemStack stack = player.getHeldItem(hand);

        if(stack.getItem() == Items.STICK && this.canInteract(player)) {

            if(this.world.isRemote) {
                DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> DogInfoScreen.open(this));
            }

            return true;
        }

        if (!stack.isEmpty()) {
            for (int i = 0; i < HELMETS.length; i++) {
                if (stack.getItem().equals(HELMETS[i])) {
                    AccessoryInstance inst;
                    if (HELMETS[i] == Items.LEATHER_HELMET) {
                        inst = DoggyAccessories.LEATHER_HELMET.create(((IDyeableArmorItem) Items.LEATHER_HELMET).getColor(stack));
                    } else {
                        inst = HELMET_ACCESSORIES.get(i).get().getDefault();
                    }
                    if (this.addAccessory(inst)) {
                        this.consumeItemFromStack(player, stack);
                        return true;
                    }
                }
            }
        }

        Optional<IDogFoodHandler> foodHandler = FoodHandler.getMatch(this, stack, player);

        if (foodHandler.isPresent()) {
            return foodHandler.get().consume(this, stack, player);
        }

        if (stack.getItem() instanceof IDogItem) {
            IDogItem item = (IDogItem) stack.getItem();
            ActionResultType result = item.processInteract(this, this.world, player, hand);
            if (result.isSuccessOrConsume()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.processInteract(this, this.world, player, hand);
            if (result.isSuccessOrConsume()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        if (super.processInteract(player, hand)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.canBeRiddenInWater(this, rider);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.canBeRiddenInWater(rider);
    }

    @Override
    public boolean canTrample(BlockState state, BlockPos pos, float fallDistance) {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.canTrample(this, state, pos, fallDistance);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.canTrample(state, pos, fallDistance);
    }

    @Override
    public boolean canBreatheUnderwater() {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.canBreatheUnderwater(this);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.canBreatheUnderwater();
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.canAttack(this, target);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.canAttack(target);
    }

    @Override
    public boolean canAttack(EntityType<?> entityType) {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.canAttack(this, entityType);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.canAttack(entityType);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        for (IDogAlteration alter : this.alterations) {
            ActionResult<Float> result = alter.attackEntityFrom(this, source, damage);

            // TODO
            if (result.getType() == ActionResultType.FAIL) {
                return false;
            } else {
                damage = result.getResult();
            }
        }

        return super.attackEntityFrom(source, damage);
    }

    @Override
    public boolean attackEntityAsMob(Entity target) {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.attackEntityAsMob(this, target);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.attackEntityAsMob(target);
    }

    @Override
    public boolean canBlockDamageSource(DamageSource source) {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.canBlockDamageSource(this, source);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.canBlockDamageSource(source);
    }

    @Override
    public void setFire(int second) {
        for (IDogAlteration alter : this.alterations) {
            ActionResult<Integer> result = alter.setFire(this, second);

            if (result.getType().isSuccess()) {
                second = result.getResult();
            }
        }

        super.setFire(second);
    }

    @Override
    public boolean isImmuneToFire() {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.isImmuneToFire(this);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.isImmuneToFire();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.isInvulnerableTo(this, source);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.isInvulnerableTo(source);
    }

    @Override
    public boolean isInvulnerable() {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.isInvulnerable(this);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.isInvulnerable();
    }

    @Override
    public void setUniqueId(UUID uniqueIdIn) {

        // If the UUID is changed remove old one and add new one
        UUID oldUniqueId = this.getUniqueID();

        if (uniqueIdIn.equals(oldUniqueId)) {
            return; // No change do nothing
        }

        super.setUniqueId(uniqueIdIn);

        if (this.world != null && !this.world.isRemote) {
            DogLocationStorage.get(this.world).remove(oldUniqueId);
            DogLocationStorage.get(this.world).getOrCreateData(this).update(this);
        }
    }

    @Override
    public void setTamedBy(PlayerEntity player) {
        super.setTamedBy(player);
        // When tamed by player cache their display name
        this.setOwnersName(player.getName());
    }

    @Override
    public void setOwnerId(@Nullable UUID uuid) {
        super.setOwnerId(uuid);

        if (uuid == null) {
            this.setOwnersName((ITextComponent) null);
        }
    }

    @Override // blockAttackFromPlayer
    public boolean hitByEntity(Entity entityIn) {
        if (entityIn instanceof PlayerEntity && !this.canPlayersAttack()) {
            return true;
        }

        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.hitByEntity(this, entityIn);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return false;
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(DoggyItems.DOGGY_CHARM.get());
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem().isIn(DoggyTags.BREEDING_ITEMS);
    }

    @Override
    public boolean getAlwaysRenderNameTagForRender() {
        return (ConfigValues.ALWAYS_SHOW_DOG_NAME && this.hasCustomName()) || super.getAlwaysRenderNameTagForRender();
    }

    @Override
    public float getRenderScale() {
        if(this.isChild()) {
            return 0.5F;
        } else {
            return this.getDogSize() * 0.3F + 0.1F;
        }
    }

    public void consumeItemFromStack(@Nullable Entity entity, ItemStack stack) {
        if (entity instanceof PlayerEntity) {
            super.consumeItemFromStack((PlayerEntity) entity, stack);
        } else {
            stack.shrink(1);
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        for (IDogAlteration alter : this.alterations) {
            LazyOptional<T> result = alter.getCapability(this, cap, side);

            if (result != null) {
                return result;
            }
        }

        return super.getCapability(cap, side);
    }

    private boolean changingDimension = false;

    @Override
    public Entity changeDimension(DimensionType dimType) {
        this.changingDimension = true;
        Entity transportedEntity = super.changeDimension(dimType);
        if (transportedEntity instanceof DogEntity) {
            DogLocationStorage.get(this.world).getOrCreateData(this).update((DogEntity) transportedEntity);
        }
        return transportedEntity;
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld(); // When the entity is added to tracking list#
        if (this.world != null && !this.world.isRemote) {
            //DogLocationData locationData = DogLocationStorage.get(this.world).getOrCreateData(this);
            //locationData.update(this);
        }
    }

    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld(); // When the entity is removed from tracking list
    }

    /**
     * When the entity is brought back to life
     */
    @Override
    public void revive() {
        super.revive();
    }

    @Override
    protected void onDeathUpdate() {
        if (this.deathTime == 19) { // 1 second after death
            if (this.world != null && !this.world.isRemote) {
//                DogRespawnStorage.get(this.world).putData(this);
//                DoggyTalents.LOGGER.debug("Saved dog as they died {}", this);
//
//                DogLocationStorage.get(this.world).remove(this);
//                DoggyTalents.LOGGER.debug("Removed dog location as they were removed from the world {}", this);
            }
        }

        super.onDeathUpdate();
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (this.world != null && !this.world.isRemote) {
            DogRespawnStorage.get(this.world).putData(this);
            DoggyTalents2.LOGGER.debug("Saved dog as they died {}", this);

            DogLocationStorage.get(this.world).remove(this);
            DoggyTalents2.LOGGER.debug("Removed dog location as they were removed from the world {}", this);
        }

        this.alterations.forEach((alter) -> alter.onDeath(this, cause));
    }

    @Override
    public void dropInventory() {
        super.dropInventory();

        this.alterations.forEach((alter) -> alter.dropInventory(this));
    }

    /**
     * When the entity is removed
     */
    @Override
    public void remove(boolean keepData) {
        super.remove(keepData);

        if (!keepData) {
            this.alterations.forEach((alter) -> alter.invalidateCapabilities(this));
        }

        if (this.world != null && !this.world.isRemote) {

            // Dog did not die, just changed dimension
            if (this.changingDimension) {
//                DogRespawnStorage.get(this.world).putData(this);
//                DoggyTalents.LOGGER.debug("Saved dog as they died {}", this);
//
//                DogLocationStorage.get(this.world).remove(this);
//                DoggyTalents.LOGGER.debug("Removed dog location as they were removed from the world {}", this);
            }
        }
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);

        NBTUtil.putTalentMap(compound, "talent_level_list", this.getTalentMap());

        ListNBT accessoryList = new ListNBT();
        List<AccessoryInstance> accessories = this.getAccessories();

        for (int i = 0; i < accessories.size(); i++) {

            CompoundNBT accessoryTag = new CompoundNBT();
            accessories.get(i).writeInstance(accessoryTag);
            accessoryList.add(accessoryTag);
        }

        compound.put("accessories", accessoryList);

        this.alterations.forEach((alter) -> alter.write(this, compound));

        compound.putString("mode", this.getMode().getSaveName());
        compound.putString("gender", this.getGender().getSaveName());
        compound.putFloat("hunger", this.getDogHunger());
        this.getOwnersName().ifPresent((comp) -> {
            NBTUtil.putTextComponent(compound, "lastKnownOwnerName", comp);
        });

        compound.putString("customSkinHash", this.getSkinHash());
        compound.putBoolean("willObey", this.willObeyOthers());
        compound.putBoolean("friendlyFire", this.canPlayersAttack());
        compound.putInt("dogSize", this.getDogSize());
        compound.putInt("level_normal", this.getLevel().getLevel(Type.NORMAL));
        compound.putInt("level_dire", this.getLevel().getLevel(Type.DIRE));
    }


    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);

        Map<Talent, Integer> talentMap = this.getTalentMap();
        talentMap.clear();

        if(compound.contains("talent_level_list", Constants.NBT.TAG_LIST)) {
            talentMap.putAll(NBTUtil.getTalentMap(compound, "talent_level_list"));
        } else {
            // Try to read old talent format if new one doesn't exist
            BackwardsComp.readTalentMapping(compound, talentMap);
        }

        this.markDataParameterDirty(TALENTS.get(), false); // Mark dirty so data is synced to client

        ListNBT accessoryList = compound.getList("accessories", Constants.NBT.TAG_COMPOUND);
        List<AccessoryInstance> accessories = this.getAccessories();
        accessories.clear();

        for (int i = 0; i < accessoryList.size(); i++) {
            // Add directly so that nothing is lost, if number allowed on changes
            AccessoryInstance.readInstance(accessoryList.getCompound(i)).ifPresent(accessories::add);
        }

        this.markDataParameterDirty(ACCESSORIES.get(), false); // Mark dirty so data is synced to client

        // Does what notifyDataManagerChange would have done but this way only does it once
        this.recalculateAlterationsCache();
        for (Entry<Talent, Integer> entry : this.getTalentMap().entrySet()) {
            entry.getKey().init(this);
        }

        this.alterations.forEach((alter) -> alter.read(this, compound));

        this.setGender(EnumGender.bySaveName(compound.getString("gender")));
        this.setMode(EnumMode.bySaveName(compound.getString("mode")));
        this.setHungerDirectly(compound.getFloat("hunger"));
        this.setOwnersName(NBTUtil.getTextComponent(compound, "lastKnownOwnerName"));
        this.setSkinHash(compound.getString("customSkinHash"));
        this.setWillObeyOthers(compound.getBoolean("willObey"));
        this.setCanPlayersAttack(compound.getBoolean("friendlyFire"));
        if(compound.contains("dogSize", Constants.NBT.TAG_ANY_NUMERIC)) {
            this.setDogSize(compound.getInt("dogSize"));
        }

        if(compound.contains("level_normal")) {
            this.getLevel().setLevel(Type.NORMAL, compound.getInt("level_normal"));
        }
        if(compound.contains("level_dire")) {
            this.getLevel().setLevel(Type.DIRE, compound.getInt("level_dire"));
        }
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if (TALENTS.get().equals(key) || ACCESSORIES.get().equals(key)) {
            this.recalculateAlterationsCache();
        }

        if (TALENTS.get().equals(key)) {
            for (Entry<Talent, Integer> entry : this.getTalentMap().entrySet()) {
                entry.getKey().init(this);
            }
        }

        if (ACCESSORIES.get().equals(key)) {
            // If client sort accessories
            if (this.world.isRemote) {
                // Does not recall this notifyDataManagerChange as list object is
                // still the same, maybe in future MC versions this will change so need to watch out
                this.getAccessories().sort(AccessoryInstance.RENDER_SORTER);
            }
        }

        if(SIZE.equals(key)) {
            this.recalculateSize();
        }
    }

    public void recalculateAlterationsCache() {
        this.alterations.clear();

        this.getAccessories().forEach((inst) -> {
            if (inst instanceof IDogAlteration) {
                this.alterations.add((IDogAlteration) inst);
            }
        });

        for (Entry<Talent, Integer> entry : this.getTalentMap().entrySet()) {
            this.alterations.add(entry.getKey());
        }

        DoggyTalents2.LOGGER.debug("Recalculate alterations, size {}", this.alterations.size());
    }

    /**
     * If the entity can make changes to the dog
     * @param livingEntity The entity
     */
    public boolean canInteract(LivingEntity livingEntity) {
        return this.isOwner(livingEntity) || this.willObeyOthers();
    }

    public List<AccessoryInstance> getAccessories() {
        return this.dataManager.get(ACCESSORIES.get());
    }

    public boolean addAccessory(@Nonnull AccessoryInstance collar) {
        List<AccessoryInstance> accessories = this.getAccessories();
        AccessoryType type = collar.getAccessory().getType();

        // Gets accessories of the same type
        List<AccessoryInstance> filtered = accessories.stream().filter((inst) -> {
            return type == inst.getAccessory().getType();
        }).collect(Collectors.toList());

//        while (!filtered.isEmpty() && filtered.size() >= type.numberToPutOn()) {
//            accessories.remove(filtered.get(0));
//            filtered.remove(0);
//        }

        if (filtered.size() >= type.numberToPutOn()) {
            return false;
        }

        accessories.add(collar);

        this.markDataParameterDirty(ACCESSORIES.get());

        return true;
    }

    public void removeAccessories() {
        this.getAccessories().clear();
        this.markDataParameterDirty(ACCESSORIES.get());
    }

    public Optional<AccessoryInstance> getAccessory(AccessoryType typeIn) {
        List<AccessoryInstance> accessories = this.getAccessories();

        for (AccessoryInstance inst : accessories) {
            if (inst.getAccessory().getType() == typeIn) {
                return Optional.of(inst);
            }
        }

        return Optional.empty();
    }

    public Optional<AccessoryInstance> getAccessory(Accessory typeIn) {
        List<AccessoryInstance> accessories = this.getAccessories();

        for (AccessoryInstance inst : accessories) {
            if (inst.getAccessory() == typeIn) {
                return Optional.of(inst);
            }
        }

        return Optional.empty();
    }

    public Optional<ITextComponent> getOwnersName() {
        return this.dataManager.get(LAST_KNOWN_NAME);
    }

    public void setOwnersName(@Nullable ITextComponent comp) {
        this.setOwnersName(Optional.ofNullable(comp));
    }

    public void setOwnersName(Optional<ITextComponent> collar) {
        this.dataManager.set(LAST_KNOWN_NAME, collar);
    }

    public EnumGender getGender() {
        return this.dataManager.get(GENDER.get());
    }

    public void setGender(EnumGender collar) {
        this.dataManager.set(GENDER.get(), collar);
    }

    public EnumMode getMode() {
        return this.dataManager.get(MODE.get());
    }

    public void setMode(EnumMode collar) {
        this.dataManager.set(MODE.get(), collar);
    }

    public Optional<BlockPos> getBowlPos() {
        return this.dataManager.get(BOWL_POS);
    }

    public void setBowlPos(@Nullable BlockPos comp) {
        this.setBowlPos(Optional.ofNullable(comp));
    }

    public void setBowlPos(Optional<BlockPos> collar) {
        this.dataManager.set(BOWL_POS, collar);
    }

    public float getMaxHunger() {
        float maxHunger = ConfigValues.DEFAULT_MAX_HUNGER;

        for (IDogAlteration alter : this.alterations) {
            ActionResult<Float> result = alter.getMaxHunger(this, maxHunger);

            if (result.getType().isSuccess()) {
                maxHunger = result.getResult();
            }
        }

        return maxHunger;
    }

    public float getDogHunger() {
        return this.dataManager.get(HUNGER_INT);
    }

    public void addHunger(float add) {
        this.setDogHunger(this.getDogHunger() + add);
    }

    public void setDogHunger(float hunger) {
        float diff = hunger - this.getDogHunger();

        for (IDogAlteration alter : this.alterations) {
            ActionResult<Float> result = alter.setDogHunger(this, hunger, diff);

            if (result.getType().isSuccess()) {
                hunger = result.getResult();
                diff = hunger - this.getDogHunger();
            }
        }

        this.setHungerDirectly(MathHelper.clamp(hunger, 0, this.getMaxHunger()));
    }

    private void setHungerDirectly(float hunger) {
        this.dataManager.set(HUNGER_INT, hunger);
    }

    public boolean hasCustomSkin() {
        return !Strings.isNullOrEmpty(this.getSkinHash());
    }

    public String getSkinHash() {
        return this.dataManager.get(CUSTOM_SKIN);
    }

    public void setSkinHash(String hash) {
        if (hash == null) {
            hash = "";
        }
        this.dataManager.set(CUSTOM_SKIN, hash);
    }

    public DogLevel getLevel() {
        return this.dataManager.get(DOG_LEVEL.get());
    }

    public void setLevel(DogLevel level) {
        this.dataManager.set(DOG_LEVEL.get(), level);
    }

    public void increaseLevel(DogLevel.Type typeIn) {
        this.getLevel().incrementLevel(typeIn);
        this.markDataParameterDirty(DOG_LEVEL.get());
    }

    public void setDogSize(int value) {
        this.dataManager.set(SIZE, (byte)Math.min(5, Math.max(1, value)));
    }

    public int getDogSize() {
        return this.dataManager.get(SIZE);
    }

    private boolean getDogFlag(int bit) {
        return (this.dataManager.get(DOG_FLAGS) & bit) != 0;
    }

    private void setDogFlag(int bits, boolean flag) {
        byte c = this.dataManager.get(DOG_FLAGS);
        this.dataManager.set(DOG_FLAGS, (byte)(flag ? c | bits : c & ~bits));
    }

    @Override
    public void setBegging(boolean begging) {
        this.setDogFlag(1, begging);
    }

    @Override
    public boolean isBegging() {
        return this.getDogFlag(1);
    }

    public void setWillObeyOthers(boolean obeyOthers) {
        this.setDogFlag(2, obeyOthers);
    }

    public boolean willObeyOthers() {
        return this.getDogFlag(2);
    }

    public void setCanPlayersAttack(boolean flag) {
        this.setDogFlag(4, flag);
    }

    public boolean canPlayersAttack() {
        return this.getDogFlag(4);
    }

    public void set8Flag(boolean collar) {
        this.setDogFlag(8, collar);
    }

    public boolean get8Flag() {
        return this.getDogFlag(8);
    }

    public void setHasSunglasses(boolean sunglasses) {
        this.setDogFlag(16, sunglasses);
    }

    public boolean hasSunglasses() {
        return this.getDogFlag(16);
    }

    public void setLyingDown(boolean lying) {
        this.setDogFlag(32, lying);
    }

    public boolean isLyingDown() {
        return this.getDogFlag(32);
    }

    public void set64Flag(boolean lying) {
        this.setDogFlag(64, lying);
    }

    public boolean get64Flag() {
        return this.getDogFlag(64);
    }

    public Map<Talent, Integer> getTalentMap() {
        return this.dataManager.get(TALENTS.get());
    }

    public void setTalentMap(Map<Talent, Integer> map) {
        this.dataManager.set(TALENTS.get(), map);
    }

    public void setTalentLevel(Talent talent, int level) {
        Map<Talent, Integer> map = this.getTalentMap();
        boolean existed = map.containsKey(talent);
        if (level > 0) {
            map.put(talent, level);

            if (!existed) {
                talent.init(this);
            }
            talent.set(this, level);
        } else {
            int preLevel = map.getOrDefault(talent, 0);
            map.remove(talent);
            talent.removed(this, preLevel);
        }

        this.markDataParameterDirty(TALENTS.get());
        DoggyTalents2.LOGGER.debug("Set talent {} to level {}", talent.getRegistryName(), level);
    }


    public <T> void markDataParameterDirty(DataParameter<T> key) {
        this.markDataParameterDirty(key, true);
    }

    public <T> void markDataParameterDirty(DataParameter<T> key, boolean notify) {
        if (notify) {
            this.notifyDataManagerChange(key);
        }

        // Force the entry to update
        DataEntry<T> dataentry = this.dataManager.getEntry(key);
        dataentry.setDirty(true);
        this.dataManager.dirty = true;
    }

    public void markAccessoriesDirty() {
        this.markDataParameterDirty(ACCESSORIES.get());
    }

    public int getLevel(Talent talentIn) {
        Map<Talent, Integer> map = this.getTalentMap();
        return map.getOrDefault(talentIn, 0);
    }

    public <T> void setData(DataKey<T> key, T value) {
        if (key.isFinal() && this.hasData(key)) {
            throw new RuntimeException("Key is final but was tried to be set again.");
        }
        this.objects.put(key.getIndex(), value);
    }

    /**
     * Tries to put the object in the map, does nothing if the key already exists
     */
    public <T> void setDataIfEmpty(DataKey<T> key, T value) {
        if (!this.hasData(key)) {
            this.objects.put(key.getIndex(), value);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getData(DataKey<T> key) {
        return (T) this.objects.get(key.getIndex());
    }

    public <T> T getDataOrDefault(DataKey<T> key, Supplier<T> other) {
        if (!this.hasData(key)) {
            return this.getData(key);
        }
        return other.get();
    }

    public <T> T getDataOrDefault(DataKey<T> key, T other) {
        if (!this.hasData(key)) {
            return this.getData(key);
        }
        return other;
    }

    public <T> boolean hasData(DataKey<T> key) {
        return this.objects.containsKey(key.getIndex());
    }

    public void untame() {
        this.setTamed(false);
        this.navigator.clearPath();
        this.sitGoal.setSitting(false);
        this.setHealth(8);

        this.getTalentMap().clear();
        this.markDataParameterDirty(TALENTS.get());

        this.setOwnerId(null);
        this.setWillObeyOthers(false);
        this.setMode(EnumMode.DOCILE);

        DoggyTalents2.LOGGER.debug("Untamed dog");
    }

    // TODO currently calculates points every time, make it cache value
    public int getSpendablePoints() {
        int totalPoints = 15 + this.getLevel().getLevel(Type.NORMAL) + this.getLevel().getLevel(Type.DIRE);
        for (Entry<Talent, Integer> entry : this.getTalentMap().entrySet()) {
            totalPoints -= entry.getKey().getCummulativeCost(entry.getValue());
        }
        return totalPoints;
    }
}
