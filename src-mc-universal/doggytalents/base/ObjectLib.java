package doggytalents.base;

import java.lang.reflect.Constructor;

import doggytalents.entity.EntityDog;
import net.minecraft.world.World;

public class ObjectLib {

	public static final IInitializationEvent INITIALIZATION = VersionControl.createObject("InitEvent", IInitializationEvent.class);
	public static final IGeneralMethods METHODS = VersionControl.createObject("GeneralMethods", IGeneralMethods.class);
	public static final IStackUtil STACK_UTIL = VersionControl.createObject("StackUtil", IStackUtil.class);
	public static final IRegistryMethods REGISTRY = VersionControl.createObject("RegistryMethods", IRegistryMethods.class);
	public static final IBridge BRIDGE = VersionControl.createObject("Bridge", IBridge.class);

	public static final Class<EntityDog> ENTITY_DOG_CLASS = VersionControl.chooseClassBasedOnVersion("EntityDogWrapper", EntityDog.class);
	public static final Constructor<EntityDog> ENTITY_DOG_CONSTRUCTOR = VersionControl.getConstructor(ENTITY_DOG_CLASS, World.class);
	
	public static final EntityDog createDog(World worldIn) {
		try {
			return ObjectLib.ENTITY_DOG_CONSTRUCTOR.newInstance(worldIn);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
