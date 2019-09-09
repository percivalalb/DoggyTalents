package doggytalents.lib;

import java.util.HashMap;

import net.minecraft.util.ResourceLocation;

/**
 * @author ProPercivalalb
 */
public class ResourceLib {
    
    public static final ResourceLocation GUI_FOOD_BOWL = getGuiTexturePath("food_bowl.png");
    public static final ResourceLocation GUI_PACK_PUPPY = getGuiTexturePath("pack_puppy.png");
    public static final ResourceLocation GUI_TREAT_BAG = getGuiTexturePath("treat_bag.png");
    
    public static HashMap<Integer, ResourceLocation> doggyTameSkins = new HashMap<>();
    public static HashMap<Integer, ResourceLocation> doggyFancyCollars = new HashMap<>();
    
    public static final ResourceLocation MOB_LAYER_CHEST = getMobTexturePath("doggy_chest.png");
    public static final ResourceLocation MOB_LAYER_SADDLE = getMobTexturePath("doggy_saddle.png");
    public static final ResourceLocation MOB_LAYER_WINGS = getMobTexturePath("doggy_wings.png");
    
    public static final ResourceLocation MOB_DOG_TAME = new ResourceLocation("textures/entity/wolf/wolf_tame.png");
    public static final ResourceLocation MOB_DOG_WILD = new ResourceLocation("textures/entity/wolf/wolf.png");
    
    public static final ResourceLocation MOB_LAYER_DOG_COLLAR = getMobTexturePath("doggy_collar.png");
    public static final ResourceLocation MOB_LAYER_DOG_HURT = getMobTexturePath("doggy_hurt.png");
    public static final ResourceLocation MOB_LAYER_RADIO_COLLAR = getMobTexturePath("doggy_radio_collar.png");
    public static final ResourceLocation MOB_LAYER_CAPE = getMobTexturePath("doggy_cape.png");
    public static final ResourceLocation MOB_LAYER_SUNGLASSES = getMobTexturePath("doggy_sunglasses.png");
    public static final ResourceLocation MOB_LAYER_SUNGLASSES_NIGHT = getMobTexturePath("doggy_sunglasses_night.png");
    public static final ResourceLocation MOB_LAYER_CAPE_COLOURED = getMobTexturePath("doggy_cape_coloured.png");
    public static final ResourceLocation MOB_LAYER_LEATHER_JACKET = getMobTexturePath("doggy_leather_jacket.png");
    public static final ResourceLocation MOB_LAYER_CAPE2_1 = getMobTexturePath("doggy_cape1_1.png");
    public static final ResourceLocation MOB_LAYER_ARMOR = getMobTexturePath("doggy_armor.png");
    
    public static ResourceLocation getTameSkin(int index) {
        //if(index == 0)
        //    return MOB_DOG_TAME;
        
        if(!doggyTameSkins.containsKey(index))
            doggyTameSkins.put(index, getMobTexturePath("dog/doggytex" + index + ".png"));
        return doggyTameSkins.get(index);
    }
    
    public static ResourceLocation getFancyCollar(int index) {
        if(!doggyFancyCollars.containsKey(index))
            doggyFancyCollars.put(index, getMobTexturePath("doggy_collar_" + index + ".png"));
        return doggyFancyCollars.get(index);
    }
    
     /**
     * Gets a local gui texture file path.
     * @param textureFileName The .png file that relates to the texture file. 
     * @return The whole path string including the given parameter.
     */
    public static ResourceLocation getGuiTexturePath(String textureFileName) {
        return get("textures/gui/" + textureFileName);
    }
    
    /**
     * Gets a local gui texture file path.
     * @param textureFileName The .png file that relates to the texture file. 
     * @return The whole path string including the given parameter.
     */
    public static ResourceLocation getMobTexturePath(String textureFileName) {
        return get("textures/mob/" + textureFileName);
    }
    
    public static ResourceLocation get(String resourcePathIn) {
        return new ResourceLocation(Reference.MOD_ID, resourcePathIn);
    }
}
