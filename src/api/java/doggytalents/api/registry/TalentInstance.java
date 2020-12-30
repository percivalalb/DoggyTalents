package doggytalents.api.registry;

import java.util.Optional;
import java.util.function.Supplier;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.inferface.IDogAlteration;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IRegistryDelegate;

public class TalentInstance implements IDogAlteration {

    protected final IRegistryDelegate<Talent> talentDelegate;

    protected int level;

    public TalentInstance(Talent talentIn, int levelIn) {
        this(talentIn.delegate, levelIn);
    }

    public TalentInstance(Talent talentIn) {
        this(talentIn.delegate, 1);
    }

    public TalentInstance(IRegistryDelegate<Talent> talentDelegateIn, int levelIn) {
        this.talentDelegate = talentDelegateIn;
        this.level = levelIn;
    }

    public Talent getTalent() {
        return this.talentDelegate.get();
    }

    public final int level() {
        return this.level;
    }

    public final void setLevel(int levelIn) {
        this.level = levelIn;
    }

    public boolean of(Supplier<Talent> talentIn) {
        return this.of(talentIn.get());
    }

    public boolean of(Talent talentIn) {
        return this.of(talentIn.delegate);
    }

    public boolean of(IRegistryDelegate<Talent> talentDelegateIn) {
        return talentDelegateIn.equals(this.talentDelegate);
    }

    public TalentInstance copy() {
        return this.talentDelegate.get().getDefault(this.level);
    }

    public void writeToNBT(AbstractDogEntity dogIn, CompoundNBT compound) {
        compound.putInt("level", this.level());
    }

    public void readFromNBT(AbstractDogEntity dogIn, CompoundNBT compound) {
        this.setLevel(compound.getInt("level"));
    }

    public void writeToBuf(PacketBuffer buf) {
        buf.writeInt(this.level());
    }

    public void readFromBuf(PacketBuffer buf) {
        this.setLevel(buf.readInt());
    }

    public final void writeInstance(AbstractDogEntity dogIn, CompoundNBT compound) {
        ResourceLocation rl = this.talentDelegate.name();
        if (rl != null) {
            compound.putString("type", rl.toString());
        }

        this.writeToNBT(dogIn, compound);
    }

    public static Optional<TalentInstance> readInstance(AbstractDogEntity dogIn, CompoundNBT compound) {
        ResourceLocation rl = ResourceLocation.tryCreate(compound.getString("type"));
        if (DoggyTalentsAPI.TALENTS.containsKey(rl)) {
            TalentInstance inst = DoggyTalentsAPI.TALENTS.getValue(rl).getDefault();
            inst.readFromNBT(dogIn, compound);
            return Optional.of(inst);
        } else {
            DoggyTalentsAPI.LOGGER.warn("Failed to load talent {}", rl);
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends TalentInstance> T cast(Class<T> type) {
        if (this.getClass().isAssignableFrom(type)) {
            return (T) this;
        } else {
            throw new RuntimeException("Could not cast " + this.getClass().getName() + " to " + type.getName());
        }
    }

    @Override
    public String toString() {
        return String.format("%s [talent: %s, level: %d]", this.getClass().getSimpleName(), talentDelegate.name(), this.level);
    }

    /**
     * Called when ever this instance is first added to a dog, this is called when
     * the level is first set on the dog or when it is loaded from NBT and when the
     * talents are synced to the client
     *
     * @param dogIn The dog
     */
    public void init(AbstractDogEntity dogIn) {

    }

    /**
     * Called when the level of the dog changes
     * Is not called when the dog is loaded from NBT
     *
     * @param dogIn The dog
     */
    public void set(AbstractDogEntity dog, int levelBefore) {

    }

    public boolean hasRenderer() {
        return this.getTalent().hasRenderer();
    }
}
