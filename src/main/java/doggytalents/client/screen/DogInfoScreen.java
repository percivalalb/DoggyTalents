package doggytalents.client.screen;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.feature.EnumMode;
import doggytalents.api.registry.Talent;
import doggytalents.client.DogTextureLoaderClient;
import doggytalents.common.config.ConfigValues;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.entity.DogLevel.Type;
import doggytalents.common.lib.Resources;
import doggytalents.common.network.PacketHandler;
import doggytalents.common.network.packet.DogModePacket;
import doggytalents.common.network.packet.DogNamePacket;
import doggytalents.common.network.packet.DogObeyPacket;
import doggytalents.common.network.packet.DogTalentPacket;
import doggytalents.common.network.packet.FriendlyFirePacket;
import doggytalents.common.network.packet.SendSkinPacket;
import doggytalents.common.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

public class DogInfoScreen extends Screen {

    public final DogEntity dog;
    public final PlayerEntity player;

    private int currentPage = 0;
    private int maxPages = 1;
    private List<Widget> talentWidgets = Lists.newArrayList();

    private Button leftBtn, rightBtn;

    private List<Talent> talentList;
    private List<ResourceLocation> customSkinList;

    public int textureIndex;

    public DogInfoScreen(DogEntity dog, PlayerEntity player) {
        super(new TranslationTextComponent("doggytalents.screen.dog.title"));
        this.dog = dog;
        this.player = player;
        this.talentList = DoggyTalentsAPI.TALENTS
                .getValues()
                .stream()
                .sorted(Comparator.comparing((t) -> I18n.format(t.getTranslationKey())))
                .collect(Collectors.toList());

        this.customSkinList = Lists.newArrayList(DogTextureLoaderClient.getClient());
        byte[] stream = DogTextureLoaderClient.getResourceBytes(Resources.DIAMOND_HELMET);
        if (stream != null) {
            DogTextureLoaderClient.saveTextureAndLoad(DogTextureLoaderClient.getClientFolder(), stream);
        }
    }

    public static void open(DogEntity dog) {
        Minecraft mc = Minecraft.getInstance();
        mc.displayGuiScreen(new DogInfoScreen(dog, mc.player));
    }

    @Override
    public void init() {
        super.init();
        this.minecraft.keyboardListener.enableRepeatEvents(true);
        int topX = this.width / 2;
        int topY = this.height / 2;

        TextFieldWidget nameTextField = new TextFieldWidget(this.font, topX - 100, topY + 50, 200, 20, "");
        nameTextField.setResponder(text ->  {
            PacketHandler.send(PacketDistributor.SERVER.noArg(), new DogNamePacket(DogInfoScreen.this.dog.getEntityId(), text));
        });
        nameTextField.setFocused2(false);
        nameTextField.setMaxStringLength(32);

        if(this.dog.hasCustomName()) {
            nameTextField.setText(this.dog.getCustomName().getUnformattedComponentText());
        }

        this.addButton(nameTextField);



        if(this.dog.isOwner(this.player)) {
            Button obeyBtn = new Button(this.width - 64, topY + 77, 42, 20, String.valueOf(this.dog.willObeyOthers()), (btn) -> {
                btn.setMessage(String.valueOf(!this.dog.willObeyOthers()));
                PacketHandler.send(PacketDistributor.SERVER.noArg(), new DogObeyPacket(this.dog.getEntityId(), !this.dog.willObeyOthers()));
            });

            this.addButton(obeyBtn);
        }

        Button attackPlayerBtn = new Button(this.width - 64, topY - 5, 42, 20, String.valueOf(this.dog.canPlayersAttack()), button -> {
            button.setMessage(String.valueOf(!this.dog.canPlayersAttack()));
            PacketHandler.send(PacketDistributor.SERVER.noArg(), new FriendlyFirePacket(this.dog.getEntityId(), !this.dog.canPlayersAttack()));
        });

        this.addButton(attackPlayerBtn);

        //if(ConfigValues.USE_DT_TEXTURES) {
            Button addBtn = new Button(this.width - 42, topY + 30, 20, 20, "+", (btn) -> {
                this.textureIndex += 1;
                this.textureIndex %= this.customSkinList.size() ;
                PacketHandler.send(PacketDistributor.SERVER.noArg(), new SendSkinPacket(this.dog.getEntityId(), DogTextureLoaderClient.getResourceBytes(this.customSkinList.get(this.textureIndex))));
            });
            Button lessBtn = new Button(this.width - 64, topY + 30, 20, 20, "-", (btn) -> {
                this.textureIndex += this.customSkinList.size() - 1;
                this.textureIndex %= this.customSkinList.size();
                PacketHandler.send(PacketDistributor.SERVER.noArg(), new SendSkinPacket(this.dog.getEntityId(), DogTextureLoaderClient.getResourceBytes(this.customSkinList.get(this.textureIndex))));
            });

            this.addButton(addBtn);
            this.addButton(lessBtn);
        //}


        Button modeBtn = new Button(topX + 40, topY + 25, 60, 20, I18n.format(this.dog.getMode().getUnlocalisedName()), button -> {
            EnumMode mode = DogInfoScreen.this.dog.getMode().nextMode();

            if(mode == EnumMode.WANDERING && !DogInfoScreen.this.dog.getBowlPos().isPresent()) {
                button.setMessage(TextFormatting.RED + I18n.format(mode.getUnlocalisedName()));
            } else {
                button.setMessage(I18n.format(mode.getUnlocalisedName()));
            }

            PacketHandler.send(PacketDistributor.SERVER.noArg(), new DogModePacket(DogInfoScreen.this.dog.getEntityId(), mode));
        }) {
            @Override
            public void renderToolTip(int mouseX, int mouseY) {
                List<String> list = new ArrayList<String>();
                String str = I18n.format(dog.getMode().getUnlocalisedInfo());
                //list.addAll(splitInto(str, 150, DogInfoScreen.this.font));
                if(DogInfoScreen.this.dog.getMode() == EnumMode.WANDERING) {


                    if(DogInfoScreen.this.dog.getBowlPos().isPresent()) {
                        double distance = DogInfoScreen.this.dog.getPosition().distanceSq(DogInfoScreen.this.dog.getBowlPos().get());

                        if (distance > 256D) {
                            list.add(TextFormatting.RED + I18n.format("dog.mode.docile.distance", (int)Math.sqrt(distance)));
                        } else {
                            list.add(TextFormatting.GREEN + I18n.format("dog.mode.docile.bowl", (int)Math.sqrt(distance)));
                        }
                    } else {
                        list.add(TextFormatting.RED + I18n.format("dog.mode.docile.nobowl"));
                    }
                }

                DogInfoScreen.this.renderTooltip(list, mouseX, mouseY, DogInfoScreen.this.font);
            }
        };

        this.addButton(modeBtn);

        // Talent level-up buttons
        int size = DoggyTalentsAPI.TALENTS.getKeys().size();
        int perPage = Math.max(MathHelper.floor((this.height - 10) / (double) 21) - 2, 1);
        this.currentPage = 0;
        this.recalculatePage(perPage);


        if(perPage < size) {
            this.leftBtn = new Button(25, perPage * 21 + 10, 20, 20, "<", (btn) -> {
                this.currentPage = Math.max(0, this.currentPage - 1);
                btn.active = this.currentPage > 0;
                this.rightBtn.active = true;
                this.recalculatePage(perPage);
            }) {
                @Override
                public void renderToolTip(int mouseX, int mouseY) {
                    List<String> list = Lists.newArrayList(TextFormatting.ITALIC + I18n.format("doggui.prevpage"));
                    DogInfoScreen.this.renderTooltip(list, mouseX, mouseY, DogInfoScreen.this.font);
                }
            };
            this.leftBtn.active = false;

            this.rightBtn = new Button(48, perPage * 21 + 10, 20, 20, ">", (btn) -> {
                this.currentPage = Math.min(this.maxPages - 1, this.currentPage + 1);
                btn.active = this.currentPage < this.maxPages - 1;
                this.leftBtn.active = true;
                this.recalculatePage(perPage);
            }) {
                @Override
                public void renderToolTip(int mouseX, int mouseY) {
                    List<String> list = Lists.newArrayList(TextFormatting.ITALIC + I18n.format("doggui.nextpage"));
                    DogInfoScreen.this.renderTooltip(list, mouseX, mouseY, DogInfoScreen.this.font);
                }
            };

            this.addButton(this.leftBtn);
            this.addButton(this.rightBtn);
        }
    }

    private void recalculatePage(int perPage) {
        this.talentWidgets.forEach(this::removeWidget);
        this.talentWidgets.clear();

        this.maxPages = MathHelper.ceil(this.talentList.size() / (double) perPage);

        for(int i = 0; i < perPage; ++i) {

            int index = this.currentPage * perPage + i;
            if(index >= this.talentList.size()) break;
            Talent talent = this.talentList.get(index);

            Button button = new TalentButton(25, 10 + i * 21, 20, 20, "+", talent, (btn) -> {
                int level = DogInfoScreen.this.dog.getLevel(talent);
                if(level < talent.getMaxLevel() && DogInfoScreen.this.dog.getSpendablePoints() >= talent.getLevelCost(level + 1)) {
                    PacketHandler.send(PacketDistributor.SERVER.noArg(), new DogTalentPacket(DogInfoScreen.this.dog.getEntityId(), talent.getRegistryName()));
                }

            }) {
                @Override
                public void renderToolTip(int mouseX, int mouseY) {
                    List<String> list = new ArrayList<String>();

                    list.add(TextFormatting.GREEN + I18n.format(talent.getTranslationKey()));
                    list.add("Level: " + DogInfoScreen.this.dog.getLevel(talent));
                    list.add(TextFormatting.GRAY + "--------------------------------");
                    //list.addAll(DogInfoScreen.this.splitInto(I18n.format(talent.getInfoTranslationKey()), 200, DogInfoScreen.this.font));

                    DogInfoScreen.this.renderTooltip(list, mouseX, mouseY, DogInfoScreen.this.font);
                }
            };
            button.active = true; //TODO De-active if talent disabled

            this.talentWidgets.add(button);
            this.addButton(button);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        //Background
        int topX = this.width / 2;
        int topY = this.height / 2;
        this.renderBackground();

        // Background
        String health = Util.format1DP(this.dog.getHealth());
        String healthMax = Util.format1DP(this.dog.getMaxHealth());
        String speedValue = Util.format2DP(this.dog.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
        String ageValue = Util.format2DP(this.dog.getGrowingAge());
        String ageRel = I18n.format(this.dog.isChild() ? "doggui.age.baby" : "doggui.age.adult");

        String ageString = ageValue + " " + ageRel;

        String tamedString = "";
        if(this.dog.isTamed()) {
            if(this.dog.isOwner(this.player)) {
                tamedString = I18n.format("doggui.owner.you");
            } else if (this.dog.getOwnersName().isPresent()) {
                tamedString = this.dog.getOwnersName().get().getFormattedText();
            }
        }

        //this.font.drawString(I18n.format("doggui.health") + healthState, this.width - 160, topY - 110, 0xFFFFFF);
        this.font.drawString(I18n.format("doggui.speed") + speedValue, this.width - 160, topY - 100, 0xFFFFFF);
        this.font.drawString(I18n.format("doggui.owner") + tamedString, this.width - 160, topY - 90, 0xFFFFFF);
        this.font.drawString(I18n.format("doggui.age") + ageString, this.width - 160, topY - 80, 0xFFFFFF);
        if(ConfigValues.DOG_GENDER) {
            this.font.drawString(I18n.format("doggui.gender") + I18n.format(this.dog.getGender().getUnlocalisedTitle()), this.width - 160, topY - 70, 0xFFFFFF);
        }

        this.font.drawString(I18n.format("doggui.newname"), topX - 100, topY + 38, 4210752);
        this.font.drawString(I18n.format("doggui.level") + " " + this.dog.getLevel().getLevel(Type.NORMAL), topX - 65, topY + 75, 0xFF10F9);
        this.font.drawString(I18n.format("doggui.leveldire") + " " + this.dog.getLevel().getLevel(Type.DIRE), topX, topY + 75, 0xFF10F9);
        this.font.drawString(I18n.format("doggui.pointsleft") + " " + this.dog.getSpendablePoints(), topX - 38, topY + 89, 0xFFFFFF);

       // if(ConfigValues.USE_DT_TEXTURES) {
            this.font.drawString(I18n.format("doggui.textureindex"), this.width - 80, topY + 20, 0xFFFFFF);
            this.font.drawString(this.dog.getSkinHash().substring(0, Math.min(this.dog.getSkinHash().length(), 10)), this.width - 73, topY + 54, 0xFFFFFF);
       // }

        if(this.dog.isOwner(this.player)) {
            this.font.drawString(I18n.format("doggui.obeyothers"), this.width - 76, topY + 67, 0xFFFFFF);
        }

        this.font.drawString(I18n.format("doggui.friendlyfire"), this.width - 76, topY - 15, 0xFFFFFF);


        this.buttons.forEach(widget -> {
            if(widget instanceof TalentButton) {
                TalentButton talBut = (TalentButton)widget;
                this.font.drawString(I18n.format(talBut.talent.getTranslationKey()), talBut.x + 25, talBut.y + 7, 0xFFFFFF);
            }
        });

        super.render(mouseX, mouseY, partialTicks);
        //RenderHelper.disableStandardItemLighting(); // 1.14 enableGUIStandardItemLighting

        for(Widget widget : this.buttons) {
            if (widget.isHovered()) {
               widget.renderToolTip(mouseX, mouseY);
               break;
            }
         }

       // RenderHelper.enableStandardItemLighting();
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

    protected <T extends Widget> T removeWidget(T widgetIn) {
        this.buttons.remove(widgetIn);
        this.children.remove(widgetIn);
        return widgetIn;
    }

    private static class TalentButton extends Button {

        protected Talent talent;
        private TalentButton(int x, int y, int widthIn, int heightIn, String buttonText, Talent talent, Consumer<TalentButton> onPress) {
            super(x, y, widthIn, heightIn, buttonText, button -> onPress.accept((TalentButton) button));
            this.talent = talent;
        }

    }
}
