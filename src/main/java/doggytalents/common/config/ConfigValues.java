package doggytalents.common.config;

import java.util.ArrayList;
import java.util.List;

import doggytalents.api.registry.Talent;

public class ConfigValues {

    public static final boolean DOG_GENDER = true;
    public static final boolean ALWAYS_SHOW_DOG_NAME = true;
    public static final boolean DISPLAY_OTHER_DOG_SKINS = true; // Requests skins that it does not have from the server
    public static final boolean SEND_SKIN = true; // Sends your custom skins to server to distribute
    public static final float DEFAULT_MAX_HUNGER = 120F;
    public static final boolean STARTING_ITEMS = false;

    public static List<Talent> DISABLED_TALENTS = new ArrayList<>();
}
