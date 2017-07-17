package doggytalents.base;

import doggytalents.entity.EntityDog;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IGeneralMethods {
	
	public boolean isTeleportFriendlyBlock(Entity entity, World world, int xBase, int zBase, int y, int xAdd, int zAdd);
	
	public float getBrightness(EntityDog dog, float partialTicks);
	
	public int getColour(EnumDyeColor colour);
	
	@SideOnly(value = Side.CLIENT)
	public void drawScreen(GuiContainer guiContainer, int mouseX, int mouseY, boolean before);
	
	@SideOnly(value = Side.CLIENT)
	public void renderLabelWithScale(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking, float scale);

	@SideOnly(value = Side.CLIENT)
	public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height);
	
	@SideOnly(value = Side.CLIENT)
	public void onModelBakeEvent(ModelBakeEvent event) throws Exception;
}
