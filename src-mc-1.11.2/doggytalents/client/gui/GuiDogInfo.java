package doggytalents.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import doggytalents.api.inferface.ITalent;
import doggytalents.api.registry.TalentRegistry;
import doggytalents.entity.EntityDog;
import doggytalents.entity.ModeUtil.EnumMode;
import doggytalents.helper.LogHelper;
import doggytalents.network.PacketDispatcher;
import doggytalents.network.packet.client.DogModeMessage;
import doggytalents.network.packet.client.DogNameMessage;
import doggytalents.network.packet.client.DogObeyMessage;
import doggytalents.network.packet.client.DogTalentMessage;
import doggytalents.network.packet.client.DogTextureMessage;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

/**
 * @author ProPercivalalb
 */
public class GuiDogInfo extends GuiScreen {

	public EntityDog dog;
	public EntityPlayer player;
	private ScaledResolution resolution;
	private List<GuiTextField> textfieldList = new ArrayList<GuiTextField>();
	private GuiTextField nameTextField;
	public int doggyTex;
	private int currentPage = 0;
	private int maxPages = 1;
	public int btnPerPages = 0;
	
	public GuiDogInfo(EntityDog dog, EntityPlayer player) {
		this.dog = dog;
		this.player = player;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();
		this.labelList.clear();
		this.textfieldList.clear();
		this.resolution = new ScaledResolution(this.mc);
		Keyboard.enableRepeatEvents(true);
		this.doggyTex = this.dog.getTameSkin();
		int topX = this.width / 2;
	    int topY = this.height / 2;
		GuiTextField nameTextField = new GuiTextField(0, this.fontRendererObj, topX - 100, topY + 50, 200, 20) {
			@Override
			public boolean textboxKeyTyped(char character, int keyId) {
				boolean typed = super.textboxKeyTyped(character, keyId);
				if(typed)
					 PacketDispatcher.sendToServer(new DogNameMessage(dog.getEntityId(), this.getText()));
				return typed;
			}
		};
		nameTextField.setFocused(false);
		nameTextField.setMaxStringLength(32);
		nameTextField.setText(this.dog.getDogName());
		this.nameTextField = nameTextField;
		
		this.textfieldList.add(nameTextField);
		
		int size = TalentRegistry.getTalents().size();
    	
    	int temp = 0;
    	while((temp + 2) * 21 + 10 < this.resolution.getScaledHeight())
    		temp += 1;
    	
  		this.btnPerPages = temp;
    	
    	if(temp < size) {
    		this.buttonList.add(new GuiButton(-1, 25, temp * 21 + 10, 20, 20, "<"));
    	    this.buttonList.add(new GuiButton(-2, 48, temp * 21 + 10, 20, 20, ">"));
    	}
		
    	if(this.btnPerPages < 1)
    		this.btnPerPages = 1;
    	
		this.maxPages = (int)Math.ceil((double)TalentRegistry.getTalents().size() / (double)this.btnPerPages);
		
		if(this.currentPage >= this.maxPages)
    		this.currentPage = 0;
		
    	for(int i = 0; i < this.btnPerPages; ++i) {
    		if((this.currentPage * this.btnPerPages + i) >= TalentRegistry.getTalents().size())
    			continue;
    		this.buttonList.add(new GuiButton(1 + this.currentPage * this.btnPerPages + i, 25, 10 + i * 21, 20, 20, "+"));
    	}
    	

        this.buttonList.add(new GuiButton(-3, this.width - 42, topY + 30, 20, 20, "+"));
        this.buttonList.add(new GuiButton(-4, this.width - 64, topY + 30, 20, 20, "-"));
        if(this.dog.isOwner(this.player)) {
        	this.buttonList.add(new GuiButton(-5, this.width - 64, topY + 65, 42, 20, String.valueOf(this.dog.willObeyOthers())));
        }
        
        this.buttonList.add(new GuiButton(-6, topX + 40, topY + 25, 60, 20, this.dog.mode.getMode().modeName()));
	}
	
	@Override
	public void drawScreen(int xMouse, int yMouse, float partialTickTime) {
		this.drawDefaultBackground();
		//Background
		int topX = this.width / 2;
		int topY = this.height / 2;
		this.fontRendererObj.drawString("New name:", topX - 100, topY + 38, 4210752);
		this.fontRendererObj.drawString("Level: " + this.dog.levels.getLevel(), topX - 65, topY + 75, 0xFF10F9);
		this.fontRendererObj.drawString("Dire Level: " + this.dog.levels.getDireLevel(), topX, topY + 75, 0xFF10F9);
		this.fontRendererObj.drawString("Points Left: " + this.dog.spendablePoints(), topX - 38, topY + 89, 0xFFFFFF);
				
		this.fontRendererObj.drawString("Texture Index", this.width - 80, topY + 20, 0xFFFFFF);
	    if(this.dog.isOwner(this.player))
	    	this.fontRendererObj.drawString("Obey Others?", this.width - 76, topY + 55, 0xFFFFFF);
				
		for(int i = 0; i < this.btnPerPages; ++i) {
			if((this.currentPage * this.btnPerPages + i) >= TalentRegistry.getTalents().size())
		    	continue;
		    this.fontRendererObj.drawString(TalentRegistry.getTalent(this.currentPage * this.btnPerPages + i).getLocalisedName(), 50, 17 + i * 21, 0xFFFFFF);
		}
				
		for(GuiTextField field : this.textfieldList)
			field.drawTextBox();
	    GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	    RenderHelper.disableStandardItemLighting();
	    GL11.glDisable(GL11.GL_LIGHTING);
	    GL11.glDisable(GL11.GL_DEPTH_TEST);
	    super.drawScreen(xMouse, yMouse, partialTickTime);
	    RenderHelper.enableGUIStandardItemLighting();
		
		
		//Foreground

		GL11.glPushMatrix();
	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    for (int k = 0; k < this.buttonList.size(); ++k) {
	    	GuiButton button = (GuiButton)this.buttonList.get(k);
	    	if(button.mousePressed(this.mc, xMouse, yMouse)) {
	    		List list = new ArrayList();
	    		if(button.id >= 1 && button.id <= TalentRegistry.getTalents().size()) {
	    			ITalent talent = TalentRegistry.getTalent(button.id - 1);
	    			 
			    	list.add(TextFormatting.GREEN + talent.getLocalisedName());
			    	list.add("Level: " + this.dog.talents.getLevel(talent));
			    	list.add(TextFormatting.GRAY + "--------------------------------");
		    		list.addAll(this.splitInto(talent.getLocalisedInfo(), 200, this.mc.fontRendererObj));
	    		}
	    		else if(button.id == -1) {
	    			list.add(TextFormatting.ITALIC + "Previous Page");
	    		}
	    		else if(button.id == -2) {
	    			list.add(TextFormatting.ITALIC + "Next Page");
	    		}
	    		else if(button.id == -6) {
    				String str = I18n.translateToLocal("doggui.modeinfo." + button.displayString.toLowerCase());
    				list.addAll(splitInto(str, 150, this.mc.fontRendererObj));
    			}
	    		
	    		this.drawHoveringText(list, xMouse, yMouse, this.mc.fontRendererObj);
	    	}
	    }
		GL11.glPopMatrix();
	}
	
	@Override
    protected void actionPerformed(GuiButton button)  {

		if(button.id >= 1 && button.id <= TalentRegistry.getTalents().size()) {
			ITalent talent = TalentRegistry.getTalent(button.id - 1);
			int level = this.dog.talents.getLevel(talent);
			
			if(level < talent.getHighestLevel(this.dog) && this.dog.spendablePoints() >= talent.getCost(this.dog, level + 1))
				PacketDispatcher.sendToServer(new DogTalentMessage(this.dog.getEntityId(), TalentRegistry.getTalent(button.id - 1).getKey()));
			
		
		}
		else if(button.id == -1) {
			if(this.currentPage > 0) {
    			this.currentPage -= 1;
    			this.initGui();
    		}
		}
		else if(button.id == -2) {
			if(this.currentPage + 1 < this.maxPages) {
				this.currentPage += 1;
    			this.initGui();
    		}
		}
		if (button.id == -4) {
            if(this.doggyTex != 0) {
            	this.doggyTex--;
            }
            else {
            	this.doggyTex = 127;
            }
    		LogHelper.info("action");
            PacketDispatcher.sendToServer(new DogTextureMessage(this.dog.getEntityId(), this.doggyTex));
        }

        if (button.id == -3) {
            if(this.doggyTex != 127) {
            	this.doggyTex++;
            }
            else {
            	this.doggyTex = 0;
            }
            
            PacketDispatcher.sendToServer(new DogTextureMessage(this.dog.getEntityId(), this.doggyTex));
        }
        
        if (button.id == -5) {
        	if(!this.dog.willObeyOthers()) {
        		button.displayString = "true";
        		PacketDispatcher.sendToServer(new DogObeyMessage(this.dog.getEntityId(), true));
        		
        	}
        	else {
        		button.displayString = "false";
        		PacketDispatcher.sendToServer(new DogObeyMessage(this.dog.getEntityId(), false));
        	}
        }
        
        if (button.id == -6) {
        	int newMode = (dog.mode.getMode().ordinal() + 1) % EnumMode.values().length;
        	EnumMode mode = EnumMode.values()[newMode];
        	button.displayString = mode.modeName();
        	PacketDispatcher.sendToServer(new DogModeMessage(this.dog.getEntityId(), newMode));
        }
	}

	@Override
	public void updateScreen() {
		for(GuiTextField field : this.textfieldList)
			field.updateCursorCounter();
	}
	
	@Override
	public void mouseClicked(int xMouse, int yMouse, int mouseButton) throws IOException {	
		super.mouseClicked(xMouse, yMouse, mouseButton);
		for(GuiTextField field : this.textfieldList)
			field.mouseClicked(xMouse, yMouse, mouseButton);
	}
	
	@Override
	public void keyTyped(char character, int keyId) {
		for(GuiTextField field : this.textfieldList)
			field.textboxKeyTyped(character, keyId);
		
		 if (keyId == Keyboard.KEY_ESCAPE)
			 this.mc.player.closeScreen();
	}
	
	@Override
	public void onGuiClosed() {
	    Keyboard.enableRepeatEvents(false);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	public List splitInto(String text, int maxLength, FontRenderer font) {
		List list = new ArrayList(); 
		
		String temp = "";
		String[] split = text.split(" ");
		
		for(int i = 0; i < split.length; ++i) {
			String str = split[i];
			int length = font.getStringWidth(temp + str);
			
			if(length > maxLength) {
				list.add(temp);
				temp = "";
			}
			
			temp += str + " ";
			
			if(i == split.length - 1)
				list.add(temp);
		}

	    return list;
	}
}
