package doggytalents.api.registry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import doggytalents.api.inferface.ITalent;
import doggytalents.helper.LogHelper;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author ProPercivalalb
 */
public class TalentRegistry {

	public static List<ITalent> talents = new ArrayList<ITalent>();
	public static Map<String, ITalent> idtalent = new LinkedHashMap<String, ITalent>();
	public static Map<ITalent, String> talentid = new LinkedHashMap<ITalent, String>();
	
	public static void registerTalent(ITalent talent) {
		if(talents.contains(talent))
			LogHelper.warning("The talent id %s has already been registered", talent.getKey());
		else if(talent.getKey().contains(":"))
			LogHelper.warning("A talent id can't have the character ':' in it (%s)", talent.getKey());
		else {
			talents.add(talent);
			idtalent.put(talent.getKey(), talent);
			talentid.put(talent, talent.getKey());
			MinecraftForge.EVENT_BUS.register(talent);
			LogHelper.info("Register the talent with the id %s", talent.getKey());
		}
	}
	
	public static List<ITalent> getTalents() {
		return talents;
	}
	
	public static ITalent getTalent(int index) {
		return talents.get(index);
	}
	
	public static ITalent getTalent(String id) {
		return idtalent.get(id);
	}
}
