package doggytalents.common.block.tileentity;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import doggytalents.DoggyTileEntityTypes;
import doggytalents.api.feature.FoodHandler;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.inventory.container.FoodBowlContainer;
import doggytalents.common.util.InventoryUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class FoodBowlTileEntity extends PlacedTileEntity implements INamedContainerProvider, ITickableTileEntity {

    private final ItemStackHandler inventory = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            // When contents change mark needs save to disc
            FoodBowlTileEntity.this.markDirty();
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return FoodHandler.isFood(stack).isPresent();
        }
    };
    private final LazyOptional<ItemStackHandler> itemStackHandler = LazyOptional.of(() -> this.inventory);


    public int timeoutCounter;

    public FoodBowlTileEntity() {
        super(DoggyTileEntityTypes.FOOD_BOWL.get());
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.inventory.deserializeNBT(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.merge(this.inventory.serializeNBT());
        return compound;
    }

    @Override
    public void tick() {

        //Only run update code every 5 ticks (0.25s)
        if(++this.timeoutCounter < 5) { return; }

        List<DogEntity> dogList = this.world.getEntitiesWithinAABB(DogEntity.class, new AxisAlignedBB(this.pos).grow(5, 5, 5));

        for(DogEntity dog : dogList) {
            //TODO make dog bowl remember who placed and only their dogs can attach to the bowl
            UUID placerId = this.getPlacerId();
            if(placerId != null && placerId.equals(dog.getOwnerId()) && !dog.getBowlPos().isPresent()) {
                dog.setBowlPos(this.pos);
            }

            if(dog.getDogHunger() < dog.getMaxHunger() / 2) {
               InventoryUtil.feedDogFrom(dog, null, this.inventory);
            }
        }

        this.timeoutCounter = 0;
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
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.doggytalents.food_bowl");
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerIn) {
        return new FoodBowlContainer(windowId, this.world, this.pos, playerInventory, playerIn);
    }
}
