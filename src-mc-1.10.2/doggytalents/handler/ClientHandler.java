package doggytalents.handler;

import doggytalents.client.renderer.block.DogBedModelTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ProPercivalalb
 */
public class ClientHandler {

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
	    TextureAtlasSprite base = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/slime");

	    for(String thing : new String[] {"inventory", "facing=north", "facing=south", "facing=east", "facing=west"}) {
	    	//TODO IFlexibleBakedModel model = (IFlexibleBakedModel)event.getModelRegistry().getObject(new ModelResourceLocation("doggytalents:dog_bed", thing));
	    	//event.getModelRegistry().putObject(new ModelResourceLocation("doggytalents:dog_bed", thing), new DogBedModelTexture.Builder(model, base, base).makeBakedModel());
	    }
	    
	}
	
	@SubscribeEvent
	public void registerTextures(TextureStitchEvent.Pre event) {
     

	}
}
