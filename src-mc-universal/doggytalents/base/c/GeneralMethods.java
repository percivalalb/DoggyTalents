package doggytalents.base.c;

import doggytalents.ModBlocks;
import doggytalents.ModItems;
import doggytalents.base.IGeneralMethods;
import doggytalents.entity.EntityDog;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 1.11.2 Code
 */
public class GeneralMethods implements IGeneralMethods {

	@Override
	public boolean isTeleportFriendlyBlock(Entity entity, World world, int xBase, int zBase, int y, int xAdd, int zAdd) {
		return world.getBlockState(new BlockPos(xBase + xAdd, y - 1, zBase + zAdd)).isTopSolid() && this.isEmptyBlock(world, new BlockPos(xBase + xAdd, y, zBase + zAdd)) && this.isEmptyBlock(world, new BlockPos(xBase + xAdd, y + 1, zBase + zAdd));
	}

	private boolean isEmptyBlock(World world, BlockPos pos) {
        IBlockState iblockstate = world.getBlockState(pos);
        return iblockstate.getMaterial() == Material.AIR ? true : !iblockstate.isFullCube();
    }
	
	@Override
	public float getBrightness(EntityDog dog, float partialTicks) {
		return dog.getBrightness(partialTicks);
	}
	
	@Override
	public int getColour(EnumDyeColor colour) {
		return colour.getMapColor().colorValue;
	}
	
	@Override
	public float[] getRGB(EnumDyeColor dyeColor) {
		return EntitySheep.getDyeRgb(dyeColor);
	}
	
	@Override
	public void registerCraftingRecipes() {
		GameRegistry.addRecipe(new ItemStack(ModItems.THROW_BONE, 1), new Object[] {" X ", "XYX", " X ", 'X', Items.BONE, 'Y', Items.SLIME_BALL});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.THROW_BONE, 1, 0), new Object[] {new ItemStack(ModItems.THROW_BONE, 1, 0)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.SUPER_TREAT, 5), new Object[] { new ItemStack(ModItems.TRAINING_TREAT, 1), new ItemStack(ModItems.TRAINING_TREAT, 1), new ItemStack(ModItems.TRAINING_TREAT, 1), new ItemStack(ModItems.TRAINING_TREAT, 1), new ItemStack(ModItems.TRAINING_TREAT, 1), new ItemStack(Items.GOLDEN_APPLE, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.DIRE_TREAT, 1), new Object[] {new ItemStack(ModItems.MASTER_TREAT, 1), new ItemStack(ModItems.MASTER_TREAT, 1), new ItemStack(ModItems.MASTER_TREAT, 1), new ItemStack(ModItems.MASTER_TREAT, 1), new ItemStack(ModItems.MASTER_TREAT, 1), new ItemStack(Blocks.END_STONE, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.BREEDING_BONE, 2), new Object[] {new ItemStack(ModItems.MASTER_TREAT, 1), new ItemStack(Items.COOKED_BEEF, 1), new ItemStack(Items.COOKED_PORKCHOP, 1), new ItemStack(Items.COOKED_CHICKEN, 1), new ItemStack(Items.COOKED_FISH, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.MASTER_TREAT, 5), new Object[] {new ItemStack(ModItems.SUPER_TREAT, 1), new ItemStack(ModItems.SUPER_TREAT, 1), new ItemStack(ModItems.SUPER_TREAT, 1), new ItemStack(ModItems.SUPER_TREAT, 1), new ItemStack(ModItems.SUPER_TREAT, 1), new ItemStack(Items.DIAMOND, 1)});
        GameRegistry.addRecipe(new ItemStack(ModItems.TRAINING_TREAT, 1), new Object[] {"TUV", "XXX", "YYY", 'T', Items.STRING, 'U', Items.BONE, 'V', Items.GUNPOWDER, 'X', Items.SUGAR, 'Y', Items.WHEAT});
        GameRegistry.addRecipe(new ItemStack(ModItems.COLLAR_SHEARS, 1), new Object[] {" X ", "XYX", " X ", 'X', Items.BONE, 'Y', Items.SHEARS});
        GameRegistry.addRecipe(new ItemStack(ModItems.COMMAND_EMBLEM, 1), new Object[] {" X ", "XYX", " X ", 'X', Items.GOLD_INGOT, 'Y', Items.BOW});
        GameRegistry.addRecipe(new ItemStack(ModBlocks.FOOD_BOWL, 1), new Object[] {"XXX", "XYX", "XXX", 'X', Items.IRON_INGOT, 'Y', Items.BONE});
        GameRegistry.addRecipe(new ItemStack(ModBlocks.DOG_BATH, 1), new Object[] {"XXX", "XYX", "XXX", 'X', Items.IRON_INGOT, 'Y', Items.WATER_BUCKET});
        GameRegistry.addRecipe(new ItemStack(ModItems.CHEW_STICK, 1), new Object[] {"SW", "WS", 'W', Items.WHEAT, 'S', Items.SUGAR});
        
        GameRegistry.addRecipe(new ItemStack(ModItems.RADIO_COLLAR, 1), new Object[] {"XX", "YX", 'X', Items.IRON_INGOT, 'Y', Items.REDSTONE});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.RADAR, 1), new Object[] {new ItemStack(Items.MAP, 1), new ItemStack(Items.REDSTONE, 1), new ItemStack(ModItems.RADIO_COLLAR, 1)});
	
        CraftingManager.getInstance().addRecipe(new RecipeDogBed());
        CraftingManager.getInstance().addRecipe(new RecipeDogCollar());
	}
	
	@Override
	public void registerEntity(Class<? extends Entity> entityClass, ResourceLocation entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		EntityRegistry.registerModEntity(entityName, entityClass, entityName.toString(), id, mod, trackingRange, updateFrequency, sendsVelocityUpdates);
	}
	
	@Override
	public void registerBlock(IForgeRegistry<Block> registry, Block block) {
		registry.register(block);
	}
	
	@Override
	public void registerItem(IForgeRegistry<Item> registry, Item item) {
		registry.register(item);
	}
	
	@SideOnly(value = Side.CLIENT)
	public void drawScreen(GuiContainer guiContainer, int mouseX, int mouseY, boolean before) {
		
	}
	
	@Override
	@SideOnly(value = Side.CLIENT)
	public void renderLabelWithScale(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float)(isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);

        if (!isSneaking)
            GlStateManager.disableDepth();

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        int i = fontRendererIn.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexBuffer = tessellator.getBuffer();
        vertexBuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vertexBuffer.pos((double)(-i - 1), (double)(-1 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        vertexBuffer.pos((double)(-i - 1), (double)(8 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        vertexBuffer.pos((double)(i + 1), (double)(8 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        vertexBuffer.pos((double)(i + 1), (double)(-1 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();

        if (!isSneaking) {
            fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, 553648127);
            GlStateManager.enableDepth();
        }

        GlStateManager.depthMask(true);
        fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, isSneaking ? 553648127 : -1);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
	
	@Override
	@SideOnly(value = Side.CLIENT)
	public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
		double zLevel = 0.0D;
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexBuffer = tessellator.getBuffer();
		vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		vertexBuffer.pos((double)(x + 0), (double)(y + height), (double)zLevel).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
		vertexBuffer.pos((double)(x + width), (double)(y + height), (double)zLevel).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
		vertexBuffer.pos((double)(x + width), (double)(y + 0), (double)zLevel).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
		vertexBuffer.pos((double)(x + 0), (double)(y + 0), (double)zLevel).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
		tessellator.draw();
	}
	
	@Override
	@SideOnly(value = Side.CLIENT)
	public void onModelBakeEvent(ModelBakeEvent event) throws Exception {
		IModel baseModel = ModelLoaderRegistry.getModel(new ResourceLocation("doggytalents:block/dog_bed"));
    	
    	for(String thing : new String[] {"inventory", "facing=north", "facing=south", "facing=east", "facing=west"}) {
	    	ModelResourceLocation modelVariantLocation = new ModelResourceLocation("doggytalents:dog_bed", thing);

	        if(baseModel instanceof IRetexturableModel) {
	        	IBakedModel variantModel = event.getModelRegistry().getObject(modelVariantLocation);

	        	if(variantModel instanceof IPerspectiveAwareModel) {

	        		IBakedModel customModel = new DogBedModel((IPerspectiveAwareModel)variantModel, (IRetexturableModel)baseModel, DefaultVertexFormats.BLOCK);

	        		event.getModelRegistry().putObject(modelVariantLocation, customModel);
	        	}
	        }
	    }
	}

	@Override
	public void drawSelectionBoundingBox(AxisAlignedBB box, float red, float green, float blue, float alpha) {
		RenderGlobal.drawSelectionBoundingBox(box, red, green, blue, alpha);
	}
}
