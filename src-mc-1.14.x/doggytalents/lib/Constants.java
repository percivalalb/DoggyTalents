package doggytalents.lib;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;

/**
 * @author ProPercivalalb
 */
public class Constants {
	
	public static int LOW_HEATH_LEVEL = 1;
	public static int HUNGER_POINTS = 120;
	public static int LOW_HUNGER = 20;
	
	//Server
	public static boolean DOGS_IMMORTAL;
	public static int TIME_TO_MATURE;
	public static boolean IS_HUNGER_ON;
	public static boolean STARTING_ITEMS;
	public static boolean DOG_GENDER;
	public static boolean DOG_WHINE_WHEN_HUNGER_LOW;
	public static boolean PUPS_GET_PARENT_LEVELS;
	
	public static Map<ResourceLocation, Boolean> ENABLED_TALENTS;
	
	//Client Only
	public static boolean DIRE_PARTICLES;
	public static boolean RENDER_BLOOD;
	public static boolean DOGGY_WINGS;
	public static boolean DOGGY_CHEST;
	public static boolean DOGGY_SADDLE;
	public static boolean USE_DT_TEXTURES;
	public static boolean DOGGY_ARMOUR;
	
	static {
		ENABLED_TALENTS = new HashMap<ResourceLocation, Boolean>(16);
	}
}
