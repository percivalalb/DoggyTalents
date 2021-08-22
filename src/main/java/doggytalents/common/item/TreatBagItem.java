package doggytalents.common.item;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.inferface.IDogFoodHandler;
import doggytalents.common.Screens;
import doggytalents.common.inventory.TreatBagItemHandler;
import doggytalents.common.util.Cache;
import doggytalents.common.util.InventoryUtil;
import doggytalents.common.util.ItemUtil;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.EmptyHandler;

import net.minecraft.world.item.Item.Properties;

public class TreatBagItem extends Item implements IDogFoodHandler {

    private Cache<String> contentsTranslationKey = Cache.make(() -> this.getDescriptionId() + ".contents");

    public TreatBagItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);

        if (worldIn.isClientSide) {
            return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, stack);
        }
        else {
            if (playerIn instanceof ServerPlayer && !(playerIn instanceof FakePlayer)) {
                ServerPlayer serverPlayer = (ServerPlayer) playerIn;

                Screens.openTreatBagScreen(serverPlayer, stack, playerIn.inventory.selected);
            }

            return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, stack);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        IItemHandler bagInventory = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(EmptyHandler.INSTANCE);
        List<ItemStack> condensedContents = ItemUtil.getContentOverview(bagInventory);

        condensedContents.forEach((food) -> {
            tooltip.add(new TranslatableComponent(this.contentsTranslationKey.get(), food.getCount(), new TranslatableComponent(food.getDescriptionId())));
        });
    }

    @Override
    public ICapabilityProvider initCapabilities(final ItemStack stack, CompoundTag nbt) {
        // https://github.com/MinecraftForge/MinecraftForge/issues/5989
        if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == null) {
            return null;
        }

        return new ICapabilityProvider() {
            final LazyOptional<IItemHandler> itemHandlerInstance = LazyOptional.of(() -> new TreatBagItemHandler(stack));

            @Override
            @Nonnull
            public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, final @Nullable Direction side) {
                if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                    return (LazyOptional<T>) this.itemHandlerInstance;
                }
                return LazyOptional.empty();
            }
        };
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !ItemStack.isSame(oldStack, newStack);
    }

    @Override
    public boolean isFood(ItemStack stackIn) {
        return false;
    }

    @Override
    public boolean canConsume(AbstractDogEntity dogIn, ItemStack stackIn, Entity entityIn) {
        return entityIn instanceof LivingEntity ? dogIn.canInteract((LivingEntity) entityIn) : false;
    }

    @Override
    public InteractionResult consume(AbstractDogEntity dogIn, ItemStack stackIn, Entity entityIn) {
        IItemHandlerModifiable treatBag = (IItemHandlerModifiable) stackIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(EmptyHandler.INSTANCE);
        return InventoryUtil.feedDogFrom(dogIn, entityIn, treatBag);
    }
}
