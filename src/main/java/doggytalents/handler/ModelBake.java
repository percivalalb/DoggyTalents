package doggytalents.handler;

import java.util.Map;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModBlocks;
import doggytalents.client.model.block.DogBedModel;
import doggytalents.lib.Reference;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModelBake {

    @SubscribeEvent
    public static void onModelBakeEvent(final ModelBakeEvent event) {
        Map<ResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();
        
        try {
            ResourceLocation resourceLocation = ForgeRegistries.BLOCKS.getKey(ModBlocks.DOG_BED);
            ResourceLocation unbakedModelLoc = new ResourceLocation(resourceLocation.getNamespace(), "block/" + resourceLocation.getPath());
            
            BlockModel model = (BlockModel)event.getModelLoader().getUnbakedModel(unbakedModelLoc);
            IBakedModel customModel = new DogBedModel(event.getModelLoader(), model, model.bake(event.getModelLoader(), ModelLoader.defaultTextureGetter(), TRSRTransformation.getRotation(Direction.NORTH), DefaultVertexFormats.BLOCK), DefaultVertexFormats.BLOCK);

            // Replace all valid block states
            ModBlocks.DOG_BED.getStateContainer().getValidStates().forEach(state -> {
                modelRegistry.put(BlockModelShapes.getModelLocation(state), customModel);
            });
            
            // Replace inventory model
            modelRegistry.put(new ModelResourceLocation(resourceLocation, "inventory"), customModel);

        }
        catch(Exception e) {
            DoggyTalentsMod.LOGGER.warn("Could not get base Dog Bed model. Reverting to default textures...");
            e.printStackTrace();
        }
    }
}
