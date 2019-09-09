package doggytalents.entity.ai;

import doggytalents.ModBlocks;
import doggytalents.entity.EntityDog;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class EntityAISitOnBed extends MoveToBlockGoal {
    
    private final EntityDog dog;

    public EntityAISitOnBed(EntityDog dogIn, double speedIn) {
        super(dogIn, speedIn, 8);
        this.dog = dogIn;
    }

    @Override
    public boolean shouldExecute() {
        return this.dog.isTamed() && !this.dog.isSitting() && super.shouldExecute();
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        this.dog.getAISit().setSitting(false);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.dog.setSitting(false);
    }

    @Override
    public void tick() {
        super.tick();
        this.dog.getAISit().setSitting(false);
        if (!this.getIsAboveDestination()) {
            this.dog.setSitting(false);
        } else if (!this.dog.isSitting()) {
            this.dog.setSitting(true);
        }
    }

    @Override
    protected boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos) {
        if (!worldIn.isAirBlock(pos.up())) {
            return false;
        } else {
            BlockState blockstate = worldIn.getBlockState(pos);
            Block block = blockstate.getBlock();

            return block == ModBlocks.DOG_BED;
        }
    }
}