package doggytalents.lib;

import java.util.Hashtable;

import net.minecraft.util.ResourceLocation;

/**
 * @author ProPercivalalb
 */
public class ResourceReference {
	
	public static final ResourceLocation foodBowl = new ResourceLocation("doggytalents", getGuiTexturePath("food_bowl.png"));
	public static final ResourceLocation packPuppy = new ResourceLocation("doggytalents", getGuiTexturePath("pack_puppy.png"));
	
	public static Hashtable<Integer, ResourceLocation> doggyTameSkins = new Hashtable<Integer, ResourceLocation>();
	
	public static final ResourceLocation doggyHurt = new ResourceLocation("doggytalents", getMobTexturePath("doggy_hurt.png"));
	public static final ResourceLocation doggyWild = new ResourceLocation("doggytalents", getMobTexturePath("doggywild.png"));
	public static final ResourceLocation doggyRadioCollar = new ResourceLocation("doggytalents", getMobTexturePath("doggy_radio_collar.png"));
	
	public static ResourceLocation getTameSkin(int index) {
		if(!doggyTameSkins.containsKey(index))
			doggyTameSkins.put(index, new ResourceLocation("doggytalents", getMobTexturePath("dog/doggytex" + index + ".png")));
		return doggyTameSkins.get(index);
	}
	
	 /**
     * Gets a local gui texture file path.
     * @param textureFileName The .png file that relates to the texture file. 
     * @return The whole path string including the given parameter.
     */
    public static String getGuiTexturePath(String textureFileName) {
	    return String.format("%s/gui/%s", new Object[] {getOverrideTexturesPath(), textureFileName});
	}
	
    /**
     * Gets a local gui texture file path.
     * @param textureFileName The .png file that relates to the texture file. 
     * @return The whole path string including the given parameter.
     */
    public static String getMobTexturePath(String textureFileName) {
	    return String.format("%s/mob/%s", new Object[] {getOverrideTexturesPath(), textureFileName});
	}
	
    
    /**
     * Gets the location of the mods textures.
     * @return The default texture local
     */
	private static String getOverrideTexturesPath() {
	    return "textures";
	}
}
