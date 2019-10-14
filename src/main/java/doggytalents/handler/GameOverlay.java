package doggytalents.handler;

import doggytalents.client.renderer.RenderUtil;
import doggytalents.entity.EntityDog;
import doggytalents.lib.ConfigValues;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 **/
@SideOnly(value = Side.CLIENT)
public class GameOverlay {

    private static Minecraft mc = Minecraft.getMinecraft();
    
    @SubscribeEvent
    public void onPreRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        ScaledResolution resolution = event.getResolution();
        if(event.getType() == RenderGameOverlayEvent.ElementType.HEALTHMOUNT && mc.player != null && mc.player.getRidingEntity() instanceof EntityDog) {
            EntityDog dog = (EntityDog)mc.player.getRidingEntity();
            int width = resolution.getScaledWidth();
            int height = resolution.getScaledHeight();
            GlStateManager.pushMatrix();
            mc.renderEngine.bindTexture(Gui.ICONS);
            
            GlStateManager.enableBlend();
            int left = width / 2 + 91;
            int top = height - GuiIngameForge.right_height;
            GuiIngameForge.right_height += 10;
            int level = MathHelper.ceil(((double)dog.getDogHunger() / (double)ConfigValues.HUNGER_POINTS) * 20.0D);

            for (int i = 0; i < 10; ++i) {
                int idx = i * 2 + 1;
                int x = left - i * 8 - 9;
                int y = top;
                int icon = 16;
                byte backgound = 12;

                RenderUtil.drawTexturedModalRect(x, y, 16 + backgound * 9, 27, 9, 9);


                if (idx < level)
                    RenderUtil.drawTexturedModalRect(x, y, icon + 36, 27, 9, 9);
                else if (idx == level)
                    RenderUtil.drawTexturedModalRect(x, y, icon + 45, 27, 9, 9);
            }
            GlStateManager.disableBlend();

            GlStateManager.enableBlend();
            left = width / 2 + 91;
            top = height - GuiIngameForge.right_height;
            GlStateManager.color(1.0F, 1.0F, 0.0F, 1.0F);
            if (dog.isInsideOfMaterial(Material.WATER)) {
                int air = dog.getAir();
                int full = MathHelper.ceil((double)(air - 2) * 10.0D / 300.0D);
                int partial = MathHelper.ceil((double)air * 10.0D / 300.0D) - full;

                for (int i = 0; i < full + partial; ++i) {
                    RenderUtil.drawTexturedModalRect(left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9);
                }
                GuiIngameForge.right_height += 10;
            }
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
            
            GlStateManager.popMatrix();
        }
    }
}