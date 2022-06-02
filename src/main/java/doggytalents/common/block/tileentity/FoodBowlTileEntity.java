package doggytalents.common.block.tileentity;

import doggytalents.DoggyTileEntityTypes;
import doggytalents.api.feature.FoodHandler;
import doggytalents.common.entity.Dog;
import doggytalents.common.inventory.container.FoodBowlContainer;
import doggytalents.common.util.InventoryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class FoodBowlTileEntity extends PlacedTileEntity implements MenuProvider {

    private final ItemStackHandler inventory = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            // When contents change mark needs save to disc
            FoodBowlTileEntity.this.setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return FoodHandler.isFood(stack).isPresent();
        }
    };
    private final LazyOptional<ItemStackHandler> itemStackHandler = LazyOptional.of(() -> this.inventory);


    public int timeoutCounter;

    public FoodBowlTileEntity(BlockPos pos, BlockState blockState) {
        super(DoggyTileEntityTypes.FOOD_BOWL.get(), pos, blockState);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.inventory.deserializeNBT(compound);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.merge(this.inventory.serializeNBT());
    }

    public static void tick(Level level, BlockPos pos, BlockState blockState, BlockEntity blockEntity) {
        if (!(blockEntity instanceof FoodBowlTileEntity bowl)) {
            return;
        }

        //Only run update code every 5 ticks (0.25s)
        if (++bowl.timeoutCounter < 5) { return; }

        List<Dog> dogList = bowl.level.getEntitiesOfClass(Dog.class, new AABB(pos).inflate(5, 5, 5));

        for (Dog dog : dogList) {
            //TODO make dog bowl remember who placed and only their dogs can attach to the bowl
            UUID placerId = bowl.getPlacerId();
            if (placerId != null && placerId.equals(dog.getOwnerUUID()) && !dog.getBowlPos().isPresent()) {
                dog.setBowlPos(bowl.worldPosition);
            }

            if (dog.getDogHunger() < dog.getMaxHunger() / 2) {
               InventoryUtil.feedDogFrom(dog, null, bowl.inventory);
            }
        }

        bowl.timeoutCounter = 0;
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (LazyOptional<T>) this.itemStackHandler;
        }
        return super.getCapability(cap, side);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container.doggytalents.food_bowl");
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerIn) {
        return new FoodBowlContainer(windowId, this.level, this.worldPosition, playerInventory, playerIn);
    }
}
