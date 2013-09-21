package doggytalents.lib;

import net.minecraft.util.ResourceLocation;

/**
 * @author ProPercivalalb
 */
public class ResourceReference {
	
	public static final ResourceLocation foodBowl = new ResourceLocation("doggytalents", getGuiTexturePath("foodBowl.png"));
	public static final ResourceLocation packPuppy = new ResourceLocation("doggytalents", getGuiTexturePath("guiPackPuppy.png"));
	
	public static final ResourceLocation doggyTame0 = new ResourceLocation("doggytalents", getMobTexturePath("doggytex0.png"));
	public static final ResourceLocation doggyTame1 = new ResourceLocation("doggytalents", getMobTexturePath("doggytex1.png"));
	public static final ResourceLocation doggyTame2 = new ResourceLocation("doggytalents", getMobTexturePath("doggytex2.png"));
	public static final ResourceLocation doggyTame3 = new ResourceLocation("doggytalents", getMobTexturePath("doggytex3.png"));
	public static final ResourceLocation doggyTame4 = new ResourceLocation("doggytalents", getMobTexturePath("doggytex4.png"));
	public static final ResourceLocation doggyTame5 = new ResourceLocation("doggytalents", getMobTexturePath("doggytex5.png"));
	public static final ResourceLocation doggyTame6 = new ResourceLocation("doggytalents", getMobTexturePath("doggytex6.png"));
	public static final ResourceLocation doggyTame7 = new ResourceLocation("doggytalents", getMobTexturePath("doggytex7.png"));
	public static final ResourceLocation doggyTame8 = new ResourceLocation("doggytalents", getMobTexturePath("doggytex8.png"));
	public static final ResourceLocation doggyTame9 = new ResourceLocation("doggytalents", getMobTexturePath("doggytex9.png"));
	public static final ResourceLocation doggyTame10 = new ResourceLocation("doggytalents", getMobTexturePath("doggytex10.png"));
	public static final ResourceLocation doggyTame11 = new ResourceLocation("doggytalents", getMobTexturePath("doggytex11.png"));
	public static final ResourceLocation doggyTame12 = new ResourceLocation("doggytalents", getMobTexturePath("doggytex12.png"));
	public static final ResourceLocation doggyTame13 = new ResourceLocation("doggytalents", getMobTexturePath("doggytex13.png"));
	public static final ResourceLocation doggyTame14 = new ResourceLocation("doggytalents", getMobTexturePath("doggytex14.png"));
	public static final ResourceLocation doggyTame15 = new ResourceLocation("doggytalents", getMobTexturePath("doggytex15.png"));
	public static final ResourceLocation doggyTame16 = new ResourceLocation("doggytalents", getMobTexturePath("doggytex16.png"));
	
	public static final ResourceLocation doggyHurt = new ResourceLocation("doggytalents", getMobTexturePath("doggy_hurt.png"));
	public static final ResourceLocation doggyWild = new ResourceLocation("doggytalents", getMobTexturePath("doggywild.png"));
    public static final ResourceLocation doggyAngry = new ResourceLocation("textures/entity/wolf/wolf_angry.png");
	
	
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
