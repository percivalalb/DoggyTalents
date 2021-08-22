package doggytalents.common.storage;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import doggytalents.DoggyEntityTypes;
import doggytalents.api.feature.EnumMode;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.util.NBTUtil;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;

public class DogRespawnData implements IDogData {

    private final DogRespawnStorage storage;
    private final UUID uuid;
    private CompoundNBT data;

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
        ITextComponent name = NBTUtil.getTextComponent(this.data, "CustomName");
        return name == null ? "" : name.getString();
    }

    @Override
    public UUID getOwnerId() {
        String str = data.getString("OwnerUUID");
        return "".equals(str) ? null : UUID.fromString(str);
    }

    @Override
    public String getOwnerName() {
        ITextComponent name = NBTUtil.getTextComponent(this.data, "lastKnownOwnerName");
        return name == null ? "" : name.getString();
    }

    public void populate(DogEntity dogIn) {
        this.data = new CompoundNBT();
        dogIn.saveWithoutId(this.data);

        // Remove tags that don't need to be saved
        for (String tag : TAGS_TO_REMOVE) {
            this.data.remove(tag);
        }

        this.data.remove("UUID");
        this.data.remove("LoveCause");
    }

    @Nullable
    public DogEntity respawn(ServerWorld worldIn, PlayerEntity playerIn, BlockPos pos) {
        DogEntity dog = DoggyEntityTypes.DOG.get().spawn(worldIn, (CompoundNBT) null, (ITextComponent) null, playerIn, pos, SpawnReason.TRIGGERED, true, false);

        // Failed for some reason
        if (dog == null) {
            return null;
        }

        CompoundNBT compoundnbt = dog.saveWithoutId(new CompoundNBT());
        UUID uuid = dog.getUUID();
        compoundnbt.merge(this.data);
        dog.setUUID(uuid);
        dog.load(compoundnbt);

        dog.setMode(EnumMode.DOCILE);
        dog.setOrderedToSit(true);

        return dog;
    }

    public void read(CompoundNBT compound) {
        this.data = compound.getCompound("data");
    }

    public CompoundNBT write(CompoundNBT compound) {
        compound.put("data", this.data);
        return compound;
    }
}
