package doggytalents.base.a;

import doggytalents.client.gui.GuiFoodBowl;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiFoodBowlWrapper extends GuiFoodBowl {

	public GuiFoodBowlWrapper(InventoryPlayer playerInventory, TileEntityFoodBowl foodBowlIn) {
		super(playerInventory, foodBowlIn);
	}
}
