package doggytalents.handler;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

import org.lwjgl.input.Keyboard;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModItems;
import doggytalents.entity.EntityDog;
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
    
   	private HashMap<KeyBinding, Boolean> keyState = new HashMap<KeyBinding, Boolean>();
    private Minecraft mc = Minecraft.getMinecraft();
    
    
    @SubscribeEvent
    public void keyEvent(ClientTickEvent event) {
    	this.keyTick(event.phase == Phase.END);
    }
    
    private void keyTick(boolean tickEnd) {
    	for(KeyBinding kb : keyBindings) {
	        if(kb.isKeyDown()) {
	            if (!tickEnd && (!this.keyState.containsKey(kb) || !this.keyState.get(kb))) {
	            	this.keyState.put(kb, true);
	            	//Key Pressed
	            	EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
	            	
	            	if(kb == mc.gameSettings.keyBindJump) {
	            		if(player.ridingEntity instanceof EntityDog && mc.currentScreen == null) {
	            			EntityDog dog = (EntityDog)player.ridingEntity;
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
	        }
	        else {
	        	this.keyState.put(kb, false);
	        }
    	}
    }
}