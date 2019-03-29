package doggytalents.handler;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import org.apache.commons.lang3.ArrayUtils;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModItems;
import doggytalents.entity.EntityDog;
import doggytalents.lib.Reference;
import doggytalents.network.PacketHandler;
import doggytalents.network.client.PacketCommand;
import doggytalents.network.client.PacketDogName;
import doggytalents.network.client.PacketJump;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.network.PacketDistributor;

/**
 * @author ProPercivalalb
 **/
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KeyState {
	
    public static final KeyBinding come = new KeyBinding("doggytalents.key.come", InputMappings.getInputByName("key.keyboard.up").getKeyCode(), "key.categories.doggytalents");
    public static final KeyBinding stay = new KeyBinding("doggytalents.key.stay", InputMappings.getInputByName("key.keyboard.down").getKeyCode(), "key.categories.doggytalents");
    public static final KeyBinding ok = new KeyBinding("doggytalents.key.ok", InputMappings.getInputByName("key.keyboard.left").getKeyCode(), "key.categories.doggytalents");
    public static final KeyBinding heel = new KeyBinding("doggytalents.key.heel", InputMappings.getInputByName("key.keyboard.right").getKeyCode(), "key.categories.doggytalents");
    public static final KeyBinding[] keyBindings = new KeyBinding[] {come, stay, ok, heel, Minecraft.getInstance().gameSettings.keyBindJump};
    
   	private static HashMap<KeyBinding, Boolean> keyState = new HashMap<KeyBinding, Boolean>();
    private static Minecraft mc = Minecraft.getInstance();
    
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
	            	
	            	if(kb == mc.gameSettings.keyBindJump) {
	            		if(player.getRidingEntity() instanceof EntityDog && mc.currentScreen == null) {
	            			EntityDog dog = (EntityDog)player.getRidingEntity();
	            			/**
	            			if(dog.onGround) {

	                			double verticalVelocity = 0.27D + 0.1D * dog.TALENTS.getLevel("wolfmount");
	                			if(dog.TALENTS.getLevel("wolfmount") == 5) verticalVelocity += 0.1D;
	                			
	                			dog.addVelocity(0D, verticalVelocity, 0D);
	                			if(dog.isPotionActive(MobEffects.JUMP_BOOST))
	                				dog.motionY += (double)((float)(dog.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
	                		}
	                		else if(dog.isInWater() && dog.TALENTS.getLevel("swimmerdog") > 0) {
	                			dog.motionY = 0.2F;
	                		}**/
	            			PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketJump(dog.getEntityId()));
	            			dog.tryToJump();
	            		}
	            	}
	            	else if(mc.isGameFocused() && player != null) {
	            		
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