package doggytalents.core.addon.forestry;

import java.util.Locale;

import cpw.mods.fml.common.Loader;
import doggytalents.api.DogBedManager;
import doggytalents.core.addon.AddonEvent;
import doggytalents.core.helper.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.event.ForgeSubscribe;

/**
 * @author ProPercivalalb
 */
public class ForestryAddon {

	private static ForestryAPI API = new ForestryAPI(ForestryLib.MOD_NAME);
	
	@ForgeSubscribe
	public void onPre(AddonEvent.Pre event) {
		if(!Loader.isModLoaded(ForestryLib.MOD_NAME))
			return;
	}
	
	@ForgeSubscribe
	public void onInit(AddonEvent.Init event) {
		if(!Loader.isModLoaded(ForestryLib.MOD_NAME))
			return;
	}

	@ForgeSubscribe
	public void onPost(AddonEvent.Post event) throws Exception {
		if(!Loader.isModLoaded(ForestryLib.MOD_NAME))
			return;
		for(int i = 0; i < API.getEnums().size(); ++i) {
			Enum en = API.getEnums().get(i);
			DogBedManager.registerBedWood("forestry." + en.toString().toLowerCase(Locale.ENGLISH) + "Plank", new ForestryBedMaterial(en), API.getStackFromEnum(en));
		}
	}
}
