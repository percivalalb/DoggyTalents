package doggytalents.base;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import doggytalents.entity.EntityDog;
import net.minecraft.world.World;

public class ObjectLib {

	public static final IGeneralMethods METHODS = VersionControl.createObject("GeneralMethods", IGeneralMethods.class);
	public static final IStackUtil STACK_UTIL = VersionControl.createObject("StackUtil", IStackUtil.class);
	
	public static final IClientMethods CLIENT = VersionControl.createObject("ClientMethods", IClientMethods.class);
	
	public static final Class<EntityDog> ENTITY_DOG_CLASS = VersionControl.chooseClassBasedOnVersion("EntityDogWrapper");
	public static final Constructor<EntityDog> ENTITY_DOG_CONSTRUCTOR = VersionControl.getConstructor(ENTITY_DOG_CLASS, World.class);
	
	public static final EntityDog createDog(World world) {
		try {
			return ObjectLib.ENTITY_DOG_CONSTRUCTOR.newInstance(world);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
