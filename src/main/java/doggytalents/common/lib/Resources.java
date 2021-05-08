package doggytalents.common.lib;

import doggytalents.common.util.Util;
import net.minecraft.util.ResourceLocation;

public class Resources {

    public static final ResourceLocation GUI_FOOD_BOWL = getGui("food_bowl");
    public static final ResourceLocation GUI_PACK_PUPPY = getGui("pack_puppy");
    public static final ResourceLocation GUI_TREAT_BAG = getGui("treat_bag");
    public static final ResourceLocation GUI_RADAR = getGui("radar");

    public static final ResourceLocation DOG_INVENTORY = getGui("dog_inventory");
    public static final ResourceLocation INVENTORY_BUTTON = getGui("dog_button");
    public static final ResourceLocation SMALL_WIDGETS = getGui("small_widgets");

    // Vanilla wolf
    public static final ResourceLocation ENTITY_WOLF = Util.getResource(Constants.VANILLA_ID, "textures/entity/wolf/wolf_tame.png");
    public static final ResourceLocation ENTITY_WOLF_WILD = Util.getResource(Constants.VANILLA_ID, "textures/entity/wolf/wolf.png");
    public static final ResourceLocation ENTITY_WOLF_ANGRY = Util.getResource(Constants.VANILLA_ID, "textures/entity/wolf/wolf_angry.png");
    public static final ResourceLocation ENTITY_WOLF_COLLAR = Util.getResource(Constants.VANILLA_ID, "textures/entity/wolf/wolf_collar.png");

    public static final ResourceLocation COLLAR_DEFAULT = getEntity("dog", "doggy_collar");
    public static final ResourceLocation COLLAR_GOLDEN = getEntity("dog", "doggy_collar_0");
    public static final ResourceLocation COLLAR_SPOTTED = getEntity("dog", "doggy_collar_1");
    public static final ResourceLocation COLLAR_MULTICOLORED = getEntity("dog", "doggy_collar_2");
    public static final ResourceLocation CLOTHING_LEATHER_JACKET = getEntity("dog", "doggy_leather_jacket");
    public static final ResourceLocation GUARD_SUIT = getEntity("dog", "doggy_guard_suit");
    public static final ResourceLocation GLASSES_SUNGLASSES = getEntity("dog", "doggy_sunglasses");
    public static final ResourceLocation BOW_TIE = getEntity("dog", "doggy_bowtie");
    public static final ResourceLocation CAPE = getEntity("dog", "doggy_cape");
    public static final ResourceLocation DYEABLE_CAPE = getEntity("dog", "doggy_cape1");
    public static final ResourceLocation RADIO_BAND = getEntity("dog", "doggy_radio_collar");
    public static final ResourceLocation TALENT_RESCUE = getEntity("dog/talents", "rescue");
    public static final ResourceLocation TALENT_CHEST = getEntity("dog", "doggy_chest");
    public static final ResourceLocation IRON_HELMET = getEntity("dog", "armor/iron_helmet");
    public static final ResourceLocation DIAMOND_HELMET = getEntity("dog", "armor/diamond_helmet");
    public static final ResourceLocation GOLDEN_HELMET = getEntity("dog", "armor/golden_helmet");
    public static final ResourceLocation CHAINMAIL_HELMET = getEntity("dog", "armor/chainmail_helmet");
    public static final ResourceLocation LEATHER_HELMET = getEntity("dog", "armor/leather_helmet");
    public static final ResourceLocation TURTLE_HELMET = getEntity("dog", "armor/turtle_helmet");
    public static final ResourceLocation NETHERITE_HELMET = getEntity("dog", "armor/netherite_helmet");

    public static final ResourceLocation IRON_BOOTS = getEntity("dog", "armor/iron_boots");
    public static final ResourceLocation DIAMOND_BOOTS = getEntity("dog", "armor/diamond_boots");
    public static final ResourceLocation GOLDEN_BOOTS = getEntity("dog", "armor/golden_boots");
    public static final ResourceLocation CHAINMAIL_BOOTS = getEntity("dog", "armor/chainmail_boots");
    public static final ResourceLocation LEATHER_BOOTS = getEntity("dog", "armor/leather_boots");
    public static final ResourceLocation NETHERITE_BOOTS = getEntity("dog", "armor/netherite_boots");

    public static final ResourceLocation IRON_BODY_PIECE = getEntity("dog", "armor/iron_body");
    public static final ResourceLocation DIAMOND_BODY_PIECE = getEntity("dog", "armor/diamond_body");
    public static final ResourceLocation GOLDEN_BODY_PIECE = getEntity("dog", "armor/golden_body");
    public static final ResourceLocation CHAINMAIL_BODY_PIECE = getEntity("dog", "armor/chainmail_body");
    public static final ResourceLocation LEATHER_BODY_PIECE = getEntity("dog", "armor/leather_body");
    public static final ResourceLocation NETHERITE_BODY_PIECE = getEntity("dog", "armor/netherite_body");

    public static ResourceLocation getEntity(String type, String textureFileName) {
        return Util.getResource("textures/entity/" + type + "/" + textureFileName + ".png");
    }

    public static ResourceLocation getGui(String textureFileName) {
        return Util.getResource("textures/gui/" + textureFileName + ".png");
    }
}
