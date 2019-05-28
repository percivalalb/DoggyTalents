package doggytalents.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import doggytalents.DoggyTalentsMod;
import doggytalents.client.model.block.DogBedModel;
import doggytalents.lib.Reference;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelBlock;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModelBake {

	@SubscribeEvent
	public static void onModelBakeEvent(final ModelBakeEvent event) {
	    try {
	    	
	    	ModelBlock model = (ModelBlock)event.getModelLoader().getUnbakedModel(new ResourceLocation(Reference.MOD_ID, "block/dog_bed"));
		    IBakedModel customModel = new DogBedModel(model, model.bake(ModelLoader.defaultModelGetter(), ModelLoader.defaultTextureGetter(), TRSRTransformation.getRotation(EnumFacing.NORTH), true, DefaultVertexFormats.BLOCK), DefaultVertexFormats.BLOCK);

		    List<ModelResourceLocation> modelsToReplace = new ArrayList<ModelResourceLocation>();
		    	
		    for(Entry<ModelResourceLocation, IBakedModel> entry : event.getModelRegistry().entrySet()) {
		    	if(entry.getKey().getNamespace().equals(Reference.MOD_ID) && entry.getKey().getPath().equals("dog_bed")) {
		    		modelsToReplace.add(entry.getKey());
		    	}
		    }

		    for(ModelResourceLocation location : modelsToReplace) {
	    		//Replace 
	    		event.getModelRegistry().put(location, customModel);
		    }
	    }
	    catch(Exception e) {
	    	DoggyTalentsMod.LOGGER.warn("Could not get base Dog Bed model. Reverting to default textures...");
	    	e.printStackTrace();
	    }
	}
}
