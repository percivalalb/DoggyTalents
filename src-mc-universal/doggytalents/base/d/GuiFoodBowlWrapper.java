package doggytalents.base.d;

import doggytalents.client.gui.GuiFoodBowl;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiFoodBowlWrapper extends GuiFoodBowl {

	public GuiFoodBowlWrapper(InventoryPlayer playerInventory, TileEntityFoodBowl foodBowlIn) {
		super(playerInventory, foodBowlIn);
	}

	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
    }
}
