package doggytalents.common.util;

import com.google.common.collect.AbstractIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class WorldUtil {

    public static Iterable<BlockPos> getAllInBoxMutable(int x1, int y1, int z1, int x2, int y2, int z2) {
        return () -> {
            return new AbstractIterator<BlockPos>() {
                final RadialCoordinateIterator coordinateIterator = new RadialCoordinateIterator(x1, y1, z1, x2, y2, z2);
                final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

                @Override
                protected BlockPos computeNext() {
                    return this.coordinateIterator.hasNext() ? this.mutablePos.set(this.coordinateIterator.getX(), this.coordinateIterator.getY(), this.coordinateIterator.getZ()) : this.endOfData();
                }
            };
        };
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T extends BlockEntity> T getTileEntity(BlockGetter worldIn, BlockPos posIn, Class<T> type) {
        BlockEntity tileEntity = worldIn.getBlockEntity(posIn);
        if (tileEntity != null && tileEntity.getClass().isAssignableFrom(type)) {
            return (T) tileEntity;
        }

        return null;
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    @Nullable
    public static <T extends Entity> T getCachedEntity(@Nullable Level worldIn, Class<T> type, @Nullable T cached, @Nullable UUID uuid) {
        if ((cached == null || cached.isRemoved()) && uuid != null && worldIn instanceof ServerLevel) {
            Entity entity = ((ServerLevel) worldIn).getEntity(uuid);
            if (entity != null && entity.getClass().isAssignableFrom(type)) {
                return (T) entity;
            } else {
                return null;
            }
        }

        return cached;
    }

    public static Optional<BlockPos> toImmutable(BlockPos pos) {
        return pos != null ? Optional.of(pos.immutable()) : Optional.empty();
    }

    public static Optional<BlockPos> toImmutable(Optional<BlockPos> pos) {
        return pos.map(BlockPos::immutable);
    }
}
