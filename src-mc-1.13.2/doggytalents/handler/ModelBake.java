package doggytalents.handler;

import doggytalents.DoggyTalentsMod;
import doggytalents.lib.Reference;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModelBake {

	@SubscribeEvent
	public static void onModelBakeEvent(final ModelBakeEvent event) {
		DoggyTalentsMod.LOGGER.info("dawd");
		DoggyTalentsMod.LOGGER.info("dawd");
		DoggyTalentsMod.LOGGER.info("dawd");
		DoggyTalentsMod.LOGGER.info("dawd");
		DoggyTalentsMod.LOGGER.info("dawd");
		DoggyTalentsMod.LOGGER.info("dawd");
		DoggyTalentsMod.LOGGER.info("dawd");
		DoggyTalentsMod.LOGGER.info("dawd");
		DoggyTalentsMod.LOGGER.info("dawd");
		DoggyTalentsMod.LOGGER.info("dawd");
		
		/**
	    try {
	    	IModel<IUnbakedModel> model = ModelLoaderRegistry.getModel(new ModelResourceLocation("doggytalents:dog_bed", "facing=east"));
	    	DoggyTalentsMod.LOGGER.info("" + model);
	    	for(String thing : new String[] {"facing=north", "facing=south", "facing=east", "facing=west"}) {
		    	ModelResourceLocation modelVariantLocation = new ModelResourceLocation(BlockNames.DOG_BED, thing);
		
		        IBakedModel bakedModel = event.getModelRegistry().get(modelVariantLocation);

		        //Replace 
		        IBakedModel customModel = new DogBedModel(model, bakedModel, DefaultVertexFormats.BLOCK);
		        event.getModelRegistry().put(modelVariantLocation, customModel);
		    }
	    }
	    catch(Exception e) {
	    	DoggyTalentsMod.LOGGER.warn("Could not get base Dog Bed model. Reverting to default textures...");
	    	e.printStackTrace();
	    }**/
	}
}
