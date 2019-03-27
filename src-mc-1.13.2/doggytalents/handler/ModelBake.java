package doggytalents.handler;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import doggytalents.DoggyTalentsMod;
import doggytalents.client.model.block.DogBedItemOverride;
import doggytalents.client.model.block.DogBedModel;
import doggytalents.lib.BlockNames;
import doggytalents.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BlockPart;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelBlock;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.model.SimpleBakedModel;
import net.minecraft.client.renderer.model.SimpleBakedModel.Builder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModelBake {

	@SubscribeEvent
	public static void onModelBakeEvent(final ModelBakeEvent event) {
	    try {
	    	
	    	ModelBlock model = (ModelBlock)event.getModelLoader().getUnbakedModel(new ResourceLocation(Reference.MOD_ID, "block/dog_bed"));
	    	DoggyTalentsMod.LOGGER.info("" + model);
	    	Iterator<EnumFacing> iterator = EnumFacing.Plane.HORIZONTAL.iterator();
            while(iterator.hasNext()) {
            	EnumFacing facing = iterator.next();
		    	ModelResourceLocation modelVariantLocation = new ModelResourceLocation(BlockNames.DOG_BED, "facing=" + facing.getName());
		
		        IBakedModel bakedModel = event.getModelRegistry().get(modelVariantLocation);

		        //Replace 
		        IBakedModel customModel = new DogBedModel(model, bakedModel, DefaultVertexFormats.BLOCK);
		        event.getModelRegistry().put(modelVariantLocation, customModel);
		    }
	    	
	    	/**
	    	IUnbakedModel ubModel = event.getModelLoader().getUnbakedModel(new ResourceLocation(Reference.MOD_ID, "block/dog_bed"));
	    	ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
            builder.put("bedding", "block/red_wool");
            builder.put("casing", "block/red_wool");
            builder.put("particle", "block/red_wool");
            DoggyTalentsMod.LOGGER.info("Model: " + ubModel);
            ImmutableMap<String, String> textures = builder.build();
           // IModel<IUnbakedModel> retexturedModel = ubModel.retexture(builder.build());
            ModelBlock model = (ModelBlock) ubModel;
	    	
            Iterator<EnumFacing> iterator = EnumFacing.Plane.HORIZONTAL.iterator();
            while(iterator.hasNext()) {
            	EnumFacing facing = iterator.next();
            	DoggyTalentsMod.LOGGER.info("Replace facing=" + facing.getName());
   

                List<BlockPart> elements = Lists.newArrayList(); //We have to duplicate this so we can edit it below.
                for (BlockPart part : model.getElements())
                {
                    elements.add(new BlockPart(part.positionFrom, part.positionTo, Maps.newHashMap(part.mapFaces), part.partRotation, part.shade));
                }

                ModelBlock newModel = new ModelBlock(model.getParentLocation(), elements,
                    Maps.newHashMap(model.textures), model.isAmbientOcclusion(), model.isGui3d(), //New Textures man VERY IMPORTANT
                    model.getAllTransforms(), Lists.newArrayList(model.getOverrides()));
                newModel.name = model.name;
                newModel.parent = model.parent;

                Set<String> removed = Sets.newHashSet();

                for (Entry<String, String> e : textures.entrySet())
                {
                    if ("".equals(e.getValue()))
                    {
                        removed.add(e.getKey());
                        newModel.textures.remove(e.getKey());
                    }
                    else
                        newModel.textures.put(e.getKey(), e.getValue());
                }

                // Map the model's texture references as if it was the parent of a model with the retexture map as its textures.
                Map<String, String> remapped = Maps.newHashMap();

                for (Entry<String, String> e : newModel.textures.entrySet())
                {
                    if (e.getValue().startsWith("#"))
                    {
                        String key = e.getValue().substring(1);
                        if (newModel.textures.containsKey(key))
                            remapped.put(e.getKey(), newModel.textures.get(key));
                    }
                }

                newModel.textures.putAll(remapped);

                //Remove any faces that use a null texture, this is for performance reasons, also allows some cool layering stuff.
                for (BlockPart part : newModel.getElements())
                {
                    part.mapFaces.entrySet().removeIf(entry -> removed.contains(entry.getValue().texture));
                }


                event.getModelRegistry().put(new ModelResourceLocation(BlockNames.DOG_BED, "facing=" + facing.getName()), newModel.bake(ModelLoader.defaultModelGetter(), ModelLoader.defaultTextureGetter(), TRSRTransformation.getRotation(facing), true, DefaultVertexFormats.BLOCK));
                		//retexturedModel.bake(ModelLoader.defaultModelGetter(), ModelLoader.defaultTextureGetter(), TRSRTransformation.getRotation(facing), false, DefaultVertexFormats.BLOCK));
            }**/
	    }
	    catch(Exception e) {
	    	DoggyTalentsMod.LOGGER.warn("Could not get base Dog Bed model. Reverting to default textures...");
	    	e.printStackTrace();
	    }
	}
}
