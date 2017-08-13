package doggytalents.base;

import java.util.Random;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value = Side.CLIENT)
public interface IClientMethods {
	
	public void renderLabelWithScale(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking, float scale);

	public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height);
	
	public void onModelBakeEvent(ModelBakeEvent event) throws Exception;

	public void drawSelectionBoundingBox(Object box, float red, float green, float blue, float alpha);
	
	public void setModel(Item item, int meta, String modelName);

	public void spawnCustomParticle(EntityPlayer player, Object pos, Random rand, float posX, float posY, float posZ, int numberOfParticles, float particleSpeed);
}
