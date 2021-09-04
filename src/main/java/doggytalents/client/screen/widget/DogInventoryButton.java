package doggytalents.client.screen.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.lib.Resources;
import doggytalents.common.talent.PackPuppyTalent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.CreativeModeTab;

import java.util.List;

public class DogInventoryButton extends Button {

    private Screen parent;
    private int baseX;

    public DogInventoryButton(int x, int y, Screen parentIn, OnPress onPress) {
        super(x, y, 13, 10, new TextComponent(""), onPress);
        this.baseX = x;
        this.parent = parentIn;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {

        if (this.parent instanceof CreativeModeInventoryScreen) {
            int tabIndex = ((CreativeModeInventoryScreen) this.parent).getSelectedTab();
            this.visible = tabIndex == CreativeModeTab.TAB_INVENTORY.getId();
            this.active = this.visible;
        }

        if (this.parent instanceof InventoryScreen) {
            RecipeBookComponent recipeBook = ((InventoryScreen) this.parent).getRecipeBookComponent();
            if (recipeBook.isVisible()) {
                this.x = this.baseX + 77;
            } else {
                this.x = this.baseX;
            }
        }

        if (this.visible) {
            Minecraft mc = Minecraft.getInstance();
            List<DogEntity> dogs = mc.level.getEntitiesOfClass(DogEntity.class, mc.player.getBoundingBox().inflate(12D, 12D, 12D), PackPuppyTalent::hasInventory);
            this.active = !dogs.isEmpty();
        }

        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderButton(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.setShaderTexture(0, Resources.SMALL_WIDGETS);
       Minecraft mc = Minecraft.getInstance();
       int i = this.getYImage(this.isHovered());
       RenderSystem.enableBlend();
       RenderSystem.defaultBlendFunc();
       RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
       this.blit(stack, this.x, this.y, 0, 36 + i * 10, this.width, this.height);
       this.renderBg(stack, mc, mouseX, mouseY);
    }

    @Override
    public void renderToolTip(PoseStack stack, int mouseX, int mouseY) {
        if (this.active) {
            Component msg = new TranslatableComponent("container.doggytalents.dog_inventories.link");
            this.parent.renderTooltip(stack, msg, mouseX, mouseY);
        } else {
            Component msg = new TranslatableComponent("container.doggytalents.dog_inventories.link").withStyle(ChatFormatting.RED);
            this.parent.renderTooltip(stack, msg, mouseX, mouseY);
        }
    }
}
