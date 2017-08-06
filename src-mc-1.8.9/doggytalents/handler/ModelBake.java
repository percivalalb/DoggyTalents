package doggytalents.handler;

import doggytalents.client.renderer.block.DogBedModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBake {

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
	    TextureAtlasSprite base = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/slime");

	    for(String thing : new String[] {"inventory", "facing=north", "facing=south", "facing=east", "facing=west"}) {
	    	IFlexibleBakedModel model = (IFlexibleBakedModel)event.modelRegistry.getObject(new ModelResourceLocation("doggytalents:dog_bed", thing));
	    	event.modelRegistry.putObject(new ModelResourceLocation("doggytalents:dog_bed", thing), new DogBedModel.Builder(model, base, base).makeBakedModel());
	    }
	    
	}
}
