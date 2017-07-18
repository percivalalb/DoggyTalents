package doggytalents.base;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import doggytalents.entity.EntityDog;
import net.minecraft.world.World;

public class ObjectLib {

	public static final IGeneralMethods METHODS = ChooseVersion.createObject("GeneralMethods", IGeneralMethods.class);
	public static final Class<EntityDog> ENTITY_DOG_CLASS = ChooseVersion.chooseClassBasedOnVersion("Dog");
	public static final Constructor<EntityDog> ENTITY_DOG_CONSTRUCTOR = ChooseVersion.getConstructor(ENTITY_DOG_CLASS, World.class);
	
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
