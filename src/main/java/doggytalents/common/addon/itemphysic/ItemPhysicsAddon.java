package doggytalents.common.addon.itemphysic;

import java.lang.reflect.Method;
import java.util.Collection;

import com.google.common.collect.Lists;

import doggytalents.DoggyBlocks;
import doggytalents.DoggyItems;
import doggytalents.common.addon.Addon;
import doggytalents.common.util.ReflectionUtil;

public class ItemPhysicsAddon implements Addon {

    private static final String className = "team.creative.itemphysic.api.ItemPhysicAPI";

    private static final String methodName = "addSortingObjects";
    private static final Class<?>[] paramTypes = new Class[] {String.class, Object[].class};

    private static final String swimmingItems = "swimmingItems";
    private static final String burningItems = "burningItems";
    private static final String undestroyableItems = "undestroyableItems";
    private static final String ignitingItems = "ignitingItems";

    @Override
    public void exec() {
        Class<?> API_CLASS = ReflectionUtil.getClass(className);

        Method addMethod = ReflectionUtil.getMethod(API_CLASS, methodName, paramTypes);

        ReflectionUtil.invokeStaticMethod(addMethod, swimmingItems,
                DoggyItems.BREEDING_BONE, DoggyItems.DIRE_TREAT, DoggyItems.MASTER_TREAT,
                DoggyItems.SUPER_TREAT, DoggyItems.TRAINING_TREAT, DoggyItems.COLLAR_SHEARS,
                DoggyItems.THROW_BONE, DoggyItems.WOOL_COLLAR, DoggyItems.TREAT_BAG,
                DoggyItems.CHEW_STICK);

        ReflectionUtil.invokeStaticMethod(addMethod, burningItems,
                DoggyBlocks.DOG_BED, DoggyItems.BREEDING_BONE, DoggyItems.DIRE_TREAT,
                DoggyItems.MASTER_TREAT, DoggyItems.SUPER_TREAT,  DoggyItems.TRAINING_TREAT,
                DoggyItems.COLLAR_SHEARS, DoggyItems.THROW_BONE, DoggyItems.RADAR,
                DoggyItems.WOOL_COLLAR, DoggyItems.TREAT_BAG, DoggyItems.CHEW_STICK);
    }

    @Override
    public Collection<String> getMods() {
        return Lists.newArrayList("itemphysic");
    }

    @Override
    public String getName() {
        return "Item Physics Addon";
    }
}