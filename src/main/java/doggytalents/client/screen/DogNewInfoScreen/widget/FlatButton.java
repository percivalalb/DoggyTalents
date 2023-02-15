package doggytalents.client.screen.DogNewInfoScreen.widget;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class FlatButton extends AbstractButton {
    static final int DEFAULT_COLOR = 0x485e5d5d;
    static final int DEFAULT_HLCOLOR = 0x835e5d5d;

    Font font;

    protected final FlatButton.OnPress onPress;

    public FlatButton(int x, int y, int width, int height, 
        Component msg, FlatButton.OnPress onPress) {
        super(x, y, width, height, msg);
        //TODO Auto-generated constructor stub
        this.font = Minecraft.getInstance().font;
        this.onPress = onPress;
    }
    
    @Override
    public void onPress() {
        this.onPress.onPress(this);
    }

    @Override
    public void renderButton(PoseStack stack, int mouseX, int mouseY, float pTicks) {

        if (!this.active) return;

        int cl = this.isHovered ? DEFAULT_HLCOLOR : DEFAULT_COLOR;
        
        fill(stack, this.getX(), this.getY(), this.getX()+this.width, this.getY()+this.height, cl);
        
        //draw text
        int mX = this.getX() + this.width/2;
        int mY = this.getY() + this.height/2;
        var msg = this.getMessage();
        int tX = mX - font.width(msg)/2;
        int tY = mY - font.lineHeight/2;
        //TODO if the name is too long, draw it cut off with a ..
        font.draw(stack, msg, tX, tY, 0xffffffff);
    }

    public interface OnPress {
        void onPress(FlatButton p_93751_);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {
        // TODO Auto-generated method stub
        
    }

}
