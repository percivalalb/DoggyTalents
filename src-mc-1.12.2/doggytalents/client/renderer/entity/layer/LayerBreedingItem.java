package doggytalents.client.renderer.entity.layer;

import doggytalents.client.model.entity.ModelDog;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.entity.EntityAbstractDog;
import doggytalents.entity.EntityDog;
import doggytalents.entity.ModeUtil;
import doggytalents.inventory.InventoryPackPuppy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerBreedingItem implements LayerRenderer<EntityDog> {
	
    protected final RenderDog dogRenderer;
    
    public LayerBreedingItem(RenderDog dogRendererIn) {
        this.dogRenderer = dogRendererIn;
    }
    
    private boolean breedingItems(Item item) {
		if(item == Items.WHEAT) return true;
		if(item instanceof ItemSeeds) return true;
		if(item == Items.GOLDEN_APPLE) return true;
		if(item == Items.GOLDEN_CARROT) return true;
		if(item == Items.CARROT) return true;
		if(item == Items.POTATO) return true;
		if(item == Item.getItemFromBlock(Blocks.YELLOW_FLOWER)) return true;
		if(item == Item.getItemFromBlock(Blocks.HAY_BLOCK)) return true;
		return false;
	}
    
    private ItemStack findBreedingItem(InventoryPackPuppy inventory)
    {
        for (int i = 0; i < inventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);

            if(!itemstack.isEmpty()) {
            	if(this.breedingItems(itemstack.getItem())) {
                    return itemstack;
            	}
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public void doRenderLayer(EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		InventoryPackPuppy inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");

    	if(!dog.isSitting() && dog.talents.getLevel("livestockcarer") > 0 && (dog.mode.isMode(ModeUtil.EnumMode.WANDERING) || dog.mode.isMode(ModeUtil.EnumMode.DOCILE))) {
	    	
	        GlStateManager.pushMatrix();
	
	        if (this.dogRenderer.getMainModel().isChild) {
	            float f = 0.5F;
	            GlStateManager.translate(0.0F, 0.75F, 0.0F);
	            GlStateManager.scale(0.5F, 0.5F, 0.5F);
	        }

            if(dog.isSneaking())
                GlStateManager.translate(0.0F, 0.2F, 0.0F);

            ((ModelDog)this.dogRenderer.getMainModel()).wolfHeadMain.postRender(0.0625F);
            GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(45.0F, 0.0F, 0.0F, 1.0F);

            GlStateManager.translate(0.20, -0.10, -0.10);
            Minecraft.getMinecraft().getItemRenderer().renderItem(dog, this.findBreedingItem(inventory), ItemCameraTransforms.TransformType.NONE);
	        GlStateManager.popMatrix();
    	}
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}