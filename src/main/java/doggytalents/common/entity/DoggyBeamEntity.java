package doggytalents.common.entity;

import java.util.UUID;

import com.google.common.base.Predicates;

import doggytalents.DoggyEntityTypes;
import doggytalents.api.feature.EnumMode;
import doggytalents.common.lib.Constants;
import doggytalents.common.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class DoggyBeamEntity extends ThrowableEntity implements IEntityAdditionalSpawnData {

    public DoggyBeamEntity(EntityType<? extends ThrowableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public DoggyBeamEntity(World worldIn, LivingEntity livingEntityIn) {
        super(DoggyEntityTypes.DOG_BEAM.get(), livingEntityIn, worldIn);
    }

    public DoggyBeamEntity(FMLPlayMessages.SpawnEntity packet, World worldIn) {
        super(DoggyEntityTypes.DOG_BEAM.get(), worldIn);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            Entity entityHit = ((EntityRayTraceResult) result).getEntity();

            Entity thrower = this.func_234616_v_();

            if (thrower instanceof LivingEntity && entityHit instanceof LivingEntity) {
                LivingEntity livingThrower = (LivingEntity) thrower;
                LivingEntity livingEntity = (LivingEntity) entityHit;

                this.world.getEntitiesWithinAABB(DogEntity.class, this.getBoundingBox().grow(64D, 16D, 64D)).stream()
                    .filter(Predicates.not(DogEntity::isSitting))
                    .filter(d -> d.isMode(EnumMode.AGGRESIVE, EnumMode.TACTICAL, EnumMode.BERSERKER))
                    .filter(d -> d.canInteract(livingThrower))
                    .filter(d -> d != livingEntity && d.shouldAttackEntity(livingEntity, d.getOwner()))
                    .filter(d -> d.getDistance(entityHit) < EntityUtil.getFollowRange(d))
                    .forEach(d -> d.setAttackTarget(livingEntity));
            }

            for (int j = 0; j < 8; ++j) {
                this.world.addParticle(ParticleTypes.ITEM_SNOWBALL, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
            }
        }

        if (!this.world.isRemote) {
            this.world.setEntityState(this, Constants.EntityState.DEATH);
            this.remove();
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        UUID ownerId = this.entityUniqueID;
        buffer.writeBoolean(ownerId != null);
        if (ownerId != null) {
            buffer.writeUniqueId(ownerId);
        }
    }

    @Override
    public void readSpawnData(PacketBuffer buffer) {
        boolean hasThrower = buffer.readBoolean();
        if (hasThrower) {
            this.entityUniqueID = buffer.readUniqueId();
        }
    }

    @Override
    protected void registerData() {
        // TODO Auto-generated method stub

    }
}
