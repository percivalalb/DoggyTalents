package doggytalents.core.handler;

import java.util.EnumSet;
import java.util.logging.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.Icon;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import doggytalents.core.helper.VersionHelper;
import doggytalents.core.helper.VersionHelper.Type;
import doggytalents.lib.Reference;

/**
 * @author ProPercivalalb
 **/
public class ClientTickHandler implements ITickHandler {
	
	public static Minecraft mc = Minecraft.getMinecraft();
	public static boolean checkedVersion = false;
	
    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {}

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
       if (type.equals(EnumSet.of(TickType.CLIENT))) {
            GuiScreen guiscreen = Minecraft.getMinecraft().currentScreen;
            if (guiscreen != null) {
                onTickInGUI(guiscreen);
            } 
            else {
                onTickInGame();
            }
        }
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT);
    }

    @Override
    public String getLabel() {
    	return Reference.MOD_ID + "(Client)";
    }
    

    public void onTickInGUI(GuiScreen guiscreen) {
    	
    }

    public void onTickInGame() {
    	onTick(mc);
    }
       
    public void onTick(Minecraft mc) {   
    	if(!checkedVersion && mc.thePlayer != null) {
    		VersionHelper.checkVersion(Type.COLOURED);
    		checkedVersion = true;
    	}
    }
}