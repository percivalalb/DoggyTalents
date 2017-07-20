package doggytalents.base.d;

import doggytalents.client.gui.GuiPackPuppy;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;

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
