package doggytalents.client.gui;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import doggytalents.entity.features.ModeFeature.EnumMode;
import doggytalents.lib.ConfigValues;
import doggytalents.network.PacketDispatcher;
import doggytalents.network.packet.client.DogFriendlyFireMessage;
import doggytalents.network.packet.client.DogModeMessage;
import doggytalents.network.packet.client.DogNameMessage;
import doggytalents.network.packet.client.DogObeyMessage;
import doggytalents.network.packet.client.DogTalentMessage;
import doggytalents.network.packet.client.DogTextureMessage;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
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
        this.buttonList.clear();
        this.labelList.clear();
        this.textfieldList.clear();
        super.initGui();
        
        Keyboard.enableRepeatEvents(true);
        int topX = this.width / 2;
        int topY = this.height / 2;
        GuiTextField nameTextField = new GuiTextField(0, this.fontRenderer, topX - 100, topY + 50, 200, 20) {
            @Override
            public boolean textboxKeyTyped(char character, int keyId) {
                boolean typed = super.textboxKeyTyped(character, keyId);
                if(typed)
                    PacketDispatcher.sendToServer(new DogNameMessage(dog.getEntityId(), this.getText()));
                return typed;
            }
            
            @Override
            public void deleteFromCursor(int index) {
                super.deleteFromCursor(index);
                PacketDispatcher.sendToServer(new DogNameMessage(dog.getEntityId(), this.getText()));
            }
        };
        nameTextField.setFocused(false);
        nameTextField.setMaxStringLength(32);
        if(this.dog.hasCustomName()) nameTextField.setText(this.dog.getCustomNameTag());
        this.nameTextField = nameTextField;
        
        this.textfieldList.add(nameTextField);
        this.doggyTex = this.dog.getTameSkin();
        
        int size = DoggyTalentsAPI.TALENTS.getKeys().size();
        
          this.btnPerPages = Math.max(MathHelper.floor((double)(this.height - 10) / 21) - 2, 1);
        
        if(this.btnPerPages < size) {
            this.addButton(new GuiButton(-1, 25, this.btnPerPages * 21 + 10, 20, 20, "<"));
            this.addButton(new GuiButton(-2, 48, this.btnPerPages * 21 + 10, 20, 20, ">"));
        }
        
        this.maxPages = MathHelper.ceil((double)size / this.btnPerPages);
        
        if(this.currentPage >= this.maxPages)
            this.currentPage = 0;
        
        int i = -1;
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues()) {
            i++;
            if(i < this.currentPage * this.btnPerPages || i >= (this.currentPage + 1) * this.btnPerPages)
                continue;
            this.addButton(new GuiTalentButton(1 + i, 25, 10 + (i - this.currentPage * this.btnPerPages) * 21, 20, 20, "+", talent));
        }
        
        if(ConfigValues.USE_DT_TEXTURES) {
            this.addButton(new GuiButton(-3, this.width - 42, topY + 30, 20, 20, "+"));
            this.addButton(new GuiButton(-4, this.width - 64, topY + 30, 20, 20, "-"));
        }
        if(this.dog.isOwner(this.player))
            this.addButton(new GuiButton(-5, this.width - 64, topY + 65, 42, 20, String.valueOf(this.dog.willObeyOthers())));
        
        
        this.addButton(new GuiButton(-7, this.width - 64, topY - 5, 42, 20, String.valueOf(this.dog.canPlayersAttack())));
        
        this.addButton(new GuiButton(-6, topX + 40, topY + 25, 60, 20, I18n.format(this.dog.MODE.getMode().getUnlocalisedName())));
    }
    
    @Override
    public void drawScreen(int xMouse, int yMouse, float partialTickTime) {
        this.drawDefaultBackground();
        //Background
        int topX = this.width / 2;
        int topY = this.height / 2;
        
        // Background
        String health = dfShort.format(this.dog.getHealth());
        String healthMax = dfShort.format(this.dog.getMaxHealth());
        String healthRel = dfShort.format(this.dog.getHealthRelative() * 100);
        String healthState = health + "/" + healthMax + " (" + healthRel + "%)";
        String speedValue = dfShortDouble.format(this.dog.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
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
        if(ConfigValues.DOG_GENDER) this.fontRenderer.drawString(I18n.format("doggui.gender") + dog.GENDER.getGenderName().getFormattedText(), this.width - 160, topY - 70, 0xFFFFFF);
        
        this.fontRenderer.drawString(I18n.format("doggui.newname"), topX - 100, topY + 38, 4210752);
        this.fontRenderer.drawString(I18n.format("doggui.level") + " " + this.dog.LEVELS.getLevel(), topX - 65, topY + 75, 0xFF10F9);
        this.fontRenderer.drawString(I18n.format("doggui.leveldire") + " " + this.dog.LEVELS.getDireLevel(), topX, topY + 75, 0xFF10F9);
        this.fontRenderer.drawString(I18n.format("doggui.pointsleft") + " " + this.dog.spendablePoints(), topX - 38, topY + 89, 0xFFFFFF);
                
        if(ConfigValues.USE_DT_TEXTURES) 
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
            field.drawTextBox();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        super.drawScreen(xMouse, yMouse, partialTickTime);
        RenderHelper.enableGUIStandardItemLighting();
        
        
        //Foreground

        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        for (int k = 0; k < this.buttonList.size(); ++k) {
            GuiButton button = (GuiButton)this.buttonList.get(k);
            if(button.mousePressed(this.mc, xMouse, yMouse)) {
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
                
                this.drawHoveringText(list, xMouse, yMouse, this.mc.fontRenderer);
            }
        }
        GlStateManager.popMatrix();
    }

    @Override
    protected void actionPerformed(GuiButton button)  {

        if(button.id >= 1 && button.id <= DoggyTalentsAPI.TALENTS.getKeys().size()) {
            Talent talent = button instanceof GuiTalentButton ? ((GuiTalentButton)button).talent : null;
            int level = this.dog.TALENTS.getLevel(talent);
            if(level < talent.getHighestLevel(this.dog) && this.dog.spendablePoints() >= talent.getCost(this.dog, level + 1))
                PacketDispatcher.sendToServer(new DogTalentMessage(this.dog.getEntityId(), talent.getRegistryName()));

        
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
        if (button.id == -4 || button.id == -3) {
            this.doggyTex += (button.id == -3 ? 1 : 127);
            this.doggyTex %= 128;
            PacketDispatcher.sendToServer(new DogTextureMessage(this.dog.getEntityId(), this.doggyTex));
        }
        
        if(button.id == -5) {
            button.displayString = String.valueOf(!this.dog.willObeyOthers());
            PacketDispatcher.sendToServer(new DogObeyMessage(this.dog.getEntityId(), !this.dog.willObeyOthers()));
        }
        
        if(button.id == -7) {
            button.displayString = String.valueOf(!this.dog.canPlayersAttack());
            PacketDispatcher.sendToServer(new DogFriendlyFireMessage(this.dog.getEntityId(), !this.dog.canPlayersAttack()));
        }
        
        if (button.id == -6) {
            EnumMode mode = GuiDogInfo.this.dog.getMode().nextMode();
            if(mode == EnumMode.WANDERING && !this.dog.COORDS.hasBowlPos())
                button.displayString = TextFormatting.RED + I18n.format(mode.getUnlocalisedName());
            else
                button.displayString = I18n.format(mode.getUnlocalisedName());
            PacketDispatcher.sendToServer(new DogModeMessage(this.dog.getEntityId(), mode));
        }
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
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
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        for(GuiTextField field : this.textfieldList)
            field.textboxKeyTyped(typedChar, keyCode);
        
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
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
