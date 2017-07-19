package doggytalents.base.fallback;

import doggytalents.ModBlocks;
import doggytalents.ModItems;
import doggytalents.base.IGeneralMethods;
import doggytalents.entity.EntityDog;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GeneralMethods implements IGeneralMethods {

	@Override
	public boolean isTeleportFriendlyBlock(Entity entity, World world, int xBase, int zBase, int y, int xAdd, int zAdd) {
		return true;
	}

	@Override
	public float getBrightness(EntityDog dog, float partialTicks) {
		return 0;
	}

	@Override
	public int getColour(EnumDyeColor dyeColor) {
		return 0;
	}
	
	@Override
	public void registerEntity(Class<? extends Entity> entityClass, ResourceLocation entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		
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
	
        //TODO CraftingManager.getInstance().addRecipe(new RecipeDogBed());
        //TODO CraftingManager.getInstance().addRecipe(new RecipeDogCollar());
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void drawScreen(GuiContainer guiContainer, int mouseX, int mouseY, boolean before) {
		
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void renderLabelWithScale(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking, float scale) {
		
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
		
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void onModelBakeEvent(ModelBakeEvent event) throws Exception {
		
	}

}
