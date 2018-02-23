package doggytalents.base;

import java.lang.reflect.Constructor;

import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import doggytalents.tileentity.TileEntityDogBed;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ObjectLib {

	public static final IInitializationEvent INITIALIZATION = VersionControl.createObject("InitEvent", IInitializationEvent.class);
	public static final IGeneralMethods METHODS = VersionControl.createObject("GeneralMethods", IGeneralMethods.class);
	public static final IStackUtil STACK_UTIL = VersionControl.createObject("StackUtil", IStackUtil.class);
	public static final IRegistryMethods REGISTRY = VersionControl.createObject("RegistryMethods", IRegistryMethods.class);
	public static final IBridge BRIDGE = VersionControl.createObject("Bridge", IBridge.class);

	public static final Class<TileEntityDogBed> TILE_DOG_BED_CLASS = VersionControl.chooseClassBasedOnVersion("TileEntityWrapper$TileEntityDogBedWrapper", TileEntityDogBed.class);
	public static final Class<TileEntityFoodBowl> TILE_FOOD_BOWL_CLASS = VersionControl.chooseClassBasedOnVersion("TileEntityWrapper$TileEntityFoodBowlWrapper", TileEntityFoodBowl.class);
	
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
	
	public static final Class<EntityDoggyBeam> ENTITY_DOGGY_BEAM_CLASS = VersionControl.chooseClassBasedOnVersion("EntityDoggyBeamWrapper", EntityDoggyBeam.class);
	public static final Constructor<EntityDoggyBeam> ENTITY_DOGGY_BEAM_CONSTRUCTOR = VersionControl.getConstructor(ENTITY_DOGGY_BEAM_CLASS, World.class, EntityLivingBase.class);
	
	public static final EntityDoggyBeam createDoggyBeam(World worldIn, EntityPlayer playerIn) {
		try {
			return ObjectLib.ENTITY_DOGGY_BEAM_CONSTRUCTOR.newInstance(worldIn, playerIn);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
