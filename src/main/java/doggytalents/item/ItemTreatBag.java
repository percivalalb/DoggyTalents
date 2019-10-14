package doggytalents.item;

import java.util.List;

import javax.annotation.Nullable;

import doggytalents.DoggyTalents;
import doggytalents.inventory.InventoryTreatBag;
import doggytalents.lib.GuiNames;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTreatBag extends ItemDT {

    public ItemTreatBag() {
        super();
        this.setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if(worldIn.isRemote) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        else {
            playerIn.openGui(DoggyTalents.INSTANCE, GuiNames.GUI_ID_FOOD_BAG, worldIn, playerIn.inventory.currentItem, 0, 0);
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        
        InventoryTreatBag inventory = new InventoryTreatBag(-1, stack);
        inventory.loadInventoryFromNBT();
        List<ItemStack> condensedContents = inventory.getContentOverview();
        
        condensedContents.forEach(food -> {
            tooltip.add(new TextComponentTranslation(this.getTranslationKey() + ".contents", food.getCount(), food.getDisplayName()).getFormattedText());
        });
    }
    
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !ItemStack.areItemsEqual(oldStack, newStack);
    } 
    
}
