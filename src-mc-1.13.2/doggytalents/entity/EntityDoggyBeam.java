package doggytalents.entity;

import java.util.List;

import doggytalents.ModEntities;
import doggytalents.entity.features.ModeFeature.EnumMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Particles;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 */
public class EntityDoggyBeam extends EntityThrowable {
	
	public EntityDoggyBeam(World worldIn) {
		super(ModEntities.DOG_BEAM, worldIn);
	}
	
    public EntityDoggyBeam(World worldIn, EntityLivingBase throwerIn) {
        super(ModEntities.DOG_BEAM, throwerIn, worldIn);
    }
    
    @Override
    public void onImpact(RayTraceResult rayTraceResult) {
    	if(rayTraceResult.type != RayTraceResult.Type.ENTITY) return;
    	
    	Entity entityHit = rayTraceResult.entity;
    	
    	if(entityHit instanceof EntityLivingBase) {
 
    		List<Entity> nearEnts = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().grow(100D, 10D, 100D));
    		for(Object o : nearEnts) {
    			
    			if(o instanceof EntityDog) {
                 	EntityDog dog = (EntityDog)o;
                 	if(!dog.isSitting() && entityHit != dog && dog.shouldAttackEntity((EntityLivingBase)entityHit, dog.getOwner()) && this.getThrower() instanceof EntityPlayer && dog.canInteract((EntityPlayer)this.getThrower())) {
                 		if(dog.getDistance(entityHit) < this.getTargetDistance(dog) && (dog.MODE.isMode(EnumMode.AGGRESIVE) || dog.MODE.isMode(EnumMode.TACTICAL))) {
                 			dog.setAttackTarget((EntityLivingBase)entityHit);
                 		}
                 	}
                 }
             }
        }
    	
        for(int j = 0; j < 8; ++j)
            this.world.addParticle(Particles.ITEM_SNOWBALL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);

        if(!this.world.isRemote)
        	this.remove();
    }
    
    protected double getTargetDistance(EntityDog dog) {
        IAttributeInstance iattributeinstance = dog.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getValue();
    }
}
