package doggytalents.base;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value = Side.CLIENT)
public interface IClientMethods {
	
	public void renderLabelWithScale(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking, float scale);

	public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height);
	
	public void onModelBakeEvent(ModelBakeEvent event) throws Exception;

	public void drawSelectionBoundingBox(AxisAlignedBB box, float red, float green, float blue, float alpha);
}
