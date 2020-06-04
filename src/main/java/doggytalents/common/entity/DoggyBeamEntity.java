package doggytalents.common.entity;

import doggytalents.DoggyEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class DoggyBeamEntity extends ThrowableEntity implements IEntityAdditionalSpawnData {

    public DoggyBeamEntity(EntityType<? extends ThrowableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public DoggyBeamEntity(FMLPlayMessages.SpawnEntity packet, World worldIn) {
        super(DoggyEntityTypes.DOG_BEAM.get(), worldIn);
    }

    @Override
    protected void onImpact(RayTraceResult result) {

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        LivingEntity thrower = this.getThrower(); // this.ownerId
        buffer.writeBoolean(thrower != null);
        if (thrower != null) {
            buffer.writeUniqueId(thrower.getUniqueID());
        }
    }

    @Override
    public void readSpawnData(PacketBuffer buffer) {
        boolean hasThrower = buffer.readBoolean();
        if (hasThrower) {
            //this.ownerId = buffer.readUniqueId();
        }
    }

    @Override
    protected void registerData() {
        // TODO Auto-generated method stub

    }
}
