package doggytalents.base.c;

import doggytalents.client.gui.GuiFoodBowl;
import doggytalents.client.gui.GuiPackPuppy;
import doggytalents.client.gui.GuiTreatBag;
import doggytalents.entity.EntityDog;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class GuiWrapper {

	public static class GuiFoodBowlWrapper extends GuiFoodBowl {

		public GuiFoodBowlWrapper(InventoryPlayer playerInventory, TileEntityFoodBowl foodBowlIn) {
			super(playerInventory, foodBowlIn);
		}
	}
	
	public static class GuiPackPuppyWrapper extends GuiPackPuppy {

		public GuiPackPuppyWrapper(EntityPlayer player, EntityDog dog) {
			super(player, dog);
		}
	}
	
	public static class GuiTreatBagWrapper extends GuiTreatBag {

		public GuiTreatBagWrapper(EntityPlayer playerIn, int slotIn, ItemStack itemstackIn) {
			super(playerIn, slotIn, itemstackIn);
		}
	}
}
