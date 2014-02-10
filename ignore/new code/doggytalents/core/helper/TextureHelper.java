package doggytalents.core.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.RenderEngine;

import org.lwjgl.opengl.GL11;

/**
 * @author ProPercivalalb
 */
public class TextureHelper {

	private static Minecraft mc = Minecraft.getMinecraft();
	
	/**
	 * Gets and texture from a URL with a local fall back texture 
	 * @param imageUrl The URL of the target image file
	 * @param fallBack The local fall back texture
	 */
    public static void bindTextureByURL(String imageUrl, String fallBack) {
    	RenderEngine renderengine = mc.renderEngine;
    	if (!renderengine.hasImageData(imageUrl)) {
    		renderengine.obtainImageData(imageUrl, new ImageBufferDownload());
    	}
        if (renderengine != null) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderengine.getTextureForDownloadableImage(imageUrl, fallBack));
        }
        renderengine.resetBoundTexture();
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
     * Gets the location of the mods textures.
     * @return The default texture local
     */
	private static String getOverrideTexturesPath() {
	    return "/mods/doggytalents/textures";
	}
}
