package doggytalents.client.screen.DogNewInfoScreen.element;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import doggytalents.client.screen.DogNewInfoScreen.store.slice.MainPanelSlice;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.MainPanelSlice.MainTab;
import doggytalents.client.screen.DogNewInfoScreen.widget.ModeSwitch;
import doggytalents.client.screen.DogNewInfoScreen.widget.TabPanelButton;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.lib.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;

public class MainButtonToolboxRowElement extends AbstractElement {

    private final int BUTTON_SPACING = 4;

    Font font;
    DogEntity dog;

    public MainButtonToolboxRowElement(AbstractElement parent, Screen screen, DogEntity dog) {
        super(parent, screen);
        this.font = Minecraft.getInstance().font;
        this.dog = dog;
    }

    @Override
    public AbstractElement init() {

        int totalWidth = 0;
        // TODO If state == MAIN then do this
        
        var modeButton = new ModeSwitch(0, this.getRealY(), 85, this.getSizeY(),
            this.dog, this.font, getScreen()
        );
        totalWidth += modeButton.getWidth() + BUTTON_SPACING;
        var editInfoButton = new TabPanelButton(0, this.getRealY(), 20, this.getSizeY(),
            getScreen(), false, Component.literal(""), 
            MainPanelSlice.class,
            MainTab.EDIT_INFO 
        ) {
            @Override
            public void render(PoseStack stack, int mouseX, int mouseY, float pTicks) {
                super.render(stack, mouseX, mouseY, pTicks);

                int mX = this.getX() + this.width/2;
                int mY = this.getY() + this.height/2;

                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
                RenderSystem.setShaderTexture(0, Resources.HAMBURGER);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                this.blit(stack, mX - 10, mY - 10, 0, 0, 20, 20);
            }
        };
        totalWidth += editInfoButton.getWidth();

        int mX = this.getRealX() + this.getSizeX()/2;
        int pX = mX - totalWidth/2;
        modeButton.setX(pX);
        pX += modeButton.getWidth() + BUTTON_SPACING;
        editInfoButton.setX(pX);

        this.addChildren(modeButton);
        this.addChildren(editInfoButton);
        return this;
    }

    @Override
    public void renderElement(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        
    }
    
}
