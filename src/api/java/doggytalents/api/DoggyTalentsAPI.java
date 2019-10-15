package doggytalents.api;

import doggytalents.api.inferface.Talent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author ProPercivalalb
 */
public class DoggyTalentsAPI {

    public static ItemList PACKPUPPY_BLACKLIST = new ItemList();

    public static ItemList BEG_TAMED_WHITELIST = new ItemList();
    public static ItemList BEG_UNTAMED_WHITELIST = new ItemList();
    public static ItemList BREED_WHITELIST = new ItemList();

    public static IForgeRegistry<Talent> TALENTS;
}
