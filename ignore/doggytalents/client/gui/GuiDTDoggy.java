package doggytalents.client.gui;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.google.common.base.Strings;

import doggytalents.api.Properties;
import doggytalents.entity.EntityDTDoggy;
import doggytalents.entity.EnumSkills;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.MathHelper;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class GuiDTDoggy extends GuiScreen
{
    private String TITLE = "Name You Pet Dog";
    private GuiTextField txt_petName;
    private String startText;
    private int level = 0;
    private int direLevel = 0;
    private EntityDTDoggy dog;
    
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
    
	public GuiDTDoggy(EntityDTDoggy par1DTDoggy, String par2, int par3, EntityPlayer par4Player, int entityID)
	{
		player = par4Player;
		dog = par1DTDoggy;
		startText = par2;
		level = par3;
		id = entityID;
		direLevel = par1DTDoggy.level.getDireLevel();
		screenTitle = "Doggy Talents";
		pointsToSpend = par1DTDoggy.spendablePoints();
		this.updateLevels();
		this.doggyTex = par1DTDoggy.getTameSkin();
		this.redoScreenText();
	}

	@Override
	public void drawScreen(int par1, int par2, float par3)
    {
	     this.drawDefaultBackground();
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
	     if(dog.getOwner().equalsIgnoreCase(player.username)) {
	    	 drawString(fontRenderer, "Obey Others", width - 115, 200, 0xffffff);
	     }
	     drawCenteredString(fontRenderer, screenTitle, width / 2, 200, 0xffffff);
		 super.drawScreen(par1, par2, par3);
    }
	
	public void updateScreen()
	{
		if(dog == null || dog.isDead) {
			this.mc.displayGuiScreen(null);
			this.mc.setIngameFocus();
		}
		if(dog != null && !dog.getOwner().equalsIgnoreCase(player.username) && !dog.willObeyOthers()) {
			this.mc.displayGuiScreen(null);
			this.mc.setIngameFocus();
			player.addChatMessage("This dog will no longer obey you.");
		}
		
	    this.txt_petName.updateCursorCounter();
	    this.updateLevels();
	    this.redoScreenText();
	}
	
	public void redoScreenText() {
	    BlackPeltStatement = (this.blackPeltLevel == 5 ? completeColour : unCompleteColour) + "BlackPelt: " + this.blackPeltLevel;
        GuardDogStatement = (this.guardDogLevel == 5 ? completeColour : unCompleteColour) + "GuardDog: " + this.guardDogLevel;
        HunterDogStatement = (this.hunterDogLevel == 5 ? completeColour : unCompleteColour) + "HunterDog: " + this.hunterDogLevel;
        HellHoundStatement = (this.hellHoundLevel == 5 ? completeColour : unCompleteColour) + "HellHound: " + this.hellHoundLevel;
        //WolfMountStatement = "WolfMount: " + this.wolfMountLevel;
        PackPuppyStatement = (this.packPuppyLevel == 5 ? completeColour : unCompleteColour) + "PackPuppy: " + this.packPuppyLevel;
        QuickHealerStatement = (this.quickHealerLevel == 5 ? completeColour : unCompleteColour) + "QuickHealer: " + this.quickHealerLevel;
        PillowPawStatement = (this.pillowPawLevel == 5 ? completeColour : unCompleteColour) + "PillowPaw: " + this.pillowPawLevel;
        CreeperSweeperStatement = (this.creeperSweeperLevel == 5 ? completeColour : unCompleteColour) + "CreeperSweeper: " + this.creeperSweeperLevel;
        DoggyDashStatement = (this.doggyDashLevel == 5 ? completeColour : unCompleteColour) + "DoggyDash: " + this.doggyDashLevel;
        FisherDogStatement = (this.fisherDogLevel == 5 ? completeColour : unCompleteColour) + "FisherDog: " + this.fisherDogLevel;
        HappyEaterStatement = (this.happyEaterLevel == 5 ? completeColour : unCompleteColour) + "HappyEater: " + this.happyEaterLevel;
        BedFinderStatement = (this.bedFinderLevel == 5 ? completeColour : unCompleteColour) + "BedFinder: " + this.bedFinderLevel;
        PestFighterStatement = (this.pestFighterLevel == 5 ? completeColour : unCompleteColour) + "PestFighter: " + this.pestFighterLevel;
        RescueDogStatement = (this.rescueDogLevel == 5 ? completeColour : unCompleteColour) + "RescueDog: " + this.rescueDogLevel;
        PoisonFangStatement = (this.posionFangLevel == 5 ? completeColour : unCompleteColour) + "PoisonFang: " + this.posionFangLevel;
        ShepherDogStatement = (this.ShepherDogLevel == 5 ? completeColour : unCompleteColour) + "ShepherDog: " + this.ShepherDogLevel;
        PuppyEyesStatement = (this.puppyEyesLevel == 5 ? completeColour : unCompleteColour) + "PuppyEyes: " + this.puppyEyesLevel;
        DoggyTexStatement = "DoggyTex: " + dog.getTameSkin();
        PointsStatement = "Points to spend: " + dog.spendablePoints();
        level = dog.level.getLevel();
        direLevel = dog.level.getDireLevel();
        if(this.buttonList.contains(21)) {
        	if(dog.willObeyOthers()) {
        		((GuiButton)this.buttonList.get(21)).displayString = "true";
        	}
        	else {
        		((GuiButton)this.buttonList.get(21)).displayString = "false";
        	}
        }
	}
	
	public void updateLevels() {
		this.blackPeltLevel = this.dog.talents.getTalentLevel(EnumSkills.BLACKPELT);
		this.guardDogLevel = this.dog.talents.getTalentLevel(EnumSkills.GUARDDOG);
		this.rescueDogLevel = this.dog.talents.getTalentLevel(EnumSkills.RESCUEDOG);
		this.creeperSweeperLevel = this.dog.talents.getTalentLevel(EnumSkills.CREEPERSWEEPER);
		this.quickHealerLevel = this.dog.talents.getTalentLevel(EnumSkills.QUICKHEALER);
		this.doggyDashLevel = this.dog.talents.getTalentLevel(EnumSkills.DOGGYDASH);
		this.hellHoundLevel = this.dog.talents.getTalentLevel(EnumSkills.HELLHOUND);
		this.happyEaterLevel = this.dog.talents.getTalentLevel(EnumSkills.HAPPYEATER);
		this.pillowPawLevel = this.dog.talents.getTalentLevel(EnumSkills.PILLOWPAW);
		this.wolfMountLevel = this.dog.talents.getTalentLevel(EnumSkills.WOLFMOUNT);
		this.posionFangLevel = this.dog.talents.getTalentLevel(EnumSkills.POSIONFANG);
		this.hunterDogLevel = this.dog.talents.getTalentLevel(EnumSkills.HUNTERDOG);
		this.bedFinderLevel = this.dog.talents.getTalentLevel(EnumSkills.BEDFINDER);
		this.pestFighterLevel = this.dog.talents.getTalentLevel(EnumSkills.PESTFIGHTER);
		this.ShepherDogLevel = this.dog.talents.getTalentLevel(EnumSkills.SHEPHERDOG);
		this.fisherDogLevel = this.dog.talents.getTalentLevel(EnumSkills.FISHERDOG);
		this.puppyEyesLevel = this.dog.talents.getTalentLevel(EnumSkills.PUPPYEYES);
		this.packPuppyLevel = this.dog.talents.getTalentLevel(EnumSkills.PACKPUPPY);
	}

	public void initGui()
	{
	    Keyboard.enableRepeatEvents(true);
	    this.buttonList.clear();
	    int var1 = this.width / 2 - 100;
	    int var2 = this.height / 2 - 50 + 47 + 100;
	    this.txt_petName = new GuiTextField(this.fontRenderer, var1, var2, 200, 20);
	    this.txt_petName.setFocused(false);
	    this.txt_petName.setMaxStringLength(32);
	    
	    try
	    {
	    	this.txt_petName.setText(startText);
	    }
	    catch (Exception var6)
		{
	    	
		}
	    
	    this.buttonList.add(new GuiDTButton(1, width / 3 - 25, 5, 20, 20, "+"));
	    this.buttonList.add(new GuiDTButton(2, (width * 2) / 3 - 25, 5, 20, 20, "+"));
	    this.buttonList.add(new GuiDTButton(3, width - 25, 5, 20, 20, "+"));
	    this.buttonList.add(new GuiDTButton(4, width / 3 - 25, 30, 20, 20, "+"));
	    this.buttonList.add(new GuiDTButton(5, (width * 2) / 3 - 25, 30, 20, 20, "+"));
	    this.buttonList.add(new GuiDTButton(6, width - 25, 30, 20, 20, "+"));
	    this.buttonList.add(new GuiDTButton(7, width / 3 - 25, 55, 20, 20, "+"));
	    this.buttonList.add(new GuiDTButton(8, (width * 2) / 3 - 25, 55, 20, 20, "+"));
	    this.buttonList.add(new GuiDTButton(9, width - 25, 55, 20, 20, "+"));
	    this.buttonList.add(new GuiDTButton(10, width / 3 - 25, 80, 20, 20, "+"));
	    this.buttonList.add(new GuiDTButton(11, (width * 2) / 3 - 25, 80, 20, 20, "+"));
	    this.buttonList.add(new GuiDTButton(12, width - 25, 80, 20, 20, "+"));
	    this.buttonList.add(new GuiDTButton(13, width / 3 - 25, 105, 20, 20, "+"));
	    this.buttonList.add(new GuiDTButton(14, (width * 2) / 3 - 25, 105, 20, 20, "+"));
	    this.buttonList.add(new GuiDTButton(15, width - 25, 105, 20, 20, "+"));
	    this.buttonList.add(new GuiDTButton(16, width / 3 - 25, 130, 20, 20, "+"));
	    this.buttonList.add(new GuiDTButton(17, (width * 2) / 3 - 25, 130, 20, 20, "+"));
	    this.buttonList.add(new GuiDTButton(18, width - 25, 130, 20, 20, "+"));
	    
        this.buttonList.add(new GuiButton(19, width - 22, 170, 20, 20, "+"));
        this.buttonList.add(new GuiButton(20, width - 44, 170, 20, 20, "-"));
        if(dog.getOwner().equalsIgnoreCase(player.username)) {
        	this.buttonList.add(new GuiButton(21, width - 44, 195, 42, 20, String.valueOf(dog.willObeyOthers())));
        }
	}
	    
	public void onGuiClosed()
	{
	    Keyboard.enableRepeatEvents(false);
	}
	
    protected void actionPerformed(GuiButton par1GuiButton) 
    {
    	if (par1GuiButton.id == 1 && this.dog.talents.getTalentLevel(EnumSkills.BLACKPELT) < 5 && dog.spendablePoints() >= this.dog.talents.getTalentLevel(EnumSkills.BLACKPELT) + 1)
        {
    		this.sendNewTalentToServer(0);
        }
    	
    	if (par1GuiButton.id == 2 && this.dog.talents.getTalentLevel(EnumSkills.GUARDDOG) < 5 && dog.spendablePoints() >= this.dog.talents.getTalentLevel(EnumSkills.GUARDDOG))
        {
    		this.sendNewTalentToServer(1);
        }
    	
    	if (par1GuiButton.id == 3 && dog.talents.getTalentLevel(EnumSkills.HUNTERDOG) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumSkills.HUNTERDOG) + 1)
        {
    		this.sendNewTalentToServer(2);
        }
    	
    	if (par1GuiButton.id == 4 && dog.talents.getTalentLevel(EnumSkills.HELLHOUND) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumSkills.HELLHOUND) + 1)
        {
    		this.sendNewTalentToServer(3);
        }
    	
    	//if (par1GuiButton.id == 5 && dog.talents.getTalentLevel(EnumSkills.WOLFMOUNT) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumSkills.WOLFMOUNT) + 1)
        //{
    	//	this.sendNewTalentToServer(4);
        //}
    	
    	if (par1GuiButton.id == 6 && dog.talents.getTalentLevel(EnumSkills.PACKPUPPY) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumSkills.PACKPUPPY) + 1)
        {
    		this.sendNewTalentToServer(5);
        }
    	
    	if (par1GuiButton.id == 7 && dog.talents.getTalentLevel(EnumSkills.PILLOWPAW) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumSkills.PILLOWPAW) + 1)
        {
    		this.sendNewTalentToServer(6);
        }
    	
    	if (par1GuiButton.id == 8 && dog.talents.getTalentLevel(EnumSkills.QUICKHEALER) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumSkills.QUICKHEALER) + 1)
        {
    		this.sendNewTalentToServer(7);
        }
    	
    	if (par1GuiButton.id == 9 && dog.talents.getTalentLevel(EnumSkills.CREEPERSWEEPER) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumSkills.CREEPERSWEEPER) + 1)
        {
    		this.sendNewTalentToServer(8);
        }
    	
    	if (par1GuiButton.id == 10 && dog.talents.getTalentLevel(EnumSkills.DOGGYDASH) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumSkills.DOGGYDASH) + 1)
        {
    		this.sendNewTalentToServer(9);
        }
    	
    	if (par1GuiButton.id == 11 && dog.talents.getTalentLevel(EnumSkills.FISHERDOG) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumSkills.FISHERDOG) + 1)
        {
    		this.sendNewTalentToServer(10);
        }
    	
    	if (par1GuiButton.id == 12 && dog.talents.getTalentLevel(EnumSkills.HAPPYEATER) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumSkills.HAPPYEATER) + 1)
        {
    		this.sendNewTalentToServer(11);
        }
    	
    	if (par1GuiButton.id == 13 && dog.talents.getTalentLevel(EnumSkills.BEDFINDER) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumSkills.BEDFINDER) + 1)
        {
    		this.sendNewTalentToServer(12);
        }
    	
    	if (par1GuiButton.id == 14 && dog.talents.getTalentLevel(EnumSkills.PESTFIGHTER) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumSkills.PESTFIGHTER) + 1)
        {
    		this.sendNewTalentToServer(13);
        }
    	
    	if (par1GuiButton.id == 15 && dog.talents.getTalentLevel(EnumSkills.POSIONFANG) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumSkills.POSIONFANG) + 1)
        {
    		this.sendNewTalentToServer(14);
        }
    	
    	
    	if (par1GuiButton.id == 16 && dog.talents.getTalentLevel(EnumSkills.SHEPHERDOG) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumSkills.SHEPHERDOG) + 1)
        {
    		this.sendNewTalentToServer(15);
        }
    	
    	if (par1GuiButton.id == 17 && dog.talents.getTalentLevel(EnumSkills.RESCUEDOG) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumSkills.RESCUEDOG) + 1)
        {
    		this.sendNewTalentToServer(16);
        }
    	
    	if (par1GuiButton.id == 18 && dog.talents.getTalentLevel(EnumSkills.PUPPYEYES) < 5 && dog.spendablePoints() >= dog.talents.getTalentLevel(EnumSkills.PUPPYEYES) + 1)
        {
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
    }
    
    public void changeDoggyTexture(int var1)
    {
    	String var2 = Properties.PACKET_TEXTURE;
        ByteArrayOutputStream var3 = new ByteArrayOutputStream();
        DataOutputStream var4 = new DataOutputStream(var3);

        try
        {
            var4.writeInt(var1);
            var4.writeInt(id);
            this.mc.getNetHandler().addToSendQueue(new Packet250CustomPayload(var2, var3.toByteArray()));
        }
        catch (Exception var6)
        {
            var6.printStackTrace();
        }
    }
    
    public void changeWillObey(boolean var1)
    {
    	String var2 = Properties.PACKET_OBEY;
        ByteArrayOutputStream var3 = new ByteArrayOutputStream();
        DataOutputStream var4 = new DataOutputStream(var3);

        try
        {
            var4.writeBoolean(var1);
            var4.writeInt(id);
            this.mc.getNetHandler().addToSendQueue(new Packet250CustomPayload(var2, var3.toByteArray()));
        }
        catch (Exception var6)
        {
            var6.printStackTrace();
        }
    }
    
    public void sendNewTalentToServer(int talentID)
    {
    	String var2 = Properties.PACKET_TALENT;
        ByteArrayOutputStream var3 = new ByteArrayOutputStream();
        DataOutputStream var4 = new DataOutputStream(var3);

        try
        {
            var4.writeInt(talentID);
            var4.writeInt(id);
            this.mc.getNetHandler().addToSendQueue(new Packet250CustomPayload(var2, var3.toByteArray()));
        }
        catch (Exception var6)
        {
            var6.printStackTrace();
        }
    }

	protected void keyTyped(char var1, int var2)
	{
	    this.txt_petName.textboxKeyTyped(var1, var2);
	    
	    if (var2 == 1)
        {
            this.mc.thePlayer.closeScreen();
        }
	    
        String var4 = this.txt_petName.getText().trim();
        this.sendNewNameToServer(var4);
	 }

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
		String var2 = Properties.PACKET_RENAME;
        ByteArrayOutputStream var3 = new ByteArrayOutputStream();
        DataOutputStream var4 = new DataOutputStream(var3);

        try
        {
            var4.writeUTF(var1);
            var4.writeInt(this.id);
            this.mc.getNetHandler().addToSendQueue(new Packet250CustomPayload(var2, var3.toByteArray()));
        }
        catch (Exception var6)
        {
            var6.printStackTrace();
        }
	}
	
	public boolean doesGuiPauseGame()
	{
	    return false;
	}
	
	
}
