package doggytalents.base.a;

import doggytalents.DoggyTalents;
import doggytalents.base.ObjectLib;
import doggytalents.base.d.DogBedModel;
import doggytalents.handler.ModelBake;
import doggytalents.lib.Reference;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

public class ModelBakeWrapper {

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		ModelBake.onModelBakeEvent(event);
	}
}
