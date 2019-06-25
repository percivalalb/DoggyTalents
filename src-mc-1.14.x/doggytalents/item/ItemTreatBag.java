package doggytalents.item;

import java.util.List;

import javax.annotation.Nullable;

import doggytalents.inventory.InventoryTreatBag;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkHooks;

public class ItemTreatBag extends Item {

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
            INamedContainerProvider bagInventory = new InventoryTreatBag(slotId, itemstack);
            
            if(bagInventory != null) {
                if(playerIn instanceof ServerPlayerEntity && !(playerIn instanceof FakePlayer)) {
                    ServerPlayerEntity entityPlayerMP = (ServerPlayerEntity)playerIn;
                    NetworkHooks.openGui(entityPlayerMP, bagInventory, buf -> buf.writeByte(slotId));
                }
            }
            
            return new ActionResult<ItemStack>(ActionResultType.PASS, itemstack);
        }
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        
        InventoryTreatBag inventory = new InventoryTreatBag(-1, stack);
        inventory.loadInventoryFromNBT();
        List<ItemStack> condensedContents = inventory.getContentOverview();
        
        condensedContents.forEach(food -> {
            tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".contents", food.getCount(), new TranslationTextComponent(food.getTranslationKey())));
        });
    }
    
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !ItemStack.areItemsEqual(oldStack, newStack);
    } 
}
