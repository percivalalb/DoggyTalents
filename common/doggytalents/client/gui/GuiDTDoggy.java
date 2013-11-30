package doggytalents.client.gui;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.google.common.base.Strings;

import doggytalents.entity.EntityDTDoggy;
import doggytalents.entity.data.DogMode;
import doggytalents.entity.data.EnumMode;
import doggytalents.entity.data.EnumTalents;
import doggytalents.inventory.ContainerDummy;
import doggytalents.network.PacketTypeHandler;
import doggytalents.network.packet.PacketDoggyMode;
import doggytalents.network.packet.PacketDoggyName;
import doggytalents.network.packet.PacketDoggyTexture;
import doggytalents.network.packet.PacketObeyOthers;
import doggytalents.network.packet.PacketTalentToServer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

/**
 * @author ProPercivalalb
 **/
public class GuiDTDoggy extends GuiContainer {
	
	private String TITLE = "Name You Pet Dog";
    private GuiTextField txt_petName;
    private String startText;
    private int level = 0;
    private int direLevel = 0;
    private EntityDTDoggy dog;
    private ScaledResolution resolution;
    
    private String BlackPeltStatement;
    private String GuardDogStatement;
    private String HunterDogStatement;
    private String HellHoundStatement;
    private String WolfMountStatement;
    private String PackPuppyStatement;
    private String QuickHealerStatement;
    private String PillowPawStatement;
    private String CreeperSweeperStatement;
    private String DoggyDashStatement;
    private String FisherDogStatement;
    private String HappyEaterStatement;
    private String BedFinderStatement;
    private String RescueDogStatement;
    private String PestFighterStatement;
    private String ShepherDogStatement;
    private String PoisonFangStatement;
    private String PuppyEyesStatement;
    private String MasteryStatement;
    private String PointsStatement;
    private String DoggyTexStatement;
    protected String screenTitle;
    
    private int pointsToSpend;
    
    private int blackPeltLevel;
    private int guardDogLevel;
    private int hunterDogLevel;
    private int hellHoundLevel;
    private int wolfMountLevel;
    private int packPuppyLevel;
    private int quickHealerLevel;
    private int pillowPawLevel;
    private int creeperSweeperLevel;
    private int doggyDashLevel;
    private int fisherDogLevel;
    private int happyEaterLevel;
    private int bedFinderLevel;
    private int pestFighterLevel;
    private int rescueDogLevel;
    private int posionFangLevel;
    private int ShepherDogLevel;
    private int puppyEyesLevel;
    private int Level;
    
    private String completeColour = "\u00A76";
    private String unCompleteColour = "";
   
    
    private EntityPlayer player;
	public int doggyTex;
	public int id;
    
	public GuiDTDoggy(EntityDTDoggy par1DTDoggy, String par2, int par3, EntityPlayer par4Player, int entityID) {
		super(new ContainerDummy());
		this.player = par4Player;
		this.dog = par1DTDoggy;
		this.startText = par2;
		this.level = par3;
		this.id = entityID;
		this.direLevel = par1DTDoggy.level.getDireLevel();
		this.screenTitle = "Doggy Talents";
		this.pointsToSpend = par1DTDoggy.spendablePoints();
		this.updateLevels();
		this.doggyTex = par1DTDoggy.getTameSkin();
		this.redoScreenText();
	}
	
	public void updateScreen()
	{
		if(dog == null || dog.isDead) {
			this.mc.displayGuiScreen(null);
			this.mc.setIngameFocus();
		}
		if(dog != null && !dog.getOwnerName().equalsIgnoreCase(player.username) && !dog.willObeyOthers()) {
			this.mc.displayGuiScreen(null);
			this.mc.setIngameFocus();
			player.addChatMessage("This dog will no longer obey you.");
		}
		
	    this.txt_petName.updateCursorCounter();
	    this.updateLevels();
	    this.redoScreenText();
	}
	
	public void redoScreenText() {
	    BlackPeltStatement = (this.blackPeltLevel == 5 ? completeColour : unCompleteColour) + "Black Pelt: " + this.blackPeltLevel;
        GuardDogStatement = (this.guardDogLevel == 5 ? completeColour : unCompleteColour) + "Guard Dog: " + this.guardDogLevel;
        HunterDogStatement = (this.hunterDogLevel == 5 ? completeColour : unCompleteColour) + "Hunter Dog: " + this.hunterDogLevel;
        HellHoundStatement = (this.hellHoundLevel == 5 ? completeColour : unCompleteColour) + "Hell Hound: " + this.hellHoundLevel;
        WolfMountStatement = (this.hellHoundLevel == 5 ? completeColour : unCompleteColour) + "Wolf Mount: " + this.wolfMountLevel;
        PackPuppyStatement = (this.packPuppyLevel == 5 ? completeColour : unCompleteColour) + "Pack Puppy: " + this.packPuppyLevel;
        QuickHealerStatement = (this.quickHealerLevel == 5 ? completeColour : unCompleteColour) + "Quick Healer: " + this.quickHealerLevel;
        PillowPawStatement = (this.pillowPawLevel == 5 ? completeColour : unCompleteColour) + "Pillow Paw: " + this.pillowPawLevel;
        CreeperSweeperStatement = (this.creeperSweeperLevel == 5 ? completeColour : unCompleteColour) + "Creeper Sweeper: " + this.creeperSweeperLevel;
        DoggyDashStatement = (this.doggyDashLevel == 5 ? completeColour : unCompleteColour) + "Doggy Dash: " + this.doggyDashLevel;
        FisherDogStatement = (this.fisherDogLevel == 5 ? completeColour : unCompleteColour) + "Fisher Dog: " + this.fisherDogLevel;
        HappyEaterStatement = (this.happyEaterLevel == 5 ? completeColour : unCompleteColour) + "Happy Eater: " + this.happyEaterLevel;
        BedFinderStatement = (this.bedFinderLevel == 5 ? completeColour : unCompleteColour) + "Bed Finder: " + this.bedFinderLevel;
        PestFighterStatement = (this.pestFighterLevel == 5 ? completeColour : unCompleteColour) + "Pest Fighter: " + this.pestFighterLevel;
        RescueDogStatement = (this.rescueDogLevel == 5 ? completeColour : unCompleteColour) + "Rescue Dog: " + this.rescueDogLevel;
        PoisonFangStatement = (this.posionFangLevel == 5 ? completeColour : unCompleteColour) + "Poison Fang: " + this.posionFangLevel;
        ShepherDogStatement = (this.ShepherDogLevel == 5 ? completeColour : unCompleteColour) + "Shepher Dog: " + this.ShepherDogLevel;
        PuppyEyesStatement = (this.puppyEyesLevel == 5 ? completeColour : unCompleteColour) + "Puppy Eyes: " + this.puppyEyesLevel;
        DoggyTexStatement = "DoggyTex: " + dog.getTameSkin();
        PointsStatement = "Points to spend: " + dog.spendablePoints();
        level = dog.level.getLevel();
        direLevel = dog.level.getDireLevel();
        if(this.buttonList.contains(21)) {
        	if(dog.willObeyOthers())
        		((GuiButton)this.buttonList.get(21)).displayString = "true";
        	else
        		((GuiButton)this.buttonList.get(21)).displayString = "false";
        }
        
        //if(this.buttonList.contains(22)) {
        //	((GuiButton)this.buttonList.get(22)).displayString = dog.mode.getMode().modeName();
        //}
	}
	
	public void updateLevels() {
		this.blackPeltLevel = this.dog.talents.getTalentLevel(EnumTalents.BLACKPELT);
		this.guardDogLevel = this.dog.talents.getTalentLevel(EnumTalents.GUARDDOG);
		this.rescueDogLevel = this.dog.talents.getTalentLevel(EnumTalents.RESCUEDOG);
		this.creeperSweeperLevel = this.dog.talents.getTalentLevel(EnumTalents.CREEPERSWEEPER);
		this.quickHealerLevel = this.dog.talents.getTalentLevel(EnumTalents.QUICKHEALER);
		this.doggyDashLevel = this.dog.talents.getTalentLevel(EnumTalents.DOGGYDASH);
		this.hellHoundLevel = this.dog.talents.getTalentLevel(EnumTalents.HELLHOUND);
		this.happyEaterLevel = this.dog.talents.getTalentLevel(EnumTalents.HAPPYEATER);
		this.pillowPawLevel = this.dog.talents.getTalentLevel(EnumTalents.PILLOWPAW);
		this.wolfMountLevel = this.dog.talents.getTalentLevel(EnumTalents.WOLFMOUNT);
		this.posionFangLevel = this.dog.talents.getTalentLevel(EnumTalents.POSIONFANG);
		this.hunterDogLevel = this.dog.talents.getTalentLevel(EnumTalents.HUNTERDOG);
		this.bedFinderLevel = this.dog.talents.getTalentLevel(EnumTalents.BEDFINDER);
		this.pestFighterLevel = this.dog.talents.getTalentLevel(EnumTalents.PESTFIGHTER);
		this.ShepherDogLevel = this.dog.talents.getTalentLevel(EnumTalents.SHEPHERDDOG);
		this.fisherDogLevel = this.dog.talents.getTalentLevel(EnumTalents.FISHERDOG);
		this.puppyEyesLevel = this.dog.talents.getTalentLevel(EnumTalents.PUPPYEYES);
		this.packPuppyLevel = this.dog.talents.getTalentLevel(EnumTalents.PACKPUPPY);
	}

	@Override
	public void initGui() {
		guiLeft = 0;
		this.resolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
	    Keyboard.enableRepeatEvents(true);
	    this.buttonList.clear();
	    int var1 = this.width / 2 - 100;
	    int var2 = this.height / 2 - 50 + 47 + 100;
	    this.txt_petName = new GuiTextField(this.fontRenderer, var1, var2, 200, 20);
	    this.txt_petName.setFocused(false);
	    this.txt_petName.setMaxStringLength(32);
	    try {this.txt_petName.setText(startText);}
	    catch (Exception e) {}
	    
	    this.buttonList.add(new GuiCustomButton(1, width / 3 - 25, 5, 20, 20, "+"));
	    this.buttonList.add(new GuiCustomButton(2, (width * 2) / 3 - 25, 5, 20, 20, "+"));
	    this.buttonList.add(new GuiCustomButton(3, width - 25, 5, 20, 20, "+"));
	    this.buttonList.add(new GuiCustomButton(4, width / 3 - 25, 30, 20, 20, "+"));
	    this.buttonList.add(new GuiCustomButton(5, (width * 2) / 3 - 25, 30, 20, 20, "+"));
	    this.buttonList.add(new GuiCustomButton(6, width - 25, 30, 20, 20, "+"));
	    this.buttonList.add(new GuiCustomButton(7, width / 3 - 25, 55, 20, 20, "+"));
	    this.buttonList.add(new GuiCustomButton(8, (width * 2) / 3 - 25, 55, 20, 20, "+"));
	    this.buttonList.add(new GuiCustomButton(9, width - 25, 55, 20, 20, "+"));
	    this.buttonList.add(new GuiCustomButton(10, width / 3 - 25, 80, 20, 20, "+"));
	    this.buttonList.add(new GuiCustomButton(11, (width * 2) / 3 - 25, 80, 20, 20, "+"));
	    this.buttonList.add(new GuiCustomButton(12, width - 25, 80, 20, 20, "+"));
	    this.buttonList.add(new GuiCustomButton(13, width / 3 - 25, 105, 20, 20, "+"));
	    this.buttonList.add(new GuiCustomButton(14, (width * 2) / 3 - 25, 105, 20, 20, "+"));
	    this.buttonList.add(new GuiCustomButton(15, width - 25, 105, 20, 20, "+"));
	    this.buttonList.add(new GuiCustomButton(16, width / 3 - 25, 130, 20, 20, "+"));
	    this.buttonList.add(new GuiCustomButton(17, (width * 2) / 3 - 25, 130, 20, 20, "+"));
	    this.buttonList.add(new GuiCustomButton(18, width - 25, 130, 20, 20, "+"));
	    
        this.buttonList.add(new GuiButton(19, width - 22, 170, 20, 20, "+"));
        this.buttonList.add(new GuiButton(20, width - 44, 170, 20, 20, "-"));
        if(dog.getOwnerName().equalsIgnoreCase(player.username)) {
        	this.buttonList.add(new GuiButton(21, width - 44, 195, 42, 20, String.valueOf(dog.willObeyOthers())));
        }
        
        this.buttonList.add(new GuiCustomButton(22, 5, 195, 60, 20, dog.mode.getMode().modeName()));
	}
	    
	@Override
	public void onGuiClosed() {
	    Keyboard.enableRepeatEvents(false);
	}
	
	@Override
    protected void actionPerformed(GuiButton par1GuiButton)  {
    	if (par1GuiButton.id == 1 && this.dog.talents.getTalentLevel(EnumTalents.BLACKPELT) < 5 && dog.spendablePoints() >= this.dog.talents.getTalentLevel(EnumTalents.BLACKPELT) + 1) {
    		this.sendNewTalentToServer(0);
        }
    	if (par1GuiButton.id == 2 && this.dog.talents.getTalentLevel(EnumTalents.GUARDDOG) < 5 && dog.spendablePoints() >= this.dog.talents.getTalentLevel(EnumTalents.GUARDDOG)) {
    		this.sendNewTalentToServer(1);
        }
    	if (par1GuiButton.id == 3 && dog.talents.getTalentLevel(EnumTalents.HUNTERDOG) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumTalents.HUNTERDOG) + 1){
    		this.sendNewTalentToServer(2);
        }
    	if (par1GuiButton.id == 4 && dog.talents.getTalentLevel(EnumTalents.HELLHOUND) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumTalents.HELLHOUND) + 1){
    		this.sendNewTalentToServer(3);
        }
    	if (par1GuiButton.id == 5 && dog.talents.getTalentLevel(EnumTalents.WOLFMOUNT) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumTalents.WOLFMOUNT) + 1) {
    		this.sendNewTalentToServer(4);
        }
    	if (par1GuiButton.id == 6 && dog.talents.getTalentLevel(EnumTalents.PACKPUPPY) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumTalents.PACKPUPPY) + 1) {
    		this.sendNewTalentToServer(5);
        }
    	if (par1GuiButton.id == 7 && dog.talents.getTalentLevel(EnumTalents.PILLOWPAW) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumTalents.PILLOWPAW) + 1) {
    		this.sendNewTalentToServer(6);
    	}
    	if (par1GuiButton.id == 8 && dog.talents.getTalentLevel(EnumTalents.QUICKHEALER) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumTalents.QUICKHEALER) + 1) {
    		this.sendNewTalentToServer(7);
        }
    	if (par1GuiButton.id == 9 && dog.talents.getTalentLevel(EnumTalents.CREEPERSWEEPER) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumTalents.CREEPERSWEEPER) + 1) {
    		this.sendNewTalentToServer(8);
        }
    	if (par1GuiButton.id == 10 && dog.talents.getTalentLevel(EnumTalents.DOGGYDASH) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumTalents.DOGGYDASH) + 1) {
    		this.sendNewTalentToServer(9);
        }
    	if (par1GuiButton.id == 11 && dog.talents.getTalentLevel(EnumTalents.FISHERDOG) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumTalents.FISHERDOG) + 1) {
    		this.sendNewTalentToServer(10);
        }
    	if (par1GuiButton.id == 12 && dog.talents.getTalentLevel(EnumTalents.HAPPYEATER) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumTalents.HAPPYEATER) + 1) {
    		this.sendNewTalentToServer(11);
        }
    	if (par1GuiButton.id == 13 && dog.talents.getTalentLevel(EnumTalents.BEDFINDER) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumTalents.BEDFINDER) + 1) {
    		this.sendNewTalentToServer(12);
        }
    	if (par1GuiButton.id == 14 && dog.talents.getTalentLevel(EnumTalents.PESTFIGHTER) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumTalents.PESTFIGHTER) + 1) {
    		this.sendNewTalentToServer(13);
        }
    	if (par1GuiButton.id == 15 && dog.talents.getTalentLevel(EnumTalents.POSIONFANG) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumTalents.POSIONFANG) + 1){
    		this.sendNewTalentToServer(14);
        }
    	if (par1GuiButton.id == 16 && dog.talents.getTalentLevel(EnumTalents.SHEPHERDDOG) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumTalents.SHEPHERDDOG) + 1) {
    		this.sendNewTalentToServer(15);
        }
    	if (par1GuiButton.id == 17 && dog.talents.getTalentLevel(EnumTalents.RESCUEDOG) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumTalents.RESCUEDOG) + 1) {
    		this.sendNewTalentToServer(16);
        }
    	if (par1GuiButton.id == 18 && dog.talents.getTalentLevel(EnumTalents.PUPPYEYES) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumTalents.PUPPYEYES) + 1) {
    		this.sendNewTalentToServer(17);
        }
    	
    	if (par1GuiButton.id == 20) {
            if (doggyTex != 0) {
                doggyTex--;
            }
            else {
                doggyTex = 16;
            }
            
            this.changeDoggyTexture(doggyTex);
        }

        if (par1GuiButton.id == 19) {
            if (doggyTex != 16) {
                doggyTex++;
            }
            else {
                doggyTex = 0;
            }
            
            this.changeDoggyTexture(doggyTex);
        }
        
        if (par1GuiButton.id == 21) {
        	if(!dog.willObeyOthers()) {
        		par1GuiButton.displayString = "true";
        		this.changeWillObey(true);
        		
        	}
        	else {
        		par1GuiButton.displayString = "false";
        		this.changeWillObey(false);
        	}
        }
        
        if (par1GuiButton.id == 22) {
        	if(dog.mode.isMode(EnumMode.DOCILE)) {
        		par1GuiButton.displayString = EnumMode.WANDERING.modeName();
        		this.changeMode(EnumMode.WANDERING);
        	}
        	else if(dog.mode.isMode(EnumMode.WANDERING)) {
        		par1GuiButton.displayString = EnumMode.TACTICAL.modeName();
        		this.changeMode(EnumMode.TACTICAL);
        	}
        	else if(dog.mode.isMode(EnumMode.TACTICAL)) {
        		par1GuiButton.displayString = EnumMode.AGGRESIVE.modeName();
        		this.changeMode(EnumMode.AGGRESIVE);
        	}
        	else if(dog.mode.isMode(EnumMode.AGGRESIVE)) {
        		par1GuiButton.displayString = EnumMode.BERSERKER.modeName();
        		this.changeMode(EnumMode.BERSERKER);
        	}
        	else {
        		par1GuiButton.displayString = EnumMode.DOCILE.modeName();
        		this.changeMode(EnumMode.DOCILE);
        	}
        }
    }
    
    public void changeDoggyTexture(int var1) {
    	PacketTypeHandler.populatePacketAndSendToServer(new PacketDoggyTexture(id, var1));
    }
    
    public void changeMode(EnumMode mode) {
    	PacketTypeHandler.populatePacketAndSendToServer(new PacketDoggyMode(id, DogMode.getId(mode)));
    }
    
    public void changeWillObey(boolean var1) {
    	PacketTypeHandler.populatePacketAndSendToServer(new PacketObeyOthers(id, var1));
    }
    
    public void sendNewTalentToServer(int talentId) {
    	PacketTypeHandler.populatePacketAndSendToServer(new PacketTalentToServer(id, talentId));
    }

    @Override
	protected void keyTyped(char var1, int var2) {
	    this.txt_petName.textboxKeyTyped(var1, var2);
	    
	    if (var2 == Keyboard.KEY_ESCAPE) {
            this.mc.thePlayer.closeScreen();
        }
	    
        String var4 = this.txt_petName.getText().trim();
        this.sendNewNameToServer(var4);
	}

    @Override
	protected void mouseClicked(int var1, int var2, int var3)
	{
	    super.mouseClicked(var1, var2, var3);
	    this.txt_petName.mouseClicked(var1, var2, var3);
	    if (var3 == 1)
        {
            for (int l = 0; l < this.buttonList.size(); ++l)
            {
                GuiButton guibutton = (GuiButton)this.buttonList.get(l);

                if (guibutton instanceof GuiDTButton && ((GuiDTButton)guibutton).rightClick(this.mc, var1, var2))
                {
                	((GuiDTButton)guibutton).lastClicked = Minecraft.getSystemTime();
                    this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                }
            }
        }
	}

	protected void sendNewNameToServer(String var1)
	{
		PacketTypeHandler.populatePacketAndSendToServer(new PacketDoggyName(id, var1));
	}
	
	@Override
	public boolean doesGuiPauseGame() {;
	    return false;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		
	}
	
	public float getScaleFactor() {
		int scale = resolution.getScaleFactor();
		if(scale == 1)
			return 1F;
		if(scale == 2)
			return 1F;
		if(scale == 3)
			return 1F / 3F * 2F;
		return 1F;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
	     int var4 = this.width / 2 - this.fontRenderer.getStringWidth(this.TITLE) / 2;
	     int var5 = this.height / 2 - 50 + 20  + 100;
	     var4 = this.width / 2 - 100;
	     var5 = this.height / 2 - 50 + 35 + 100;
	     this.fontRenderer.drawString("New name:", var4, var5, 4210752);
	     this.drawString(fontRenderer, "Level: " + level, 0 + 10, 160, 0xff10f9);
	     this.drawString(fontRenderer, "Level Dire: " + direLevel, 0 + 10, 160 + 15, 0xff10f9);
	     this.txt_petName.drawTextBox();
	     drawString(fontRenderer, BlackPeltStatement, 0 + 10, 10, 0xffffff);
	     drawString(fontRenderer, GuardDogStatement, width / 3, 10, 0xffffff);
	     drawString(fontRenderer, HunterDogStatement, (2 * width) / 3, 10, 0xffffff);
	     drawString(fontRenderer, HellHoundStatement, 0 + 10, 35, 0xffffff);
	     drawString(fontRenderer, WolfMountStatement, width / 3, 35, 0xffffff);
	     drawString(fontRenderer, PackPuppyStatement, (2 * width) / 3, 35, 0xffffff);
	     drawString(fontRenderer, PillowPawStatement, 0 + 10, 60, 0xffffff);
	     drawString(fontRenderer, QuickHealerStatement, width / 3, 60, 0xffffff);
	     drawString(fontRenderer, CreeperSweeperStatement, (2 * width) / 3, 60, 0xffffff);
	     drawString(fontRenderer, DoggyDashStatement, 0 + 10, 85, 0xffffff);
	     drawString(fontRenderer, FisherDogStatement, width / 3, 85, 0xffffff);
	     drawString(fontRenderer, HappyEaterStatement, (2 * width) / 3, 85, 0xffffff);
	     drawString(fontRenderer, BedFinderStatement, 0 + 10, 110, 0xffffff);
	     drawString(fontRenderer, PestFighterStatement, width / 3, 110, 0xffffff);
	     drawString(fontRenderer, PoisonFangStatement, (2 * width) / 3, 110, 0xffffff);
	     drawString(fontRenderer, ShepherDogStatement, 0 + 10, 135, 0xffffff);
	     drawString(fontRenderer, RescueDogStatement, width / 3, 135, 0xffffff);
	     drawString(fontRenderer, PuppyEyesStatement, (2 * width) / 3, 135, 0xffffff);
	     drawString(fontRenderer, PointsStatement, width / 3, 160, 0xffffff);
	     drawString(fontRenderer, DoggyTexStatement, width - 115, 175, 0xffffff);
	     if(dog.getOwnerName().equalsIgnoreCase(player.username)) {
	    	 drawString(fontRenderer, "Obey Others", width - 115, 200, 0xffffff);
	     }
	     drawCenteredString(fontRenderer, screenTitle, width / 2, 200, 0xffffff);
	     GL11.glPushMatrix();
	     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		 GL11.glScaled(getScaleFactor(), getScaleFactor(), getScaleFactor());
	     for (int k = 0; k < this.buttonList.size(); ++k) {
	    	 GuiButton guibutton = (GuiButton)this.buttonList.get(k);
	    	 if(guibutton instanceof GuiCustomButton) {
	    		 GuiCustomButton btn = (GuiCustomButton)guibutton;
	    		 if(btn.isMouseAbove(mouseX, mouseY)) {
		    		List list = new ArrayList();
	    			if(btn.id >= 1 && btn.id <= 18)
			    		list.add(I18n.getString("dogGui.talentName." + btn.id));
	    			String str = I18n.getString("dogGui.buttonInfo." + btn.id);
	    			list.addAll(splitInto(str, 150, this.mc.fontRenderer));
	    			this.drawHoveringText(list, mouseX, mouseY, this.mc.fontRenderer);
	         	}
	    	 }
	     }
		 GL11.glPopMatrix();
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
		}

	        
	    return list;
	}
	
	protected void drawHoveringText(List par1List, int xMouse, int yMouse, FontRenderer font) {
		if (!par1List.isEmpty()) {
			xMouse = (int)(xMouse / this.getScaleFactor());
			yMouse = (int)(yMouse / this.getScaleFactor());
	        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	        RenderHelper.disableStandardItemLighting();
	        GL11.glDisable(GL11.GL_LIGHTING);
	        GL11.glDisable(GL11.GL_DEPTH_TEST);
	        int k = 0;
	        Iterator iterator = par1List.iterator();

	        while (iterator.hasNext()) {
	            String s = (String)iterator.next();
	            int l = font.getStringWidth(s);

	            if (l > k) {
	                k = l;
	            }
	        }

	        int i1 = xMouse + 12;
	        int j1 = yMouse - 12;
	        int k1 = 8;

	        if (par1List.size() > 1) {
	                k1 += 2 + (par1List.size() - 1) * 10;
	            }

	        if (i1 + k > this.width) {
	            i1 -= 28 + k;
	        }

	        if (j1 + k1 + 6 > this.height)
	        {
	            j1 = this.height - k1 - 6;
	        }

	        this.zLevel = 300.0F;
	        itemRenderer.zLevel = 300.0F;
	        int l1 = -267386864;
	        this.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
	        this.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
	        this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
	        this.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
	        this.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
	        int i2 = 1347420415;
	        int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
	        this.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
	        this.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
	        this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
	        this.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

	        for (int k2 = 0; k2 < par1List.size(); ++k2)
	        {
	        	String s1 = (String)par1List.get(k2);
	            font.drawStringWithShadow(s1, i1, j1, -1);

	            if (k2 == 0)
	            {
	                j1 += 2;
	            }

	                j1 += 10;
	            }

	            this.zLevel = 0.0F;
	            itemRenderer.zLevel = 0.0F;
	            GL11.glEnable(GL11.GL_LIGHTING);
	            GL11.glEnable(GL11.GL_DEPTH_TEST);
	            RenderHelper.enableStandardItemLighting();
	            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	        }
	    }
}
