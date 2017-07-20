package doggytalents.base.d;

import doggytalents.base.ObjectLib;
import doggytalents.client.gui.GuiTreatBag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GuiTreatBagWrapper extends GuiTreatBag {

	public GuiTreatBagWrapper(EntityPlayer playerIn, int slotIn, ItemStack itemstackIn) {
		super(playerIn, slotIn, itemstackIn);
	}

	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
    }
}
