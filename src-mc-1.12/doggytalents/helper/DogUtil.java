package doggytalents.helper;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class DogUtil {

	public static void teleportDogToOwner(Entity owner, Entity entity, World world, PathNavigate pathfinder) {
    	int i = MathHelper.floor(owner.posX) - 2;
        int j = MathHelper.floor(owner.posZ) - 2;
        int k = MathHelper.floor(owner.getEntityBoundingBox().minY);

        for(int l = 0; l <= 4; ++l) {
            for(int i1 = 0; i1 <= 4; ++i1) {
                if((l < 1 || i1 < 1 || l > 3 || i1 > 3) && DogUtil.isTeleportFriendlyBlock(entity, world, i, j, k, l, i1)) {
                	entity.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), entity.rotationYaw, entity.rotationPitch);
                    pathfinder.clearPathEntity();
                    return;
                }
            }
        }
    }
	
    public static boolean isTeleportFriendlyBlock(Entity entity, World world, int xBase, int zBase, int y, int xAdd, int zAdd) {
        BlockPos blockpos = new BlockPos(xBase + xAdd, y - 1, zBase + zAdd);
        IBlockState iblockstate = world.getBlockState(blockpos);
        return iblockstate.getBlockFaceShape(world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID && iblockstate.canEntitySpawn(entity) && world.isAirBlock(blockpos.up()) && world.isAirBlock(blockpos.up(2));
    }
}
