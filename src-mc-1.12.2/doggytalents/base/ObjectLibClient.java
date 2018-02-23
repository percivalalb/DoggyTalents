package doggytalents.base;

import java.lang.reflect.Constructor;

import doggytalents.client.gui.GuiFoodBowl;
import doggytalents.client.gui.GuiPackPuppy;
import doggytalents.client.gui.GuiTreatBag;
import doggytalents.entity.EntityDog;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value = Side.CLIENT)
public class ObjectLibClient {

	public static final IInitializationEvent INITIALIZATION = VersionControl.createObject("InitEventClient", IInitializationEvent.class);
	
	public static final IClientMethods METHODS = VersionControl.createObject("ClientMethods", IClientMethods.class);
	
	public static final Class<GuiFoodBowl> GUI_FOOD_BOWL_CLASS = VersionControl.chooseClassBasedOnVersion("GuiWrapper$GuiFoodBowlWrapper", GuiFoodBowl.class);
	public static final Class<GuiPackPuppy> GUI_PACK_PUPPY_CLASS = VersionControl.chooseClassBasedOnVersion("GuiWrapper$GuiPackPuppyWrapper", GuiPackPuppy.class);
	public static final Class<GuiTreatBag> GUI_TREAT_BAG_CLASS = VersionControl.chooseClassBasedOnVersion("GuiWrapper$GuiTreatBagWrapper", GuiTreatBag.class);
	
	public static final Constructor<GuiFoodBowl> GUI_FOOD_BOWL_CONSTRUCTOR = VersionControl.getConstructor(GUI_FOOD_BOWL_CLASS, InventoryPlayer.class, TileEntityFoodBowl.class);
	public static final Constructor<GuiPackPuppy> GUI_PACK_PUPPY_CONSTRUCTOR = VersionControl.getConstructor(GUI_PACK_PUPPY_CLASS, EntityPlayer.class, EntityDog.class);
	public static final Constructor<GuiTreatBag> GUI_TREAT_BAG_CONSTRUCTOR = VersionControl.getConstructor(GUI_TREAT_BAG_CLASS, EntityPlayer.class, Integer.TYPE, ItemStack.class);

	public static final GuiFoodBowl createGuiFoodBowl(InventoryPlayer inventoryPlayerIn, TileEntityFoodBowl foodBowlIn) {
		try {
			return ObjectLibClient.GUI_FOOD_BOWL_CONSTRUCTOR.newInstance(inventoryPlayerIn, foodBowlIn);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static final GuiPackPuppy createGuiPackPuppy(EntityPlayer playerIn, EntityDog dogIn) {
		try {
			return ObjectLibClient.GUI_PACK_PUPPY_CONSTRUCTOR.newInstance(playerIn, dogIn);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static final GuiTreatBag createGuiTreatBag(EntityPlayer playerIn, int slotIn, ItemStack stackIn) {
		try {
			return ObjectLibClient.GUI_TREAT_BAG_CONSTRUCTOR.newInstance(playerIn, slotIn, stackIn);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
