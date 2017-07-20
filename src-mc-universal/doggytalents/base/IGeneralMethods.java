package doggytalents.base;

import doggytalents.entity.EntityDog;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IGeneralMethods {
	
	public boolean isTeleportFriendlyBlock(Entity entity, World world, int xBase, int zBase, int y, int xAdd, int zAdd);
	
	public float getBrightness(EntityDog dog, float partialTicks);
	
	public int getColour(EnumDyeColor dyeColor);
	public float[] getRGB(EnumDyeColor dyeColor);
	
	public void registerCraftingRecipes();
	
	public void registerEntity(Class<? extends Entity> entityClass, ResourceLocation entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates);
	
	public void registerBlock(Object registry, Block block);
	public void registerItem(Object registry, Item item);
	
	@SideOnly(value = Side.CLIENT)
	public void renderLabelWithScale(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking, float scale);

	@SideOnly(value = Side.CLIENT)
	public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height);
	
	@SideOnly(value = Side.CLIENT)
	public void onModelBakeEvent(ModelBakeEvent event) throws Exception;

	@SideOnly(value = Side.CLIENT)
	public void drawSelectionBoundingBox(AxisAlignedBB box, float red, float green, float blue, float alpha);

}
