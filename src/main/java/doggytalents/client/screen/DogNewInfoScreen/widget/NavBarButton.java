package doggytalents.client.screen.DogNewInfoScreen.widget;

import com.mojang.blaze3d.vertex.PoseStack;

import doggytalents.client.screen.DogNewInfoScreen.store.Store;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.ActiveTabSlice;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.ActiveTabSlice.Tab;
import doggytalents.common.entity.DogEntity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class NavBarButton extends AbstractButton {
    protected final Tab tab;
    private Font font;
    private Screen screen;
    private DogEntity dog;

    public NavBarButton(int x, int y, Component text, Tab tab,
            Font font, Screen screen, DogEntity dog) {
        super(x, y, font.width(text), font.lineHeight, text);
        this.tab = tab;
        this.font = font;
        this.screen = screen;
        this.dog = dog;
    }

    public void onPress() {
        Store.get(screen)
        //dispatch all to notify all slice of changetab so they can do setup before
        //appearing in the tab.
        .dispatchAll(
            ActiveTabSlice.UIActionCreator(dog, tab)
        );
    }

    public void renderButton(PoseStack stack, int mouseX, int mouseY, float pticks) {
        // No HightLight
        if (this.isHovered) {
            var s = this.getMessage().copy();
            s.withStyle(s.getStyle().withUnderlined(true).withBold(true));
            this.font.draw(stack, s, this.getX()-4, this.getY(), 0xffffffff);

        } else {
            this.font.draw(stack, this.getMessage(), this.getX(), this.getY(), 0xffffffff);

        }

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {
    }
}
