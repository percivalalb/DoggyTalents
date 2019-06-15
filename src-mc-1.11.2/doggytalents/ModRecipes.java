package doggytalents;

import doggytalents.inventory.recipe.RecipeDogBed;
import doggytalents.inventory.recipe.RecipeDogCape;
import doggytalents.inventory.recipe.RecipeDogCollar;
import doggytalents.lib.Reference;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;

public class ModRecipes {
	
	public static void onRegister() {
		GameRegistry.addRecipe(new RecipeDogBed());
		GameRegistry.addRecipe(new RecipeDogCollar());
		GameRegistry.addRecipe(new RecipeDogCape());
		
		RecipeSorter.register("doggytalents:dog_bed", RecipeDogBed.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped before:minecraft:shapeless");
        RecipeSorter.register("doggytalents:collar_colouring", RecipeDogCollar.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
        RecipeSorter.register("doggytalents:cape_colouring", RecipeDogCape.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
        
        GameRegistry.addRecipe(new ItemStack(ModItems.THROW_BONE, 1), new Object[] {" X ", "XYX", " X ", 'X', Items.BONE, 'Y', Items.SLIME_BALL});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.THROW_BONE, 1), new Object[] {new ItemStack(ModItems.THROW_BONE_WET, 1)});
        GameRegistry.addRecipe(new ItemStack(ModItems.THROW_STICK, 1), new Object[] {" X ", "XYX", " X ", 'X', Items.STICK, 'Y', Items.SLIME_BALL});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.THROW_STICK, 1), new Object[] {new ItemStack(ModItems.THROW_STICK_WET, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.SUPER_TREAT, 5), new Object[] { new ItemStack(ModItems.TRAINING_TREAT, 1), new ItemStack(ModItems.TRAINING_TREAT, 1), new ItemStack(ModItems.TRAINING_TREAT, 1), new ItemStack(ModItems.TRAINING_TREAT, 1), new ItemStack(ModItems.TRAINING_TREAT, 1), new ItemStack(Items.GOLDEN_APPLE, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.DIRE_TREAT, 1), new Object[] {new ItemStack(ModItems.MASTER_TREAT, 1), new ItemStack(ModItems.MASTER_TREAT, 1), new ItemStack(ModItems.MASTER_TREAT, 1), new ItemStack(ModItems.MASTER_TREAT, 1), new ItemStack(ModItems.MASTER_TREAT, 1), new ItemStack(Blocks.END_STONE, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.BREEDING_BONE, 2), new Object[] {new ItemStack(ModItems.MASTER_TREAT, 1), new ItemStack(Items.COOKED_BEEF, 1), new ItemStack(Items.COOKED_PORKCHOP, 1), new ItemStack(Items.COOKED_CHICKEN, 1), new ItemStack(Items.COOKED_FISH, 1)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.MASTER_TREAT, 5), new Object[] {new ItemStack(ModItems.SUPER_TREAT, 1), new ItemStack(ModItems.SUPER_TREAT, 1), new ItemStack(ModItems.SUPER_TREAT, 1), new ItemStack(ModItems.SUPER_TREAT, 1), new ItemStack(ModItems.SUPER_TREAT, 1), new ItemStack(Items.DIAMOND, 1)});
        GameRegistry.addRecipe(new ItemStack(ModItems.TRAINING_TREAT, 1), new Object[] {"TUV", "XXX", "YYY", 'T', Items.STRING, 'U', Items.BONE, 'V', Items.GUNPOWDER, 'X', Items.SUGAR, 'Y', Items.WHEAT});
        GameRegistry.addRecipe(new ItemStack(ModItems.COLLAR_SHEARS, 1), new Object[] {" X ", "XYX", " X ", 'X', Items.BONE, 'Y', Items.SHEARS});
        GameRegistry.addRecipe(new ItemStack(ModItems.WHISTLE, 1), new Object[] {"IRI", "II ", 'I', Items.IRON_INGOT, 'R', Items.REDSTONE});
        GameRegistry.addRecipe(new ItemStack(ModBlocks.FOOD_BOWL, 1), new Object[] {"XXX", "XYX", "XXX", 'X', Items.IRON_INGOT, 'Y', Items.BONE});
        GameRegistry.addRecipe(new ItemStack(ModBlocks.DOG_BATH, 1), new Object[] {"XXX", "XYX", "XXX", 'X', Items.IRON_INGOT, 'Y', Items.WATER_BUCKET});
        GameRegistry.addRecipe(new ItemStack(ModItems.CHEW_STICK, 1), new Object[] {"SW", "WS", 'W', Items.WHEAT, 'S', Items.SUGAR});
        GameRegistry.addRecipe(new ItemStack(ModItems.WOOL_COLLAR, 1), new Object[] {"SSS", "S S", "SSS", 'S', Items.STRING});
        GameRegistry.addRecipe(new ItemStack(ModItems.TREAT_BAG, 1), new Object[] {"LCL", "LLL", 'L', Items.LEATHER, 'C', ModItems.CHEW_STICK});
        
        GameRegistry.addRecipe(new ItemStack(ModItems.CAPE, 1), new Object[] {"S S", "LWL", "WLW", 'L', Items.LEATHER, 'S', Items.STRING, 'W', Blocks.WOOL});
        GameRegistry.addRecipe(new ItemStack(ModItems.CAPE_COLOURED, 1), new Object[] {"S S", "LLL", "LLL", 'L', Items.LEATHER, 'S', Items.STRING});
        GameRegistry.addRecipe(new ItemStack(ModItems.LEATHER_JACKET, 1), new Object[] {"L L", "LWL", "LWL", 'L', Items.LEATHER, 'W', new ItemStack(Blocks.WOOL)});
        GameRegistry.addRecipe(new ItemStack(ModItems.SPOTTED_COLLAR, 1), new Object[] {"BWB", "WCW", "BSB", 'C', new ItemStack(ModItems.WOOL_COLLAR), 'B', new ItemStack(Items.DYE, 1, 0), 'W', new ItemStack(Items.DYE, 1, 15), 'S', new ItemStack(Items.STRING)});
        GameRegistry.addRecipe(new ItemStack(ModItems.SPOTTED_COLLAR, 1), new Object[] {"WBW", "BCB", "WSW", 'C', new ItemStack(ModItems.WOOL_COLLAR), 'B', new ItemStack(Items.DYE, 1, 0), 'W', new ItemStack(Items.DYE, 1, 15), 'S', new ItemStack(Items.STRING)});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.MULTICOLOURED_COLLAR, 1), new Object[] {new ItemStack(ModItems.WOOL_COLLAR), new ItemStack(Items.STRING), new ItemStack(Items.DYE, 1, 4), new ItemStack(Items.DYE, 1, 10), new ItemStack(Items.DYE, 1, 11), new ItemStack(Items.DYE, 1, 14), new ItemStack(Items.DYE, 1, 1), new ItemStack(Items.DYE, 1, 5)});
       
        GameRegistry.addRecipe(new ItemStack(ModItems.SUNGLASSES, 1), new Object[] {"S S", "GSG", 'S', Items.STICK, 'G', new ItemStack(Blocks.GLASS_PANE)});
        GameRegistry.addRecipe(new ItemStack(ModItems.TINY_BONE, 1), new Object[] {"BI", "IB", 'B', Items.BONE, 'I', Items.IRON_INGOT});
        GameRegistry.addRecipe(new ItemStack(ModItems.BIG_BONE, 1), new Object[] {"BI", "IB", "BI", 'B', Items.BONE, 'I', Items.IRON_INGOT});
        
        GameRegistry.addRecipe(new ItemStack(ModItems.RADIO_COLLAR, 1), new Object[] {"XX", "YX", 'X', Items.IRON_INGOT, 'Y', Items.REDSTONE});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.RADAR, 1), new Object[] {new ItemStack(Items.MAP, 1), new ItemStack(Items.REDSTONE, 1), new ItemStack(ModItems.RADIO_COLLAR, 1)});
	}
}
