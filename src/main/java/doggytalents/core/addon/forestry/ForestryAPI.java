package doggytalents.core.addon.forestry;

import java.lang.reflect.Field;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.google.common.base.Optional;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import doggytalents.core.helper.ReflectionHelper;

/**
 * @author ProPercivalalb
 */
public class ForestryAPI {
	
	public ForestryAPI(String modId) {
		if(!Loader.isModLoaded(modId))
			return;
		
	}
}
