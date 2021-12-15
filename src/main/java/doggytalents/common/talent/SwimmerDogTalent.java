package doggytalents.common.talent;

import java.util.EnumSet;
import java.util.UUID;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import doggytalents.common.entity.DogEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ForgeMod;

public class SwimmerDogTalent extends TalentInstance {

    private SwimmerDogMovementController mv_ctrl;
    private SwimmerPathNavigator navigator;
    private SwimmerDogGoal goal;
    private static final UUID SWIM_BOOST_ID = UUID.fromString("50671e42-1ded-4f97-9e2b-78bbeb1e8772");
    //TODO Make dog swim up to get air when low on air supply
    //TODO Resolve dog still try to tp eventhough the owner is stil in water
    //I think the owner of the alteration should be passed in the constructor and set as a field for
    //easier access, the alteration must have an owner, right ?

    public SwimmerDogTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public void init(AbstractDogEntity d) {
        d.setAttributeModifier(ForgeMod.SWIM_SPEED.get(), SWIM_BOOST_ID, (dd, u) -> 
            new AttributeModifier(u, "Swim Boost", 1.5*this.level, Operation.ADDITION)
        );
        d.setPathfindingMalus(PathNodeType.WATER_BORDER, 0);
        d.setPathfindingMalus(PathNodeType.WATER, 0);
        this.mv_ctrl = new SwimmerDogMovementController(d);
        this.navigator = new SwimmerPathNavigator(d, d.level);
        this.goal = new SwimmerDogGoal((DogEntity)d, this);
        d.goalSelector.addGoal(10, this.goal);
    }

    @Override
    public void remove(AbstractDogEntity d) {
        d.removeAttributeModifier(ForgeMod.SWIM_SPEED.get(), SWIM_BOOST_ID);
        d.setPathfindingMalus(PathNodeType.WATER_BORDER, 8);
        d.setPathfindingMalus(PathNodeType.WATER, 8);
        d.goalSelector.removeGoal(this.goal);
    }

    @Override
    public void livingTick(AbstractDogEntity dogIn) {
        if (this.level() >= 5 && dogIn.isVehicle() && dogIn.canBeControlledByRider()) {
            // canBeSteered checks entity is LivingEntity
            LivingEntity rider = (LivingEntity) dogIn.getControllingPassenger();
            if (rider.isInWater()) {
                rider.addEffect(new EffectInstance(Effects.NIGHT_VISION, 80, 1, true, false));
            }
        }
    }

    @Override
    public ActionResultType canBeRiddenInWater(AbstractDogEntity dogIn, Entity rider) {
        return this.level() >= 5 ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }

    @Override
    public ActionResultType canBreatheUnderwater(AbstractDogEntity dogIn) {
        return this.level() >= 5 ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }

    @Override
    public ActionResult<Integer> decreaseAirSupply(AbstractDogEntity dogIn, int air) {
        if (this.level() > 0 && dogIn.getRandom().nextInt(this.level() + 1) > 0) {
            return ActionResult.success(air);
        }

        return ActionResult.pass(air);
    }

    @Override
    public ActionResult<Integer> determineNextAir(AbstractDogEntity dogIn, int currentAir) {
        if (this.level() > 0) {
            return ActionResult.pass(currentAir + this.level());
        }

        return ActionResult.pass(currentAir);
    }

    /*
    @Override
    public ActionResult<MovementController> getMoveControl(AbstractDogEntity dogIn) {
        if (dogIn.isInWater() && !this.checkSurroundingForLand(dogIn, dogIn.blockPosition())) {
            if (this.mv_ctrl == null) {
                this.mv_ctrl = new SwimmerDogMovementController(dogIn);
            }
            return ActionResult.success(this.mv_ctrl);
        }
        return ActionResult.pass(null);
    }
    */

    @Override
    public ActionResultType canSwim(AbstractDogEntity dogIn) {
        return ActionResultType.SUCCESS;
    }

    /*

    @Override
    public ActionResult<PathNavigator> getNavigation(AbstractDogEntity dogIn) {
        if (dogIn.isInWater() && !this.checkSurroundingForLand(dogIn, dogIn.blockPosition())) {
            if (this.navigator == null) {
                this.navigator = new SwimmerPathNavigator(dogIn, dogIn.level);
            }
            return ActionResult.success(this.navigator);
        }
        return ActionResult.pass(null);
    }
    */

    @Override
    public ActionResultType isBlockSafe(AbstractDogEntity dogIn, BlockPos p) {
        if (dogIn.level.getFluidState(p).is(FluidTags.WATER) && dogIn.getAirSupply() >= 50) return ActionResultType.SUCCESS;
        return ActionResultType.PASS;
    }

    public static class SwimmerDogGoal extends Goal {

        private DogEntity dog;
        private SwimmerDogTalent talent;

        public SwimmerDogGoal(DogEntity d, SwimmerDogTalent t) {
            this.dog = d;
            this.talent = t; 
        }
        
        @Override
        public boolean canUse() {
            return this.dog.isInWater() && !this.checkSurroundingForLand(this.dog, this.dog.blockPosition());
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public void start() {
            this.dog.setNavigation(talent.navigator);
            this.dog.setMoveControl(talent.mv_ctrl);
        }

        @Override
        public void stop() {
            this.dog.resetMoveControl();
            this.dog.resetNavigation();
        }

        private boolean checkSurroundingForLand(DogEntity dogIn, BlockPos p) {
            for (BlockPos dp : BlockPos.betweenClosed(p.offset(-1, -1, -1), p.offset(1, 1, 1))) {
                PathNodeType pn = WalkNodeProcessor.getBlockPathTypeStatic(dogIn.level, dp.mutable());
                if (pn == PathNodeType.WALKABLE || pn == PathNodeType.WATER_BORDER) return true;
            }
            return false;
        }
        
    }

    

    static class SwimmerDogMovementController extends MovementController {
        private final AbstractDogEntity dog;
  
        public SwimmerDogMovementController(AbstractDogEntity d) {
           super(d);
           this.dog = d;
        }
        
        
        @Override
        public void tick() {
            //FLoat
            if (this.dog.isEyeInFluid(FluidTags.WATER)) {
              this.dog.setDeltaMovement(this.dog.getDeltaMovement().add(0.0D, 0.007D, 0.0D));
            }
           
           //When dog has target pos and wants to move to it
           if (this.operation == MovementController.Action.MOVE_TO && !this.dog.getNavigation().isDone()) {

                //get deltas
                double dx = this.wantedX - this.dog.getX();
                double dy = this.wantedY - this.dog.getY();
                double dz = this.wantedZ - this.dog.getZ();
                /*
                if (dy != 0.0D) {
                    double dl = (double)MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
                    this.dog.setDeltaMovement(this.dog.getDeltaMovement().add(0.0D, (double)this.dog.getSpeed() * (dy / dl) * 0.3D, 0.0D));
                }
                */

                // Set Move Direction for the XZ plane
                float yrot = (float)(MathHelper.atan2(dz, dx) * (double)(180F / (float)Math.PI)) - 90.0F;  
                this.dog.yRot = this.rotlerp(this.dog.yRot, yrot, 90.0F);
                this.dog.yBodyRot = this.dog.yRot;

                //Set Move Direction for the Y plane
                float ydl = MathHelper.sqrt(dx * dx + dz * dz);
                float xrot = (float)(MathHelper.atan2(dy, ydl) * (double)(180F / (float)Math.PI));
                this.dog.xRot = this.rotlerp(this.dog.xRot, xrot, 90.0f);

                //Set Move Speed
                float s = (float)(this.speedModifier * this.dog.getAttributeValue(Attributes.MOVEMENT_SPEED));
                this.dog.setSpeed(s);

                //Move with ratio of the dir vector
                this.dog.setYya(this.dog.getSpeed()*MathHelper.sin(this.dog.xRot*(((float)Math.PI)/180F)));
                this.dog.setZza(this.dog.getSpeed()*MathHelper.cos(this.dog.xRot*(((float)Math.PI)/180F)));

            } else {
                this.dog.setSpeed(0.0F);
                this.dog.setZza(0.0F);
                this.dog.setYya(0.0F);
            }        
        }
    }
}
