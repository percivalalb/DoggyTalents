package doggytalents.entity;

import com.google.common.base.Predicates;

import doggytalents.api.feature.EnumMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 */
public class EntityDoggyBeam extends EntityThrowable {

    public EntityDoggyBeam(World worldIn) {
        super(worldIn);
    }

    public EntityDoggyBeam(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    @Override
    public void onImpact(RayTraceResult rayTraceResult) {
        if (rayTraceResult.typeOfHit == RayTraceResult.Type.ENTITY) {
            Entity entityHit = rayTraceResult.entityHit;

            EntityLivingBase thrower = this.getThrower();

            if (thrower != null && entityHit instanceof EntityLiving) {
                EntityLiving livingEntity = (EntityLiving) entityHit;

                this.world.getEntitiesWithinAABB(EntityDog.class, this.getEntityBoundingBox().grow(64D, 16D, 64D)).stream()
                    .filter(Predicates.not(EntityDog::isSitting))
                    .filter(d -> d.MODE.isMode(EnumMode.AGGRESIVE, EnumMode.TACTICAL, EnumMode.BERSERKER))
                    .filter(d -> d.canInteract(thrower))
                    .filter(d -> d != livingEntity && d.shouldAttackEntity(livingEntity, d.getOwner()))
                    .filter(d -> d.getDistance(entityHit) < this.getTargetDistance(d))
                    .forEach(d -> d.setAttackTarget(livingEntity));
            }

            for (int j = 0; j < 8; ++j) {
                this.world.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }

        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte)3);
            this.setDead();
        }
    }

    protected double getTargetDistance(EntityDog dog) {
        IAttributeInstance iattributeinstance = dog.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
    }
}
