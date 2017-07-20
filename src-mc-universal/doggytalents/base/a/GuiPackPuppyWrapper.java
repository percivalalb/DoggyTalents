package doggytalents.base.a;

import doggytalents.client.gui.GuiPackPuppy;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;

public class GuiPackPuppyWrapper extends GuiPackPuppy {

	public GuiPackPuppyWrapper(EntityPlayer player, EntityDog dog) {
		super(player, dog);
	}
}
