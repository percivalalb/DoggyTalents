package doggytalents.client.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.mojang.blaze3d.platform.GlStateManager;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import doggytalents.entity.features.ModeFeature.EnumMode;
import doggytalents.lib.ConfigValues;
import doggytalents.network.PacketHandler;
import doggytalents.network.client.PacketDogMode;
import doggytalents.network.client.PacketDogName;
import doggytalents.network.client.PacketDogObey;
import doggytalents.network.client.PacketDogTalent;
import doggytalents.network.client.PacketDogTexture;
import doggytalents.network.client.PacketFriendlyFire;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

/**
 * @author ProPercivalalb
 */
@OnlyIn(Dist.CLIENT)
public class GuiDogInfo extends Screen {

    public EntityDog dog;
    public PlayerEntity player;
    public int doggyTex;
    private int currentPage = 0;
    private int maxPages = 1;
    public int btnPerPages = 0;
    private final DecimalFormat dfShort = new DecimalFormat("0.0");
    private final DecimalFormat dfShortDouble = new DecimalFormat("0.00");
    
    public GuiDogInfo(EntityDog dog, PlayerEntity player) {
        super(new TranslationTextComponent("doggytalents.screen.dog.title"));
        this.dog = dog;
        this.player = player;
    }

    @Override
    public void init() {
        this.buttons.clear();
        this.children.clear();
        super.init();
        
        this.minecraft.keyboardListener.enableRepeatEvents(true);
        int topX = this.width / 2;
        int topY = this.height / 2;
        TextFieldWidget nameTextField = new TextFieldWidget(this.font, topX - 100, topY + 50, 200, 20, "TEST");
        nameTextField.func_212954_a(text ->  {
            PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketDogName(GuiDogInfo.this.dog.getEntityId(), text));
        });
        nameTextField.setFocused2(false);
        nameTextField.setMaxStringLength(32);
        if(this.dog.hasCustomName()) nameTextField.setText(this.dog.getCustomName().getUnformattedComponentText());
        
        this.addButton(nameTextField);
        this.doggyTex = this.dog.getTameSkin();
        
        int size = (int)DoggyTalentsAPI.TALENTS.getKeys().stream().filter(loc -> ConfigValues.ENABLED_TALENTS.getOrDefault(loc, false)).count();
        
        this.btnPerPages = Math.max(MathHelper.floor((double)(this.height - 10) / 21) - 2, 1);
        
        if(this.btnPerPages < size) {
            this.addButton(new Button(25, this.btnPerPages * 21 + 10, 20, 20, "<", button -> {
                if(GuiDogInfo.this.currentPage > 0) {
                    GuiDogInfo.this.currentPage -= 1;
                    GuiDogInfo.this.init();
                }
            }) {
                @Override
                public void renderToolTip(int mouseX, int mouseY) {
                    List<String> list = new ArrayList<String>();
                    list.add(TextFormatting.ITALIC + I18n.format("doggui.prevpage"));
                    GuiDogInfo.this.renderTooltip(list, mouseX, mouseY, GuiDogInfo.this.font);
                }
            });
            this.addButton(new Button(48, this.btnPerPages * 21 + 10, 20, 20, ">", button -> {
                if(GuiDogInfo.this.currentPage + 1 < maxPages) {
                    GuiDogInfo.this.currentPage += 1;
                    GuiDogInfo.this.init();
                }
            }) {
                @Override
                public void renderToolTip(int mouseX, int mouseY) {
                    List<String> list = new ArrayList<String>();
                    list.add(TextFormatting.ITALIC + I18n.format("doggui.nextpage"));
                    GuiDogInfo.this.renderTooltip(list, mouseX, mouseY, GuiDogInfo.this.font);
                }
            });
        }
        
        this.maxPages = MathHelper.ceil((double)size / this.btnPerPages);
        
        if(this.currentPage >= this.maxPages)
            this.currentPage = 0;
        
        int i = -1;
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues()) {
            if(!ConfigValues.ENABLED_TALENTS.getOrDefault(talent.getRegistryName(), false))
                continue;
            
            i++;
            if(i < this.currentPage * this.btnPerPages || i >= (this.currentPage + 1) * this.btnPerPages)
                continue;
            this.addButton(new TalentButton(25, 10 + (i - this.currentPage * this.btnPerPages) * 21, 20, 20, "+", talent, button -> {
                int level = GuiDogInfo.this.dog.TALENTS.getLevel(button.talent);
                if(level < button.talent.getHighestLevel(GuiDogInfo.this.dog) && GuiDogInfo.this.dog.spendablePoints() >= button.talent.getCost(GuiDogInfo.this.dog, level + 1))
                    PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketDogTalent(GuiDogInfo.this.dog.getEntityId(), button.talent.getRegistryName()));
                
            }) {
                @Override
                public void renderToolTip(int mouseX, int mouseY) {
                    List<String> list = new ArrayList<String>();

                    list.add(TextFormatting.GREEN + I18n.format(this.talent.getTranslationKey()));
                    list.add("Level: " + GuiDogInfo.this.dog.TALENTS.getLevel(this.talent));
                    list.add(TextFormatting.GRAY + "--------------------------------");
                    list.addAll(GuiDogInfo.this.splitInto(I18n.format(this.talent.getInfoTranslationKey()), 200, GuiDogInfo.this.font));

                    GuiDogInfo.this.renderTooltip(list, mouseX, mouseY, GuiDogInfo.this.font);
                }
            });
        }
        
        if(ConfigValues.USE_DT_TEXTURES) {
            this.addButton(new Button(this.width - 42, topY + 30, 20, 20, "+", button -> {
                GuiDogInfo.this.doggyTex += 1;
                GuiDogInfo.this.doggyTex %= 128;
                PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketDogTexture(GuiDogInfo.this.dog.getEntityId(), GuiDogInfo.this.doggyTex));
            }));
            this.addButton(new Button(this.width - 64, topY + 30, 20, 20, "-", button -> {
                GuiDogInfo.this.doggyTex += 127;
                GuiDogInfo.this.doggyTex %= 128;
                PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketDogTexture(GuiDogInfo.this.dog.getEntityId(), GuiDogInfo.this.doggyTex));
            }));
        }
        if(this.dog.isOwner(this.player))
            this.addButton(new Button(this.width - 64, topY + 65, 42, 20, String.valueOf(this.dog.willObeyOthers()), button -> {
                button.setMessage(String.valueOf(!dog.willObeyOthers()));
                PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketDogObey(GuiDogInfo.this.dog.getEntityId(), !GuiDogInfo.this.dog.willObeyOthers()));
            }));
        
        
        this.addButton(new Button(this.width - 64, topY - 5, 42, 20, String.valueOf(this.dog.canPlayersAttack()), button -> {
            button.setMessage(String.valueOf(!GuiDogInfo.this.dog.canPlayersAttack()));
            PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketFriendlyFire(GuiDogInfo.this.dog.getEntityId(), !GuiDogInfo.this.dog.canPlayersAttack()));
        }));
        
        this.addButton(new Button(topX + 40, topY + 25, 60, 20, I18n.format(this.dog.MODE.getMode().getUnlocalisedName()), button -> {
            EnumMode mode = GuiDogInfo.this.dog.getMode().nextMode();
            if(mode == EnumMode.WANDERING && !GuiDogInfo.this.dog.COORDS.hasBowlPos())
                button.setMessage(TextFormatting.RED + I18n.format(mode.getUnlocalisedName()));
            else
                button.setMessage(I18n.format(mode.getUnlocalisedName()));
            PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketDogMode(GuiDogInfo.this.dog.getEntityId(), mode));
        }) {
            @Override
            public void renderToolTip(int mouseX, int mouseY) {
                List<String> list = new ArrayList<String>();
                String str = I18n.format(dog.MODE.getMode().getUnlocalisedInfo());
                list.addAll(splitInto(str, 150, GuiDogInfo.this.font));
                if(GuiDogInfo.this.dog.MODE.isMode(EnumMode.WANDERING)) {
                    if(!GuiDogInfo.this.dog.COORDS.hasBowlPos()) {
                        list.add(TextFormatting.RED + I18n.format("dog.mode.docile.nobowl"));
                    } else if(GuiDogInfo.this.dog.getPosition().distanceSq(GuiDogInfo.this.dog.COORDS.getBowlPos()) > 256.0D) {
                        list.add(TextFormatting.RED + I18n.format("dog.mode.docile.distance", (int)Math.sqrt(GuiDogInfo.this.dog.getPosition().distanceSq(GuiDogInfo.this.dog.COORDS.getBowlPos()))));
                    } else {
                        list.add(TextFormatting.GREEN + I18n.format("dog.mode.docile.bowl", (int)Math.sqrt(GuiDogInfo.this.dog.getPosition().distanceSq(GuiDogInfo.this.dog.COORDS.getBowlPos()))));
                    }
                }
                GuiDogInfo.this.renderTooltip(list, mouseX, mouseY, GuiDogInfo.this.font);
            }
        });
    }
    
    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        //Background
        int topX = this.width / 2;
        int topY = this.height / 2;
        this.renderBackground();
        
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
        
        this.font.drawString(I18n.format("doggui.health") + healthState, this.width - 160, topY - 110, 0xFFFFFF);
        this.font.drawString(I18n.format("doggui.speed") + speedValue, this.width - 160, topY - 100, 0xFFFFFF);
        this.font.drawString(I18n.format("doggui.owner") + tamedString, this.width - 160, topY - 90, 0xFFFFFF);
        this.font.drawString(I18n.format("doggui.age") + ageString, this.width - 160, topY - 80, 0xFFFFFF);
        if(ConfigValues.DOG_GENDER) this.font.drawString(I18n.format("doggui.gender") + dog.GENDER.getGenderName().getFormattedText(), this.width - 160, topY - 70, 0xFFFFFF);
        
        this.font.drawString(I18n.format("doggui.newname"), topX - 100, topY + 38, 4210752);
        this.font.drawString(I18n.format("doggui.level") + " " + this.dog.LEVELS.getLevel(), topX - 65, topY + 75, 0xFF10F9);
        this.font.drawString(I18n.format("doggui.leveldire") + " " + this.dog.LEVELS.getDireLevel(), topX, topY + 75, 0xFF10F9);
        this.font.drawString(I18n.format("doggui.pointsleft") + " " + this.dog.spendablePoints(), topX - 38, topY + 89, 0xFFFFFF);
                
        if(ConfigValues.USE_DT_TEXTURES) 
            this.font.drawString(I18n.format("doggui.textureindex"), this.width - 80, topY + 20, 0xFFFFFF);
        
        if(this.dog.isOwner(this.player))
            this.font.drawString(I18n.format("doggui.obeyothers"), this.width - 76, topY + 55, 0xFFFFFF);
        
        this.font.drawString(I18n.format("doggui.friendlyfire"), this.width - 76, topY - 15, 0xFFFFFF);
        
        this.buttons.forEach(widget -> {
            if(widget instanceof TalentButton) {
                TalentButton talBut = (TalentButton)widget;
                this.font.drawString(I18n.format(talBut.talent.getTranslationKey()), talBut.x + 25, talBut.y + 7, 0xFFFFFF);
            }
        });
        
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepthTest();
        super.render(mouseX, mouseY, partialTicks);
        RenderHelper.enableGUIStandardItemLighting();
        
        
        //Foreground

        
        GlStateManager.pushMatrix();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        
        this.buttons.forEach(widget -> {
            if(widget.isMouseOver(mouseX, mouseY)) {
                widget.renderToolTip(mouseX, mouseY);
            }
        });
        
        GlStateManager.popMatrix();
    }
    
    @Override
    public void onClose() {
        super.onClose();
        this.minecraft.keyboardListener.enableRepeatEvents(false);
    }
    
    @Override
    public boolean isPauseScreen() {
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
    
    private static class TalentButton extends Button {

        protected Talent talent;
        private TalentButton(int x, int y, int widthIn, int heightIn, String buttonText, Talent talent, Consumer<TalentButton> onPress) {
            super(x, y, widthIn, heightIn, buttonText, button -> onPress.accept((TalentButton)button));
            this.talent = talent;
        }
        
    }
}
