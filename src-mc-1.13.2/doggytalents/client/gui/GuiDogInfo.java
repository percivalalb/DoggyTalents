package doggytalents.client.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.Talent;
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
import net.minecraft.util.math.MathHelper;
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
		this.buttons.clear();
		this.children.clear();
		this.labels.clear();
		this.textfieldList.clear();
		super.initGui();
		
	    this.mc.keyboardListener.enableRepeatEvents(true);
		int topX = this.width / 2;
	    int topY = this.height / 2;
		GuiTextField nameTextField = new GuiTextField(0, this.fontRenderer, topX - 100, topY + 50, 200, 20) {
			@Override
			public boolean charTyped(char character, int keyId) {
				boolean typed = super.charTyped(character, keyId);
				if(typed)
					PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketDogName(GuiDogInfo.this.dog.getEntityId(), this.getText()));
				return typed;
			}
			
			@Override
			public void deleteFromCursor(int index) {
				super.deleteFromCursor(index);
				PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketDogName(GuiDogInfo.this.dog.getEntityId(), this.getText()));
			}
		};
		nameTextField.setFocused(false);
		nameTextField.setMaxStringLength(32);
		if(this.dog.hasCustomName()) nameTextField.setText(this.dog.getCustomName().getUnformattedComponentText());
		this.nameTextField = nameTextField;
		
		this.textfieldList.add(nameTextField);
		this.doggyTex = this.dog.getTameSkin();
		
		int size = DoggyTalentsAPI.TALENTS.getKeys().size();
		
  		this.btnPerPages = Math.max(MathHelper.floor((double)(Minecraft.getInstance().mainWindow.getScaledHeight() - 10) / 21) - 2, 1);
    	
    	if(this.btnPerPages < size) {
    		this.addButton(new GuiButton(-1, 25, this.btnPerPages * 21 + 10, 20, 20, "<") {
    			@Override
    			public void onClick(double mouseX, double mouseY) {
    				if(GuiDogInfo.this.currentPage > 0) {
    					GuiDogInfo.this.currentPage -= 1;
    	    			GuiDogInfo.this.initGui();
    	    		}
    			}
    		});
    	    this.addButton(new GuiButton(-2, 48, this.btnPerPages * 21 + 10, 20, 20, ">") {
    	    	@Override
    	    	public void onClick(double mouseX, double mouseY) {
    	    		if(GuiDogInfo.this.currentPage + 1 < maxPages) {
    	    			GuiDogInfo.this.currentPage += 1;
    					GuiDogInfo.this.initGui();
    	    		}
    	    	}
    	    });
    	}
    	
		this.maxPages = MathHelper.ceil((double)size / this.btnPerPages);
		
		if(this.currentPage >= this.maxPages)
    		this.currentPage = 0;
		
		int i = -1;
		for(Talent talent : DoggyTalentsAPI.TALENTS.getValues()) {
			i++;
			if(i < this.currentPage * this.btnPerPages || i >= (this.currentPage + 1) * this.btnPerPages)
				continue;
    		this.addButton(new GuiTalentButton(1 + i, 25, 10 + (i - this.currentPage * this.btnPerPages) * 21, 20, 20, "+", talent) {
    			@Override
    			public void onClick(double mouseX, double mouseY) {
    				int level = GuiDogInfo.this.dog.TALENTS.getLevel(this.talent);
    				if(level < this.talent.getHighestLevel(GuiDogInfo.this.dog) && GuiDogInfo.this.dog.spendablePoints() >= this.talent.getCost(GuiDogInfo.this.dog, level + 1))
    					PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketDogTalent(GuiDogInfo.this.dog.getEntityId(), this.talent.getRegistryName()));
    			}
    		});
    	}
    	
    	if(ConfigHandler.CLIENT.useDTTextures()) {
	    	this.addButton(new GuiButton(-3, this.width - 42, topY + 30, 20, 20, "+") {
				@Override
				public void onClick(double mouseX, double mouseY) {
					GuiDogInfo.this.doggyTex += 1;
					GuiDogInfo.this.doggyTex %= 128;
					PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketDogTexture(GuiDogInfo.this.dog.getEntityId(), GuiDogInfo.this.doggyTex));
				}
			});
	        this.addButton(new GuiButton(-4, this.width - 64, topY + 30, 20, 20, "-") {
				@Override
				public void onClick(double mouseX, double mouseY) {
					GuiDogInfo.this.doggyTex += 127;
					GuiDogInfo.this.doggyTex %= 128;
					PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketDogTexture(GuiDogInfo.this.dog.getEntityId(), GuiDogInfo.this.doggyTex));
				}
			});
    	}
        if(this.dog.isOwner(this.player))
        	this.addButton(new GuiButton(-5, this.width - 64, topY + 65, 42, 20, String.valueOf(this.dog.willObeyOthers())) {
    	    	@Override
    	    	public void onClick(double mouseX, double mouseY) {
    	    		this.displayString = String.valueOf(!dog.willObeyOthers());
    	        	PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketDogObey(GuiDogInfo.this.dog.getEntityId(), !GuiDogInfo.this.dog.willObeyOthers()));
    	    	}
    	    });
        
        
        this.addButton(new GuiButton(-7, this.width - 64, topY - 5, 42, 20, String.valueOf(this.dog.canFriendlyFire())) {
	    	@Override
	    	public void onClick(double mouseX, double mouseY) {
	    		this.displayString = String.valueOf(!GuiDogInfo.this.dog.canFriendlyFire());
	        	PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketFriendlyFire(GuiDogInfo.this.dog.getEntityId(), !GuiDogInfo.this.dog.canFriendlyFire()));
	    	}
	    });
        
        this.addButton(new GuiButton(-6, topX + 40, topY + 25, 60, 20, I18n.format(this.dog.MODE.getMode().getUnlocalisedName())) {
	    	@Override
	    	public void onClick(double mouseX, double mouseY) {
	    		int newMode = (GuiDogInfo.this.dog.MODE.getMode().ordinal() + 1) % EnumMode.values().length;
	        	EnumMode mode = EnumMode.values()[newMode];
	        	if(mode == EnumMode.WANDERING && !dog.COORDS.hasBowlPos())
	        		this.displayString = TextFormatting.RED + I18n.format(mode.getUnlocalisedName());
	        	else
	        		this.displayString = I18n.format(mode.getUnlocalisedName());
	        	PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketDogMode(GuiDogInfo.this.dog.getEntityId(), newMode));
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
		} 
		else {
			ageRel = I18n.format("doggui.age.adult");
		}
		String ageString = ageValue +" "+ ageRel;
		
		String tamedString = "";
		if(this.dog.isTamed()) {
			if(this.dog.isOwner(this.player)) {
				tamedString = I18n.format("doggui.owner.you");
			} else {
				tamedString = this.dog.getOwnersName().getFormattedText();
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
				
		if(ConfigHandler.CLIENT.useDTTextures()) 
			this.fontRenderer.drawString(I18n.format("doggui.textureindex"), this.width - 80, topY + 20, 0xFFFFFF);
		
	    if(this.dog.isOwner(this.player))
	    	this.fontRenderer.drawString(I18n.format("doggui.obeyothers"), this.width - 76, topY + 55, 0xFFFFFF);
	    
	    this.fontRenderer.drawString(I18n.format("doggui.friendlyfire"), this.width - 76, topY - 15, 0xFFFFFF);
		
		int i = -1;
		for(Talent talent : DoggyTalentsAPI.TALENTS.getValues()) {
			i++;
			if(i < this.currentPage * this.btnPerPages || i >= (this.currentPage + 1) * this.btnPerPages)
				continue;
			this.fontRenderer.drawString(I18n.format(talent.getTranslationKey()), 50, 17 + (i - this.currentPage * this.btnPerPages) * 21, 0xFFFFFF);
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
	    		if(button.id >= 1 && button.id <= DoggyTalentsAPI.TALENTS.getKeys().size()) {
	    			Talent talent = button instanceof GuiTalentButton ? ((GuiTalentButton)button).talent : null;
	    			
			    	list.add(TextFormatting.GREEN + I18n.format(talent.getTranslationKey()));
			    	list.add("Level: " + this.dog.TALENTS.getLevel(talent));
			    	list.add(TextFormatting.GRAY + "--------------------------------");
		    		list.addAll(this.splitInto(I18n.format(talent.getInfoTranslationKey()), 200, this.mc.fontRenderer));
	    		}
	    		if(button.id == -1) {
	    			list.add(TextFormatting.ITALIC + I18n.format("doggui.prevpage"));
	    		}
	    		else if(button.id == -2) {
	    			list.add(TextFormatting.ITALIC + I18n.format("doggui.nextpage"));
	    		}
	    		else if(button.id == -6) {
    				String str = I18n.format(dog.MODE.getMode().getUnlocalisedInfo());
    				list.addAll(splitInto(str, 150, this.mc.fontRenderer));
    				if(this.dog.MODE.isMode(EnumMode.WANDERING)) {
    					if(!this.dog.COORDS.hasBowlPos()) {
    						list.add(TextFormatting.RED + I18n.format("dog.mode.docile.nobowl"));
    					} else if(this.dog.getDistanceSq(this.dog.COORDS.getBowlPos()) > 256.0D) {
    						list.add(TextFormatting.RED + I18n.format("dog.mode.docile.distance", (int)Math.sqrt(this.dog.getPosition().distanceSq(this.dog.COORDS.getBowlPos()))));
    					} else {
    						list.add(TextFormatting.GREEN + I18n.format("dog.mode.docile.bowl", (int)Math.sqrt(this.dog.getPosition().distanceSq(this.dog.COORDS.getBowlPos()))));
    					}
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
		this.mc.keyboardListener.enableRepeatEvents(false);
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
	
	private static class GuiTalentButton extends GuiButton {

		protected Talent talent;
		private GuiTalentButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, Talent talent) {
			super(buttonId, x, y, widthIn, heightIn, buttonText);
			this.talent = talent;
		}
		
	}
}
