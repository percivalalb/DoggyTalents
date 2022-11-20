package doggytalents.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import doggytalents.common.lib.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class RadarScreen extends Screen {

    private Player player;

    protected RadarScreen(Player playerIn) {
        super(Component.translatable("doggytalents.screen.radar.title"));
        this.player = playerIn;
    }

    public static void open(Player player) {
        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(new RadarScreen(player));
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, Resources.GUI_RADAR);
        int xSize = 210;
        int ySize = 210;
        int x = (this.width - xSize) / 2;
        int y = (this.height - ySize) / 2;
        this.blit(stack, x, y, 0, 0, xSize, ySize);
    }
}
