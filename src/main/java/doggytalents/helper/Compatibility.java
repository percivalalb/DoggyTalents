package doggytalents.helper;

import java.util.HashMap;
import java.util.function.Supplier;

import doggytalents.DoggyTalents;
import doggytalents.ModItems;
import doggytalents.ModTalents;
import doggytalents.api.inferface.Talent;
import doggytalents.api.lib.Reference;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IFixableData;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

//@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Compatibility {

    public static final ResourceLocation DOGGY_BEAM = new ResourceLocation(Reference.MOD_ID, "attackbeam");
    public static final ResourceLocation COMMAND_EMBLEM = new ResourceLocation(Reference.MOD_ID, "command_emblem");
    public static final ResourceLocation CREATIVE_RADAR = new ResourceLocation(Reference.MOD_ID, "creative");
    public static final ResourceLocation FANCY_COLLAR = new ResourceLocation(Reference.MOD_ID, "fancy_collar");
    
    private static HashMap<String, Supplier<Talent>> OLD_NEW_TALENT = new HashMap<String, Supplier<Talent>>();
    
    public static Talent getTalentOldNamingScheme(String name) {
        return OLD_NEW_TALENT.containsKey(name) ? OLD_NEW_TALENT.get(name).get() : null;
    }
    
    static {
        OLD_NEW_TALENT.put("bedfinder", ()->ModTalents.BED_FINDER);
        OLD_NEW_TALENT.put("blackpelt", ()->ModTalents.BLACK_PELT);
        OLD_NEW_TALENT.put("creepersweeper", ()->ModTalents.CREEPER_SWEEPER);
        OLD_NEW_TALENT.put("doggydash", ()->ModTalents.DOGGY_DASH);
        OLD_NEW_TALENT.put("fisherdog", ()->ModTalents.FISHER_DOG);
        OLD_NEW_TALENT.put("guarddog", ()->ModTalents.GUARD_DOG);
        OLD_NEW_TALENT.put("happyeater", ()->ModTalents.HAPPY_EATER);
        OLD_NEW_TALENT.put("hellhound", ()->ModTalents.HELL_HOUND);
        OLD_NEW_TALENT.put("hunterdog", ()->ModTalents.HUNTER_DOG);
        OLD_NEW_TALENT.put("packpuppy", ()->ModTalents.PACK_PUPPY);
        OLD_NEW_TALENT.put("pestfighter", ()->ModTalents.PEST_FIGHTER);
        OLD_NEW_TALENT.put("pillowpaw", ()->ModTalents.PILLOW_PAW);
        OLD_NEW_TALENT.put("poisonfang", ()->ModTalents.POISON_FANG);
        OLD_NEW_TALENT.put("puppyeyes", ()->ModTalents.PUPPY_EYES);
        OLD_NEW_TALENT.put("quickhealer", ()->ModTalents.QUICK_HEALER);
        OLD_NEW_TALENT.put("rangedattacker", ()->ModTalents.RANGED_ATTACKER);
        OLD_NEW_TALENT.put("rescuedog", ()->ModTalents.RESCUE_DOG);
        OLD_NEW_TALENT.put("roaringgale", ()->ModTalents.ROARING_GALE);
        OLD_NEW_TALENT.put("shepherddog", ()->ModTalents.SHEPHERD_DOG);
        OLD_NEW_TALENT.put("swimmerdog", ()->ModTalents.SWIMMER_DOG);
        OLD_NEW_TALENT.put("wolfmount", ()->ModTalents.WOLF_MOUNT);
    }
    
    public static class ThrowBoneDataFixer implements IFixableData {

        @Override
        public int getFixVersion() {
            return 1;
        }

        @Override
        public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
            String id = compound.getString("id");
            short damage = compound.getShort("Damage");
            
            if(id.equals("doggytalents:throw_bone") && damage > 0) {
                Item replacement = null;
                if(damage == 1) {
                    replacement = ModItems.THROW_BONE_WET;
                } else if(damage == 2) {
                    replacement = ModItems.THROW_STICK;
                } else if(damage == 3) {
                    replacement = ModItems.THROW_STICK_WET;
                }
                
                if(replacement != null)
                    compound.setString("id", ForgeRegistries.ITEMS.getKey(replacement).toString());
                
                compound.setShort("Damage", (short)0);
                DoggyTalents.LOGGER.debug("Throw bone damage fixed");
                
            } else if(id.equals("doggytalents:fancy_collar")) {
                Item replacement = null;
                if(damage == 0) {
                    replacement = ModItems.CREATIVE_COLLAR;
                } else if(damage == 1) {
                    replacement = ModItems.SPOTTED_COLLAR;
                } else if(damage == 2) {
                    replacement = ModItems.MULTICOLOURED_COLLAR;
                }
                
                if(replacement != null)
                    compound.setString("id", ForgeRegistries.ITEMS.getKey(replacement).toString());
                
                compound.setShort("Damage", (short)0);
                
                DoggyTalents.LOGGER.debug("Fancy Collar damage fixed");
            }
            
            return compound;
        }
    }
    
    public static class EntityDogDataFixer implements IFixableData {

        @Override
        public int getFixVersion() {
            return 1;
        }

        @Override
        public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
            String s = compound.getString("id");

            if("minecraft:doggytalents.dog".equals(s) || "minecraft:doggytalents:dog".equals(s) || "doggytalents.doggytalents:dog".equals(s)) {
                compound.setString("id", "doggytalents.dog");
                DoggyTalents.LOGGER.debug("Fixer: EntityDog id fixed");
            }

            return compound;
        }
    }
}
