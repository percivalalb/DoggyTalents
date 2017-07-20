package doggytalents.base.a;

import doggytalents.client.gui.GuiTreatBag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GuiTreatBagWrapper extends GuiTreatBag {

	public GuiTreatBagWrapper(EntityPlayer playerIn, int slotIn, ItemStack itemstackIn) {
		super(playerIn, slotIn, itemstackIn);
	}

}
