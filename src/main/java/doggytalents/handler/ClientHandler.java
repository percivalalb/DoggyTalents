package doggytalents.handler;

import java.util.List;

import scala.actors.threadpool.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import doggytalents.ModBlocks;
import doggytalents.client.renderer.block.DogBedModelTexture;
import doggytalents.helper.LogHelper;

/**
 * @author ProPercivalalb
 */
public class ClientHandler {

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
	    TextureAtlasSprite base = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/slime");

	    for(String thing : new String[] {"inventory", "facing=north", "facing=south", "facing=east", "facing=west"}) {
	    	IFlexibleBakedModel model = (IFlexibleBakedModel)event.modelRegistry.getObject(new ModelResourceLocation("doggytalents:dog_bed", thing));
	    	event.modelRegistry.putObject(new ModelResourceLocation("doggytalents:dog_bed", thing), new DogBedModelTexture.Builder(model, base, base).makeBakedModel());
	    }
	    
	}
	
	@SubscribeEvent
	public void registerTextures(TextureStitchEvent.Pre event) {
        TextureMap map = event.map;

	}
}
