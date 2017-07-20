package doggytalents.base.a;

import doggytalents.base.IClientMethods;
import net.minecraftforge.common.MinecraftForge;

public class ClientMethods implements IClientMethods {

	@Override
	public void registerEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new ModelBakeWrapper());
	}
}
