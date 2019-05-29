package doggytalents.handler;

import java.util.HashMap;

import org.lwjgl.glfw.GLFW;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModItems;
import doggytalents.lib.Reference;
import doggytalents.network.PacketHandler;
import doggytalents.network.client.PacketCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.network.PacketDistributor;

/**
 * @author ProPercivalalb
 **/
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class KeyState {
	
    private static Minecraft mc = Minecraft.getInstance();
    public static final KeyBinding come = new KeyBinding("doggytalents.key.come", GLFW.GLFW_KEY_UP, "key.categories.doggytalents");
    public static final KeyBinding stay = new KeyBinding("doggytalents.key.stay", GLFW.GLFW_KEY_DOWN, "key.categories.doggytalents");
    public static final KeyBinding ok = new KeyBinding("doggytalents.key.ok", GLFW.GLFW_KEY_LEFT, "key.categories.doggytalents");
    public static final KeyBinding heel = new KeyBinding("doggytalents.key.heel", GLFW.GLFW_KEY_RIGHT, "key.categories.doggytalents");
    public static final KeyBinding[] keyBindings = new KeyBinding[] {come, stay, ok, heel};
    
   	private static HashMap<KeyBinding, Boolean> keyState = new HashMap<KeyBinding, Boolean>();
    
    static {
    	net.minecraftforge.client.settings.KeyConflictContext inGame = net.minecraftforge.client.settings.KeyConflictContext.IN_GAME;
    	come.setKeyConflictContext(inGame);
    	stay.setKeyConflictContext(inGame);
    	ok.setKeyConflictContext(inGame);
    	heel.setKeyConflictContext(inGame);
    }
    
    
    @SubscribeEvent
    public static void keyEvent(final TickEvent.ClientTickEvent event) {
    	keyTick(event.phase == Phase.END);
    }
    
    private static void keyTick(boolean tickEnd) {
    	for(KeyBinding kb : keyBindings) {
	        if(kb.isKeyDown()) {
	            if (!tickEnd && (!keyState.containsKey(kb) || !keyState.get(kb))) {
	            	keyState.put(kb, true);
	            	//Key Pressed
	            	EntityPlayer player = DoggyTalentsMod.PROXY.getPlayerEntity();
	            	
	            	if(mc.isGameFocused() && player != null) {
	            		
	            		for(ItemStack heldStack : player.getHeldEquipment()) {
	            			if(heldStack.isEmpty() || heldStack.getItem() != ModItems.COMMAND_EMBLEM) continue;
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
		                		PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketCommand(command));
		                	break;
	            		}
	               }
	            }
	            else if(!tickEnd) {
	            	//Key Released
	            	
	            }
	        }
	        else {
	        	keyState.put(kb, false);
	        }
    	}
    }
}