package doggytalents.entity;

import java.util.List;

import doggytalents.entity.ModeUtil.EnumMode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 */
public class EntityDoggyBeam extends EntityThrowable {
	
    public EntityDoggyBeam(World par1World) {
        super(par1World);
    }

    public EntityDoggyBeam(World par1World, EntityLivingBase par2EntityLivingBase) {
        super(par1World, par2EntityLivingBase);
    }

    public EntityDoggyBeam(World par1World, double par2, double par4, double par6) {
        super(par1World, par2, par4, par6);
    }

    
    //TODO
   // public static void func_189662_a(DataFixer p_189662_0_)
    //{
    //    EntityThrowable.func_189661_a(p_189662_0_, "Snowball");
    //}
    
    @Override
    protected void onImpact(RayTraceResult result) {
        if(result.entityHit instanceof EntityLivingBase) {
        	 byte var2 = 0;
             
             List nearEnts = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(100D, 10D, 100D));
             for (Object o : nearEnts)
             {
                 if (o instanceof EntityDog)
                 {
                 	EntityDog dog = (EntityDog)o;
                 	if(!dog.isSitting() && result.entityHit != dog && dog.shouldAttackEntity((EntityLivingBase)result.entityHit, dog.getOwner()) && this.getThrower() instanceof EntityPlayer && dog.canInteract((EntityPlayer)this.getThrower())) {
                 		if(dog.getDistanceToEntity(result.entityHit) < this.getTargetDistance(dog) && (dog.mode.isMode(EnumMode.AGGRESIVE) || dog.mode.isMode(EnumMode.TACTICAL))) {
                 			dog.setAttackTarget((EntityLivingBase)result.entityHit);
                 		}
                 	}
                 }
             }
        }

        for (int j = 0; j < 8; ++j)
        {
            this.world.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
        }

        if (!this.world.isRemote)
        {
            this.setDead();
        }
    }
    
    protected double getTargetDistance(EntityDog dog) {
        IAttributeInstance iattributeinstance = dog.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
    }
}
