package doggytalents.entity.ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import doggytalents.api.inferface.IWaterMovement;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigate;

public class EntityAIClosestItem extends EntityAIBase {
    
    protected final EntityDog dog;
    private final Predicate<EntityItem> predicate;
    private final Comparator<Entity> sorter;
    private final double followSpeed;
    private final PathNavigate dogPathfinder;
    private final IWaterMovement waterMovement;
    
    protected EntityItem target;
    private int timeToRecalcPath;
    private float maxDist;
    private double oldRangeSense;

    public EntityAIClosestItem(EntityDog dogIn, double speedIn, float range, @Nullable Predicate<ItemStack> targetSelector) {
        this.dog = dogIn;
        this.dogPathfinder = dogIn.getNavigator();
        this.followSpeed = speedIn;
        this.maxDist = range;
        this.predicate = (entity) -> {
            double d0 = this.getFollowRange();
            if(entity.isInvisible()) {
                return false;
            }
            else if (targetSelector != null && !targetSelector.test(entity.getItem())) {
                return false;
            } else {
                return (double)entity.getDistance(this.dog) > d0 ? false : true;
            }
        };
        this.sorter = new EntityAINearestAttackableTarget.Sorter(dogIn);
        this.waterMovement = new WaterMovementHandler(this.dog);
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        double d0 = this.getFollowRange();
        List<EntityItem> list = this.dog.world.getEntitiesWithinAABB(EntityItem.class, this.dog.getEntityBoundingBox().grow(d0, 4.0D, d0), this.predicate);
        Collections.sort(list, this.sorter);
        if(list.isEmpty()) {
            return false;
        } 
        else {
            this.target = list.get(0);
            return true;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        EntityItem target = this.target;
        if(target == null) {
            return false;
        } else if(!target.isEntityAlive()) {
            return false;
        } else {
            double d0 = this.getFollowRange();
            if (this.dog.getDistanceSq(target) > d0 * d0) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.oldRangeSense = this.dog.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getBaseValue();
        this.dog.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(this.maxDist);
    }

    @Override
    public void updateTask() {
        this.dog.getLookHelper().setLookPositionWithEntity(this.target, 10.0F, (float)this.dog.getVerticalFaceSpeed());

        if(!this.dog.isSitting()) {
            if(--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;

                this.dogPathfinder.tryMoveToEntityLiving(this.target, this.followSpeed);
            }
        }
    }
    
    @Override
    public void resetTask() {
        this.dogPathfinder.clearPath();
        this.waterMovement.resetTask();
        this.dog.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(this.oldRangeSense);
    }

    protected double getFollowRange() {
        IAttributeInstance iattributeinstance = this.dog.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getBaseValue();
    }
}