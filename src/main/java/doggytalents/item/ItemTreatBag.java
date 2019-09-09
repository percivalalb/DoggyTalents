package doggytalents.item;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import doggytalents.helper.CapabilityHelper;
import doggytalents.inventory.TreatBagItemHandler;
import doggytalents.inventory.container.ContainerTreatBag;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemTreatBag extends Item {

    @CapabilityInject(ItemStackHandler.class)
    public static Capability<ItemStackHandler> TREAT_BAG_CAPABILITY = null;
    
    public ItemTreatBag(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if(worldIn.isRemote) {
            return new ActionResult<ItemStack>(ActionResultType.PASS, itemstack);
        }
        else {
            int slotId = playerIn.inventory.currentItem;

            if(playerIn instanceof ServerPlayerEntity && !(playerIn instanceof FakePlayer)) {
                ServerPlayerEntity entityPlayerMP = (ServerPlayerEntity)playerIn;
                NetworkHooks.openGui(entityPlayerMP, new INamedContainerProvider() {
                    @Override
                    public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
                        return new ContainerTreatBag(windowId, inventory, slotId, itemstack);
                    }

                    @Override
                    public ITextComponent getDisplayName() {
                        return new TranslationTextComponent("container.doggytalents.treat_bag");
                    }
                }, buf -> {
                    buf.writeVarInt(slotId);
                    buf.writeItemStack(itemstack);
                });
            }
            
            return new ActionResult<ItemStack>(ActionResultType.PASS, itemstack);
        }
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        
        ItemStackHandler bagInventory = CapabilityHelper.getOrThrow(stack, TREAT_BAG_CAPABILITY);
        List<ItemStack> condensedContents = this.getContentOverview(bagInventory);
        
        condensedContents.forEach(food -> {
            tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".contents", food.getCount(), new TranslationTextComponent(food.getTranslationKey())));
        });
    }
    
    public List<ItemStack> getContentOverview(ItemStackHandler bagInventory) {
        List<ItemStack> items = new ArrayList<>();
        
        for(int i = 0; i < bagInventory.getSlots(); ++i) {
            ItemStack itemstack = bagInventory.getStackInSlot(i).copy();

            if(!itemstack.isEmpty()) {
                boolean found = false;
                
                for(int j = 0; j < items.size(); j++) {
                    ItemStack stack = items.get(j);
                    if(ItemStack.areItemsEqual(stack, itemstack)) {
                        stack.grow(itemstack.getCount());
                        found = true;
                    }
                }
                
                if(!found) {
                    items.add(itemstack);
                }
            }
        }
        return items;
    }
    
    @Override
    public ICapabilityProvider initCapabilities(final ItemStack stack, CompoundNBT nbt) {
        return new ICapabilityProvider() {
            final IItemHandler itemHandler = new TreatBagItemHandler(stack);

            final LazyOptional<IItemHandler> itemHandlerInstance = LazyOptional.of(() -> itemHandler);

            @Override
            @Nonnull
            public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, final @Nullable Direction side) {
                if (cap == ItemTreatBag.TREAT_BAG_CAPABILITY)
                    return this.itemHandlerInstance.cast();
                return LazyOptional.empty();
            }
        };
    }
    
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !ItemStack.areItemsEqual(oldStack, newStack);
    } 
}
