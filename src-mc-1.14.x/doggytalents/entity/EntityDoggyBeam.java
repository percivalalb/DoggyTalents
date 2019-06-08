package doggytalents.entity;

import java.util.List;

import doggytalents.entity.features.ModeFeature.EnumMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 */
public class EntityDoggyBeam extends ThrowableEntity implements IRendersAsItem {
	
	private ItemStack renderItem;
	
	public EntityDoggyBeam(EntityType<EntityDoggyBeam> type, World worldIn) {
		super(type, worldIn);
	}
	
    public EntityDoggyBeam(EntityType<EntityDoggyBeam> type, World worldIn, LivingEntity throwerIn) {
        super(type, throwerIn, worldIn);
    }
    
    @Override
    public void onImpact(RayTraceResult rayTraceResult) {
    	if(rayTraceResult.getType() != RayTraceResult.Type.ENTITY) return;
    	
    	Entity entityHit = (Entity)rayTraceResult.hitInfo;
    	
    	if(entityHit instanceof LivingEntity) {
 
    		List<Entity> nearEnts = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().grow(100D, 10D, 100D));
    		for(Object o : nearEnts) {
    			
    			if(o instanceof EntityDog) {
                 	EntityDog dog = (EntityDog)o;
                 	if(!dog.isSitting() && entityHit != dog && dog.shouldAttackEntity((LivingEntity)entityHit, dog.getOwner()) && this.getThrower() instanceof PlayerEntity && dog.canInteract((PlayerEntity)this.getThrower())) {
                 		if(dog.getDistance(entityHit) < this.getTargetDistance(dog) && (dog.MODE.isMode(EnumMode.AGGRESIVE) || dog.MODE.isMode(EnumMode.TACTICAL))) {
                 			dog.setAttackTarget((LivingEntity)entityHit);
                 		}
                 	}
                 }
             }
        }
    	
        for(int j = 0; j < 8; ++j)
            this.world.addParticle(ParticleTypes.ITEM_SNOWBALL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);

        if(!this.world.isRemote)
        	this.remove();
    }
    
    protected double getTargetDistance(EntityDog dog) {
        IAttributeInstance iattributeinstance = dog.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getValue();
    }

	@Override
	protected void registerData() {
		
	}

	@Override
	public ItemStack getPotion() {
		if(this.renderItem == null) {
			this.renderItem = new ItemStack(Items.SNOWBALL);
		}
		return this.renderItem;
	}
}
