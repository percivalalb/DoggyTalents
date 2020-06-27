package doggytalents.common.config;

import java.util.ArrayList;
import java.util.List;

import doggytalents.api.registry.Talent;

public class ConfigValues {

    public static final boolean DISABLE_HUNGER = false;
    public static final boolean PUPS_GET_PARENT_LEVELS = true;
    public static boolean DOG_GENDER = true;
    public static boolean ALWAYS_SHOW_DOG_NAME = true;
    public static boolean DISPLAY_OTHER_DOG_SKINS = true; // Requests skins that it does not have from the server
    public static boolean SEND_SKIN = true; // Sends your custom skins to server to distribute
    public static float DEFAULT_MAX_HUNGER = 120F;
    public static boolean STARTING_ITEMS = false;

    public static List<Talent> DISABLED_TALENTS = new ArrayList<>();
}
