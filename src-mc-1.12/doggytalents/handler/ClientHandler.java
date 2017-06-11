package doggytalents.handler;
import doggytalents.client.renderer.block.DogBedModel;
import doggytalents.helper.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ProPercivalalb
 */
public class ClientHandler {

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
	    IModel baseModel;
	    
	    try {
	    	baseModel = ModelLoaderRegistry.getModel(new ResourceLocation("doggytalents:block/dog_bed"));
	    	
	    	for(String thing : new String[] {"inventory", "facing=north", "facing=south", "facing=east", "facing=west"}) {
		    	ModelResourceLocation modelVariantLocation = new ModelResourceLocation("doggytalents:dog_bed", thing);

		        if(baseModel instanceof IRetexturableModel) {
		        	IBakedModel variantModel = event.getModelRegistry().getObject(modelVariantLocation);

		        	if(variantModel instanceof IPerspectiveAwareModel) {

		        		IBakedModel customModel = new DogBedModel((IPerspectiveAwareModel)variantModel, (IRetexturableModel)baseModel, DefaultVertexFormats.BLOCK);

		        		event.getModelRegistry().putObject(modelVariantLocation, customModel);
		        	}
		        }
		    }
	    }
	    catch(Exception e) {
	    	LogHelper.warning("Could not get base Dog Bed model. Reverting to default textures...");
	    }
	}
	
	@SubscribeEvent
	public void registerTextures(TextureStitchEvent.Pre event) {
     

	}
}
