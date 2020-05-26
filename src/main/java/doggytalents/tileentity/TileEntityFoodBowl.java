package doggytalents.tileentity;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import doggytalents.ModTileEntities;
import doggytalents.entity.EntityDog;
import doggytalents.helper.CapabilityHelper;
import doggytalents.helper.DogUtil;
import doggytalents.inventory.container.ContainerFoodBowl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityFoodBowl extends TileEntity implements INamedContainerProvider, ITickableTileEntity {

    private LazyOptional<ItemStackHandler> itemStackHandler = LazyOptional.of(() -> new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            TileEntityFoodBowl.this.markDirty();
        }
    });


    public int timeoutCounter;

    public TileEntityFoodBowl() {
        super(ModTileEntities.FOOD_BOWL.get());
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.itemStackHandler.ifPresent(h -> h.deserializeNBT(compound));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        this.itemStackHandler.ifPresent(h -> compound.merge(h.serializeNBT()));
        return compound;
    }

    @Override
    public void tick() {

        //Only run update code every 5 ticks (0.25s)
        if(++this.timeoutCounter < 5) { return; }

        List<EntityDog> dogList = this.world.getEntitiesWithinAABB(EntityDog.class, new AxisAlignedBB(this.pos).grow(5, 5, 5));
        IItemHandler inventory = CapabilityHelper.getOrThrow(this, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);


        for(EntityDog dog : dogList) {
            //TODO make dog bowl remember who placed and only their dogs can attach to the bowl
            if(!dog.COORDS.hasBowlPos()) {
                dog.COORDS.setBowlPos(this.pos);
            }

            if(dog.getDogHunger() < 60) {
                DogUtil.feedDogFrom(dog, inventory);
            }
        }

        this.timeoutCounter = 0;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.itemStackHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.doggytalents.food_bowl");
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerIn) {
        return new ContainerFoodBowl(windowId, this.world, this.pos, playerInventory, playerIn);
    }
}
