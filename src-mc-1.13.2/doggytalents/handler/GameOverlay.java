package doggytalents.handler;

import doggytalents.entity.EntityDog;
import doggytalents.lib.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author ProPercivalalb
 **/
@OnlyIn(Dist.CLIENT)
public class GameOverlay {

	private static Minecraft mc = Minecraft.getInstance();
	
	@SubscribeEvent
	public void onPreRenderGameOverlay(RenderGameOverlayEvent.Post event) {
		if(event.getType() == RenderGameOverlayEvent.ElementType.HEALTHMOUNT && mc.player != null && mc.player.getRidingEntity() instanceof EntityDog) {
			EntityDog dog = (EntityDog)mc.player.getRidingEntity();
			int width = Minecraft.getInstance().mainWindow.getScaledWidth();
			int height = Minecraft.getInstance().mainWindow.getScaledHeight();
			GlStateManager.pushMatrix();
			mc.getTextureManager().bindTexture(Gui.ICONS);
			
			GlStateManager.enableBlend();
	        int left = width / 2 + 91;
	        int top = height - GuiIngameForge.right_height;
	        GuiIngameForge.right_height += 10;
	        int level = MathHelper.ceil(((double)dog.getDogHunger() / (double)Constants.HUNGER_POINTS) * 20.0D);

	        for (int i = 0; i < 10; ++i) {
	            int idx = i * 2 + 1;
	            int x = left - i * 8 - 9;
	            int y = top;
	            int icon = 16;
	            byte backgound = 12;

	            mc.ingameGUI.drawTexturedModalRect(x, y, 16 + backgound * 9, 27, 9, 9);


	            if (idx < level)
	            	mc.ingameGUI.drawTexturedModalRect(x, y, icon + 36, 27, 9, 9);
	            else if (idx == level)
	            	mc.ingameGUI.drawTexturedModalRect(x, y, icon + 45, 27, 9, 9);
	        }
	        GlStateManager.disableBlend();

	        GlStateManager.enableBlend();
	        left = width / 2 + 91;
	        top = height - GuiIngameForge.right_height;
	        GlStateManager.color4f(1.0F, 1.0F, 0.0F, 1.0F);
	        int l6 = dog.getAir();
	        int j7 = dog.getMaxAir();
	        
	        if(dog.areEyesInFluid(FluidTags.WATER) || l6 < j7) {
	            int air = dog.getAir();
	            int full = MathHelper.ceil((double)(air - 2) * 10.0D / 300.0D);
	            int partial = MathHelper.ceil((double)air * 10.0D / 300.0D) - full;

	            for (int i = 0; i < full + partial; ++i) {
	            	mc.ingameGUI.drawTexturedModalRect(left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9);
	            }
	            GuiIngameForge.right_height += 10;
	        }
	        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	        GlStateManager.disableBlend();
	        
	        GlStateManager.popMatrix();
		}
	}
}