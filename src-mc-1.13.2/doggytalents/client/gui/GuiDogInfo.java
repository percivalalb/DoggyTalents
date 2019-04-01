package doggytalents.client.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import doggytalents.api.inferface.ITalent;
import doggytalents.api.registry.TalentRegistry;
import doggytalents.configuration.ConfigHandler;
import doggytalents.entity.EntityDog;
import doggytalents.entity.features.ModeFeature.EnumMode;
import doggytalents.network.PacketHandler;
import doggytalents.network.client.PacketDogMode;
import doggytalents.network.client.PacketDogName;
import doggytalents.network.client.PacketDogObey;
import doggytalents.network.client.PacketDogTalent;
import doggytalents.network.client.PacketDogTexture;
import doggytalents.network.client.PacketFriendlyFire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

/**
 * @author ProPercivalalb
 */
@OnlyIn(Dist.CLIENT)
public class GuiDogInfo extends GuiScreen {

	public EntityDog dog;
	public EntityPlayer player;
	private List<GuiTextField> textfieldList = new ArrayList<GuiTextField>();
	private GuiTextField nameTextField;
	public int doggyTex;
	private int currentPage = 0;
	private int maxPages = 1;
	public int btnPerPages = 0;
	private final DecimalFormat dfShort = new DecimalFormat("0.0");
	private final DecimalFormat dfShortDouble = new DecimalFormat("0.00");
	
	public GuiDogInfo(EntityDog dog, EntityPlayer player) {
		this.dog = dog;
		this.player = player;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttons.clear();
		this.labels.clear();
		this.textfieldList.clear();
		
		//TODO Keyboard.enableRepeatEvents(true);
		int topX = this.width / 2;
	    int topY = this.height / 2;
		GuiTextField nameTextField = new GuiTextField(0, this.fontRenderer, topX - 100, topY + 50, 200, 20) {
			@Override
			public boolean charTyped(char character, int keyId) {
				boolean typed = super.charTyped(character, keyId);
				if(typed)
					PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketDogName(dog.getEntityId(), this.getText()));
				return typed;
			}
			
			@Override
			public void deleteFromCursor(int index) {
				super.deleteFromCursor(index);
				PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketDogName(dog.getEntityId(), this.getText()));
			}
		};
		nameTextField.setFocused(false);
		nameTextField.setMaxStringLength(32);
		if(this.dog.hasCustomName()) nameTextField.setText(this.dog.getCustomName().getUnformattedComponentText());
		this.nameTextField = nameTextField;
		
		this.textfieldList.add(nameTextField);
		this.doggyTex = this.dog.getTameSkin();
		
		int size = TalentRegistry.getTalents().size();
    	
    	int temp = 0;
    	while((temp + 2) * 21 + 10 < Minecraft.getInstance().mainWindow.getScaledHeight())
    		temp += 1;
    	
  		this.btnPerPages = temp;
    	
    	if(temp < size) {
    		this.addButton(new GuiButton(-1, 25, temp * 21 + 10, 20, 20, "<") {
    			@Override
    			public void onClick(double mouseX, double mouseY) {
    				if(currentPage > 0) {
    	    			currentPage -= 1;
    	    			initGui();
    	    		}
    			}
    		});
    	    this.addButton(new GuiButton(-2, 48, temp * 21 + 10, 20, 20, ">") {
    	    	@Override
    	    	public void onClick(double mouseX, double mouseY) {
    	    		if(currentPage + 1 < maxPages) {
    					currentPage += 1;
    	    			initGui();
    	    		}
    	    	}
    	    });
    	}
		
    	if(this.btnPerPages < 1)
    		this.btnPerPages = 1;
    	
		this.maxPages = (int)Math.ceil((double)TalentRegistry.getTalents().size() / (double)this.btnPerPages);
		
		if(this.currentPage >= this.maxPages)
    		this.currentPage = 0;
		
    	for(int i = 0; i < this.btnPerPages; ++i) {
    		final int t = i;
    		if((this.currentPage * this.btnPerPages + i) >= TalentRegistry.getTalents().size())
    			continue;
    		this.addButton(new GuiButton(1 + this.currentPage * this.btnPerPages + i, 25, 10 + i * 21, 20, 20, "+") {
    			@Override
    			public void onClick(double mouseX, double mouseY) {
    				ITalent talent = TalentRegistry.getTalent(currentPage * btnPerPages + t);
    				int level = dog.TALENTS.getLevel(talent);
    				
    				if(level < talent.getHighestLevel(dog) && dog.spendablePoints() >= talent.getCost(dog, level + 1))
    					PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketDogTalent(dog.getEntityId(), TalentRegistry.getTalent(currentPage * btnPerPages + t).getKey()));
    			}
    		});
    	}
    	
    	this.addButton(new GuiButton(-3, this.width - 42, topY + 30, 20, 20, "+") {
			@Override
			public void onClick(double mouseX, double mouseY) {
				doggyTex += 1;
				doggyTex %= 128;
				PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketDogTexture(dog.getEntityId(), doggyTex));
			}
		});
        this.addButton(new GuiButton(-4, this.width - 64, topY + 30, 20, 20, "-") {
			@Override
			public void onClick(double mouseX, double mouseY) {
				doggyTex += 127;
				doggyTex %= 128;
				PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketDogTexture(dog.getEntityId(), doggyTex));
			}
		});
        if(this.dog.isOwner(this.player))
        	this.addButton(new GuiButton(-5, this.width - 64, topY + 65, 42, 20, String.valueOf(this.dog.willObeyOthers())) {
    	    	@Override
    	    	public void onClick(double mouseX, double mouseY) {
    	    		this.displayString = String.valueOf(!dog.willObeyOthers());
    	        	PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketDogObey(dog.getEntityId(), !dog.willObeyOthers()));
    	    	}
    	    });
        
        
        this.addButton(new GuiButton(-7, this.width - 64, topY - 5, 42, 20, String.valueOf(this.dog.canFriendlyFire())) {
	    	@Override
	    	public void onClick(double mouseX, double mouseY) {
	    		this.displayString = String.valueOf(!dog.canFriendlyFire());
	        	PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketFriendlyFire(dog.getEntityId(), !dog.canFriendlyFire()));
	    	}
	    });
        
        this.addButton(new GuiButton(-6, topX + 40, topY + 25, 60, 20, I18n.format(this.dog.MODE.getMode().getUnlocalisedName())) {
	    	@Override
	    	public void onClick(double mouseX, double mouseY) {
	    		int newMode = (dog.MODE.getMode().ordinal() + 1) % EnumMode.values().length;
	        	EnumMode mode = EnumMode.values()[newMode];
	        	if(mode == EnumMode.WANDERING && !dog.COORDS.hasBowlPos())
	        		this.displayString = TextFormatting.RED + I18n.format(mode.getUnlocalisedName());
	        	else
	        		this.displayString = I18n.format(mode.getUnlocalisedName());
	        	PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketDogMode(dog.getEntityId(), newMode));
	    	}
	    });
        
        this.textfieldList.stream().forEach(t -> this.children.add(t));
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		//Background
		int topX = this.width / 2;
		int topY = this.height / 2;
		
		// Background
		String health = dfShort.format(this.dog.getHealth());
		String healthMax = dfShort.format(this.dog.getMaxHealth());
		String healthRel = dfShort.format(this.dog.getHealthRelative() * 100);
		String healthState = health + "/" + healthMax + " (" + healthRel + "%)";
		String speedValue = dfShortDouble.format(this.dog.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
		String ageValue = dfShortDouble.format(this.dog.getGrowingAge());
		String ageRel = "";
		if(this.dog.isChild()) {
			ageRel = I18n.format("doggui.age.baby");
		}else {
			ageRel = I18n.format("doggui.age.adult");
		}
		String ageString = ageValue +" "+ ageRel;
		
		String tamedString = "";
		if (this.dog.isTamed()) {
			if (this.dog.getOwner().getDisplayName().getUnformattedComponentText().equals(this.player.getDisplayName().getUnformattedComponentText()) || this.dog.getOwnerId().toString().equals(this.player.getUniqueID().toString())) {
				tamedString = I18n.format("doggui.owner.you");
			}
			else {
				tamedString = this.dog.getOwner().getDisplayName().getUnformattedComponentText();
			}
		}
		
		this.fontRenderer.drawString(I18n.format("doggui.health") + healthState, this.width - 160, topY - 110, 0xFFFFFF);
		this.fontRenderer.drawString(I18n.format("doggui.speed") + speedValue, this.width - 160, topY - 100, 0xFFFFFF);
		this.fontRenderer.drawString(I18n.format("doggui.owner") + tamedString, this.width - 160, topY - 90, 0xFFFFFF);
		this.fontRenderer.drawString(I18n.format("doggui.age") + ageString, this.width - 160, topY - 80, 0xFFFFFF);
		if(ConfigHandler.COMMON.dogGender()) this.fontRenderer.drawString(I18n.format("doggui.gender") + I18n.format(dog.GENDER.getGenderName()), this.width - 160, topY - 70, 0xFFFFFF);
		
		this.fontRenderer.drawString(I18n.format("doggui.newname"), topX - 100, topY + 38, 4210752);
		this.fontRenderer.drawString(I18n.format("doggui.level") + " " + this.dog.LEVELS.getLevel(), topX - 65, topY + 75, 0xFF10F9);
		this.fontRenderer.drawString(I18n.format("doggui.leveldire") + " " + this.dog.LEVELS.getDireLevel(), topX, topY + 75, 0xFF10F9);
		this.fontRenderer.drawString(I18n.format("doggui.pointsleft") + " " + this.dog.spendablePoints(), topX - 38, topY + 89, 0xFFFFFF);
				
		this.fontRenderer.drawString(I18n.format("doggui.textureindex"), this.width - 80, topY + 20, 0xFFFFFF);
		
	    if(this.dog.isOwner(this.player))
	    	this.fontRenderer.drawString(I18n.format("doggui.obeyothers"), this.width - 76, topY + 55, 0xFFFFFF);
	    
	    this.fontRenderer.drawString(I18n.format("doggui.friendlyfire"), this.width - 76, topY - 15, 0xFFFFFF);
				
		for(int i = 0; i < this.btnPerPages; ++i) {
			if((this.currentPage * this.btnPerPages + i) >= TalentRegistry.getTalents().size())
		    	continue;
		    this.fontRenderer.drawString(TalentRegistry.getTalent(this.currentPage * this.btnPerPages + i).getLocalisedName(), 50, 17 + i * 21, 0xFFFFFF);
		}
				
		for(GuiTextField field : this.textfieldList)
			field.drawTextField(mouseX, mouseY, partialTicks);
		GlStateManager.disableRescaleNormal();
	    RenderHelper.disableStandardItemLighting();
	    GlStateManager.disableLighting();
	    GlStateManager.disableDepthTest();
	    super.render(mouseX, mouseY, partialTicks);
	    RenderHelper.enableGUIStandardItemLighting();
		
		
		//Foreground

	    GlStateManager.pushMatrix();
	    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	    for (int k = 0; k < this.buttons.size(); ++k) {
	    	GuiButton button = (GuiButton)this.buttons.get(k);
	    	if(button.isMouseOver()) {
	    		List<String> list = new ArrayList<String>();
	    		if(button.id >= 1 && button.id <= TalentRegistry.getTalents().size()) {
	    			ITalent talent = TalentRegistry.getTalent(button.id - 1);
	    			
			    	list.add(TextFormatting.GREEN + talent.getLocalisedName());
			    	list.add("Level: " + this.dog.TALENTS.getLevel(talent));
			    	list.add(TextFormatting.GRAY + "--------------------------------");
		    		list.addAll(this.splitInto(talent.getLocalisedInfo(), 200, this.mc.fontRenderer));
	    		}
	    		else if(button.id == -1) {
	    			list.add(TextFormatting.ITALIC + I18n.format("doggui.prevpage"));
	    		}
	    		else if(button.id == -2) {
	    			list.add(TextFormatting.ITALIC + I18n.format("doggui.nextpage"));
	    		}
	    		else if(button.id == -6) {
    				String str = I18n.format(dog.MODE.getMode().getUnlocalisedInfo());
    				list.addAll(splitInto(str, 150, this.mc.fontRenderer));
    				if(this.dog.MODE.isMode(EnumMode.WANDERING)) {
    					if(!this.dog.COORDS.hasBowlPos())
    						list.add(TextFormatting.RED + I18n.format("doggui.mode.docile.nobowl"));
    					else 
    						list.add(TextFormatting.GREEN + I18n.format("doggui.mode.docile.bowl", (int)Math.sqrt(this.dog.getPosition().distanceSq(this.dog.COORDS.getBowlX(), this.dog.COORDS.getBowlY(), this.dog.COORDS.getBowlZ()))));
    				}
    			}
	    		
	    		this.drawHoveringText(list, mouseX, mouseY, this.mc.fontRenderer);
	    	}
	    }
	    GlStateManager.popMatrix();
	}

	@Override
	public void tick() {
		super.tick();
		for(GuiTextField field : this.textfieldList)
			field.tick();
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	    //Keyboard.enableRepeatEvents(false);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	public List<String> splitInto(String text, int maxLength, FontRenderer font) {
		List<String> list = new ArrayList<String>(); 
		
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
