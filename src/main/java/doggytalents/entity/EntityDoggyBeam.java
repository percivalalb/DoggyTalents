package doggytalents.entity;

import com.google.common.base.Predicates;

import doggytalents.ModEntities;
import doggytalents.api.feature.EnumMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.FMLPlayMessages.SpawnEntity;
import net.minecraftforge.fml.network.NetworkHooks;

@OnlyIn(
    value = Dist.CLIENT,
    _interface = IRendersAsItem.class
)
public class EntityDoggyBeam extends ThrowableEntity implements IRendersAsItem, IEntityAdditionalSpawnData {

    private ItemStack renderItem;

    public EntityDoggyBeam(SpawnEntity packet, World worldIn) {
        this(ModEntities.DOG_BEAM, worldIn);
    }

    public EntityDoggyBeam(EntityType<EntityDoggyBeam> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityDoggyBeam(World worldIn, LivingEntity throwerIn) {
        super(ModEntities.DOG_BEAM, throwerIn, worldIn);
    }

    @Override
    public void onImpact(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            Entity entityHit = ((EntityRayTraceResult) result).getEntity();

            LivingEntity thrower = this.getThrower();

            if (thrower != null && entityHit instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entityHit;

                this.world.getEntitiesWithinAABB(EntityDog.class, this.getBoundingBox().grow(64D, 16D, 64D)).stream()
                    .filter(Predicates.not(EntityDog::isSitting))
                    .filter(d -> d.MODE.isMode(EnumMode.AGGRESIVE, EnumMode.TACTICAL, EnumMode.BERSERKER))
                    .filter(d -> d.canInteract(thrower))
                    .filter(d -> d != livingEntity && d.shouldAttackEntity(livingEntity, d.getOwner()))
                    .filter(d -> d.getDistance(entityHit) < this.getTargetDistance(d))
                    .forEach(d -> d.setAttackTarget(livingEntity));
            }

            for (int j = 0; j < 8; ++j) {
                this.world.addParticle(ParticleTypes.ITEM_SNOWBALL, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
            }
        }

        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte)3);
            this.remove();
        }
    }

    protected double getTargetDistance(EntityDog dog) {
        IAttributeInstance iattributeinstance = dog.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getValue();
    }

    @Override
    protected void registerData() {

    }

    @Override
    public ItemStack getItem() {
        if (this.renderItem == null) {
            this.renderItem = new ItemStack(Items.SNOWBALL);
        }
        return this.renderItem;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {

        buffer.writeBoolean(this.entityUniqueID != null);
        if (this.entityUniqueID != null) {
            buffer.writeUniqueId(this.entityUniqueID);
        }
    }

    @Override
    public void readSpawnData(PacketBuffer buffer) {
        boolean hasThrower = buffer.readBoolean();
        if (hasThrower) {
            this.entityUniqueID = buffer.readUniqueId();
        }
    }
}
