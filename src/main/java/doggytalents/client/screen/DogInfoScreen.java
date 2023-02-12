package doggytalents.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import doggytalents.DoggyAccessories;
import doggytalents.DoggyTalents2;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.feature.DogLevel.Type;
import doggytalents.api.feature.EnumMode;
import doggytalents.api.registry.Talent;
import doggytalents.client.DogTextureManager;
import doggytalents.common.config.ConfigHandler;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.network.PacketHandler;
import doggytalents.common.network.packet.data.*;
import doggytalents.common.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DogInfoScreen extends Screen {

    public final DogEntity dog;
    public final Player player;

    private int currentPage = 0;
    private int maxPages = 1;
    private List<AbstractWidget> talentWidgets = new ArrayList<>(16);

    private Button leftBtn, rightBtn;

    private List<Talent> talentList;
    private List<ResourceLocation> customSkinList;

    public int textureIndex;

    public DogInfoScreen(DogEntity dog, Player player) {
        super(Component.translatable("doggytalents.screen.dog.title"));
        this.dog = dog;
        this.player = player;
        this.talentList = DoggyTalentsAPI.TALENTS.get()
                .getValues()
                .stream()
                .sorted(Comparator.comparing((t) -> I18n.get(t.getTranslationKey())))
                .collect(Collectors.toList());

        this.customSkinList = DogTextureManager.INSTANCE.getAll();
        this.textureIndex = this.customSkinList.indexOf(DogTextureManager.INSTANCE.getTextureLoc(dog.getSkinHash()));
        this.textureIndex = this.textureIndex >= 0 ? this.textureIndex : 0;
    }

    public static void open(DogEntity dog) {
        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(new DogInfoScreen(dog, mc.player));
    }

    @Override
    public void init() {
        super.init();
        int topX = this.width / 2;
        int topY = this.height / 2;

        EditBox nameTextField = new EditBox(this.font, topX - 100, topY + 50, 200, 20,  Component.translatable("dogInfo.enterName"));
        nameTextField.setResponder(text ->  {
            PacketHandler.send(PacketDistributor.SERVER.noArg(), new DogNameData(DogInfoScreen.this.dog.getId(), text));
        });
        nameTextField.setFocus(false);
        nameTextField.setMaxLength(32);

        if (this.dog.hasCustomName()) {
            nameTextField.setValue(this.dog.getCustomName().getString());
        }

        this.addRenderableWidget(nameTextField);

        if (this.dog.isOwnedBy(this.player)) {
            Button obeyBtn = new Button.Builder(Component.literal(String.valueOf(this.dog.willObeyOthers())), (btn) -> {
                btn.setMessage(Component.literal(String.valueOf(!this.dog.willObeyOthers())));
                PacketHandler.send(PacketDistributor.SERVER.noArg(), new DogObeyData(this.dog.getId(), !this.dog.willObeyOthers()));
            }).pos(this.width - 64, topY + 77).size(42, 20).build();

            this.addRenderableWidget(obeyBtn);
        }

        Button attackPlayerBtn = new Button.Builder(Component.literal(String.valueOf(this.dog.canPlayersAttack())), (btn) -> {
            btn.setMessage(Component.literal(String.valueOf(!this.dog.canPlayersAttack())));
            PacketHandler.send(PacketDistributor.SERVER.noArg(), new FriendlyFireData(this.dog.getId(), !this.dog.canPlayersAttack()));
        }).pos(this.width - 64, topY - 5).size(42, 20).build();

        this.addRenderableWidget(attackPlayerBtn);

        if (ConfigHandler.CLIENT.USE_DT_TEXTURES.get()) {
            Button addBtn = new Button.Builder(Component.literal("+"), (btn) -> {
                this.textureIndex += 1;
                this.textureIndex %= this.customSkinList.size();
                ResourceLocation rl = this.customSkinList.get(this.textureIndex);
                this.setDogTexture(rl);
            }).pos(this.width - 42, topY + 30).size(20, 20).build();

            Button lessBtn = new Button.Builder(Component.literal("-"), (btn) -> {
                this.textureIndex += this.customSkinList.size() - 1;
                this.textureIndex %= this.customSkinList.size();
                ResourceLocation rl = this.customSkinList.get(this.textureIndex);
                this.setDogTexture(rl);
            }).pos(this.width - 64, topY + 30).size(20, 20).build();

            this.addRenderableWidget(addBtn);
            this.addRenderableWidget(lessBtn);
        }

        Button modeBtn = new Button.Builder(Component.translatable(this.dog.getMode().getUnlocalisedName()), (btn) -> {
            EnumMode mode = DogInfoScreen.this.dog.getMode().nextMode();

            if (mode == EnumMode.WANDERING && !DogInfoScreen.this.dog.getBowlPos().isPresent()) {
                btn.setMessage(Component.translatable(mode.getUnlocalisedName()).withStyle(ChatFormatting.RED));
            } else {
                btn.setMessage(Component.translatable(mode.getUnlocalisedName()));
            }

            PacketHandler.send(PacketDistributor.SERVER.noArg(), new DogModeData(DogInfoScreen.this.dog.getId(), mode));

            // Update tooltip with new mode.
            btn.setTooltip(this.getTooltipForMode(mode));
        }).pos(topX + 40, topY + 25).size(60, 20).tooltip(this.getTooltipForMode(this.dog.getMode())).build();

        this.addRenderableWidget(modeBtn);

        // Talent level-up buttons
        int size = DoggyTalentsAPI.TALENTS.get().getKeys().size();
        int perPage = Math.max(Mth.floor((this.height - 10) / (double) 21) - 2, 1);
        this.currentPage = 0;
        this.recalculatePage(perPage);

        if (perPage < size) {
            this.leftBtn = new Button.Builder(Component.literal("<"), (btn) -> {
                this.currentPage = Math.max(0, this.currentPage - 1);
                btn.active = this.currentPage > 0;
                this.rightBtn.active = true;
                this.recalculatePage(perPage);
            }).pos(25, perPage * 21 + 10).size(20, 20).tooltip(Tooltip.create(Component.translatable("doggui.prevpage").withStyle(ChatFormatting.ITALIC))).build();
            this.leftBtn.active = false;

            this.rightBtn = new Button.Builder(Component.literal(">"), (btn) -> {
                this.currentPage = Math.min(this.maxPages - 1, this.currentPage + 1);
                btn.active = this.currentPage < this.maxPages - 1;
                this.leftBtn.active = true;
                this.recalculatePage(perPage);
            }).pos(48, perPage * 21 + 10).size(20, 20).tooltip(Tooltip.create(Component.translatable("doggui.nextpage").withStyle(ChatFormatting.ITALIC))).build();

            this.addRenderableWidget(this.leftBtn);
            this.addRenderableWidget(this.rightBtn);
        }
    }

    private void setDogTexture(ResourceLocation rl) {
        if (ConfigHandler.SEND_SKIN) {
            try {
                byte[] data = DogTextureManager.INSTANCE.getResourceBytes(rl);
                PacketHandler.send(PacketDistributor.SERVER.noArg(), new SendSkinData(this.dog.getId(), data));
            } catch (IOException e) {
                DoggyTalents2.LOGGER.error("Was unable to get resource data for {}, {}", rl, e);
            }
        } else {
            PacketHandler.send(PacketDistributor.SERVER.noArg(), new DogTextureData(this.dog.getId(), DogTextureManager.INSTANCE.getTextureHash(rl)));
        }
    }

    private void recalculatePage(int perPage) {
        this.talentWidgets.forEach(this::removeWidget);
        this.talentWidgets.clear();

        this.maxPages = Mth.ceil(this.talentList.size() / (double) perPage);

        for (int i = 0; i < perPage; ++i) {
            int index = this.currentPage * perPage + i;
            if (index >= this.talentList.size()) break;
            Talent talent = this.talentList.get(index);

            Button button = new Button.Builder(Component.literal("+"), (btn) -> {
                int level = DogInfoScreen.this.dog.getDogLevel(talent);
                if (level < talent.getMaxLevel() && DogInfoScreen.this.dog.canSpendPoints(talent.getLevelCost(level + 1))) {
                    PacketHandler.send(PacketDistributor.SERVER.noArg(), new DogTalentData(DogInfoScreen.this.dog.getId(), talent));

                    btn.setTooltip(DogInfoScreen.this.getTooltipForTalent(talent, level + 1));
                }
            }).pos(25, 10 + i * 21).size(20, 20).tooltip(this.getTooltipForTalent(talent, this.dog.getDogLevel(talent))).build(b -> new TalentButton(b, talent));
            button.active = ConfigHandler.TALENT.getFlag(talent);

            this.talentWidgets.add(button);
            this.addRenderableWidget(button);
        }
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        //Background
        int topX = this.width / 2;
        int topY = this.height / 2;
        this.renderBackground(stack);

        // Background
        String health = Util.format1DP(this.dog.getHealth());
        String healthMax = Util.format1DP(this.dog.getMaxHealth());
        String speedValue = Util.format2DP(this.dog.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
        String armorValue = Util.format2DP(this.dog.getAttribute(Attributes.ARMOR).getValue());
        String ageValue = Util.format2DP(this.dog.getAge());
        String ageRel = I18n.get(this.dog.isBaby() ? "doggui.age.baby" : "doggui.age.adult");

        String ageString = ageValue + " " + ageRel;

        String tamedString = "";
        if (this.dog.isTame()) {
            if (this.dog.isOwnedBy(this.player)) {
                tamedString = I18n.get("doggui.owner.you");
            } else if (this.dog.getOwnersName().isPresent()) {
                tamedString = this.dog.getOwnersName().get().getString();
            }
        }

        //this.font.drawString(I18n.format("doggui.health") + healthState, this.width - 160, topY - 110, 0xFFFFFF);
        this.font.draw(stack, I18n.get("doggui.speed") + " " + speedValue, this.width - 160, topY - 100, 0xFFFFFF);
        this.font.draw(stack, I18n.get("doggui.owner") + " " + tamedString, this.width - 160, topY - 90, 0xFFFFFF);
        this.font.draw(stack, I18n.get("doggui.age") + " " + ageString, this.width - 160, topY - 80, 0xFFFFFF);
        this.font.draw(stack, I18n.get("doggui.armor") + " " + armorValue, this.width - 160, topY - 70, 0xFFFFFF);
        if (ConfigHandler.SERVER.DOG_GENDER.get()) {
            this.font.draw(stack, I18n.get("doggui.gender") + " "+ I18n.get(this.dog.getGender().getUnlocalisedName()), this.width - 160, topY - 60, 0xFFFFFF);
        }

        this.font.draw(stack, I18n.get("doggui.newname"), topX - 100, topY + 38, 4210752);
        this.font.draw(stack, I18n.get("doggui.level") + " " + this.dog.getDogLevel().getLevel(Type.NORMAL), topX - 65, topY + 75, 0xFF10F9);
        this.font.draw(stack, I18n.get("doggui.leveldire") + " " + this.dog.getDogLevel().getLevel(Type.DIRE), topX, topY + 75, 0xFF10F9);
        if (this.dog.getAccessory(DoggyAccessories.GOLDEN_COLLAR.get()).isPresent()) {
            this.font.draw(stack, ChatFormatting.GOLD + "Unlimited Points", topX - 38, topY + 89, 0xFFFFFF); //TODO translation
        } else {
            this.font.draw(stack, I18n.get("doggui.pointsleft") + " " + this.dog.getSpendablePoints(), topX - 38, topY + 89, 0xFFFFFF);
        }
       // if (ConfigHandler.CLIENT.USE_DT_TEXTURES.get()) {
            this.font.draw(stack, I18n.get("doggui.textureindex"), this.width - 80, topY + 20, 0xFFFFFF);
            this.font.draw(stack, this.dog.getSkinHash().substring(0, Math.min(this.dog.getSkinHash().length(), 10)), this.width - 73, topY + 54, 0xFFFFFF);
       // }

        if (this.dog.isOwnedBy(this.player)) {
            this.font.draw(stack, I18n.get("doggui.obeyothers"), this.width - 76, topY + 67, 0xFFFFFF);
        }

        this.font.draw(stack, I18n.get("doggui.friendlyfire"), this.width - 76, topY - 15, 0xFFFFFF);

        this.renderables.forEach(widget -> {
            if (widget instanceof TalentButton) {
                TalentButton talBut = (TalentButton)widget;
                this.font.draw(stack, I18n.get(talBut.talent.getTranslationKey()), talBut.getX() + 25, talBut.getY() + 7, 0xFFFFFF);
            }
        });

        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // TODO: since level is the local value, it is possible for the packet to fail to level up a talent. This could be improved.
    private Tooltip getTooltipForTalent(Talent talent, int level) {
        List<Component> list = Lists.newArrayList(Component.translatable(talent.getTranslationKey()).withStyle(ChatFormatting.GREEN));

        if (ConfigHandler.TALENT.getFlag(talent)) {
            list.add(Component.literal("Level: " + level));
            list.add(Component.literal("----------------------------").withStyle(ChatFormatting.GRAY));
            list.add(Component.translatable(talent.getInfoTranslationKey()));
        } else {
            list.add(Component.literal("Talent disabled").withStyle(ChatFormatting.RED));
        }

        return Tooltip.create(ComponentUtils.formatList(list, CommonComponents.NEW_LINE));
    }
    private Tooltip getTooltipForMode(EnumMode mode) {
        List<Component> list = new ArrayList<>();
        String str = I18n.get(mode.getUnlocalisedInfo());
        list.add(Component.translatable(str));
        if (mode == EnumMode.WANDERING) {
            if (DogInfoScreen.this.dog.getBowlPos().isPresent()) {
                double distance = DogInfoScreen.this.dog.blockPosition().distSqr(DogInfoScreen.this.dog.getBowlPos().get());

                if (distance > 256D) {
                    list.add(Component.translatable("dog.mode.docile.distance", (int) Math.sqrt(distance)).withStyle(ChatFormatting.RED));
                } else {
                    list.add(Component.translatable("dog.mode.docile.bowl", (int) Math.sqrt(distance)).withStyle(ChatFormatting.GREEN));
                }
            } else {
                list.add(Component.translatable("dog.mode.docile.nobowl").withStyle(ChatFormatting.RED));
            }
        }

        return Tooltip.create(ComponentUtils.formatList(list, CommonComponents.NEW_LINE));
    }

    private static class TalentButton extends Button {

        protected Talent talent;
        TalentButton(Button.Builder builder, Talent talent) {
            super(builder);
            this.talent = talent;
        }

    }
}
