package doggytalents.base.d;

import doggytalents.base.ObjectLib;
import doggytalents.client.gui.GuiPackPuppy;
import doggytalents.client.gui.GuiTreatBag;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GuiPackPuppyWrapper extends GuiPackPuppy {

	public GuiPackPuppyWrapper(EntityPlayer player, EntityDog dog) {
		super(player, dog);
	}

	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
    }
}
