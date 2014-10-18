package doggytalents.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import doggytalents.DoggyTalentsMod;
import doggytalents.ModItems;
import doggytalents.entity.EntityDTDoggy;
import doggytalents.network.packet.PacketCommand;
import doggytalents.network.packet.PacketDogJump;

/**
 * @author ProPercivalalb
 **/
public class KeyStateHandler {
	
    public static final KeyBinding come = new KeyBinding("doggytalents.key.come", Keyboard.KEY_UP, "doggytalents.key.category");
    public static final KeyBinding stay = new KeyBinding("doggytalents.key.stay", Keyboard.KEY_DOWN, "doggytalents.key.category");
    public static final KeyBinding ok = new KeyBinding("doggytalents.key.ok", Keyboard.KEY_LEFT, "doggytalents.key.category");
    public static final KeyBinding heel = new KeyBinding("doggytalents.key.heel", Keyboard.KEY_RIGHT, "doggytalents.key.category");
    public static final KeyBinding[] keyBindings = new KeyBinding[] {come, stay, ok, heel, Minecraft.getMinecraft().gameSettings.keyBindJump};
    
    protected boolean[] keyDown = new boolean[Keyboard.KEYBOARD_SIZE];
    private Minecraft mc = Minecraft.getMinecraft();
    
    @SubscribeEvent
    public void keyEvent(ClientTickEvent event) {
    	this.keyTick(event.phase == Phase.END);
    }
    
    private void keyTick(boolean tickEnd) {
    	for(KeyBinding kb : keyBindings) {
	    	int keyCode = kb.getKeyCode();
	        boolean state = (keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) : Keyboard.isKeyDown(keyCode));
	        if (state != keyDown[keyCode]) {
	            if (state && !tickEnd) {
	            	//Key Pressed
	            	EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
	            	
	            	if(kb == mc.gameSettings.keyBindJump) {
	            		if(player.ridingEntity instanceof EntityDTDoggy && mc.currentScreen == null) {
	            			EntityDTDoggy dog = (EntityDTDoggy)player.ridingEntity;
	            			DoggyTalentsMod.NETWORK_MANAGER.sendPacketToServer(new PacketDogJump(dog.getEntityId()));
	            		}
	            	}
	            	else if(FMLClientHandler.instance().getClient().inGameHasFocus && player != null && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == ModItems.commandEmblem) {
	            	    int command = -1;
	            	   
	                	if(kb == come) {
	                		command = 1;
	                	}
	                	else if(kb == stay) {
	                		command = 2;
	                	}
	                	else if(kb == ok) {
	                		command = 3;
	                	}
	                	else if(kb == heel) {
	                		command = 4;
	                	}

	                	if(command != -1)
	                		DoggyTalentsMod.NETWORK_MANAGER.sendPacketToServer(new PacketCommand(command));
	               }
	            }
	            else if(!tickEnd) {
	            	//Key Released
	            	
	            }
	            if (tickEnd)
	                keyDown[keyCode] = state;
	        }
    	}
    }
}