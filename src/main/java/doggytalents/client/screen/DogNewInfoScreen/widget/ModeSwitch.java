package doggytalents.client.screen.DogNewInfoScreen.widget;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import doggytalents.api.feature.EnumMode;
import doggytalents.client.screen.DogInfoScreen;
import doggytalents.client.screen.ScreenUtil;
import doggytalents.client.screen.DogNewInfoScreen.store.ToolTipOverlayManager;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.network.PacketHandler;
import doggytalents.common.network.packet.data.DogModeData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraftforge.network.PacketDistributor;

public class ModeSwitch extends AbstractWidget {

    static final int DEFAULT_COLOR = 0x485e5d5d;
    static final int DEFAULT_HLCOLOR = 0x835e5d5d;
    static final int PADDING_HORIZONTAL = 3;
    static final int TICK_HOVERED_NO_CLK_TILL_SHOW_INFO = 30;

    DogEntity dog;
    Font font;
    Screen screen;

    boolean hoveredLeft = false;
    boolean hoveredRight = false;

    int timeHoveredWithoutClick = 0;
    boolean stillHovered;
    long tickCount0;

    public ModeSwitch(int x, int y, int width, int height, DogEntity dog, Font font, Screen screen) {
        super(x, y, width, height, Component.translatable(dog.getMode().getUnlocalisedName()));
        this.dog = dog;
        this.font = font;
        this.screen = screen;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {

        this.timeHoveredWithoutClick = 0;

        EnumMode mode;
        if (hoveredLeft) {
            mode = this.dog.getMode().previousMode();
        } else {
            mode = this.dog.getMode().nextMode();
        }
        if (mode == EnumMode.WANDERING && !this.dog.getBowlPos().isPresent()) {
            this.setMessage(Component.translatable(mode.getUnlocalisedName()).withStyle(ChatFormatting.RED));
        } else {
            this.setMessage(Component.translatable(mode.getUnlocalisedName()));
        }

        PacketHandler.send(PacketDistributor.SERVER.noArg(), new DogModeData(this.dog.getId(), mode));
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float pTicks) {
        if (!this.visible) return;

        int cl = this.isHovered ? DEFAULT_HLCOLOR : DEFAULT_COLOR;
        fill(stack, this.getX(), this.getY(), this.getX()+this.width, this.getY()+this.height, cl);

        this.updateHover(mouseX, mouseY);

        hoveredLeft = false;
        hoveredRight = false;

        if (this.isHovered) {
            if (mouseX - this.getX() < this.width/2) {
                hoveredLeft = true;
                hoveredRight = false;
            } else {
                hoveredLeft = false;
                hoveredRight = true;
            }
        }

        int mX = this.getX() + this.width/2;
        int mY = this.getY() + this.height/2;

        var back_c1 = Component.literal("<");
        back_c1.withStyle(
            Style.EMPTY.withBold(hoveredLeft)
        );
        int back_tX = this.getX() + PADDING_HORIZONTAL;
        int back_tY = mY - font.lineHeight/2;
        this.font.draw(stack, back_c1, back_tX, back_tY, hoveredLeft ? 0xffffffff : 0xa5ffffff);

        var next_c1 = Component.literal(">");
        next_c1.withStyle(
            Style.EMPTY.withBold(hoveredRight)
        );
        int next_tX = this.getX() + this.width - PADDING_HORIZONTAL - font.width(next_c1);
        int next_tY = mY - font.lineHeight/2;
        this.font.draw(stack, next_c1, next_tX, next_tY, hoveredRight ? 0xffffffff : 0xa5ffffff);

        var mode_c1 = this.getMessage();
        int mode_tX = mX - this.font.width(mode_c1)/2;
        int mode_tY = mY - this.font.lineHeight/2;
        this.font.draw(stack, mode_c1, mode_tX, mode_tY, 0xffffffff);

        if (this.stillHovered) {
            if (this.dog.tickCount - this.tickCount0 >= 1) {
                ++this.timeHoveredWithoutClick;
                this.tickCount0 = this.dog.tickCount;
            }
        }

        if (this.timeHoveredWithoutClick >= 25) {
            this.setOverlayToolTip(stack, mouseX, mouseY);
        }

    }

    private void updateHover(int mouseX, int mouseY) {
        boolean isHovered0 = this.isHovered;
        this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
        if (isHovered0 != this.isHovered) {
            this.stillHovered = this.isHovered;
            if (this.isHovered) {
                this.onStartHovering();
            } else {
                this.onStopHovering();
            }
        }
    }

    private void onStartHovering() {
        this.timeHoveredWithoutClick = 0;
    }

    private void onStopHovering() {
        this.timeHoveredWithoutClick = 0;
    }

    public void setOverlayToolTip(PoseStack stack, int mouseX, int mouseY) {
        List<Component> list = new ArrayList<>();
        String str = I18n.get(dog.getMode().getUnlocalisedInfo());
        list.addAll(ScreenUtil.splitInto(str, 150, this.font));
        if (this.dog.getMode() == EnumMode.WANDERING) {


            if (this.dog.getBowlPos().isPresent()) {
                double distance = this.dog.blockPosition().distSqr(this.dog.getBowlPos().get());

                if (distance > 256D) {
                    list.add(Component.translatable("dog.mode.docile.distance", (int) Math.sqrt(distance)).withStyle(ChatFormatting.RED));
                } else {
                    list.add(Component.translatable("dog.mode.docile.bowl", (int) Math.sqrt(distance)).withStyle(ChatFormatting.GREEN));
                }
            } else {
                list.add(Component.translatable("dog.mode.docile.nobowl").withStyle(ChatFormatting.RED));
            }
        }

        ToolTipOverlayManager.get().setComponents(list);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {
        // TODO Auto-generated method stub
        
    }
    
}
