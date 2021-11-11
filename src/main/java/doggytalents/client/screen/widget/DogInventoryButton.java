package doggytalents.client.screen.widget;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import doggytalents.common.entity.DogEntity;
import doggytalents.common.lib.Resources;
import doggytalents.common.talent.PackPuppyTalent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import net.minecraft.client.gui.widget.button.Button.IPressable;

public class DogInventoryButton extends Button {

    private Screen parent;
    private int baseX;

    public DogInventoryButton(int x, int y, Screen parentIn, IPressable onPress) {
        super(x, y, 13, 10, new StringTextComponent(""), onPress);
        this.baseX = x;
        this.parent = parentIn;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {

        if (this.parent instanceof CreativeScreen) {
            int tabIndex = ((CreativeScreen) this.parent).getSelectedTab();
            this.visible = tabIndex == ItemGroup.TAB_INVENTORY.getId();
            this.active = this.visible;
        }

        if (this.parent instanceof InventoryScreen) {
            RecipeBookGui recipeBook = ((InventoryScreen) this.parent).getRecipeBookComponent();
            if (recipeBook.isVisible()) {
                this.x = this.baseX + 77;
            } else {
                this.x = this.baseX;
            }
        }

        if (this.visible) {
            Minecraft mc = Minecraft.getInstance();
            List<DogEntity> dogs = mc.level.getEntitiesOfClass(DogEntity.class, mc.player.getBoundingBox().inflate(12D, 12D, 12D), 
                d -> ( d.canInteract(mc.player) && PackPuppyTalent.hasInventory(d) )
            );
            this.active = !dogs.isEmpty();
        }

        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
       Minecraft mc = Minecraft.getInstance();
       mc.getTextureManager().bind(Resources.SMALL_WIDGETS);
       RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
       int i = this.getYImage(this.isHovered());
       RenderSystem.enableBlend();
       RenderSystem.defaultBlendFunc();
       RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
       this.blit(stack, this.x, this.y, 0, 36 + i * 10, this.width, this.height);
       this.renderBg(stack, mc, mouseX, mouseY);
    }

    @Override
    public void renderToolTip(MatrixStack stack, int mouseX, int mouseY) {
        if (this.active) {
            ITextComponent msg = new TranslationTextComponent("container.doggytalents.dog_inventories.link");
            this.parent.renderTooltip(stack, msg, mouseX, mouseY);
        } else {
            ITextComponent msg = new TranslationTextComponent("container.doggytalents.dog_inventories.link").withStyle(TextFormatting.RED);
            this.parent.renderTooltip(stack, msg, mouseX, mouseY);
        }
    }
}
