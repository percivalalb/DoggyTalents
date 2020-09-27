package doggytalents.common.data;

import java.nio.file.Path;
import java.util.function.Consumer;

import com.google.gson.JsonObject;

import doggytalents.DoggyBlocks;
import doggytalents.DoggyItems;
import doggytalents.DoggyRecipeSerializers;
import doggytalents.common.util.Util;
import net.minecraft.block.Blocks;
import net.minecraft.data.CustomRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;

public class DTRecipeProvider extends RecipeProvider {

    public DTRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public String getName() {
        return "DoggyTalents Recipes";
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        //TODO
        ShapedRecipeBuilder.shapedRecipe(DoggyItems.THROW_BONE.get()).patternLine(" X ").patternLine("XYX").patternLine(" X ").key('X', Items.BONE).key('Y', Items.SLIME_BALL).addCriterion("has_bone", this.hasItem(Items.BONE)).build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(DoggyItems.THROW_BONE.get()).addIngredient(DoggyItems.THROW_BONE_WET.get(), 1).addCriterion("has_throw_bone", this.hasItem(DoggyItems.THROW_BONE.get())).build(consumer, Util.getResource("throw_bone_wet"));
        ShapedRecipeBuilder.shapedRecipe(DoggyItems.THROW_STICK.get(), 1).patternLine(" X ").patternLine("XYX").patternLine(" X ").key('X', Items.STICK).key('Y', Items.SLIME_BALL).addCriterion("has_slime_ball", this.hasItem(Items.SLIME_BALL)).build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(DoggyItems.THROW_STICK.get(), 1).addIngredient(DoggyItems.THROW_STICK_WET.get(), 1).addCriterion("has_throw_stick", this.hasItem(DoggyItems.THROW_STICK.get())).build(consumer, Util.getResource("throw_stick_wet"));
        ShapelessRecipeBuilder.shapelessRecipe(DoggyItems.SUPER_TREAT.get(), 5).addIngredient(DoggyItems.TRAINING_TREAT.get(), 5).addIngredient(Items.GOLDEN_APPLE, 1).addCriterion("has_golden_apple", this.hasItem(Items.GOLDEN_APPLE)).build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(DoggyItems.DIRE_TREAT.get(), 1).addIngredient(DoggyItems.MASTER_TREAT.get(), 5).addIngredient(Blocks.END_STONE, 1).addCriterion("has_master_treat", this.hasItem(DoggyItems.MASTER_TREAT.get())).build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(DoggyItems.BREEDING_BONE.get(), 2).addIngredient(DoggyItems.MASTER_TREAT.get(), 1).addIngredient(Items.COOKED_BEEF, 1).addIngredient(Items.COOKED_PORKCHOP, 1).addIngredient(Items.COOKED_CHICKEN, 1).addIngredient(Items.COOKED_COD, 1).addCriterion("has_cooked_porkchop", this.hasItem(Items.COOKED_PORKCHOP)).build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(DoggyItems.MASTER_TREAT.get(), 5).addIngredient(DoggyItems.SUPER_TREAT.get(), 5).addIngredient(Items.DIAMOND, 1).addCriterion("has_master_treat", this.hasItem(DoggyItems.SUPER_TREAT.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(DoggyItems.TRAINING_TREAT.get(), 1).patternLine("TUV").patternLine("XXX").patternLine("YYY").key('T', Items.STRING).key('U', Items.BONE).key('V', Items.GUNPOWDER).key('X', Items.SUGAR).key('Y', Items.WHEAT).addCriterion("has_wheat", this.hasItem(Items.WHEAT)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(DoggyItems.COLLAR_SHEARS.get(), 1).patternLine(" X ").patternLine("XYX").patternLine(" X ").key('X', Items.BONE).key('Y', Items.SHEARS).addCriterion("has_shears", this.hasItem(Items.SHEARS)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(DoggyItems.WHISTLE.get(), 1).patternLine("IRI").patternLine("II ").key('I', Items.IRON_INGOT).key('R', Items.REDSTONE).addCriterion("has_iron_ingot", this.hasItem(Items.IRON_INGOT)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(DoggyBlocks.FOOD_BOWL.get(), 1).patternLine("XXX").patternLine("XYX").patternLine("XXX").key('X', Items.IRON_INGOT).key('Y', Items.BONE).addCriterion("has_iron_ingot", this.hasItem(Items.IRON_INGOT)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(DoggyBlocks.DOG_BATH.get(), 1).patternLine("XXX").patternLine("XYX").patternLine("XXX").key('X', Items.IRON_INGOT).key('Y', Items.WATER_BUCKET).addCriterion("has_iron_ingot", this.hasItem(Items.IRON_INGOT)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(DoggyItems.CHEW_STICK.get(), 1).patternLine("SW").patternLine("WS").key('W', Items.WHEAT).key('S', Items.SUGAR).addCriterion("has_sugar", this.hasItem(Items.SUGAR)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(DoggyItems.WOOL_COLLAR.get(), 1).patternLine("SSS").patternLine("S S").patternLine("SSS").key('S', Items.STRING).addCriterion("has_stick", this.hasItem(Items.STRING)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(DoggyItems.TREAT_BAG.get(), 1).patternLine("LCL").patternLine("LLL").key('L', Items.LEATHER).key('C', DoggyItems.CHEW_STICK.get()).addCriterion("has_leather", this.hasItem(Items.LEATHER)).build(consumer);

        ShapedRecipeBuilder.shapedRecipe(DoggyItems.CAPE.get(), 1).patternLine("S S").patternLine("LWL").patternLine("WLW").key('L', Items.LEATHER).key('S', Items.STRING).key('W', ItemTags.WOOL).addCriterion("has_leather", this.hasItem(Items.LEATHER)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(DoggyItems.CAPE_COLOURED.get(), 1).patternLine("S S").patternLine("LLL").patternLine("LLL").key('L', Items.LEATHER).key('S', Items.STRING).addCriterion("has_leather", this.hasItem(Items.LEATHER)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(DoggyItems.GUARD_SUIT.get(), 1).patternLine("S S").patternLine("BWB").patternLine("BWB").key('S', Items.STRING).key('W', Blocks.WHITE_WOOL).key('B', Blocks.BLACK_WOOL).addCriterion("has_string", this.hasItem(Items.STRING)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(DoggyItems.LEATHER_JACKET.get(), 1).patternLine("L L").patternLine("LWL").patternLine("LWL").key('L', Items.LEATHER).key('W', ItemTags.WOOL).addCriterion("has_leather", this.hasItem(Items.LEATHER)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(DoggyItems.SPOTTED_COLLAR.get(), 1).patternLine("BWB").patternLine("WCW").patternLine("BSB").key('C', DoggyItems.WOOL_COLLAR.get()).key('B', Items.BLACK_DYE).key('W', Items.WHITE_DYE).key('S', Items.STRING).addCriterion("has_wool_collar", this.hasItem(DoggyItems.WOOL_COLLAR.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(DoggyItems.SPOTTED_COLLAR.get(), 1).patternLine("WBW").patternLine("BCB").patternLine("WSW").key('C', DoggyItems.WOOL_COLLAR.get()).key('B', Items.BLACK_DYE).key('W', Items.WHITE_DYE).key('S', Items.STRING).addCriterion("has_wool_collar", this.hasItem(DoggyItems.WOOL_COLLAR.get())).build(consumer, Util.getResource("spotted_collar_alt"));
        ShapelessRecipeBuilder.shapelessRecipe(DoggyItems.MULTICOLOURED_COLLAR.get(), 1).addIngredient(DoggyItems.WOOL_COLLAR.get()).addIngredient(Items.STRING).addIngredient(Items.BLUE_DYE).addIngredient(Items.LIME_DYE).addIngredient(Items.YELLOW_DYE).addIngredient(Items.ORANGE_DYE).addIngredient(Items.RED_DYE).addIngredient(Items.PURPLE_DYE).addCriterion("has_wool_collar", this.hasItem(DoggyItems.WOOL_COLLAR.get())).build(consumer);

        ShapedRecipeBuilder.shapedRecipe(DoggyItems.SUNGLASSES.get(), 1).patternLine("S S").patternLine("GSG").key('S', Items.STICK).key('G', Blocks.GLASS_PANE).addCriterion("has_stick", this.hasItem(Items.STICK)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(DoggyItems.TINY_BONE.get(), 1).patternLine("BI").patternLine("IB").key('B', Items.BONE).key('I', Items.IRON_INGOT).addCriterion("has_iron_ingot", this.hasItem(Items.IRON_INGOT)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(DoggyItems.BIG_BONE.get(), 1).patternLine("BI").patternLine("IB").patternLine("BI").key('B', Items.BONE).key('I', Items.IRON_INGOT).addCriterion("has_iron_ingot", this.hasItem(Items.IRON_INGOT)).build(consumer);

        ShapedRecipeBuilder.shapedRecipe(DoggyItems.RADIO_COLLAR.get(), 1).patternLine("XX").patternLine("YX").key('X', Items.IRON_INGOT).key('Y', Items.REDSTONE).addCriterion("has_redstone", this.hasItem(Items.REDSTONE)).build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(DoggyItems.RADAR.get(), 1).addIngredient(Items.MAP, 1).addIngredient(Items.REDSTONE, 1).addIngredient(DoggyItems.RADIO_COLLAR.get(), 1).addCriterion("has_redstone", this.hasItem(Items.REDSTONE)).build(consumer);

        CustomRecipeBuilder.customRecipe(DoggyRecipeSerializers.DOG_BED.get()).build(consumer, Util.getResourcePath("dog_bed"));
    }

    @Override
    protected void saveRecipeAdvancement(DirectoryCache cache, JsonObject advancementJson, Path pathIn) {
        //NOOP - We dont replace any of the advancement things yet...
    }
}
