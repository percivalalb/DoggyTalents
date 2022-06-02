package doggytalents.common.storage;

import com.google.common.collect.Lists;
import doggytalents.DoggyEntityTypes;
import doggytalents.api.feature.EnumMode;
import doggytalents.common.entity.Dog;
import doggytalents.common.util.NBTUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class DogRespawnData implements IDogData {

    private final DogRespawnStorage storage;
    private final UUID uuid;
    private CompoundTag data;

    //TODO Make it list you can only add too
    private static final List<String> TAGS_TO_REMOVE = Lists.newArrayList(
            "Pos", "Health", "Motion", "Rotation", "FallDistance", "Fire", "Air", "OnGround",
            "Dimension", "PortalCooldown", "Passengers", "Leash", "InLove", "Leash", "HurtTime",
            "HurtByTimestamp", "DeathTime", "AbsorptionAmount", "FallFlying", "Brain", "Sitting"); // Remove dog mode

    protected DogRespawnData(DogRespawnStorage storageIn, UUID uuid) {
        this.storage = storageIn;
        this.uuid = uuid;
    }

    @Override
    public UUID getDogId() {
        return this.uuid;
    }

    @Override
    public String getDogName() {
        Component name = NBTUtil.getTextComponent(this.data, "CustomName");
        return name == null ? "" : name.getString();
    }

    @Override
    public UUID getOwnerId() {
        String str = data.getString("OwnerUUID");
        return "".equals(str) ? null : UUID.fromString(str);
    }

    @Override
    public String getOwnerName() {
        Component name = NBTUtil.getTextComponent(this.data, "lastKnownOwnerName");
        return name == null ? "" : name.getString();
    }

    public void populate(Dog dogIn) {
        this.data = new CompoundTag();
        dogIn.saveWithoutId(this.data);

        // Remove tags that don't need to be saved
        for (String tag : TAGS_TO_REMOVE) {
            this.data.remove(tag);
        }

        this.data.remove("UUID");
        this.data.remove("LoveCause");
    }

    @Nullable
    public Dog respawn(ServerLevel worldIn, Player playerIn, BlockPos pos) {
        Dog dog = DoggyEntityTypes.DOG.get().spawn(worldIn, (CompoundTag) null, (Component) null, playerIn, pos, MobSpawnType.TRIGGERED, true, false);

        // Failed for some reason
        if (dog == null) {
            return null;
        }

        CompoundTag compoundnbt = dog.saveWithoutId(new CompoundTag());
        UUID uuid = dog.getUUID();
        compoundnbt.merge(this.data);
        dog.setUUID(uuid);
        dog.load(compoundnbt);

        dog.setMode(EnumMode.DOCILE);
        dog.setOrderedToSit(true);

        return dog;
    }

    public void read(CompoundTag compound) {
        this.data = compound.getCompound("data");
    }

    public CompoundTag write(CompoundTag compound) {
        compound.put("data", this.data);
        return compound;
    }
}
