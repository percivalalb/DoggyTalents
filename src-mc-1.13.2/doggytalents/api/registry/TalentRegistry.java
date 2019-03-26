package doggytalents.api.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import doggytalents.DoggyTalentsMod;
import doggytalents.api.inferface.ITalent;
import doggytalents.configuration.ConfigHandler;
import doggytalents.lib.Constants;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author ProPercivalalb
 */
public class TalentRegistry {

	public static List<ITalent> 		TALENTS 		= new ArrayList<>();
	public static Map<String, ITalent> 	ID_TALENT_MAP 	= new LinkedHashMap<>();
	
	public static void registerTalent(ITalent talent) {
		if(ConfigHandler.CONFIG.isTalentDisabled(talent.getKey()))
			DoggyTalentsMod.LOGGER.warn("The talent id {} has been disabled in the config file", talent.getKey());
		else if(TALENTS.contains(talent))
			DoggyTalentsMod.LOGGER.warn("The talent id {} has already been registered", talent.getKey());
		else if(talent.getKey().contains(":"))
			DoggyTalentsMod.LOGGER.warn("A talent id can't have the character ':' in it ({})", talent.getKey());
		else {
			TALENTS.add(talent);
			ID_TALENT_MAP.put(talent.getKey(), talent);
			MinecraftForge.EVENT_BUS.register(talent);
			DoggyTalentsMod.LOGGER.info("Register the talent with the id {}", talent.getKey());
		}
	}
	
	public static List<ITalent> getTalents() {
		return TALENTS;
	}
	
	public static ITalent getTalent(int index) {
		return TALENTS.get(index);
	}
	
	public static ITalent getTalent(String id) {
		return ID_TALENT_MAP.get(id);
	}
}
