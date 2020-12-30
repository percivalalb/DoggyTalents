package doggytalents.common.config;

import java.util.ArrayList;
import java.util.List;

import doggytalents.api.registry.Talent;

public class ConfigValues {

    public static boolean DISABLE_HUNGER          = false;
    public static boolean PUPS_GET_PARENT_LEVELS  = true;
    public static boolean WHISTLE_SOUNDS          = true;
    public static boolean DOG_GENDER              = true;
    public static boolean ALWAYS_SHOW_DOG_NAME    = true;
    public static boolean DISPLAY_OTHER_DOG_SKINS = false; // Requests skins that it does not have from the server
    public static boolean SEND_SKIN               = false; // Sends your custom skins to server to distribute
    public static float   DEFAULT_MAX_HUNGER      = 120F;
    public static boolean STARTING_ITEMS          = false;

    public static boolean DIRE_PARTICLES          = true;
    public static boolean RENDER_CHEST            = true;
    public static boolean USE_DT_TEXTURES         = true;

    public static List<Talent> DISABLED_TALENTS = new ArrayList<>();
}
