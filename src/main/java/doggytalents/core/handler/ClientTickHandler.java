package doggytalents.core.handler;

import static net.minecraftforge.common.ForgeVersion.Status.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import doggytalents.core.helper.Version;
import doggytalents.core.helper.VersionHelper;
import doggytalents.core.helper.VersionHelper.UpdateType;

/**
 * @author ProPercivalalb
 **/
public class ClientTickHandler {
	
	public static Minecraft mc = Minecraft.getMinecraft();
	public static boolean checkedVersion = false;
	
	@SubscribeEvent
	public void tickEnd(ClientTickEvent event) {
		if(event.phase == Phase.START && event.type == TickEvent.Type.CLIENT && event.side == Side.CLIENT) {
			
			if (mc.thePlayer != null)
				if(!checkedVersion && mc.thePlayer != null) {
					if(Version.status == OUTDATED)
	          		checkedVersion = true;
				}
		}
	}
}