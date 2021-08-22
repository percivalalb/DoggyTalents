package doggytalents.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import doggytalents.common.lib.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class RadarScreen extends Screen {

    private PlayerEntity player;

    protected RadarScreen(PlayerEntity playerIn) {
        super(new TranslationTextComponent("doggytalents.screen.radar.title"));
        this.player = playerIn;
    }

    public static void open(PlayerEntity player) {
        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(new RadarScreen(player));
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(Resources.GUI_RADAR);
        int xSize = 210;
        int ySize = 210;
        int x = (this.width - xSize) / 2;
        int y = (this.height - ySize) / 2;
        this.blit(stack, x, y, 0, 0, xSize, ySize);
    }
}
